package com.xiaobiao.jchatserver.result;

import com.xiaobiao.base.BaseResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class UserResult extends BaseResult implements Serializable {

    private static final long serialVersionUID = -404528855079489582L;

    /**
     * id
     */
    private String id;

    /**
     * 用户码
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 被锁次数
     */
    private String locked;

    /**
     * 创建时间
     */
    private Date createTime;
}