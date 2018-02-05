package com.xiaobiao.jchatserver.param;

import com.xiaobiao.base.BaseParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wuxiaobiao
 * @create 2017-12-07 12:04
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Setter
@Getter
@ToString
public class UserParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = 2569799996823829929L;

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
