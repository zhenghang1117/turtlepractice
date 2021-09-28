package com.nowcoder.practice.practice.Dao;

import com.nowcoder.practice.practice.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    //返回插入行数
    int insertUser(User user);

    //返回修改条数
    int updateStatus(int id,int status);

    int updateHeader(int id,String headerUrl);

    int updatePassword(int id,String password);

    int deleteUser(int id);
}
