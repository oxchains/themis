package com.oxchains.themis.order.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Luo_xuri on 2017/10/20.
 * @author huohuo
 */
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // user表的id
    @Column(name = "userid")
    private Long userId;

    @Column(name = "noticetype")
    private Long noticeType;      // 购买BTC/出售BTC

    private Long location;        // 所在地

    private Long currency;        // 货币类型

    private Double premium = 0D;         // 溢价

    private BigDecimal price;           // 价格

    @Column(name = "minprice")
    private BigDecimal minPrice;        // 最低价

    @Column(name = "mintxlimit")
    private BigDecimal minTxLimit;      // 最小交易限额

    @Column(name = "maxtxlimit")
    private BigDecimal maxTxLimit;      // 最大交易限额

    @Column(name = "paytype")
    private Long payType;         // 支付方式/付款方式

    @Column(name = "noticecontent")
    private String noticeContent;   // 公告内容

    @Column(name = "validpaytime")
    private Long validPayTime = 1800000L;      // 付款期限，默认30分钟的毫秒值1800000

    @Transient
    private Integer searchType;

    @Column(name = "txstatus")
    private Integer txStatus = 0;           // 交易状态，默认0:非交易,1:交易进行,2:交易完成

    public Notice(){}

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

    public Long getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Long noticeType) {
        this.noticeType = noticeType;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMinTxLimit() {
        return minTxLimit;
    }

    public void setMinTxLimit(BigDecimal minTxLimit) {
        this.minTxLimit = minTxLimit;
    }

    public BigDecimal getMaxTxLimit() {
        return maxTxLimit;
    }

    public void setMaxTxLimit(BigDecimal maxTxLimit) {
        this.maxTxLimit = maxTxLimit;
    }

    public Long getPayType() {
        return payType;
    }

    public void setPayType(Long payType) {
        this.payType = payType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Long getValidPayTime() {
        return validPayTime;
    }

    public void setValidPayTime(Long validPayTime) {
        this.validPayTime = validPayTime;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Integer getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(Integer txStatus) {
        this.txStatus = txStatus;
    }
}
