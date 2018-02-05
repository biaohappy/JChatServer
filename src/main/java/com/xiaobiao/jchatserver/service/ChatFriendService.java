package com.xiaobiao.jchatserver.service;


import com.xiaobiao.jchatserver.mapper.ChatFriendMapper;
import com.xiaobiao.jchatserver.param.ChatFriend;
import com.xiaobiao.utils.exception.ChatErrorEnum;
import com.xiaobiao.utils.exception.ChatException;
import com.xiaobiao.utils.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuxiaobiao
 * @create 2018-01-16 17:16
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Service
public class ChatFriendService {

    @Autowired
    private ChatFriendMapper chatFriendMapper;

    @Transactional(readOnly = true, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ChatFriend> queryFriends(String userId) {
        List<ChatFriend> list = chatFriendMapper.queryFriends(userId);
        if ((ObjectUtils.isNullOrEmpty(list))) {
            throw new ChatException(ChatErrorEnum.ERROR_CODE_341001.getErrorCode(),
                    ChatErrorEnum.ERROR_CODE_341001.getErrorDesc());
        }
        return list;
    }
}
