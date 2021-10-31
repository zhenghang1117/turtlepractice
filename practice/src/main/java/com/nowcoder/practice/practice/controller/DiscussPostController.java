package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.entity.Comment;
import com.nowcoder.practice.practice.entity.DiscussPost;
import com.nowcoder.practice.practice.entity.Page;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.CommentService;
import com.nowcoder.practice.practice.service.DiscussPostService;
import com.nowcoder.practice.practice.service.LikeService;
import com.nowcoder.practice.practice.service.UserService;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
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
    @RequestMapping(path = "/detail/{disscussPostId}",method = RequestMethod.GET)
    public String getDetailPage(@PathVariable("disscussPostId") int disscussPostId, Model model, Page page){
        DiscussPost discussPost = discussPostService.selectDiscussPost(disscussPostId);
        model.addAttribute("post",discussPost);
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        page.setPath("/discuss/detail/"+disscussPostId);
        page.setRows(discussPost.getCommentCount());
        page.setLimit(5);
        model.addAttribute("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,disscussPostId));
        if(hostHolder.getUser() == null){
            model.addAttribute("likeStatus",0);
        }else{
            model.addAttribute("likeStatus",likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST,disscussPostId));
        }
        List<Comment> commentList = commentService.selectComment(ENTITY_TYPE_POST,discussPost.getId(),page.getOffset(),page.getLimit());
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment : commentList){
                Map<String,Object> commentMap = new HashMap<>();
                commentMap.put("comment",comment);
                commentMap.put("user",userService.findUserById(comment.getUserId()));
                commentMap.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()));
                if(hostHolder.getUser() == null){
                    commentMap.put("likeStatus",0);
                }else{
                    commentMap.put("likeStatus",likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,comment.getId()));
                }
                List<Comment> replyList = commentService.selectComment(ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                for(Comment reply : replyList){
                    Map<String,Object> replyMap = new HashMap<>();
                    replyMap.put("reply",reply);
                    replyMap.put("user",userService.findUserById(reply.getUserId()));
                    User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                    replyMap.put("target",target);
                    replyMap.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId()));
                    if(hostHolder.getUser() == null){
                        replyMap.put("likeStatus",0);
                    }else{
                        replyMap.put("likeStatus",likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT
                                ,reply.getId()));
                    }
                    replyVoList.add(replyMap);
                }
                commentMap.put("replys",replyVoList);
                int replyConut = commentService.selectCommentRows(ENTITY_TYPE_COMMENT,comment.getId());
                commentMap.put("replyCount",replyConut);
                commentVoList.add(commentMap);
            }
            model.addAttribute("comments",commentVoList);
        }
        return "/site/discuss-detail";
    }
}
