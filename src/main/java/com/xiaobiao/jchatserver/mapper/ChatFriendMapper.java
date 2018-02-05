package com.xiaobiao.jchatserver.mapper;


import com.xiaobiao.jchatserver.param.ChatFriend;

import java.util.List;

public interface ChatFriendMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatFriend record);

    int insertSelective(ChatFriend record);

    ChatFriend selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatFriend record);

    int updateByPrimaryKey(ChatFriend record);

    List<ChatFriend> queryFriends(String userId);
}