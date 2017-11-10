package com.oxchains.themis.chat.entity;

import javax.persistence.*;
/**
 * create by huohuo
 * @author huohuo
 */
@Entity
public class ChatContent {
    @Id
    private String id;
    private Integer senderId;
    private String chatContent;
    private String createTime;
    private String senderName;
    private Integer receiverId;
    private String chatId;
    private Integer msgType;
    private String status;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatContent() {
        return chatContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ChatContent{" +
                "id='" + id + '\'' +
                ", senderId=" + senderId +
                ", chatContent='" + chatContent + '\'' +
                ", createTime='" + createTime + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverId=" + receiverId +
                ", chatId='" + chatId + '\'' +
                ", msgType=" + msgType +
                ", status='" + status + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
}
