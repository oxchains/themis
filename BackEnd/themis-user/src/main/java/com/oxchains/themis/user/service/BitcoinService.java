package com.oxchains.themis.user.service;

import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import com.oxchains.themis.common.bitcoin.BitcoinConst;
import com.oxchains.themis.common.bitcoin.BitcoinConst.VoutHashType;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.model.ScriptHash;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.repo.dao.TransactionDao;

import com.oxchains.themis.repo.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ccl
 * @time 2017-10-24 15:38
 * @name BitcoinService
 * @desc:
 */
@Service
public class BitcoinService {
    private static final Logger logger = LoggerFactory.getLogger(BitcoinService.class);
    private static BitcoinJSONRPCClient client = null;

    static {
        try {
            URL url = new URL("http://admin1:123@192.168.1.195:18332/");
            client = new BitcoinJSONRPCClient(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private final int UTXO_VOUT = 0;


    private int nRequired = 2;

    @Resource
    private TransactionDao transactionDao;

    public RestResp getKeys(){
        String address=client.getNewAddress(BitcoinConst.OXCHAINS_DEFAULT_KEYS_ACCOUNT);
        String pubKey=client.validateAddress(address).pubKey();
        String prvKey=client.dumpPrivKey(address);
        return  RestResp.success(new AddressKeys(address,pubKey,prvKey));
    }


    public RestResp getScriptHash(String orderId,List<String> signPubKeys,Double amount){
        try{
            Transaction order=transactionDao.findByOrderId(orderId);
            BitcoindRpcClient.MultiSig multiSig = client.createMultiSig(nRequired, signPubKeys);
            String p2shAddress = multiSig.address();
            String redeemScript = multiSig.redeemScript();
            logger.info("\n{\nP2SH_ADDRESS:"+p2shAddress+",\nP2SH_REDEEMSCRIPT:"+redeemScript+"\n}");
            client.addMultiSigAddress(nRequired,signPubKeys,BitcoinConst.OXCHAINS_DEFAULT_MULTISIG_ACCOUNT);

            if(order == null){
                order = new Transaction();
                order.setOrderId(orderId);
                order.setFromAddress(null);
                order.setSignTx(null);
                order.setRecvAddress(null);
                order.setTxStatus(2);
            }
            order.setP2shAddress(p2shAddress);
            order.setP2shRedeemScript(redeemScript);
            order.setAmount(amount);
            order = transactionDao.save(order);
            logger.info("*** 订单{}, 生成协商地址: {}" ,orderId, p2shAddress);
            return RestResp.success(new ScriptHash(p2shAddress,redeemScript,"bitcoin:"+p2shAddress+"?amount="+amount));

        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public RestResp addTxid(String orderId,String txId){
        try{
            logger.info("*** 订单{}, 添加UTXO_ID: {}" ,orderId, txId);

            Transaction transaction = transactionDao.findByOrderId(orderId);
            if(null != transaction){
                transaction.setUtxoTxid(txId);
                transactionDao.save(transaction);
                return RestResp.success(transaction);

            }
            return RestResp.fail("订单不存在");
        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail("交易不成立,请重新发送比特币到合约地址",e.getMessage());
        }
    }

    public RestResp getTransactionStatus(String orderId){
        try{
            logger.info("*** 订单 {}, 获取订单状态" ,orderId);
            Transaction order = transactionDao.findByOrderId(orderId);
            if(order == null){
                logger.error("订单 {} 未形成交易",orderId);
                return RestResp.fail("订单未创建交易");
            }
            String txId = order.getUtxoTxid();
            if(null == txId || "".equals(txId)){
                logger.error("订单 {} 还未进行比特币转账",orderId);
                return RestResp.fail("订单未将比特币转入协商地址");
            }
            BitcoindRpcClient.RawTransaction rawTransaction = client.getRawTransaction(txId);
            if(null != rawTransaction){
                for(BitcoindRpcClient.RawTransaction.Out out : rawTransaction.vOut()){
                    if(VoutHashType.SCRIPT_HASH.getName().equals(out.scriptPubKey().type())){
                        double amount = out.value();
                        double diff = ArithmeticUtils.minus(amount,order.getAmount());
                        //卖家或承担中继费
                        if (diff == BitcoinConst.OXCHAINS_DEFAULT_TX_FEE || diff == 0){
                        }else {
                            return RestResp.fail("交易比特币数量有误,无法继续交易");
                        }
                    }
                }
                try {
                    int confirmations = rawTransaction.confirmations();
                    return RestResp.success("交易已有 "+confirmations+" 个确认");
                }catch (Exception e){
                    return RestResp.success("交易还未确认");
                }
            }else {
                return RestResp.fail("订单 {} 不存在",orderId);
            }

        }catch (Exception e){
            logger.error("订单 {},状态查询出错: {}",orderId,e.getMessage());
            return RestResp.fail(e.getMessage());
        }

    }

    public RestResp payToUser(String orderId,String recvAddress,List<String> signPrvKeys,Double amount){
        try {
            logger.info("*** 订单{}, 将比特币发送到指定账户:{}" , orderId, recvAddress);
            Transaction order = transactionDao.findByOrderId(orderId);

            String p2shRedeemScript = order.getP2shRedeemScript();

            BitcoindRpcClient.RawTransaction rawTransaction = client.getRawTransaction(order.getUtxoTxid());
            logger.info("rawTransaction:\n"+rawTransaction.toString());

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
            int vout = 0;
            for(BitcoindRpcClient.RawTransaction.Out out : outs){
                BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = out.scriptPubKey();
                String type = scriptPubKey.type();
                if(VoutHashType.SCRIPT_HASH.getName().equals(type)){
                    //BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript, BigDecimal.valueOf(amount));
                    //0  Previous output scriptPubKey mismatch
                    BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), vout);
                    txInputs.add(txInput);
                    logger.info("Input: "+txInputs.toString());
                    amount = out.value();
                    BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(amount, BitcoinConst.OXCHAINS_DEFAULT_TX_FEE));
                    txOutputs.add(txOutput);
                    logger.info("Output: "+txOutputs.toString());

                    String rawTx = client.createRawTransaction(txInputs, txOutputs);
                    logger.info("RAW_TX: "+rawTx);
                    List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();
                    //BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript, BigDecimal.valueOf(amount - TX_FEE));//outputAmount
                    BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript);
                    txInputs1.add(txInput1);
                    logger.info("SignInput: "+txInputs1.toString());
                    logger.info("SignOutput: "+signPrvKeys.toString());
                    String lastTx = client.signRawTransaction1(rawTx, txInput1, signPrvKeys);
                    client.sendRawTransaction(lastTx);

                    order.setRecvAddress(recvAddress);
                    transactionDao.save(order);

                    return RestResp.success(RestResp.success(order));
                }
                vout ++;
            }
            return RestResp.fail("支付比特币到买家失败");
        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public RestResp payToUserWithFees(String orderId,String recvAddress,List<String> signPrvKeys,Double amount){
        try {
            logger.info("*** 订单 {}, 将比特币发送到指定账户:{}" , orderId, recvAddress);
            Transaction order = transactionDao.findByOrderId(orderId);

            String p2shRedeemScript = order.getP2shRedeemScript();

            BitcoindRpcClient.RawTransaction rawTransaction = client.getRawTransaction(order.getUtxoTxid());
            logger.info("rawTransaction:\n"+rawTransaction.toString());

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
            int vout = 0;
            for(BitcoindRpcClient.RawTransaction.Out out : outs){
                BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = out.scriptPubKey();
                String type = scriptPubKey.type();
                if(VoutHashType.SCRIPT_HASH.getName().equals(type)){
                    BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), vout);
                    txInputs.add(txInput);

                    //减去中介费
                    amount = ArithmeticUtils.minus(out.value(),BitcoinConst.OXCHAINS_DEFAULT_TX_FEE);

                    //支付到卖家账户 减去矿工费
                    BitcoindRpcClient.TxOutput txOutput1 = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(amount, BitcoinConst.OXCHAINS_DEFAULT_MINER_FEE));
                    txOutputs.add(txOutput1);

                    String feeAddress = getOxchainFeeAddress();
                    //支付中介费到oxchains账户
                    BitcoindRpcClient.TxOutput txOutput2 = new BitcoindRpcClient.BasicTxOutput(feeAddress, BitcoinConst.OXCHAINS_DEFAULT_TX_FEE);
                    txOutputs.add(txOutput2);

                    String rawTx = client.createRawTransaction(txInputs, txOutputs);

                    List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();

                    BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript);
                    txInputs1.add(txInput1);

                    String lastTx = client.signRawTransaction1(rawTx, txInput1, signPrvKeys);
                    client.sendRawTransaction(lastTx);

                    order.setRecvAddress(recvAddress);
                    transactionDao.save(order);

                    return RestResp.success(RestResp.success(order));
                }
                vout ++;
            }
            return RestResp.fail("支付比特币到买家失败");
        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public String getOxchainFeeAddress(){
        List<String> addresses = client.getAddressesByAccount(BitcoinConst.OXCHAINS_DEFAULT_FEE_ACCOUNT);
        if(null == addresses || addresses.size()<1){
            return client.getNewAddress(BitcoinConst.OXCHAINS_DEFAULT_FEE_ACCOUNT);
        }
        return addresses.get(0);
    }

    /**
    * 1. 生成公钥/私钥
    * 2. 生成协商地址和赎回脚本
    * 3. 发送到协商地址
    * 4. 发送到接收地址 createrawtransaction return RAW_TX
    * 5.
    */
    public RestResp createTransaction(String fromAddress,String utxoTxid, String prvKey,String recvAddress, double amount, List<String> signPubKeys, int nRequired) {
        try {
            BitcoindRpcClient.MultiSig multiSig = client.createMultiSig(nRequired, signPubKeys);
            String p2shAddress = multiSig.address();
            String p2shRedeemScript = multiSig.redeemScript();

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            //BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(UTXO_TXID, 0);//UTXO_VOUT
            BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.BasicTxInput(utxoTxid, 0);
            txInputs.add(txInput);
            amount = ArithmeticUtils.minus(amount, BitcoinConst.OXCHAINS_DEFAULT_MINER_FEE);
            BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(p2shAddress, amount);
            txOutputs.add(txOutput);

            // TODO
            //dangerous ！！！
            client.importPrivKey(prvKey,fromAddress,true);

            String rawTx = client.createRawTransaction(txInputs, txOutputs);
            String signedTx = client.signRawTransaction(rawTx);

            String txId = client.sendRawTransaction(signedTx);

            logger.info(txId);

            Transaction order = new Transaction();
            order.setFromAddress(fromAddress);
            order.setP2shAddress(p2shAddress);
            order.setP2shRedeemScript(p2shRedeemScript);
            order.setSignTx(signedTx);
            order.setRecvAddress(recvAddress);
            order.setTxStatus(2);
            order = transactionDao.save(order);

            return RestResp.success(order);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public RestResp confirmTransaction(String recvAddress, double amount, List<String> signPrvKeys, int type) {//type 0:取消,1:确认
        String message = null;
        try {
            //String toAddress=null;
            Transaction order = transactionDao.findByRecvAddress(recvAddress);
            String p2shAddress = order.getP2shAddress();
            String p2shRedeemScript = order.getP2shRedeemScript();
            String signedTx = order.getSignTx();
            BitcoindRpcClient.RawTransaction rawTransaction = client.decodeRawTransaction(order.getSignTx());

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
            BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = outs.get(0).scriptPubKey();
            BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript, BigDecimal.valueOf(amount));
            txInputs.add(txInput);
            BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(amount, BitcoinConst.OXCHAINS_DEFAULT_MINER_FEE));
            txOutputs.add(txOutput);

            String rawTx = client.createRawTransaction(txInputs, txOutputs);

            List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();
            BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript, BigDecimal.valueOf(amount - BitcoinConst.OXCHAINS_DEFAULT_MINER_FEE));
            txInputs1.add(txInput1);
            String lastTx = client.signRawTransaction(rawTx, txInputs1, signPrvKeys);
            client.sendRawTransaction(lastTx);

            if (type == 0) {
                order.setTxStatus(0);

                message = "交易取消成功";
            } else if (type == 1) {
                order.setTxStatus(1);
                message = "交易成功";
            }

            transactionDao.save(order);
            return RestResp.success(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public RestResp getTransaction(String orderId){
        Transaction transaction = transactionDao.findByOrderId(orderId);
        return RestResp.success(transaction);
    }
}
