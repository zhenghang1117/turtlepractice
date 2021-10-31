package com.nowcoder.practice.practice.dao;

import com.nowcoder.practice.practice.entity.Comment;

import java.util.List;

public interface CommentMapper {
    List<Comment> selectComment(int entityType, int entityId, int offset, int limit);
    int selectCommentRows(int entityType,int entityId);
    int insertComment(Comment comment);
    Comment selectCommentById(int id);
}
