package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 站内信文本
 *
 * @author luoxuri
 * @create 2017-11-06 14:56
 **/
@Entity
@Data
@Table(name = "message_text")
public class MessageText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;        // 编号

    private Long senderId;    // 发送者编号

    private String message; // 站内信的内容

    private Integer messageType;   // 信息类型 1.private(私信) 2.public(公共消息) 3.global(系统消息)

    private Long userGroup;     // 用户组ID 1.admin 2.仲裁 3.客服 4.普通用户

    private String postDate;   // 站内信发送时间

    private String orderId;



    public MessageText(Long senderId, String message, Integer messageType, Long userGroup, String postDate,String orderId) {
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
        this.userGroup = userGroup;
        this.postDate = postDate;
        this.orderId = orderId;
    }

    public MessageText() {
    }
}
