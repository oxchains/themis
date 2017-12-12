package com.oxchains.themis.arbitrate.blockchain;

import lombok.Data;

/**
 * Created by xuqi on 2017/12/11.
 */
@Data
public class OrderChainInfo {
    private String orderId;   //订单id
    private String buyerId;    //买家id
    private String buyerPrivKey; //买家私钥
    private String sellerId;      //卖家id
    private String sellerPrivKey; //卖家私钥
    private String K;             //门限方案中的K值
    private String N;             //门限方案中的N值
    private String arbitratePubKey; //仲裁者公钥
    private String arbitratePriKey; //仲裁者私钥

}
