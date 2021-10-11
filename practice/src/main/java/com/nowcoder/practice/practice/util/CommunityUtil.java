package com.nowcoder.practice.practice.util;

import com.mysql.cj.util.StringUtils;
import org.springframework.util.DigestUtils;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    public static String md5(String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
    public static String getJsonString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJsonString(int code,String msg){
        return getJsonString(code,msg,null);
    }
    public static String getJsonString(int code){
        return getJsonString(code,null,null);
    }
}
