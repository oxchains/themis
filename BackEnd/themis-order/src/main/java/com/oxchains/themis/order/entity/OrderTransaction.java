package com.oxchains.themis.order.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/26.
 * @author huohuo
 */
@Entity
@Table(name = "tbl_biz_transaction")
@Data
public class OrderTransaction {
    @Id
    private Long id;
    private String fromAddress;
    private String recvAddress;
    private String p2shAddress;
    private String p2shRedeemScript;
    private String signTx;
    private String orderId;
    private Integer txStatus;
    private String txNo;
}
