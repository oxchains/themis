package com.oxchains.themis.bitcoin.rpcclient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author oxchains
 * @Time 2017-10-30 9:53
 * @Name BitcoindRpcClient
 * @Desc:
 * @url https://bitcoin.org/en/developer-reference#rpcs
 */
public interface BitcoindRpcClient extends BaseRpcClient{
    /** 1.
     * Added in Bitcoin Core 0.12.0
     * The abandontransaction RPC marks an in-wallet transaction and all its in-wallet descendants as abandoned.
     * This allows their inputs to be respent.
     * @param txid
     * @throws BitcoinRpcException
     */
    void abandonTransaction(String txid) throws BitcoinRpcException;

    /** 2.
     * Requires wallet support.
     * The addmultisigaddress RPC adds a P2SH multisig address to the wallet.
     * @param nRequired
     * @param keyObject
     * @return
     */
    String addMultiSigAddress(int nRequired, List<String> keyObject);

    String addMultiSigAddress(int nRequired, List<String> keyObject, String account);

    /** 3.
     * The addnode RPC attempts to add or remove a node from the addnode list,or to try a connection to a node once.
     * @param node
     * @param command
     */
    void addNode(String node, String command);

    /** 4.
     * Added in Bitcoin Core 0.13.0 Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The addwitnessaddress RPC adds a witness address for a script (with pubkey or redeem script known).
     * @param address
     * @return
     */
    String addWitnessAdress(String address);

    /** 5.
     * Requires wallet support.
     * The backupwallet RPC safely copies wallet.dat to the specified file, which can be a directory or a path with filename.
     * @param destination
     */
    void backupWallet(String destination);

    /** 6.
     * Added in Bitcoin Core 0.14.0 Requires wallet support. Wallet must be unlocked.
     */
    //void bumpFee(String txid);

    /** 7.
     * Added in Bitcoin Core 0.12.0
     * The clearbanned RPC clears list of banned nodes.
     */
    void clearBanned();

    /** 8.
     * The createmultisig RPC creates a P2SH multi-signature address.
     * @param nRequired
     * @param keys
     * @return
     * @throws BitcoinRpcException
     */
    MultiSig createMultiSig(int nRequired, List<String> keys) throws BitcoinRpcException;

