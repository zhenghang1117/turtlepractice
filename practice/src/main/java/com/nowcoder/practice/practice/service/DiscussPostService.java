package com.nowcoder.practice.practice.service;

import com.nowcoder.practice.practice.dao.DiscussPostMapper;
import com.nowcoder.practice.practice.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    DiscussPostMapper discussPostMapper;
    public int findDiscussPostsRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit,orderMode);
    }
}
