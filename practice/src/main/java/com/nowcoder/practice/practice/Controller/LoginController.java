package com.nowcoder.practice.practice.Controller;

import com.nowcoder.practice.practice.Service.UserService;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    UserService userService;
    @RequestMapping(path = "/testLogin",method = RequestMethod.GET)
    @ResponseBody
    public String test(){
        return "success";
    }
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String register(){
        return "/site/register";
    }
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model,User user){
        Map<String,Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封邮件,请尽快激活!");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效的操作,该账号已激活!");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败,您提供的激活码有误!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
}
