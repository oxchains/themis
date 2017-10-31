package com.oxchains.themis.repo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Author ccl
 * @Time 2017-10-30 19:00
 * @Name UserTxDetail
 * @Desc:
 */
@Entity
@Table(name = "user_tx_detail")
public class UserTxDetail {
    @Id
    private Long id;
    private Long userId;
    private Integer txNum;     //交易次数
    private Integer goodDesc;   //好评次数
    private Integer badDesc;    //差评次数
    private String firstBuyTime;  //第一次购买时间
    private String createTime;     //用户创建时间
    private Integer believeNum;    // 信任次数

    /**
     * 交总量
     */
    @Transient
    private Double buyAmount;

    @Transient
    private Double sellAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTxNum() {
        return txNum;
    }

    public void setTxNum(Integer txNum) {
        this.txNum = txNum;
    }

    public Integer getGoodDesc() {
        return goodDesc;
    }

    public void setGoodDesc(Integer goodDesc) {
        this.goodDesc = goodDesc;
    }

    public Integer getBadDesc() {
        return badDesc;
    }

    public void setBadDesc(Integer badDesc) {
        this.badDesc = badDesc;
    }

    public String getFirstBuyTime() {
        return firstBuyTime;
    }

    public void setFirstBuyTime(String firstBuyTime) {
        this.firstBuyTime = firstBuyTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getBelieveNum() {
        return believeNum;
    }

    public void setBelieveNum(Integer believeNum) {
        this.believeNum = believeNum;
    }

    public Double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(Double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public Double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(Double sellAmount) {
        this.sellAmount = sellAmount;
    }
}
