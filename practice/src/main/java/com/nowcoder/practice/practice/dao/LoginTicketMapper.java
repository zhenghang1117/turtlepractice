package com.nowcoder.practice.practice.dao;

import com.nowcoder.practice.practice.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {
    //ticket是服务端生成的给客户端的，所以ticket是唯一的，所有操作，比如说查询，是围绕ticket来的。
    public int insertTicket(LoginTicket loginTicket);
    public void updateTicket(String ticket,int status);
    public LoginTicket selectByTicket(String ticket);
    public void deleteTicket(String ticket);
}
