package com.nowcoder.practice.practice.service;

import com.mysql.cj.util.StringUtils;
import com.nowcoder.practice.practice.dao.LoginTicketMapper;
import com.nowcoder.practice.practice.dao.UserMapper;
import com.nowcoder.practice.practice.entity.LoginTicket;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.print.DocFlavor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    MailClient mailClient;
    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Value("${turtle.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    public User findUserById(int id){
        return userMapper.selectById(id);
    }
    public User findUserByName(String username){return userMapper.selectByName(username);}
    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        if(user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if(StringUtils.isNullOrEmpty((user.getUsername()))){
            map.put("usernameMsg","账号不能为空!");
            return map;
        }
        if(StringUtils.isNullOrEmpty((user.getPassword()))){
            map.put("passwordMsg","密码不能为空!");
            return map;
        }
        if(StringUtils.isNullOrEmpty((user.getEmail()))){
            map.put("emailMsg","邮箱不能为空!");
            return map;
        }
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该账号已存在!");
            return map;
        }
        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","该邮箱已注册!");
            return map;
        }
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));//生成一个随机头像
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //动态生成一个Url，也就是用户点击链接之后蹦到哪个网页去激活
        // 生成的url模板: http://localhost:8080/community/activation/101/code
        String url = domain + contextPath +"/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }
    public Map<String,Object> login(String username,String password,long expiredSeconds){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isNullOrEmpty(username)){
            map.put("usernameMsg","用户名不能为空!");
            return map;
        }
        if(StringUtils.isNullOrEmpty(password)){
            map.put("passwordMsg","密码不能为空!");
            return map;
        }
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","该用户不存在");
            return map;
        }
        password = CommunityUtil.md5(password + user.getSalt());
        if(!password.equals(user.getPassword())){
            map.put("passwordMsg","密码不正确!");
            return map;
        }
        //到这已经是登陆成功了，可以给他分配一个LoginTicket了
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserid(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicketMapper.insertTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }
    public void logout(String ticket){
        loginTicketMapper.updateTicket(ticket,1);
    }
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }
    public void updateHeader(int userId,String headerUrl){
        userMapper.updateHeader(userId,headerUrl);
    }
    public Map<String,Object> updatePassword(User user,String oldPassword,String newPassword){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isNullOrEmpty(oldPassword)) {
            map.put("oldPasswordMsg", "原密码不能为空!");
            return map;
        }
        if (StringUtils.isNullOrEmpty(newPassword)) {
            map.put("newPasswordMsg", "新密码不能为空!");
            return map;
        }
        oldPassword = CommunityUtil.md5(oldPassword+user.getSalt());
        if (!user.getPassword().equals(oldPassword)) {
            map.put("oldPasswordMsg", "原密码输入有误!");
            return map;
        }
        newPassword = CommunityUtil.md5(newPassword+user.getSalt());
        userMapper.updatePassword(user.getId(),newPassword);
        return map;
    }
    public User findUserByEmail(String email){
        return userMapper.selectByEmail(email);
    }
    public void forgetPassword(String email,String text){
        Context context = new Context();
        context.setVariable("email",email);
        context.setVariable("kaptcha",text);
        String content = templateEngine.process("/mail/forget",context);
        mailClient.sendMail(email,"忘记密码",content);
    }
    public void updatePassword(User user,String newpassword){
        userMapper.updatePassword(user.getId(),CommunityUtil.md5(newpassword + user.getSalt()));
    }
}
