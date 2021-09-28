package com.nowcoder.practice.practice.service;

import com.mysql.cj.util.StringUtils;
import com.nowcoder.practice.practice.dao.UserMapper;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    @Value("${turtle.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    public User findUserById(int id){
        return userMapper.selectById(id);
    }
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

        //激活邮件
//        Context context = new Context();
//        context.setVariable("email",user.getEmail());
//        String url = domain + contextPath +"/activation/"+user.getId()+"/"+user.getActivationCode();
//        context.setVariable("url",url);
//        String content = templateEngine.process("/mail/activation",context);
//        mailClient.sendMail(user.getEmail(),"激活账号",content);


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
}
