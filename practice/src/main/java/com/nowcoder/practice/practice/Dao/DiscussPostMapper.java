package com.nowcoder.practice.practice.Dao;

import com.nowcoder.practice.practice.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);//在个人主页可以用userId查到个人所发的帖子，在首页不用userId(userId=0)可以查所有用户发的帖子
    //offerset是每一页起始行的行号，limit是每一页最多的数据条数

    int selectDiscussPostRows(@Param("userId") int userId);//查询帖子的行数  @Param注解是给参数取别名，如果这个方法只有一个参数，并且在<if>里使用，则必须加别名

    int insertDiscussPosts(DiscussPost discussPost);

    int deletDiscussPosts(@Param("userId") int userId);

    DiscussPost selectDiscussPost(int id);

    int updateCommentCount(int id,int commentCounts);

    int updatePostType(int id,int type);

    int updatePostStatus(int id,int status);

    int updatePostScore(int postId,double score);

}
