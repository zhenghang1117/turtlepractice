package com.nowcoder.practice.practice.controller.interceptor;

import com.nowcoder.practice.practice.entity.LoginTicket;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.UserService;
import com.nowcoder.practice.practice.util.CookieUtil;
import com.nowcoder.practice.practice.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request,"ticket");
        if(ticket != null){
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if(loginTicket != null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
                User user = userService.findUserById(loginTicket.getUserid());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
