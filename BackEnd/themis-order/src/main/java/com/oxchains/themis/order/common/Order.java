package com.oxchains.themis.order.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.1.1.
 */
public final class Order extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60008054600160a060020a033316600160a060020a0319909116179055610d878061003b6000396000f300606060405236156100805763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663302f802c8114610085578063364f30f6146101e057806355184edc146102b157806396c7767e146103025780639c3f1e9014610397578063a6f9dae1146105a6578063e6ad63e9146105d2575b600080fd5b341561009057600080fd5b6101de60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405281815292919060208401838380828437509496506106e995505050505050565b005b34156101eb57600080fd5b61023160046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506107b095505050505050565b604051821515815260406020820181815290820183818151815260200191508051906020019080838360005b8381101561027557808201518382015260200161025d565b50505050905090810190601f1680156102a25780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34156102bc57600080fd5b61023160046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061088c95505050505050565b341561030d57600080fd5b61023160046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f016020809104026020016040519081016040528181529291906020840183838082843750949650509335935061092c92505050565b34156103a257600080fd5b6103ad600435610ac7565b6040516080808252855460026000196101006001841615020190911604908201819052819060208201906040830190606084019060a08501908a9080156104355780601f1061040a57610100808354040283529160200191610435565b820191906000526020600020905b81548152906001019060200180831161041857829003601f168201915b50508581038452885460026000196101006001841615020190911604808252602090910190899080156104a95780601f1061047e576101008083540402835291602001916104a9565b820191906000526020600020905b81548152906001019060200180831161048c57829003601f168201915b505085810383528754600260001961010060018416150201909116048082526020909101908890801561051d5780601f106104f25761010080835404028352916020019161051d565b820191906000526020600020905b81548152906001019060200180831161050057829003601f168201915b50508581038252865460026000196101006001841615020190911604808252602090910190879080156105915780601f1061056657610100808354040283529160200191610591565b820191906000526020600020905b81548152906001019060200180831161057457829003601f168201915b50509850505050505050505060405180910390f35b34156105b157600080fd5b6101de73ffffffffffffffffffffffffffffffffffffffff60043516610ae6565b34156105dd57600080fd5b6101de60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f016020809104026020016040519081016040528181529291906020840183838082843750949650610ba095505050505050565b600080543373ffffffffffffffffffffffffffffffffffffffff90811691161461071257600080fd5b61071b86610c50565b600081815260016020526040902090915085805161073d929160200190610c7b565b50600081815260016020819052604090912001848051610761929160200190610c7b565b506000818152600160205260409020600201838051610784929160200190610c7b565b5060008181526001602052604090206003018280516107a7929160200190610c7b565b50505050505050565b60006107ba610cf9565b60006107c584610c50565b905060018060008360001916600019168152602001908152602001600020600101808054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561087b5780601f106108505761010080835404028352916020019161087b565b820191906000526020600020905b81548152906001019060200180831161085e57829003601f168201915b505050505090509250925050915091565b6000610896610cf9565b60006108a184610c50565b905060018060008360001916600019168152602001908152602001600020600301808054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561087b5780601f106108505761010080835404028352916020019161087b565b6000610936610cf9565b60008061094287610c50565b915061094d86610c50565b90508460011415610a245760008281526001602081815260408084208585526004018252928390208201805492939092839260026101008388161502600019019092169190910491601f8301829004820290910190519081016040528092919081815260200182805460018160011615610100020316600290048015610a145780601f106109e957610100808354040283529160200191610a14565b820191906000526020600020905b8154815290600101906020018083116109f757829003601f168201915b5050505050905093509350610abd565b8460021415610abd576000828152600160208181526040808420858552600401825292839020600290810180549394909384936101008288161502600019019091169290920491601f830181900481020190519081016040528092919081815260200182805460018160011615610100020316600290048015610a145780601f106109e957610100808354040283529160200191610a14565b5050935093915050565b6001602081905260009182526040909120908101600282016003830184565b6000543373ffffffffffffffffffffffffffffffffffffffff908116911614610b0e57600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff191673ffffffffffffffffffffffffffffffffffffffff83811691909117918290557f9aecf86140d81442289f667eb72e1202a8fbb3478a686659952e145e8531965691168260405173ffffffffffffffffffffffffffffffffffffffff9283168152911660208201526040908101905180910390a150565b600080610bab610d0b565b610bb487610c50565b9250610bbf86610c50565b9150606060405190810160409081528782526020808301889052818301879052600086815260018252828120868252600401909152209091508190815181908051610c0e929160200190610c7b565b50602082015181600101908051610c29929160200190610c7b565b50604082015181600201908051610c44929160200190610c7b565b50505050505050505050565b6000610c5a610cf9565b508180511515610c6d5760009150610c75565b602083015191505b50919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610cbc57805160ff1916838001178555610ce9565b82800160010185558215610ce9579182015b82811115610ce9578251825591602001919060010190610cce565b50610cf5929150610d3e565b5090565b60206040519081016040526000815290565b606060405190810160405280610d1f610cf9565b8152602001610d2c610cf9565b8152602001610d39610cf9565b905290565b610d5891905b80821115610cf55760008155600101610d44565b905600a165627a7a723058202fa5ee4cc6a09f7f0055514048d54d5b2cd974f703c50e859b395ec8dcced8900029";

    private Order(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Order(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<ChangeOwnerEventResponse> getChangeOwnerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ChangeOwner", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ChangeOwnerEventResponse> responses = new ArrayList<ChangeOwnerEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ChangeOwnerEventResponse typedResponse = new ChangeOwnerEventResponse();
            typedResponse.oriOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ChangeOwnerEventResponse> changeOwnerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ChangeOwner", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ChangeOwnerEventResponse>() {
            @Override
            public ChangeOwnerEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ChangeOwnerEventResponse typedResponse = new ChangeOwnerEventResponse();
                typedResponse.oriOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> RequestHostingService(String orderId, String buyerId, String buyerEncryPrivKey, String sellerId, String sellerEncryPrivKey) {
        Function function = new Function(
                "RequestHostingService", 
                Arrays.<Type>asList(new Utf8String(orderId),
                new Utf8String(buyerId),
                new Utf8String(buyerEncryPrivKey),
                new Utf8String(sellerId),
                new Utf8String(sellerEncryPrivKey)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> GetEncryBuyerPrivKey(String orderId) {
        Function function = new Function(
                "GetEncryBuyerPrivKey", 
                Arrays.<Type>asList(new Utf8String(orderId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> GetEncrySellerPrivKey(String orderId) {
        Function function = new Function(
                "GetEncrySellerPrivKey", 
                Arrays.<Type>asList(new Utf8String(orderId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> GetTrusteeStoreBuyerOrSellerEncryPrivKey(String orderId, String trusteeId, BigInteger buyerOrSeller) {
        Function function = new Function(
                "GetTrusteeStoreBuyerOrSellerEncryPrivKey", 
                Arrays.<Type>asList(new Utf8String(orderId),
                new Utf8String(trusteeId),
                new org.web3j.abi.datatypes.generated.Uint256(buyerOrSeller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple4<String, String, String, String>> orders(byte[] param0) {
        final Function function = new Function("orders", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple4<String, String, String, String>>(
                new Callable<Tuple4<String, String, String, String>>() {
                    @Override
                    public Tuple4<String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple4<String, String, String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> changeOwner(String newOwner) {
        Function function = new Function(
                "changeOwner", 
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> RequestHostingServiceTrustee(String orderId, String trusteeId, String hostingEncryBuyerPrivKey, String hostingEncrySellerPrivKey) {
        Function function = new Function(
                "RequestHostingServiceTrustee", 
                Arrays.<Type>asList(new Utf8String(orderId),
                new Utf8String(trusteeId),
                new Utf8String(hostingEncryBuyerPrivKey),
                new Utf8String(hostingEncrySellerPrivKey)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Order> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Order.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Order> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Order.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Order load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Order(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Order load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Order(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ChangeOwnerEventResponse {
        public String oriOwner;

        public String newOwner;
    }
}
