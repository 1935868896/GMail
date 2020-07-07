package com.zc.gmail.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GmailThirdPartyApplicationTests {
    @Test
    public void uploadOss() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4GKntQJ2XDKF3deJQiRD";
        String accessKeySecret = "zG5Wz8BuOKuXql7tp8kY6Ub9ufzCMr";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        InputStream inputStream = new FileInputStream("D:\\自己整理\\技术相关文章\\计划\\七月 微服务 谷粒商城\\1-分布式基础_全栈开发篇\\docs\\pics\\0d40c24b264aa511.jpg");
        ossClient.putObject("zc-gmail", "test.jpg", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();

    }

    @Test
    void contextLoads() {
    }

}
