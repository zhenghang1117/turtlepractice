package com.nowcoder.practice.practice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    //另一种响应方式(比较常用）
    //在我理解来看，model层和view层比较贴近，你输入url，通过DispathcerServlet找到这个方法执行，然后返回model数据和view的地址，然后实际上是在view层，也就是html中去利用thymeleaf将model转化成view的，最终返回给浏览器
    //model更像是view中的东西，比如说设置这个北京大学，年龄80，你把所有东西都弄好了，通过thymeleaf弄成一个好的view,返回给浏览器
    @RequestMapping(path="/home",method = RequestMethod.GET)
    public String getSchool(Model model){
        return "/index";
    }
}
