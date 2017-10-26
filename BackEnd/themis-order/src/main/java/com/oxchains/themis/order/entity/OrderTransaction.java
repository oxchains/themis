package com.oxchains.themis.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/26.
 */
@Entity
@Table(name = "tbl_biz_transaction")
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

    public String getSignTx() {
        return signTx;
    }

    public void setSignTx(String signTx) {
        this.signTx = signTx;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(Integer txStatus) {
        this.txStatus = txStatus;
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }

    public String getP2shAddress() {
        return p2shAddress;
    }

    public void setP2shAddress(String p2shAddress) {
        this.p2shAddress = p2shAddress;
    }

    public String getP2shRedeemScript() {
        return p2shRedeemScript;
    }

    public void setP2shRedeemScript(String p2shRedeemScript) {
        this.p2shRedeemScript = p2shRedeemScript;
    }
}
