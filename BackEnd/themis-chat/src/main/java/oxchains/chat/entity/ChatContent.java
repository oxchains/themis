package oxchains.chat.entity;

import javax.persistence.*;

/**
 * Created by xuqi on 2017/10/17.
 */
@Entity
public class ChatContent {
    @Id
    private String id;
    private Long senderId;
    private String chatContent;
    private String createTime;
    private String senderName;
    private Long receiverId;
    private String chatId;
    @Transient
    private Integer msgType;
    @Transient
    private String status;
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
                '}';
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
}
