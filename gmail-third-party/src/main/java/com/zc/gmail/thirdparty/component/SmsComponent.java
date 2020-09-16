package com.zc.gmail.thirdparty.component;

import com.zc.gmail.thirdparty.util.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "spring.alicloud.sms")
public class SmsComponent {
    String host;
    String path;
    String appcode;
    String templateId;

    public void sendSmsCode(String phone,String code){
//        String host = "https://smssend.shumaidata.com";
//        String path = "/sms/send";
        String method = "POST";
//        String appcode = "4881d28fa76c4188aeaf375dc100a9c1";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("receive", phone);
        querys.put("tag", code);
//        querys.put("templateId", "M4F8845237");
        querys.put("templateId", templateId);

        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
