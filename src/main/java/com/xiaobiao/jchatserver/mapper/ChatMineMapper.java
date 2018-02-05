package com.xiaobiao.jchatserver.mapper;


import com.xiaobiao.jchatserver.param.ChatMine;


public interface ChatMineMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatMine record);

    int insertSelective(ChatMine record);

    ChatMine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatMine record);

    int updateByPrimaryKey(ChatMine record);

    ChatMine selectByParam(String usercode);
}