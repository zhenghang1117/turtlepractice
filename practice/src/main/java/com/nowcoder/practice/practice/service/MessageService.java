package com.nowcoder.practice.practice.service;

import com.nowcoder.practice.practice.dao.MessageMapper;
import com.nowcoder.practice.practice.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;
    public List<Message> selectConversations(int userId, int offset, int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }
    public int selectConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }
    public List<Message> selectLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }
    public int selectLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }
    public int selectLetterUnReadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnReadCount(userId,conversationId);
    }
    public int insertMessage(Message message){
        return messageMapper.insertMessage(message);
    }
    public int readMessage(List<Integer> ids,int status){
        return messageMapper.readMessage(ids,status);
    }
    public Message selectLatestMessage(int userId,String topic){
        return messageMapper.selectLatestMessage(userId,topic);
    }
    public int selectNoticeUnReadCount(int userId,String topic){
        return messageMapper.selectNoticeUnReadCount(userId,topic);
    }
    public int selectNoticeTopicCount(int userId,String topic){
        return messageMapper.selectNoticeTopicCount(userId,topic);
    }
    public List<Message> selectNotices(int userId,String topic,int offset,int limit){
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }
}
