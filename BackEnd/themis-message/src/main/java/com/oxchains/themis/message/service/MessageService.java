package com.oxchains.themis.message.service;

import com.oxchains.themis.message.dao.MessageDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author luoxuri
 * @create 2017-11-06 15:02
 **/
@Service
public class MessageService {

    @Resource
    private MessageDao messageDao;
}
