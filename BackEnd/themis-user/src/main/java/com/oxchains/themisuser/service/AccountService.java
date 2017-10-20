package com.oxchains.themisuser.service;

import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import com.oxchains.common.model.RestResp;
import com.oxchains.common.util.ArithmeticUtils;
import com.oxchains.themisuser.dao.P2SHTransactionDao;
import com.oxchains.themisuser.domain.P2SHTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @Author oxchains
 * @Time 2017-10-16 15:25
 * @Name AccountService
 * @Desc:
 */
@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static BitcoinJSONRPCClient client = null;

    static {
        try {
            URL url = new URL("http://admin1:123@192.168.1.195:8332/");
            client = new BitcoinJSONRPCClient(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> pubKeyMap = new HashMap<>();
    private Map<String, String> prvKeyMap = new HashMap<>();

    private List<String> signPubKeys = new ArrayList<>();
    private List<String> signPrvKeys = new ArrayList<>();

    private String P2SH_ADDRESS = null;
    private String P2SH_REDEEM_SCRIPT = null;
    private String SIGNED_TX = null;

    private String UTXO_TXID = null;//"f7deaad94a0157432fe20203e68d0f3e139dd1031da9e2e53eb52781e891a916";
    private int UTXO_VOUT = 0;
    private String UTXO_OUTPUT_SCRIPT = null;
    private String RAW_TX = null;
    private double TX_FEE = 0.0001D;

    private BitcoindRpcClient.MultiSig multiSig = null;

    private BitcoindRpcClient.RawTransaction preRawTransaction = null;
    private BitcoindRpcClient.RawTransaction rawTransaction = null;

    @Resource
    private P2SHTransactionDao p2SHTransactionDao;

    /**
     * Sign up for an account
     *
     * @param accountName
     * @return address
     */
    public String enrollAccount(String accountName) {
        return getAddress(accountName);
    }

    /**
     * get account's balance
     *
     * @param accountName
     * @return
     */
    public double getBalance(String accountName) {
        double balance = 0.0d;
        try {
            balance = client.getBalance(accountName);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return balance;
        }
    }

    public String getAddress(String accountName) {
        String address = null;
        try {
            address = client.getNewAddress(accountName);
            logger.info(address);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return address;
        }
    }

    /*
    * 1. 生成公钥/私钥
    * 2. 生成协商地址和赎回脚本
    * 3. 发送到协商地址
    * 4. 发送到接收地址 createrawtransaction return RAW_TX
    * 5.
    */
    public RestResp createTransaction(String accountName, String recvAddress, double amount, List<String> signPubKeys, int nRequired) {
        //getKeys(signAddresses);
        if (getBalance(accountName) < amount) {
            return RestResp.fail("余额不足!");
        }

        this.signPubKeys = signPubKeys;
        try {

            createScriptHash(nRequired);

            String fromAddress = getAddress(accountName);
            UTXO_TXID = sendToAddress(accountName, fromAddress, amount);

            sendToScriptHash(accountName, amount);

            P2SHTransaction p2SHTransaction = new P2SHTransaction();
            p2SHTransaction.setFromAddress(fromAddress);
            p2SHTransaction.setP2shAddress(P2SH_ADDRESS);
            p2SHTransaction.setP2shRedeemScript(P2SH_REDEEM_SCRIPT);
            p2SHTransaction.setSignTx(SIGNED_TX);
            p2SHTransaction.setRecvAddress(recvAddress);
            p2SHTransaction.setTxStatus(2);

            p2SHTransaction = p2SHTransactionDao.save(p2SHTransaction);
            return RestResp.success(p2SHTransaction);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    public RestResp confirmTransaction(String recvAddress, double amount, List<String> signPrvKeys, int type) {//type 0:取消,1:确认
        this.signPrvKeys = signPrvKeys;

        try {
            P2SHTransaction p2SHTransaction = p2SHTransactionDao.findByRecvAddress(recvAddress);
            P2SH_ADDRESS = p2SHTransaction.getP2shAddress();
            P2SH_REDEEM_SCRIPT = p2SHTransaction.getP2shRedeemScript();
            SIGNED_TX = p2SHTransaction.getSignTx();
            rawTransaction = client.decodeRawTransaction(p2SHTransaction.getSignTx());
            if(type == 0){
                sendToUser(p2SHTransaction.getFromAddress(), amount);
                p2SHTransaction.setTxStatus(0);
                p2SHTransactionDao.save(p2SHTransaction);
                return RestResp.success("交易取消成功");
            }else {
                sendToUser(recvAddress, amount);
                p2SHTransaction.setTxStatus(1);
                p2SHTransactionDao.save(p2SHTransaction);
                return RestResp.success("交易成功");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return RestResp.fail(e.getMessage());
        }
    }

    /**
     * get public/private keys
     *
     * @param addresses
     */
    private void getKeys(List<String> addresses) {
        Iterator<String> it = addresses.iterator();
        while (it.hasNext()) {
            String address = it.next();
            String pubKey = getPublicKey(address);
            String prvKey = getPrivateKey(address);
            pubKeyMap.put(address, pubKey);
            prvKeyMap.put(address, prvKey);
        }
    }

    /**
     * @param accountName
     * @param recvAddress
     * @param amount
     * @return txid
     */
    public String sendToAddress(String accountName, String recvAddress, double amount) {
        BitcoindRpcClient.TxOutput pOutputs = new BitcoindRpcClient.BasicTxOutput(recvAddress, amount);
        List<BitcoindRpcClient.TxOutput> list = new ArrayList<>();
        list.add(pOutputs);
        UTXO_TXID = client.sendMany(accountName, list);
        return UTXO_TXID;
    }

    @Deprecated
    public String sendToAddress(String address, double amount) {
        UTXO_TXID = client.sendToAddress(address, amount);
        return UTXO_TXID;
    }

    /**
     * create script hash
     *
     * @param nRequired
     */
    private void createScriptHash(int nRequired) {
        multiSig = client.createMultiSig(nRequired, signPubKeys);
        P2SH_ADDRESS = multiSig.address();
        P2SH_REDEEM_SCRIPT = multiSig.redeemScript();
    }

    public void getRawTransactin(String utxo_txid) {
        preRawTransaction = client.getRawTransaction(utxo_txid);
        UTXO_VOUT = 0;
        UTXO_OUTPUT_SCRIPT = preRawTransaction.vOut().get(0).scriptPubKey().hex();
    }

    /**
     * send to script hash address
     *
     * @param inputAmount
     */
    private void sendToScriptHash(String accountName, double inputAmount) {

        List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
        List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
        BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(UTXO_TXID, UTXO_VOUT);//UTXO_VOUT
        txInputs.add(txInput);
        inputAmount = ArithmeticUtils.minus(inputAmount, TX_FEE);
        BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(P2SH_ADDRESS, inputAmount);
        txOutputs.add(txOutput);
        String rawTx = client.createRawTransaction(txInputs, txOutputs);
        SIGNED_TX = client.signRawTransaction(rawTx);
        //rawTransaction = client.decodeRawTransaction(SIGNED_TX);
        submitRawTransaction(SIGNED_TX);
    }

    /**
     * send to user
     *
     * @param recvAddress
     */
    private void sendToUser(String recvAddress, double inputAmount) {
        List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
        List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
        List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
        BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = outs.get(0).scriptPubKey();
        BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT, BigDecimal.valueOf(inputAmount));
        txInputs.add(txInput);
        BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(inputAmount, TX_FEE));//outputAmount
        txOutputs.add(txOutput);

        String rawTx = client.createRawTransaction(txInputs, txOutputs);

        List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();
        BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), P2SH_REDEEM_SCRIPT, BigDecimal.valueOf(inputAmount - TX_FEE));//outputAmount
        txInputs1.add(txInput1);
        String lastTx = client.signRawTransaction(rawTx, txInputs1, signPrvKeys);

        submitRawTransaction(lastTx);
    }

    private void getRawTransaction(String txId) {
        try {
            rawTransaction = client.getRawTransaction(txId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public String getPublicKey(String address) {
        try {
            BitcoindRpcClient.AddressValidationResult result = client.validateAddress(address);
            return result.pubKey();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private String getPrivateKey(String address) {
        try {
            return client.dumpPrivKey(address);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private String submitRawTransaction(String hex) {
        String result = null;

        return client.sendRawTransaction(hex);
    }
}
