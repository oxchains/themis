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
     * 显示私信未读
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/privateMsgNoRead")
    public RestResp queryPrivateMsgNoRead(@RequestParam Long userId){
        return messageService.queryPrivateMsgNoRead(userId);
    }

    /**
     * 显示私信已读
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/privateMsgYesRead")
    public RestResp queryPrivateMsgYesRead(@RequestParam Long userId){
        return messageService.queryPrivateMsgYesRead(userId);
    }

    /**
     * 显示公共消息未读
     * @param userId
     * @return ajax请求实体
     */
    @GetMapping(value = "/query/publicMsgNoRead")
    public RestResp queryPublicMsgNoRead(@RequestParam Long userId, @RequestParam Integer userGroup){
        return messageService.queryPublicMsgNoRead(userId, userGroup);
    }

    /**
     * 显示公共消息已读
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/publicMsgYesRead")
    public RestResp queryPublicMsgYesRead(@RequestParam Long userId){
        return messageService.queryPublicMsgYesRead(userId);
    }

    /**
     * 显示系统信息未读
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/globalMsgNoRead")
    public RestResp queryGlobalMsgNoRead(@RequestParam Long userId){
        return messageService.queryGlobalMsgNoRead(userId);
    }

    /**
     * 显示系统信息已读
     * @param userId
     * @return
     */
    @GetMapping(value = "/query/globalMsgYesRead")
    public RestResp queryGlobalMsgYesRead(@RequestParam Long userId){
        return messageService.queryGlobalMsgYesRead(userId);
    }
}
