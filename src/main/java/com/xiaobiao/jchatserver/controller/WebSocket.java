package com.xiaobiao.jchatserver.controller;

import com.xiaobiao.jchatserver.model.Chat;
import com.xiaobiao.jchatserver.model.Mine;
import com.xiaobiao.jchatserver.model.To;
import com.xiaobiao.jchatserver.param.ChatFriend;
import com.xiaobiao.jchatserver.param.ChatMine;
import com.xiaobiao.jchatserver.service.ChatFriendService;
import com.xiaobiao.jchatserver.service.ChatMineService;
import com.xiaobiao.utils.util.BeanMapper;
import com.xiaobiao.utils.util.DateUtils;
import com.xiaobiao.utils.util.JsonUtil;
import com.xiaobiao.utils.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuxiaobiao
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * @create 2018-01-11 9:26
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    @Autowired
    private ChatMineService chatMineService;

    @Autowired
    private ChatFriendService chatFriendService;

    //静态初使化一个工具类  这样是为了在spring初使化之前进行初始化(chatMineService注入为Null时的操作)
    private static WebSocket webSocket;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static final String MONITOR_ID = "123456";

    //上下线标识
    @Getter
    @Setter
    private String status;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    //private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    private static Map<String, WebSocket> webSocketMap = new HashMap<String, WebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作（chatMineService注入为Null时的操作）
    @PostConstruct
    public void init() {
        webSocket = this;
        // 初使化时将已静态化的chatMineService实例化
        webSocket.chatMineService = this.chatMineService;
        webSocket.chatFriendService = this.chatFriendService;
    }


    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId, Session session) throws IOException {
        this.session = session;
        //加入map中
        webSocketMap.put(userId, this);
        if (!MONITOR_ID.equals(userId)) {
            //在线数加1
            addOnlineCount();
            ChatMine chatMine = webSocket.queryMine(userId);
            Mine mine = new Mine();
            BeanMapper.copy(chatMine, mine);
            mine.setContent("提醒：" + mine.getUsername() + " 上线了！当前在线人数：" + getOnlineCount() + "人。");
            mine.setMine("true");
            Chat chat = new Chat();
            chat.setMine(mine);
            String jsonStr = JsonUtil.obj2JsonString(chat);
            webSocket.setStatus("0");
            onMessage(userId, jsonStr, session);
        } else {
            ChatMine chatMine = webSocket.queryMine(userId);
            Mine mine = new Mine();
            BeanMapper.copy(chatMine, mine);
            mine.setContent("管理员你好！当前在线人数为:" + getOnlineCount() + "人。");
            Chat chat = new Chat();
            chat.setMine(mine);
            String jsonStr = JsonUtil.obj2JsonString(chat);
            webSocket.setStatus("2");
            onMessage(userId, jsonStr, session);
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "userId") String userId) throws IOException {
        //从map中删除
        webSocketMap.remove(userId);
        if (!MONITOR_ID.equals(userId)) {
            //在线数减1
            subOnlineCount();
            ChatMine chatMine = webSocket.queryMine(userId);
            Mine mine = new Mine();
            BeanMapper.copy(chatMine, mine);
            mine.setContent("提醒：" + mine.getUsername() + " 下线了！当前在线人数：" + getOnlineCount() + "人。");
            Chat chat = new Chat();
            chat.setMine(mine);
            String jsonStr = JsonUtil.obj2JsonString(chat);
            webSocket.setStatus("1");
            ChatMine mineParam = new ChatMine();
            mineParam.setId(userId);
            mineParam.setStatus("offline");
            webSocket.updataOnlineStatus(mineParam);
            onMessage(userId, jsonStr, session);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(@PathParam(value = "userId") String userId, String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        Chat chat = JsonUtil.json2Object(message, Chat.class);

        List<ChatFriend> chatFriendList = null;
        if (ObjectUtils.isNullOrEmpty(chat.getTo()) && !MONITOR_ID.equals(userId)) {
            chatFriendList = webSocket.queryFriend(userId);
        }
        // 群发消息
        for (Map.Entry<String, WebSocket> entry : webSocketMap.entrySet()) {
            WebSocket item = entry.getValue();
            String key = entry.getKey();
            try {
                //上线下线通知
                if (ObjectUtils.isNullOrEmpty(chat.getTo())) {
                    //发送到监控台
                    if (MONITOR_ID.equals(key)) {
                        item.sendMessage(DateUtils.getToDay(DateUtils.DATE_YMDHMS_SPLIT_14) + ": " + chat.getMine().getContent());
                    }else if (ObjectUtils.isNotNullAndEmpty(chatFriendList)) {//把上下线通知发送给好友
                        for (ChatFriend friend : chatFriendList) {
                            if (key.equals(friend.getFriendId())) {
                                Chat chatOL = JsonUtil.json2Object(message, Chat.class);
                                To to = new To();
                                to.setStatus(webSocket.getStatus());
                                to.setId(friend.getFriendId());
                                chatOL.setTo(to);
                                String messages = JsonUtil.obj2JsonString(chatOL);
                                item.sendMessage(messages);
                                System.out.println("来自客户端的上下线提醒消息:" + messages);
                            }
                        }
                    }

                }

                //消息发送
                if (ObjectUtils.isNotNullAndEmpty(chat.getTo())) {
                    if (chat.getTo().getId().equals(key)) {
                        item.sendMessage(message);
                    }
                    if (MONITOR_ID.equals(key)) {//发往监控台
                        item.sendMessage(DateUtils.getToDay(DateUtils.DATE_YMDHMS_SPLIT_14) + ": " + userId + "发送给" + chat.getTo().getId() + " 消息:" + chat.getMine().getContent());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public ChatMine queryMine(String userId) {
        ChatMine chatMine = chatMineService.queryChatMine(userId);
        return chatMine;
    }

    public List<ChatFriend> queryFriend(String userId) {
        List<ChatFriend> chatFriendList = chatFriendService.queryFriends(userId);
        return chatFriendList;
    }

    public void updataOnlineStatus(ChatMine chatMine) {
        chatMineService.updataChatMine(chatMine);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}
