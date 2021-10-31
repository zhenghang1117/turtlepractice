package com.nowcoder.practice.practice.service;

import com.nowcoder.practice.practice.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    RedisTemplate redisTemplate;
    //三个业务方法，一个是点赞和取消点赞，一个是获取点赞个数，一个是判断是否已经点赞

    //点赞和取消点赞
    public void like(int userId,int entityType,int entityId,int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
                String entityUserKey = RedisKeyUtil.getEntityUserKey(entityUserId);
                boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId);
                operations.multi();//事务开始，个人页面的赞的个数情况和点赞情况应该要统一执行
                if(isMember){
                    //如果当前用户已经点了赞，那么就取消点赞，相应的发这个回复或者帖子的人收到的总赞的个数就要减1
                    redisTemplate.opsForSet().remove(entityLikeKey,userId);
                    redisTemplate.opsForValue().decrement(entityUserKey);
                }else {
                    redisTemplate.opsForSet().add(entityLikeKey,userId);
                    redisTemplate.opsForValue().increment(entityUserKey);
                }
                return operations.exec();
            }
        });
    }
    //获取点赞个数
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    //判断是否已经点赞(用于给用户显示，如果是点过，那么赞就得变成已赞)
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }

    //获取某个用户收到的赞的个数，用于个人页面显示
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getEntityUserKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 :count;
    }
}
