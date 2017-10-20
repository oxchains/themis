package oxchains.chat.entity;

/**
 * Created by xuqi on 2017/10/17.
 */
public class ChatContent {
    private Long id;
    private Long senderId;
    private String chatContent;
    private String createTime;
    private String senderName;
    private Long receiverId;
    private String chatId;
    private Integer msgType;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatContent() {
        return chatContent;
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
                "id=" + id +
                ", senderId=" + senderId +
                ", chatContent='" + chatContent + '\'' +
                ", createTime='" + createTime + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverId=" + receiverId +
                ", chatId='" + chatId + '\'' +
                ", msgType=" + msgType +
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
