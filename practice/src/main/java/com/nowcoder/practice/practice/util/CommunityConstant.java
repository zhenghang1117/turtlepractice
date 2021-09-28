package com.nowcoder.practice.practice.util;

public interface CommunityConstant {
    int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;
    /*
     * 默认状态的登录凭证的超时时间：不选记住我这个选项的话，凭证就只有12小时，也就是说你12小时内再上这个网站的时候不需要你再输入账号密码登陆一次
     * */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /*
    记住状态的登录凭证的超时时间
    * */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
    /*
    实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;
    /*
   实体类型：评论
    */
    int ENTITY_TYPE_COMMENT = 2;
    /*
    实体类型: 用户
     */
    int ENTITY_TYPE_USER = 3;
    /*
     * 发布主题为评论
     * */
    String TOPIC_COMMENT = "comment";
    /*
     * 发布主题为点赞
     * */
    String TOPIC_LIKE = "like";
    /*
     * 发布主题为关注
     * */
    String TOPIC_FOLLOW = "follow";
    /*
     * 消息发送者为系统
     * */
    int SYSTEM_USER_ID = 1;
    /*
     * 发布主题为发帖
     * */
    String TOPIC_PUBLISH = "publish";
    /*
     * 发布主题为删除帖子
     * */
    String TOPIC_DELETE = "delete";
    /*
     * 权限:普通用户
     * */
    String AUTHORITY_USER = "user";
    /*
     * 权限：管理员
     * */
    String AUTHORITY_ADMIN = "admin";
    /*
     * 权限：版主
     * */
    String AUTHORITY_MODERATOR = "moderator";
}
