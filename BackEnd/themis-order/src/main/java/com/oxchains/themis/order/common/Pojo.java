package com.oxchains.themis.order.common;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/25.
 */
public class Pojo implements Serializable {
    private Long userId;
    private String id;
    private Long successId;
    private Long noticeId;
    private String txId;
    private String content;
    private Integer status;
    private String amount;
    private BigDecimal money;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

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
                ", txId='" + txId + '\'' +
                '}';
    }
}
