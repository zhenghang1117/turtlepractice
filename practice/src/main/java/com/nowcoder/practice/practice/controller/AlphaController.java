package com.nowcoder.practice.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/alpha")
public class AlphaController {
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
}
