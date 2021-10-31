package com.nowcoder.practice.practice.dao;

import com.nowcoder.practice.practice.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlphaMapper {
    User selectById(int id);
}
