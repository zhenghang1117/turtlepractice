package com.nowcoder.practice.practice.controller;

import com.nowcoder.practice.practice.entity.Message;
import com.nowcoder.practice.practice.entity.Page;
import com.nowcoder.practice.practice.entity.User;
import com.nowcoder.practice.practice.service.MessageService;
import com.nowcoder.practice.practice.service.UserService;
import com.nowcoder.practice.practice.util.CommunityUtil;
import com.nowcoder.practice.practice.util.HostHolder;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        page.setPath("/letter/list");
        page.setLimit(5);
        page.setRows(messageService.selectConversationCount(user.getId()));
        List<Message> messageList = messageService.selectConversations(user.getId(),page.getOffset(), page.getLimit());
        List<Map<String,Object>> conversations = new ArrayList<>();
        if(messageList != null){
            for(Message message : messageList){
                Map<String,Object> map = new HashMap<>();
                map.put("conversation",message);
                map.put("letterUnreadCount",messageService.selectLetterUnReadCount(user.getId(),message.getConversationId()));
                map.put("conversationCount",messageService.selectLetterCount(message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target",userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        model.addAttribute("letterUnReadCount",messageService.selectLetterUnReadCount(user.getId(),null));
        model.addAttribute("noticeUnReadCount",messageService.selectNoticeUnReadCount(user.getId(),null));
        return "/site/letter";
    }
    @RequestMapping(path = "/letter/send",method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String username,String content){
        User current = hostHolder.getUser();
        User target = userService.findUserByName(username);
        Message message = new Message();
        message.setContent(content);
        message.setConversationId(createConversationId(current.getId(),target.getId()));
        message.setCreateTime(new Date());
        message.setFromId(current.getId());
        message.setToId(target.getId());
        message.setStatus(0);
        messageService.insertMessage(message);
        return CommunityUtil.getJsonString(0);

    }
    @RequestMapping(path = "/letter/detail/{conversationId}" , method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,Page page,Model model){
        page.setLimit(5);
        page.setPath("/letter/detail/"+conversationId);
        page.setRows(messageService.selectLetterCount(conversationId));
        List<Message> list = messageService.selectLetters(conversationId,page.getOffset(),page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        if(list!=null){
            for(Message message:list){
                Map<String,Object> map = new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        User target = getLetterTarget(conversationId);
        model.addAttribute("target",target);
        //当前用户看到某些消息后，如果这些消息之前是未读的，需要把它设置成已读的
        //写个方法，先把需要设置已读的消息的id给取出来
        List<Integer> ids = getIds(list);
        if(!ids.isEmpty()){
            messageService.readMessage(ids,1);
        }
        return "/site/letter-detail";
    }
    private User getLetterTarget(String conversationId){
        String[] ids = conversationId.split("_");
        int id1 = Integer.parseInt(ids[0]);
        int id2 = Integer.parseInt(ids[1]);
        int targetId = hostHolder.getUser().getId() == id1 ? id2 : id1;
        User target = userService.findUserById(targetId);
        return target;
    }
    public List<Integer> getIds(List<Message> list){
        List<Integer> id = new ArrayList<>();
        if(list!=null){
            for(Message message:list){
                if(message.getToId() == hostHolder.getUser().getId() && message.getStatus() == 0){ //只有这条消息是发给当前用户的，才能把它设置为已读
                    id.add(message.getId());
                }
            }
        }
        return id;
    }
    public String createConversationId(int fromId,int toId){
        return fromId <= toId ? fromId+"_"+toId : toId+"_"+fromId;
    }
}
