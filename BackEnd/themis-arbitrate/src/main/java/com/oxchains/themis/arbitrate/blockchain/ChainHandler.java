package com.oxchains.themis.arbitrate.blockchain;

import com.oxchains.themis.repo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;
/**
 * Created by xuqi on 2017/12/11.
 */
public class ChainHandler {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    BigInteger gasPrice = new BigInteger("0");
    BigInteger gasLimit = new BigInteger("4500000");
    private static final String url = "http://192.168.1.155:8545";
    private static final BigInteger buyer = new BigInteger("1");
    private static final BigInteger seller = new BigInteger("2");

    private Web3j GetConnection(String url) {
        Web3j web3j = null;
        try {
            web3j = Web3j.build(new HttpService(url));
        } catch (Exception e) {
            LOG.error("web3j get connection faild : {}",e.getMessage(),e);
        }
        return web3j;
    }
    public String DeployContract(Web3j web3j){
        String contractAddress = null;
        try {
            Credentials credentials = WalletUtils.loadCredentials("test", "wallet.json");
            Order contract = Order.deploy(web3j, credentials, gasPrice, gasLimit).send();
            contractAddress = contract.getContractAddress();
        } catch (Exception e) {
            LOG.error("deploy contract faild:{}",e.getMessage(),e);
        }
        return contractAddress;
    }
    public Order getOrder(){
        Order contract = null;
        try {
            Web3j web3j = GetConnection(url);
            String contractAddress = DeployContract(web3j);
            Credentials credentials = WalletUtils.loadCredentials("test", "wallet.json");
            contract = Order.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        } catch (Exception e) {
            LOG.error("get Order faild:{}",e.getMessage(),e);
        }
        return contract;

    }
    //添加订单时 在链中存入一条订单信息 包括买家公私钥 仲裁者公私钥 买家id,卖家id 卖家私钥碎片 们现方案的K N值
    public void setOrderInfoToChain(OrderChainInfo orderChainInfo, String buyerPriKey[],String sellerPrikey[], List<User> list,Integer K){
        try {
            Order order = this.getOrder();
            order.RequestHostingService(orderChainInfo.getOrderId(), orderChainInfo.getBuyerId(), orderChainInfo.getBuyerPrivKey(), orderChainInfo.getSellerId(), orderChainInfo.getSellerPrivKey()).send();
            for (int i = 0;i<K;i++){
                order.RequestHostingServiceTrustee(orderChainInfo.getOrderId(), list.get(i).getId().toString(), buyerPriKey[i], sellerPrikey[i]).send();
            }
        } catch (Exception e) {
            LOG.error("set order info to chain faild:{}",e.getMessage(),e);
        }
    }
    //获取买家私钥
    public String getBuyerPriKeyByOrderid(String orderId) throws Exception {
        Order order = this.getOrder();
        String str = order.GetEncryBuyerPrivKey(orderId).send();
        return str;
    }
    //获取买家公钥
    public String getBuyerPubKeyByOrderId(String orderId){
        return null;
    }
    //获取卖家私钥
    public String getSellerPriKeyByOrderid(String orderId) throws Exception {
        Order order = this.getOrder();
        String str = order.GetEncrySellerPrivKey(orderId).send();
        return str;
    }
    //获取卖家公钥
    public String getSellerPubKeyByOrderId(String orderId){
        return null;
    }
    //获取仲裁者公钥
    public String getArbitratePubKey(String orderId,Long userId){
        return null;
    }
    //上传卖家的公私钥
    public void setSellerPriPubKeyToChain(OrderChainInfo orderChainInfo,String priKey[]){

    }
    //获取订单的胜利者
    public Integer getArbitrateWin(String orderId){
        return null;
    }
    //仲裁者判断买家或者卖家胜利
    public void arbitrateToUser(Long arbitrateUserId,String orderId,String userId){

    }
    //获取用户的私钥碎片
    public void getUserShardKeyByOrderId(String orderId,String userId){

    }

    public String getArbitrateBuyerPriKey(String orderId,Long userId) throws Exception {
        Order order = this.getOrder();
        String str = order.GetTrusteeStoreBuyerOrSellerEncryPrivKey(orderId, userId.toString(), buyer).send();
        return str;
    }

}
