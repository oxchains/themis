package com.oxchains.themis.user.service;

import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import com.oxchains.themis.common.model.RestResp;
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
 * @Author ccl
 * @Time 2017-10-24 15:38
 * @Name BitcoinService
 * @Desc:
 */
@Service
public class BitcoinService {
    private static final Logger logger = LoggerFactory.getLogger(BitcoinService.class);
    private static BitcoinJSONRPCClient client = null;

    static {
        try {
            URL url = new URL("http://admin1:123@192.168.1.192:8332/");
            client = new BitcoinJSONRPCClient(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private final double TX_FEE = 0.0001d;
    private int UTXO_VOUT = 0;

    @Resource
    private TransactionDao transactionDao;

    /*
    * 1. 生成公钥/私钥
    * 2. 生成协商地址和赎回脚本
    * 3. 发送到协商地址
    * 4. 发送到接收地址 createrawtransaction return RAW_TX
    * 5.
    */
    public RestResp createTransaction(String fromAddress,String UTXO_TXID, String recvAddress, double amount, List<String> signPubKeys, int nRequired) {
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
            String rawTx = client.createRawTransaction(txInputs, txOutputs);
            String SIGNED_TX = client.signRawTransaction(rawTx);
            //rawTransaction = client.decodeRawTransaction(SIGNED_TX);
            //submitRawTransaction(SIGNED_TX);
            client.sendRawTransaction(SIGNED_TX);

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
