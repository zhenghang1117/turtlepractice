package com.nowcoder.practice.practice.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.practice.practice.service.UserService;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.CommunityUtil;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @Autowired
    Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private  String context;
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
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        session.setAttribute("kaptcha",text);
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLogin(){
        return "/site/login";
    }
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(Model model,String username,String password,String code,HttpSession session,boolean rememberme,HttpServletResponse response){
        String kaptcha = session.getAttribute("kaptcha").toString();
        if(!code.equalsIgnoreCase(kaptcha)){
            model.addAttribute("codeMsg","验证码输入错误!");
            return "/site/login";
        }
        int expiredSeconds = rememberme ==true ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map = userService.login(username,password,expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(context);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }
        model.addAttribute("usernameMsg",map.get("usernameMsg"));
        model.addAttribute("passwordMsg",map.get("passwordMsg"));
        //如果登陆失败，就重新返回登陆界面
        return "/site/login";
    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/index";
    }
    @RequestMapping(path = "/forget",method = RequestMethod.GET)
    public String getForgetPage(){
        return "/site/forget";
    }
    @RequestMapping(path = "/forget/password",method = RequestMethod.POST)
    public String forget(String email,String code,String newpassword,HttpSession session,Model model){
        boolean judgeCode = session.getAttribute("kaptcha") == null || !session.getAttribute("kaptcha").toString().equals(code);
        if(judgeCode){
            model.addAttribute("codeMsg","验证码输入错误!");
            return "/site/forget";
        }
        User user = userService.findUserByEmail(email);
        userService.updatePassword(user,newpassword);
        return "redirect:/login";
    }
    @RequestMapping(path = "/forget/code",method = RequestMethod.GET)
    @ResponseBody
    public String getForgetKaptcha(String email,HttpSession session){
        User user = userService.findUserByEmail(email);
        if(user == null){
            return CommunityUtil.getJsonString(1,"该邮箱不存在!");
        }
        String text = CommunityUtil.generateUUID().substring(0,4);
        session.setAttribute("kaptcha",text);
        userService.forgetPassword(email,text);
        return CommunityUtil.getJsonString(0);
    }
}
