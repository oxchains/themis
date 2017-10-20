package oxchains.chat.entity;

/**
 * Created by xuqi on 2017/10/17.
 */
public class ChatContent {
    private Long bid;
    private String chatContent;
    private String createTime;
    private String username;
    private Long did;
    private String chatId;


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getBid() {
        return bid;
    }
    public void setBid(Long bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "ChatContent{" +
                "bid=" + bid +
                ", chatContent='" + chatContent + '\'' +
                ", createTime='" + createTime + '\'' +
                ", username='" + username + '\'' +
                ", did=" + did +
                ", chatId='" + chatId + '\'' +
                '}';
    }
}
