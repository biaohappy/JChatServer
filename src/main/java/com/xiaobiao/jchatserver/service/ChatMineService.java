package com.xiaobiao.jchatserver.service;

import com.xiaobiao.jchatserver.mapper.ChatMineMapper;
import com.xiaobiao.jchatserver.param.ChatMine;
import com.xiaobiao.utils.exception.ChatErrorEnum;
import com.xiaobiao.utils.exception.ChatException;
import com.xiaobiao.utils.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wuxiaobiao
 * @create 2018-01-16 17:01
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Service
public class ChatMineService {

    @Autowired
    private ChatMineMapper chatMineMapper;

    @Transactional(readOnly = true, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ChatMine queryChatMine(String userId) {
        ChatMine chatMine = chatMineMapper.selectByPrimaryKey(userId);
        if ((ObjectUtils.isNullOrEmpty(chatMine))) {
            throw new ChatException(ChatErrorEnum.ERROR_CODE_341001.getErrorCode(),
                    ChatErrorEnum.ERROR_CODE_341001.getErrorDesc());
        }
        return chatMine;
    }

    @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer updataChatMine(ChatMine param) {
        Integer result = chatMineMapper.updateByPrimaryKeySelective(param);
        if ((ObjectUtils.isNullOrEmpty(result))) {
            throw new ChatException(ChatErrorEnum.ERROR_CODE_341001.getErrorCode(),
                    ChatErrorEnum.ERROR_CODE_341001.getErrorDesc());
        }
        return result;
    }
}
