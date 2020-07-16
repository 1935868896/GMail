package com.zc.gmail.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zc.gmail.product.service.CategoryBrandRelationService;
import com.zc.gmail.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.common.utils.PageUtils;
import com.zc.common.utils.Query;

import com.zc.gmail.product.dao.CategoryDao;
import com.zc.gmail.product.entity.CategoryEntity;
import com.zc.gmail.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    //    @Autowired
//    CategoryDao categoryDao;
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entites = baseMapper.selectList(null);

        //2.组装成父子的树形结构
        List<CategoryEntity> level1Menus = entites.stream().filter((categoryEntity) ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entites));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单,是否被别的地方引用


        //直接删除
        baseMapper.deleteBatchIds(asList);

    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths=new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return (Long[])parentPath.toArray(new Long[parentPath.size()]);
    }

    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
     this.updateById(category);
     categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1CateGorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Override
    public Map<String,  List<Catalog2Vo>> getCatalogJson() {
        //缓存中存取的数据都是json字符串,json是跨语言跨平台兼容的
        //空结果缓存
        //设置过期时间(加上随机值)
        //加锁
        Map<String, List<Catalog2Vo>> catalogJson=null;
        String catelogString = redisTemplate.opsForValue().get("catelogJSON");
        if(StringUtils.isEmpty(catelogString)){
             catalogJson= getCatalogJsonFromDb();
            String string = JSON.toJSONString(catalogJson);
            redisTemplate.opsForValue().set("catelogJSON",string);
        }else {
            catalogJson = JSON.parseObject(catelogString,new TypeReference<Map<String, List<Catalog2Vo>>>(){});
        }
        return catalogJson;
    }
    public Map<String,  List<Catalog2Vo>> getCatalogJsonFromDb() {
        //查出所有分类
        List<CategoryEntity> level1CateGorys = getLevel1CateGorys();
        Map<String, List<Catalog2Vo>> map = level1CateGorys.stream().collect(Collectors.toMap(k ->
                        k.getCatId().toString(), v -> {
                    List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
                    List<Catalog2Vo> catelog2VoList = null;
                    if (categoryEntities != null) {
                        catelog2VoList = categoryEntities.stream().map(l2 -> {
                            Catalog2Vo catelog2Vo = new Catalog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                            //找当前二级分类的三级分类封装成vo
                            List<CategoryEntity> level3Catelog = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
                            if(level3Catelog!=null){
                                //封装成指定格式
                                List<Catalog2Vo.Catalog3Vo> catelog3VoStream = level3Catelog.stream().map(l3 -> {
                                    Catalog2Vo.Catalog3Vo catelog3Vo = new Catalog2Vo.Catalog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName());
                                    return catelog3Vo;

                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(catelog3VoStream);

                            }

                            return catelog2Vo;
                        }).collect(Collectors.toList());

                    }
                    return catelog2VoList;
                })
        );

        return map;
    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        CategoryEntity byId = this.getById(catelogId);
        paths.add(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter((categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        })).map(categoryEntity -> {
         categoryEntity.setChildren(getChildrens(categoryEntity,all));
         return categoryEntity;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

}