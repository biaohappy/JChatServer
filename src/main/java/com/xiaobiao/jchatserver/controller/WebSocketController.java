package com.xiaobiao.jchatserver.controller;

import com.xiaobiao.jchatserver.param.ChatMine;
import com.xiaobiao.jchatserver.service.ChatMineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 监控
 * @author wuxiaobiao
 * @create 2018-01-12 10:13
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Controller
@RequestMapping("server")
public class WebSocketController {

    @RequestMapping("monitor")
    public String monitor(HttpServletRequest request){
        return "server";
    }
}
