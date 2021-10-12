package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.entity.DiscussPost;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.DiscussPostService;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    DiscussPostService discussPostService;
    static final Logger log = LoggerFactory.getLogger(DiscussPostController.class);
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJsonString(403,"你还没有登陆哦!");
        }
        if(title == null || title.equals("")){
            return CommunityUtil.getJsonString(403,"请输入标题!");
        }
        if(content == null || content.equals("")){
            return CommunityUtil.getJsonString(403,"请输入内容!");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setTitle(title);
        discussPostService.insertDiscussPosts(discussPost);
        return CommunityUtil.getJsonString(0,"发布成功!");
    }
}
