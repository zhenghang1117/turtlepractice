package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.entity.Comment;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.CommentService;
import com.nowcoder.practice.practice.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Date;

@Controller
@RequestMapping(path = "/comment")
public class CommentController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @RequestMapping(path = "/add/{postId}",method = RequestMethod.POST)
    public String addComent(@PathVariable("postId") String postId, Comment comment){
        User user = hostHolder.getUser();
        comment.setCreateTime(new Date());
        comment.setUserId(user.getId());
        comment.setStatus(0);
        commentService.addComments(comment);
        return "redirect:/discuss/detail/"+postId;
    }
}
