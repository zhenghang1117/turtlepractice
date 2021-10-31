package com.nowcoder.practice.practice.service;

import com.nowcoder.practice.practice.dao.CommentMapper;
import com.nowcoder.practice.practice.entity.Comment;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Autowired
    DiscussPostService discussPostService;
    public List<Comment> selectComment(int entityType, int entityId, int offset, int limit){
        return commentMapper.selectComment(entityType,entityId,offset,limit);
    }
    public int selectCommentRows(int entityType,int entityId){
        return commentMapper.selectCommentRows(entityType,entityId);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComments(Comment comment){
        //先对comment做敏感词处理
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        //添加评论的时候，要判断是给帖子评论，还是给回复评论
        //如果是给帖子评论，那么得修改帖子的回复数量，即discusspost这个表里的comment_count这个值
        //如果是给回复评论，不用修改数量，因为当时取数量的时候，不是通过表里的某个值取得，而是通过自己写的sql直接select出来的，所以不用修改
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            //先把原来帖子的回复数量给取出来;
            int commentCount = commentMapper.selectCommentRows(ENTITY_TYPE_POST,comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),commentCount+1);
        }
        int rows = commentMapper.insertComment(comment);
        return rows;
    }
    public Comment selectCommentById(int id){
        return commentMapper.selectCommentById(id);
    }
}
