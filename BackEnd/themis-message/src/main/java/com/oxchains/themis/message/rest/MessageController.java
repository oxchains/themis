package com.oxchains.themis.message.rest;

import com.oxchains.themis.message.service.MessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
