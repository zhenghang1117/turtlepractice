package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.service.AlphaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/alpha")
public class AlphaController {
    @Autowired
    AlphaService alphaService;
    private  static final Logger logger = LoggerFactory.getLogger(AlphaController.class);
   @RequestMapping(path = "/hello",method = RequestMethod.GET)
   @ResponseBody
    public String sayHello(){
       return "helloworld";
   }
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }
    @RequestMapping(path = "/test/{userId}/{code}",method = RequestMethod.GET)
    @ResponseBody
    public String test(@RequestParam(name = "current",defaultValue = "0") String name){
       return name;
    }
    @RequestMapping(path="/home",method = RequestMethod.GET)
    public String getSchool(Model model){
        return "/index";
    }
    @RequestMapping(path = "/testCookie",method = RequestMethod.GET)
    @ResponseBody
    public String testCookie(@CookieValue("ticket") String ticket){
       return ticket;
    }



}
