package com.zc.gmail.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zc.common.utils.HttpUtils;
import com.zc.gmail.member.exception.PhoneExistException;
import com.zc.gmail.member.exception.UserNameExistException;
import com.zc.gmail.member.vo.MemberLoginVo;
import com.zc.gmail.member.vo.RegisterVo;
import com.zc.gmail.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.common.utils.PageUtils;
import com.zc.common.utils.Query;

import com.zc.gmail.member.dao.MemberDao;
import com.zc.gmail.member.entity.MemberEntity;
import com.zc.gmail.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(RegisterVo vo) {
        MemberEntity entity = new MemberEntity();
        /*默认等级 查询default_status=1 */
        entity.setLevelId(1L);
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());
        //检查用户名和手机名是否唯一,为了让controller感知道异常,异常机制
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String bcryptPassword = passwordEncoder.encode(vo.getPassword());

        entity.setUsername(vo.getUserName());
        entity.setPassword(bcryptPassword);
        entity.setMobile(vo.getPhone());
        entity.setNickname(vo.getUserName());

        MemberDao memberDao = this.baseMapper;
        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
        Integer result = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (result > 0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameUnique(String userName) throws UserNameExistException {
        MemberDao memberDao = this.baseMapper;
        Integer result = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (result > 0) {
            throw new PhoneExistException();
        }


    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();
        MemberDao baseMapper = this.baseMapper;
        MemberEntity entity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct).or().eq("mobile", loginacct));
        if (entity == null) {
            return null;
        } else {
            String dbPassword = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(password, dbPassword);
            if (matches) {
                return entity;
            } else {
                return null;
            }

        }

    }

    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {
        //登录和注册
        String uid=socialUser.getUid();
        MemberDao baseMapper = this.baseMapper;
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", socialUser.getUid()));
        if(memberEntity!=null){
            MemberEntity update=new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());

            baseMapper.updateById(update);
            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());
            return memberEntity;
        }else {
            //没有查到当前社交用户对应的记录,需要我们注册
            MemberEntity regist=new MemberEntity();
            try{
            //查询当前社交用户的社交账号信息(昵称,性别)
            //https://api.weibo.com/2/users/show.json
            Map<String,String> query=new HashMap<>();
            query.put("access_token",socialUser.getAccess_token());
            query.put("uid",socialUser.getUid());
            HttpResponse responese = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<>(), query);
            if(responese.getStatusLine().getStatusCode()==200) {
                //success
                String json = EntityUtils.toString(responese.getEntity());
                JSONObject jsonObject = JSON.parseObject(json);
                String name = jsonObject.getString("name");
                String gender = jsonObject.getString("gender");
                regist.setNickname(name);
                regist.setGender("m".equals(gender) ? 1 : 0);

            }}catch (Exception e){

            }
                regist.setSocialUid(socialUser.getUid());
                regist.setAccessToken(socialUser.getAccess_token());
                regist.setExpiresIn(socialUser.getExpires_in());

                baseMapper.insert(regist);
                return regist;
        }
    }

}