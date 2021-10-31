package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.controller.annotation.LoginRequired;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.UserService;
import com.nowcoder.practice.practice.util.CommunityConstant;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping(path = "/user")
public class UserController implements CommunityConstant {
    @Value("${turtle.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${turtle.path.upload}")
    private String uploadPath;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    @LoginRequired
    public String getSetting(){
        return "/site/setting";
    }
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(Model model, MultipartFile headImage){
        if(headImage == null){
            model.addAttribute("error","您还没有选择图片!");
            return "/site/setting";
        }
        String fileName = headImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = CommunityUtil.generateUUID()+suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headImage.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = hostHolder.getUser();
        fileName = domain + contextPath + "/user/header/"+fileName;
        userService.updateHeader(user.getId(),fileName);
        return "redirect:/index";
    }
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("/image/"+suffix);
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(path = "/updatePassword",method = RequestMethod.POST)
    public String updatePassWord(String oldPassword,String newPassword,String confirmPassword,Model model){
       User user = hostHolder.getUser();
       Map<String,Object> map = userService.updatePassword(user,oldPassword,newPassword);
       if(map==null || map.isEmpty()){
           if(!newPassword.equals(confirmPassword)){
               model.addAttribute("confirmPasswordMsg","两次密码输入不一致!");
               return "/site/setting";
           }
           return "redirect:/logout";
       }else {
           model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
           model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
           return "/site/setting";
       }

    }

}