    /** 9.
     * The createrawtransaction RPC creates an unsigned serialized transaction that spends a previous output to a new output with a P2PKH or P2SH address.
     * The transaction is not stored in the wallet or transmitted to the network.
     * @param inputs
     * @param outputs
     * @return
     * @throws BitcoinRpcException
     */
    String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs) throws BitcoinRpcException;

    String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs, int locktime) throws BitcoinRpcException;

    /** 10.
     *
     */
    //void decodeRawTransaction(String hex);

    /** 11.
     * The decodescript RPC decodes a hex-encoded P2SH redeem script.
     * @param hex (redeem script)
     * @return
     */
    DecodedScript decodeScript(String hex);

    /** 12.
     * Added in Bitcoin Core 0.12.0 Updated in Bitcoin Core 0.14.1
     * The disconnectnode RPC immediately disconnects from a specified node.
     * @param address (hostname/ip)(ip:port)
     */
    void disconnectNode(String address);

    /** 13.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The dumpprivkey RPC returns the wallet-import-format (WIP) private key corresponding to an address. (But does not remove it from the wallet.)
     * @param address
     * @return
     * @throws BitcoinRpcException
     */
    String dumpPrivKey(String address) throws BitcoinRpcException;

    /** 14.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.The dumpwallet RPC creates or overwrites a file with all wallet keys in a human-readable format.
     * @param filename
     */
    void dumpWallet(String filename);

    /** 15.
     * Requires wallet support.
     * The encryptwallet RPC encrypts the wallet with a passphrase. This is only to enable encryption for the first time. After encryption is enabled, you will need to enter the passphrase to use private keys.
     * @param passPhrase
     */
    void encryptWallet(String passPhrase);

    /** 16.
     * The estimatefee RPC estimates the transaction fee per kilobyte that needs to be paid for a transaction to be included within a certain number of blocks.
     * @param blockNum
     * @return
     */
    double estimateFee(int blockNum);

    /** 17.
     * Added in Bitcoin Core 0.10.0.
     * Warning:estimatepriority has been removed and will no longer be available in the next major release (planned for Bitcoin Core 0.15.0). Use the RPC listed in the “See Also” subsection below instead.
     * The estimatepriority RPC estimates the priority (coin age) that a transaction needs in order to be included within a certain number of blocks as a free high-priority transaction.
     * This should not to be confused with the prioritisetransaction RPC which will remain supported for adding fee deltas to transactions.
     * @param blockNum
     * @return
     */
    @Deprecated
    double estimatePriority(int blockNum);

    /** 18.
     *  Requires wallet support.
     * The fundrawtransaction RPC adds inputs to a transaction until it has enough in value to meet its out value. This will not modify existing inputs, and will add one change output to the outputs.
     * Note that inputs which were signed may need to be resigned after completion since in/outputs have been added. The inputs added will not be signed, use signrawtransaction for that.
     * All existing inputs must have their previous output transaction be in the wallet.
     */
    //void  fundRawTranscaction();

    /** 19.
     * Requires wallet support. The generate RPC nearly instantly generates blocks. Used in regtest mode to generate an arbitrary number of blocks
     * @param numBlocks a boolean indicating if blocks must be generated with the
     * cpu
     * @return the list of hashes of the generated blocks
     * @throws BitcoinRpcException
     */
    List<String> generate(int numBlocks) throws BitcoinRpcException;

    List<String> generate(int numBlocks,int maxtries) throws BitcoinRpcException;

    /** 20.
     * Added in Bitcoin Core 0.13.0 Requires wallet support.
     * The generatetoaddress RPC mines blocks immediately to a specified address.
     * @param numBlocks
     * @param address
     * @return
     * @throws BitcoinRpcException
     */
    List<String> generateToAddress(int numBlocks,String address) throws BitcoinRpcException;

    List<String> generateToAddress(int numBlocks,String address,int maxtries) throws BitcoinRpcException;

    /** 21.
     * Requires wallet support.
     * The getaccountaddress RPC returns the current Bitcoin address for receiving payments to this account. If the account doesn’t exist, it creates both the account and a new address for receiving payment.
     * Once a payment has been received to an address, future calls to this RPC for the same account will return a different address.
     * Warning: getaccountaddress will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @param account
     * @return
     * @throws BitcoinRpcException
     */
    String getAccountAddress(String account) throws BitcoinRpcException;

    /** 22.
     * Requires wallet support.
     * The getaccount RPC returns the name of the account associated with the given address.
     * @param address
     * @return
     * @throws BitcoinRpcException
     */
    String getAccount(String address) throws BitcoinRpcException;

    /** 23.
     * The getaddednodeinfo RPC returns information about the given added node, or all added nodes (except onetry nodes). Only nodes which have been manually added using the addnode RPC will have their information displayed.
     * parameter #1 Removed in Bitcoin Core 0.14.0
     * @param dummy
     * @param node
     * @return
     */
    List<NodeInfo> getAddedNodeInfo(boolean dummy, String node);

    /** 24.
     * Requires wallet support.
     * The getaddressesbyaccount RPC returns a list of every address assigned to a particular account.
     * Warning icon Warning: getaddressesbyaccount will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @param account
     * @return
     * @throws BitcoinRpcException
     */
    List<String> getAddressesByAccount(String account) throws BitcoinRpcException;

    /** 25.
     * @param account
     * @param minConf
     * @return returns the balance in the account
     * @throws BitcoinRpcException
     */
    double getBalance(String account, int minConf) throws BitcoinRpcException;

    double getBalance(String account) throws BitcoinRpcException;

    double getBalance() throws BitcoinRpcException;




    /**
     * @return infos about the bitcoind instance
     * @throws BitcoinRpcException
     */
    public Info getInfo() throws BitcoinRpcException;

    /**
     *
     * @return miningInfo about the bitcoind instance
     * @throws BitcoinRpcException
     */
    public MiningInfo getMiningInfo() throws BitcoinRpcException;


    public NetworkInfo getNetworkInfo() throws BitcoinRpcException;

    public TxOutSetInfo getTxOutSetInfo();

    public WalletInfo getWalletInfo();



    public Block getBlock(int height) throws BitcoinRpcException;

    public Block getBlock(String blockHash) throws BitcoinRpcException;

    public String getBlockHash(int height) throws BitcoinRpcException;

    public BlockChainInfo getBlockChainInfo() throws BitcoinRpcException;

    public int getBlockCount() throws BitcoinRpcException;

    public String getNewAddress() throws BitcoinRpcException;

    public String getNewAddress(String account) throws BitcoinRpcException;

    public List<String> getRawMemPool() throws BitcoinRpcException;

    public String getBestBlockHash() throws BitcoinRpcException;

    public String getRawTransactionHex(String txId) throws BitcoinRpcException;

    public RawTransaction getRawTransaction(String txId) throws BitcoinRpcException;

    RawTransaction getRawTransaction(String txId,boolean format) throws BitcoinRpcException;

    public double getReceivedByAddress(String address) throws BitcoinRpcException;

    /**
     * Returns the total amount received by &lt;bitcoinaddress&gt; in transactions
     * with at least [minconf] confirmations. While some might consider this
     * obvious, value reported by this only considers *receiving* transactions. It
     * does not check payments that have been made *from* this address. In other
     * words, this is not "getaddressbalance". Works only for addresses in the
     * local wallet, external addresses will always show 0.
     *
     * @param address
     * @param minConf
     * @return the total amount received by &lt;bitcoinaddress&gt;
     */
    public double getReceivedByAddress(String address, int minConf) throws BitcoinRpcException;

    public void importPrivKey(String bitcoinPrivKey) throws BitcoinRpcException;

    public void importPrivKey(String bitcoinPrivKey, String label) throws BitcoinRpcException;

    public void importPrivKey(String bitcoinPrivKey, String label, boolean rescan) throws BitcoinRpcException;

    Object importAddress(String address, String label, boolean rescan) throws BitcoinRpcException;

    /**
     * listaccounts [minconf=1]
     *
     * @return Map that has account names as keys, account balances as values
     * @throws BitcoinRpcException
     */
    public Map<String, Number> listAccounts() throws BitcoinRpcException;

    public Map<String, Number> listAccounts(int minConf) throws BitcoinRpcException;



    public List<ReceivedAddress> listReceivedByAddress() throws BitcoinRpcException;

    public List<ReceivedAddress> listReceivedByAddress(int minConf) throws BitcoinRpcException;

    public List<ReceivedAddress> listReceivedByAddress(int minConf, boolean includeEmpty) throws BitcoinRpcException;

    public TransactionsSinceBlock listSinceBlock() throws BitcoinRpcException;

    public TransactionsSinceBlock listSinceBlock(String blockHash) throws BitcoinRpcException;

    public TransactionsSinceBlock listSinceBlock(String blockHash, int targetConfirmations) throws BitcoinRpcException;

    public List<Transaction> listTransactions() throws BitcoinRpcException;

    public List<Transaction> listTransactions(String account) throws BitcoinRpcException;

    public List<Transaction> listTransactions(String account, int count) throws BitcoinRpcException;

    public List<Transaction> listTransactions(String account, int count, int from) throws BitcoinRpcException;

    public List<Unspent> listUnspent() throws BitcoinRpcException;

    public List<Unspent> listUnspent(int minConf) throws BitcoinRpcException;

    public List<Unspent> listUnspent(int minConf, int maxConf) throws BitcoinRpcException;

    public List<Unspent> listUnspent(int minConf, int maxConf, String... addresses) throws BitcoinRpcException;

    public String move(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinRpcException;

    public String move(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinRpcException;

    public String move(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinRpcException;

    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinRpcException;

    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinRpcException;

    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinRpcException;

    String sendMany(String fromAccount,List<TxOutput> outputs) throws BitcoinRpcException;
    /**
     * Will send the given amount to the given address, ensuring the account has a
     * valid balance using minConf confirmations.
     *
     * @param fromAccount
     * @param toBitcoinAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param minConf
     * @param comment
     * @param commentTo
     * @return the transaction ID if successful
     * @throws BitcoinRpcException
     */
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment, String commentTo) throws BitcoinRpcException;

    public String sendRawTransaction(String hex) throws BitcoinRpcException;

    public String sendToAddress(String toAddress, double amount) throws BitcoinRpcException;

    public String sendToAddress(String toAddress, double amount, String comment) throws BitcoinRpcException;

    /**
     * @param toAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param comment
     * @param commentTo
     * @return the transaction ID &lt;txid&gt; if successful
     * @throws BitcoinRpcException
     */
    public String sendToAddress(String toAddress, double amount, String comment, String commentTo) throws BitcoinRpcException;

    public String signRawTransaction(String hex, List<ExtendedTxInput> inputs, List<String> privateKeys) throws BitcoinRpcException;
    String signRawTransaction1(String hex, ExtendedTxInput input, List<String> privateKeys) throws BitcoinRpcException;



    /**
     * @param doGenerate a boolean indicating if blocks must be generated with the
     * cpu
     * @throws BitcoinRpcException
     */
    public void setGenerate(boolean doGenerate) throws BitcoinRpcException;


    public AddressValidationResult validateAddress(String address) throws BitcoinRpcException;

    public double getEstimateFee(int nBlocks) throws BitcoinRpcException;

    public double getEstimatePriority(int nBlocks) throws BitcoinRpcException;

    /**
     * In regtest mode, invalidates a block to create an orphan chain
     *
     * @param hash
     * @throws BitcoinRpcException
     */
    public void invalidateBlock(String hash) throws BitcoinRpcException;

    /**
     * In regtest mode, undo the invalidation of a block, possibly making it on
     * the top of the chain
     *
     * @param hash
     * @throws BitcoinRpcException
     */
    public void reconsiderBlock(String hash) throws BitcoinRpcException;


    List<PeerInfoResult> getPeerInfo();

    void stop();

    String getRawChangeAddress();

    long getConnectionCount();

    double getUnconfirmedBalance();

    double getDifficulty();

    void ping();


    NetTotals getNetTotals();

    boolean getGenerate();

    double getNetworkHashPs();

    boolean setTxFee(BigDecimal amount);



    String signMessage(String bitcoinAdress, String message);

    void importWallet(String filename);

    void keyPoolRefill();

    BigDecimal getReceivedByAccount(String account);



    void walletPassPhrase(String passPhrase, long timeOut);

    boolean verifyMessage(String bitcoinAddress, String signature, String message);


    boolean verifyChain();



    void submitBlock(String hexData);

    TxOut getTxOut(String txId, long vout);

}
