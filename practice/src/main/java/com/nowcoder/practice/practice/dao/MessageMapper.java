package com.nowcoder.practice.practice.dao;

import com.nowcoder.practice.practice.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询某个用户所有的对话列表
    List<Message> selectConversations(int userId, int offset, int limit);
    //查询某个用户的对话列表的数量
    int selectConversationCount(int userId);
    //查询某两个用户之间对话的所有对话
    List<Message> selectLetters(String conversationId,int offset,int limit);
    //查询某两个用户之间的所有对话数量
    int selectLetterCount(String conversationId);
    //查询未读消息数目，包括某个用户的所有对话的未读数目，和某两个用户之间的对话的未读数目
    //status是0就是未读
    int selectLetterUnReadCount(int userId,String conversationId);
    //发送私信的时候，插入一条消息
    int insertMessage(Message message);
    //将未读消息改为已读
    int readMessage(List<Integer> ids,int status);
    //查询未读系统通知数目，如果有topic，就查某个topic的，如果topic为null，就查未读系统通知总数目
    int selectNoticeUnReadCount(int userId,String topic);
    //查询某topic最近的一条消息
    Message selectLatestMessage(int userId,String topic);
    //查询某个topic总的消息数量
    int selectNoticeTopicCount(int userId,String topic);
    //查询某个topic的所有消息
    List<Message> selectNotices(int userId,String topic,int offset,int limit);
}
