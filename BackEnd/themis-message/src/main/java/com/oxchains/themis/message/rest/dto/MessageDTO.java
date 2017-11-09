package com.oxchains.themis.message.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * 站内信DTO
 *
 * @author luoxuri
 * @create 2017-11-07 10:30
 **/
@Data
public class MessageDTO <T> {
    /**
     * 消息类型，1.私信 2.公共信息 3.系统信息
     */
    private Integer messageType;

    /**
     * 阅读状态，1.未读 2.已读 3.删除
     */
    private Integer readStatus;

    /**
     * 信息的条数
     */
    private Integer messageSize;

    /**
     * T 类型集合
     */
    private List<T> list;

    private String time;
}
