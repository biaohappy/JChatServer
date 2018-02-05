package com.xiaobiao.utils.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * @author wuxiaobiao
 * @create 2017-12-06 15:03
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Setter
@Getter
public class ChatException extends RuntimeException {


    private String code;

    public ChatException(String code) {
        this.code = code;
    }

    public ChatException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ChatException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public ChatException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
