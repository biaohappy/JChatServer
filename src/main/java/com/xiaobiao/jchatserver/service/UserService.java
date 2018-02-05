package com.xiaobiao.jchatserver.service;

import com.xiaobiao.jchatserver.mapper.UserMapper;
import com.xiaobiao.jchatserver.param.UserParam;
import com.xiaobiao.jchatserver.result.UserResult;
import com.xiaobiao.utils.exception.ChatErrorEnum;
import com.xiaobiao.utils.exception.ChatException;
import com.xiaobiao.utils.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuxiaobiao
 * @create 2017-12-08 15:30
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<UserResult> queryUserList(UserParam userParam) {
        List<UserResult> list = userMapper.selectUserList(userParam);
        if ((ObjectUtils.isNullOrEmpty(list)) && userParam.getIsNullError()) {
            throw new ChatException(ChatErrorEnum.ERROR_CODE_341001.getErrorCode(),
                    ChatErrorEnum.ERROR_CODE_341001.getErrorDesc());
        }
        return list;
    }

}
