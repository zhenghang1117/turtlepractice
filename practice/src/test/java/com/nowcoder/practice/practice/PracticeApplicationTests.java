package com.nowcoder.practice.practice;

import com.nowcoder.practice.practice.dao.DiscussPostMapper;
import com.nowcoder.practice.practice.dao.UserMapper;
import com.nowcoder.practice.practice.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
class PracticeApplicationTests {
    @Autowired
	UserMapper userMapper;
    @Autowired
	DiscussPostMapper discussPostMapper;
    @Autowired
	TemplateEngine templateEngine;
	@Test
	public void testFindUserById(){
		System.out.print(discussPostMapper.selectDiscussPost(109));
	}
	@Autowired
	MailClient mailClient;
	@Test
	public void testMailSend(){
		mailClient.sendMail("380110237@qq.com","Test","Welcome.");
	}
	@Test
	public void testHtmlMain(){
		Context context = new Context();
		context.setVariable("username","sunday");
		String content = templateEngine.process("/mail/demo.html",context);
		mailClient.sendMail("380110237@qq.com","HTML",content);
	}
	@Test
	public void deleteUser(){
		userMapper.deleteUser(183);
	}

}
