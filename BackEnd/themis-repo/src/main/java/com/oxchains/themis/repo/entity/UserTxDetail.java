package com.oxchains.themis.repo.entity;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2017-10-30 19:00
 * @nameUserTxDetail
 * @desc:
 */
@Entity
@Table(name = "user_tx_detail")
public class UserTxDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Integer txNum;     //交易次数
    private Integer goodDesc;   //好评次数
    private Integer badDesc;    //差评次数
    private String firstBuyTime;  //第一次购买时间
    private Integer believeNum;    // 信任次数

    private Double successCount;

    public UserTxDetail(){}

    public UserTxDetail(boolean init){
        if(init){
            this.txNum = 0;
            this.goodDesc = 0;
            this.badDesc = 0;
            this.believeNum = 0;
            this.successCount = 0.0d;
        }
    }
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

    public Double getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Double successCount) {
        this.successCount = successCount;
    }
}
