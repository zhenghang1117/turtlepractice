package com.nowcoder.practice.practice.service;

import com.nowcoder.practice.practice.dao.DiscussPostMapper;
import com.nowcoder.practice.practice.entity.DiscussPost;
import com.nowcoder.practice.practice.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    SensitiveFilter sensitiveFilter;
    public int findDiscussPostsRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit,orderMode);
    }
    public int insertDiscussPosts(DiscussPost discussPost){
        if(discussPost == null){
            throw new IllegalArgumentException(("参数不能为空!"));
        }
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        return discussPostMapper.insertDiscussPosts(discussPost);
    }
    public DiscussPost selectDiscussPost(int discussPostId){
        return discussPostMapper.selectDiscussPost(discussPostId);
    }  public int updateCommentCount(int id,int commentCounts){
        return discussPostMapper.updateCommentCount(id,commentCounts);
    }
    public int updatePostType(int id,int type){
        return discussPostMapper.updatePostType(id,type);
    }
    public int updatePostStatus(int id,int status){
        return discussPostMapper.updatePostStatus(id,status);
    }
    public int updatePostScore(int id,double score){
        return discussPostMapper.updatePostScore(id,score);
    }
}
