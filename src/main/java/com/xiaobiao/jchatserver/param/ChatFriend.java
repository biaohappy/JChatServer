package com.xiaobiao.jchatserver.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatFriend {

    private String id;

    private String userId;

    private String friendId;

    private String groupId;

    private String groupName;

    private String userCode;

    private String userName;

    private String status;

    private String sign;

    private String avatar;

    private Date createDate;

}