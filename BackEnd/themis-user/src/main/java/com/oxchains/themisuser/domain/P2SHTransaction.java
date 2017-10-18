package com.oxchains.themisuser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @Author ccl
 * @Time 2017-10-17 11:11
 * @Name P2SHTransaction
 * @Desc:
 */
@Entity
@Table(name = "tbl_biz_p2shtransaction")
public class P2SHTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 35)
    private String recvAddress;

    @Column(length = 35)
    private String p2shAddress;

    @Column(length = 1024)
    private String p2shRedeemScript;

    @Column(length = 1024)
    private String signTx;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSignTx() {
        return signTx;
    }

    public void setSignTx(String signTx) {
        this.signTx = signTx;
    }
}
