package com.oxchains.themis.arbitrate.common;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
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
    private String fileName;
    private String thumbUrl;
    private Integer pageNum;
    private Integer pageSize;
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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
