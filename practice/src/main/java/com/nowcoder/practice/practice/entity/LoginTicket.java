package com.nowcoder.practice.practice.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LoginTicket {
    private int id;
    private int userid;
    private String ticket;
    private int status;
    private Date expired;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userid=" + userid +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
