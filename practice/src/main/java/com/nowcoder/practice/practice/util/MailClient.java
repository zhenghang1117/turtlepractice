package com.nowcoder.practice.practice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {
    private  static final Logger logger = LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from; //发件人，值就是在pom里面配置的usrname
    public void sendMail(String to,String subject,String content){

        try {
            MimeMessage message= mailSender.createMimeMessage();  //JavaMailSender是spring自带的发邮件的接口，里面有很多个可以实现的方法，这个createMinmeMessage就是一个
            MimeMessageHelper helper = new MimeMessageHelper(message); //可以理解成message只是用create这个方法建出来的一个空容器，这个空容器需要用到helper这个帮助类，来填充信息
            helper.setFrom(from); //通过helper帮助类填充邮件信息，from发件人，to收件人，subject主题，content内容
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);  //加入true之后表示支持html文本，不加true的话，就默认是text文本
            mailSender.send(helper.getMimeMessage()); //又用了javaMailSender的send方法和getMimeMessage方法就把邮件发出去了
        } catch (MessagingException e) {
            logger.error("发送邮件失败" + e.getMessage());

        }

    }
}
