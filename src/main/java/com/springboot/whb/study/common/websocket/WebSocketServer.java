package com.springboot.whb.study.common.websocket;

import com.springboot.whb.study.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: whb
 * @date: 2019/7/12 17:38
 * @description: WebSocket服务
 */
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 用于记录当前在线连接数
     */
    private static volatile int onlineCount = 0;
    /**
     * 用来存放每个客户端对应的WebSocket对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 用户ID
     */
    private String userId = "";

    /**
     * 建立连接
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        //加入set
        webSocketServerSet.add(this);
        //在线数加1
        addOnlineCount();
        logger.info("有新窗口开始监听：{}，当前在线人数：{}", userId, getOnlineCount());
        this.userId = userId;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("建立websocket连接异常:{}", e);
        }
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketServerSet.remove(this);
        //在线人数减一
        subOnlineCount();
        logger.info("有一连接关闭！当前在线人数为：{}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("收到来自窗口【{}】的消息：{}", userId, message);
        //群发消息
        for (WebSocketServer item : webSocketServerSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error("给用户【{}】发送消息【{}】失败:{}", item.userId, message, e);
            }
        }
    }

    /**
     * 出现错误
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        logger.error("发生错误：{}", error);
    }

    /**
     * 服务器主动推送消息
     *
     * @param message 消息
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 给特定用户发消息
     *
     * @param message 消息内容
     * @param userId  用户ID
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) {
        logger.info("推送消息到窗口【{}】，推送内容：{}", userId, message);
        for (WebSocketServer item : webSocketServerSet) {
            try {
                //userId为null则给所有人推送
                if (StringUtils.isBlank(userId)) {
                    item.sendMessage(message);
                } else if (item.userId.equals(userId)) {
                    //否则只给指定用户推送
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 返回在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人数加1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 在线人数减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
