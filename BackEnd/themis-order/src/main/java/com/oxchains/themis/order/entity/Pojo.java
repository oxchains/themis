package com.oxchains.themis.order.entity;

/**
 * Created by huohuo on 2017/10/25.
 */
public class Pojo {
    private Long userId;
    private String id;
    private Long successId;

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
                ", successId='" + successId + '\'' +
                '}';
    }
}
