package com.oxchains.themis.user.service;

import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.model.ScriptHash;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.user.dao.TransactionDao;
import com.oxchains.themis.user.domain.Transaction;
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

    private final double TX_FEE = 0.0001d;
    private int UTXO_VOUT = 0;
    private int DEFAULT_CONFIRMATIONS = 6;

    private int nRequired = 2;

    @Resource
    private TransactionDao transactionDao;

    public RestResp getKeys(){
        String address=client.getNewAddress("AllKeys");
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
            client.addMultiSigAddress(nRequired,signPubKeys,"multisig");

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
            order = transactionDao.save(order);

            return RestResp.success(new ScriptHash(p2shAddress,redeemScript,"bitcoin:"+p2shAddress+"?amount="+amount));

        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public RestResp addTxid(String orderId,String txId){
        try{
            BitcoindRpcClient.RawTransaction rawTransaction = client.getRawTransaction(txId);
            if("scripthash".equals(rawTransaction.vOut().get(0).scriptPubKey().type())){
                Transaction transaction = transactionDao.findByOrderId(orderId);
                if(null != transaction){
                    transaction.setUtxoTxid(txId);
                    transactionDao.save(transaction);
                    return RestResp.success(transaction);
                }else {
                    return RestResp.fail("订单不成立");
                }

            }else {
               return RestResp.fail("交易不成立,请重新发送比特币到合约地址");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail("交易不成立,请重新发送比特币到合约地址",e.getMessage());
        }
    }

    public RestResp getTransactionStatus(String orderId){
        try{
            Transaction order = transactionDao.findByOrderId(orderId);
            String txId = order.getUtxoTxid();
            BitcoindRpcClient.RawTransaction rawTransaction = client.getRawTransaction(txId);
            if(null != rawTransaction){
                try {
                    int confirmations = rawTransaction.confirmations();
                    double value = rawTransaction.vOut().get(0).value();
                    return RestResp.success("交易已有 "+confirmations+" 个确认");
                }catch (Exception e){
                    return RestResp.success("交易还未确认");
                }
            }else {
                return RestResp.fail("订单交易不存在");
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }

    }

    public RestResp payToUser(String orderId,String recvAddress,List<String> signPrvKeys,Double amount){
        try {
            Transaction order = transactionDao.findByOrderId(orderId);
            //String P2SH_ADDRESS = order.getP2shAddress();
            String P2SH_REDEEM_SCRIPT = order.getP2shRedeemScript();
            //String SIGNED_TX = order.getSignTx();
            BitcoindRpcClient.RawTransaction rawTransaction = client.getRawTransaction(order.getUtxoTxid());
            logger.info("rawTransaction:\n"+rawTransaction.toString());

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
            for(BitcoindRpcClient.RawTransaction.Out out : outs){
                BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = out.scriptPubKey();
                String type = scriptPubKey.type();
                if("scripthash".equals(type)){
                    //BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT, BigDecimal.valueOf(amount));
                    BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT);
                    txInputs.add(txInput);
                    logger.info("Input: "+txInputs.toString());
                    BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(amount, TX_FEE));//outputAmount
                    txOutputs.add(txOutput);
                    logger.info("Output: "+txOutputs.toString());

                    String rawTx = client.createRawTransaction(txInputs, txOutputs);
                    logger.info("RAW_TX: "+rawTx);
                    List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();
                    //BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT, BigDecimal.valueOf(amount - TX_FEE));//outputAmount
                    BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT);//outputAmount
                    txInputs1.add(txInput1);
                    logger.info("SignInput: "+txInputs1.toString());
                    logger.info("SignOutput: "+signPrvKeys.toString());
                    String lastTx = client.signRawTransaction1(rawTx, txInput1, signPrvKeys);
                    client.sendRawTransaction(lastTx);

                    order.setRecvAddress(recvAddress);
                    transactionDao.save(order);

                    return RestResp.success(RestResp.success(order));
                }
            }
            return RestResp.fail("++++++++++");
        }catch (Exception e){
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }
    /*
    * 1. 生成公钥/私钥
    * 2. 生成协商地址和赎回脚本
    * 3. 发送到协商地址
    * 4. 发送到接收地址 createrawtransaction return RAW_TX
    * 5.
    */
    public RestResp createTransaction(String fromAddress,String UTXO_TXID, String prvKey,String recvAddress, double amount, List<String> signPubKeys, int nRequired) {
        try {
            BitcoindRpcClient.MultiSig multiSig = client.createMultiSig(nRequired, signPubKeys);
            String P2SH_ADDRESS = multiSig.address();
            String P2SH_REDEEM_SCRIPT = multiSig.redeemScript();

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            //BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(UTXO_TXID, 0);//UTXO_VOUT
            BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.BasicTxInput(UTXO_TXID, 0);//UTXO_VOUT
            txInputs.add(txInput);
            amount = ArithmeticUtils.minus(amount, TX_FEE);
            BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(P2SH_ADDRESS, amount);
            txOutputs.add(txOutput);

            // TODO
            client.importPrivKey(prvKey,fromAddress,true);//dangerous ！！！

            String rawTx = client.createRawTransaction(txInputs, txOutputs);
            String SIGNED_TX = client.signRawTransaction(rawTx);
            //rawTransaction = client.decodeRawTransaction(SIGNED_TX);
            //submitRawTransaction(SIGNED_TX);
            String txId = client.sendRawTransaction(SIGNED_TX);

            logger.info(txId);

            Transaction order = new Transaction();
            order.setFromAddress(fromAddress);
            order.setP2shAddress(P2SH_ADDRESS);
            order.setP2shRedeemScript(P2SH_REDEEM_SCRIPT);
            order.setSignTx(SIGNED_TX);
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
            String P2SH_ADDRESS = order.getP2shAddress();
            String P2SH_REDEEM_SCRIPT = order.getP2shRedeemScript();
            String SIGNED_TX = order.getSignTx();
            BitcoindRpcClient.RawTransaction rawTransaction = client.decodeRawTransaction(order.getSignTx());

            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
            List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
            BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = outs.get(0).scriptPubKey();
            BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT, BigDecimal.valueOf(amount));
            txInputs.add(txInput);
            BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(amount, TX_FEE));//outputAmount
            txOutputs.add(txOutput);

            String rawTx = client.createRawTransaction(txInputs, txOutputs);

            List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();
            BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT, BigDecimal.valueOf(amount - TX_FEE));//outputAmount
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

}
