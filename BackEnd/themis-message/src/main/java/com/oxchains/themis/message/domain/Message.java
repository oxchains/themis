package com.oxchains.themis.message.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 站内信
 *
 * @author luoxuri
 * @create 2017-11-06 14:42
 **/
@Entity
@Data
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;        // 编号

    private Long receiverId;     // 接受者编号

    private Long messageTextId; // 站内信编号

    private Integer readStatus; // 站内信的查看状态 1.未读 2.已读 3.删除

    public Message(Long receiverId, Long messageTextId, Integer readStatus) {
        receiverId = receiverId;
        this.messageTextId = messageTextId;
        this.readStatus = readStatus;
    }

    public Message() {
    }
}
