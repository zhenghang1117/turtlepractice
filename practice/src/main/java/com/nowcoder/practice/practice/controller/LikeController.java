package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.LikeService;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant{
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(),entityType,entityId,entityUserId);
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        int likeStatus = likeService.findEntityLikeStatus(user.getId(),entityType,entityId);
        Map<String,Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);
        return CommunityUtil.getJsonString(0,null,map);
    }

}
