package com.oxchains.themis.order.common;

/**
 * Created by huohuo on 2017/10/25.
 */
public class Pojo {
    private Long userId;
    private String id;
    private Long successId;
    private Long noticeId;
    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getSuccessId() {
        return successId;
    }

    public void setSuccessId(Long successId) {
        this.successId = successId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "userId=" + userId +
                ", id='" + id + '\'' +
                ", successId=" + successId +
                ", noticeId=" + noticeId +
                '}';
    }
}
