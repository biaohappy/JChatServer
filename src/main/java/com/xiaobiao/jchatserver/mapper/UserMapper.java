package com.xiaobiao.jchatserver.mapper;


import com.xiaobiao.jchatserver.param.UserParam;
import com.xiaobiao.jchatserver.result.UserResult;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(UserParam record);

    UserParam selectUserInfo(Integer id);

    int updateByPrimaryKeySelective(UserParam record);

    List<UserResult> selectUserList(UserParam record);
}