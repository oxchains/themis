package com.oxchains.themis.common.model;

/**
 * @Author ccl
 * @Time 2017-10-25 18:44
 * @Name ScriptHash
 * @Desc:
 */
public class ScriptHash {
    private String address;
    private String redeemScript;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRedeemScript() {
        return redeemScript;
    }

    public void setRedeemScript(String redeemScript) {
        this.redeemScript = redeemScript;
    }

    public ScriptHash(){}

    public ScriptHash(String address, String redeemScript) {
        this.address = address;
        this.redeemScript = redeemScript;
    }
}
