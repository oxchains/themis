package com.oxchains.themis.message.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.domain.MessageText;
import com.oxchains.themis.message.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设计思路
 * 我们将消息分为私信(private)、公共信息(public)、系统信息(global)
 * 1，点到点：一对一发送，属于私信(private)
 * 2，点到个别：(接受面对百位用户)一对多(几百)发送，采用私信方式(private)
 * 3，点到局部：(接收面对具有没写公共特性，如用户组、用户角色),属于公共消息(public)
 * 4，点到全部：一对全部发送，属于系统消息(global)
 *
 * @author luoxuri
 * @create 2017-11-06 15:01
 **/
@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * Admin发送系统消息
     * @param messageText
     * @return
     */
    @PostMapping(value = "/send/globalMessage")
    public RestResp sendGlobalMessage(@RequestBody MessageText messageText){
        return messageService.sendGlobalMessage(messageText);

    }

    /**
     * 显示系统信息
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/globalMsg")
    public RestResp queryGlobalMsg(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return messageService.queryGlobalMsg(userId, pageNum, pageSize);
    }

    /**
     * 显示私信
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/privateMsg")
    public RestResp queryPrivateMsg(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return messageService.queryPrivateMsg(userId, pageNum, pageSize);
    }

    /**
     * 显示公告信息
     * @param userId
     * @return ajax请求实体
     */
    @GetMapping(value = "/query/noticeMsg")
    public RestResp queryNoticeMsg(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return messageService.queryNoticeMsg(userId, pageNum, pageSize);
    }

    /**
     * 未读信息数量
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/unReadCount")
    public RestResp unReadCount(@RequestParam Long userId){
        return messageService.unReadCount(userId);
    }
}
