package com.xiaobiao.jchatserver.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatMine {
    private String id;

    private String usercode;

    private String username;

    private String status;

    private String sign;

    private String avatar;

    private Date createDate;

}