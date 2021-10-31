package com.nowcoder.practice.practice.util;

public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";
    public static final String PREFIX_ENTITY_USER = "like:user";
    public static final String PREFIX_FOLLOWEE = "followee";
    public static final String PREFIX_FOLLOWER = "follower";
    public static final String PREFIX_KAPTCHA = "kaptcha";
    public static final String PREFIX_TICKET = "ticket";
    public static final String PREFIX_USER = "user";
    public static final String PREFIX_POST = "post";
    //用来统计网站总登陆人数
    public static final String PREFIX_UV = "uv";
    //用来统计网站日活跃用户
    public static final String PREFIX_DAU = "dau";

    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    public static String getEntityUserKey(int userId){
        return PREFIX_ENTITY_USER + SPLIT +userId;
    }
    public static String getFolloeeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId +SPLIT +entityType;
    }
}
