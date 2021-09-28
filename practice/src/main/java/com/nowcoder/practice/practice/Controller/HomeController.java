package com.nowcoder.practice.practice.Controller;

import com.nowcoder.practice.practice.Service.DiscussPostService;
import com.nowcoder.practice.practice.Service.UserService;
import com.nowcoder.practice.practice.entity.DiscussPost;
import com.nowcoder.practice.practice.entity.Page;
import com.nowcoder.practice.practice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    UserService userService;
    @RequestMapping("/index")
    public String getIndexPage(Model model, Page page, @RequestParam(name = "orderMode",defaultValue = "0") int orderMode){
        page.setRows(discussPostService.findDiscussPostsRows(0));
        page.setPath("/index?orderMode="+orderMode);
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(),page.getLimit(),orderMode);
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        for(DiscussPost post : list){
            Map<String,Object> map = new HashMap<>();
            map.put("post",post);
            User user = userService.findUserById(post.getUserId());
            map.put("user",user);
            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("orderMode",orderMode);
        return "/index";
    }
}
