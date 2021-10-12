package com.nowcoder.practice.practice.util;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    public static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    public static final String REPLACEMENT="***";
    public TrieTreeNode rootNode = new TrieTreeNode();
    //服务一启动，就找到这个bean，如果写好这个注解，就会来先执行这个方法，根据我们之前写好的敏感词txt文件,去调用addKeyWord方法，把前缀树给构建好。
    @PostConstruct
    public void init(){
        try(
                InputStream is =this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");//把字节流转化成字符流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));//再把字符流转化成缓冲流。读取数据效率比较高

        ){
             String keyword;
             while((keyword = reader.readLine())!=null){
                 this.addKeyWord(keyword); //调用addKeyWord方法，把读出来的敏感词keyword给挂到前缀树中去
             }
        }
        catch (IOException e){
            logger.error("加载敏感词文件信息失败:"+e.getMessage());
        }

    }

    //调用已经构建好的前缀树，把text里面的敏感词给替换掉，返回替换***后之后的text字符串
    public String filter(String text){
        StringBuilder sb = new StringBuilder();
        TrieTreeNode tempTreeNode = rootNode; //指针1
        int begin = 0; //指针2
        int position = 0; //指针3
        while(begin != text.length()){
            Character c = text.charAt(position);
            //判断c是不是符号 比如有的人故意打吸#毒，你如果不把中间的#给跳过，这个吸毒敏感词就检索不到了，所以说有这样的符号，都得给他跳过
            if(isSymbol(c)){
                //如果begin这个指针这时候指向c，且此时是从头节点开始的，代表第一个符号就是特殊符号，那肯定不是敏感词的开始头部，直接跳过就好
                if(tempTreeNode == rootNode){
                    sb.append(c);
                    position = begin++;
                    continue;
                }else{//如果这时候不是从头结点开始的，代表中间时候遇到了特殊符号，就比如吸#毒，那就应该begin不动，position往后加
                    sb.append(c);
                    position++;
                    continue;

                }
            }else {//如果此时c不是一个符号，那就要判断c是不是敏感词了，先看从前缀树中能不能找到下级节点，如果找不到，说明以begin开头的不是敏感词。
                   // 再看c是不是isKeyWordEnd,是的话就打星号，然后加到sb,begin往后动
                   //如果都不符合，那说明还是疑似敏感词。比如说吸大麻，这时候begin在吸，position在大，position还要往后判断
                tempTreeNode = tempTreeNode.getSubNode(c);
                if(tempTreeNode == null){
                   sb.append(text.charAt(begin));
                   position = ++begin;
                   tempTreeNode = rootNode;//如果不是敏感词，代表中断了，比如吸嫖娼，吸的时候疑似，到了嫖，成了吸嫖，不是敏感词，中断了，你的tempTreeNode就返回头结点.
                }else if(tempTreeNode.isKeyWordEnd){
                    sb.append(REPLACEMENT);
                    begin = ++position;
                }else {
                    if(position<text.length()-1){
                        position++;
                    }

                }

            }
        }
        return  sb.toString();

    }

    //判断字符是不是一个符号
    private  boolean isSymbol(Character c){
        //CharUtils.isAsciiAlphanumeric(c)判断c是不是符号，如果是符号，返回false，所以给他加个！，就是如果是符号，返回true
        //0X2E80~0X9FFF是东亚汉字，因为这个工具类会判断一些东亚汉字也是特殊符号，所以给他标定一下
        return !CharUtils.isAsciiAlphanumeric(c) && (c<0X2E80 && c>0X9FFF);
    }
    private void addKeyWord(String keyword){
        TrieTreeNode tempTreeNode = rootNode;
        for(int i = 0;i< keyword.length();i++){
            char c = keyword.charAt(i);
            TrieTreeNode subTreeNode = tempTreeNode.getSubNode(c);
            if(subTreeNode==null){
                subTreeNode = new TrieTreeNode();
                tempTreeNode.addSubNode(c,subTreeNode);
            }
            tempTreeNode = subTreeNode;
            if(i==keyword.length()-1){
                tempTreeNode.isKeyWordEnd = true;
            }
        }

    }
    //前缀树
    private class TrieTreeNode{
        private boolean isKeyWordEnd = false;
        private Map<Character,TrieTreeNode> subNodes = new HashMap<>();
        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
        public void addSubNode(Character c,TrieTreeNode node){
            subNodes.put(c,node);
        }
        public TrieTreeNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }


}
