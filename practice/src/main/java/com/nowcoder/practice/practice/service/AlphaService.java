package com.nowcoder.practice.practice.service;

import com.nowcoder.practice.practice.dao.AlphaMapper;
import com.nowcoder.practice.practice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlphaService {
    @Autowired
    AlphaMapper alphaMapper;
    public User testTransction(int id){
        User user = alphaMapper.selectById(id);
        System.out.println("我拿到锁啦!!!!!!!!!!!!!!!!!");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }
    public void testGit(){
        System.out.println("测试分支");
    }
    public void testGit2(){
        System.out.println("再次测试分支");
    }
}
