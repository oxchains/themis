package com.oxchains.themis.notice.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.DefaultProperty;
import lombok.Data;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Entity
@Data
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "userid")
    private Long userId;       // user表的id

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
}
