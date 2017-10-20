package com.oxchains.themisuser.rest;

import com.oxchains.common.model.RestResp;
import com.oxchains.themisuser.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Author ccl
 * @Time 2017-10-16 17:44
 * @Name AccountController
 * @Desc:
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 查询余额
     * @param accountName
     * @return
     */
    @GetMapping(value = "/balance/{accountName}")
    public RestResp getBalance(@PathVariable String accountName) {
        return RestResp.success(accountService.getBalance(accountName));
    }

    /**
     * 获取交易地址
     * @param accountName
     * @return
     */
    @GetMapping(value = "/address/{accountName}")
    public RestResp getNewAddress(@PathVariable String accountName) {
        return RestResp.success(accountService.getAddress(accountName));
    }

    /**
     * 转账(协商地址)
     * @param accountName
     * @param recvAddress
     * @param amount
     * @param pubKeys
     * @param nRequired
     * @return
     */
    @PostMapping(value = "/transfer/{accountName}")
    public RestResp transferAccounts(@PathVariable String accountName, String recvAddress,double amount,String pubKeys,int nRequired) {
        return accountService.createTransaction(accountName,recvAddress,amount,Arrays.asList(pubKeys.split(",")),nRequired);
    }

    /**
     * 确认交易
     * @param accountName
     * @param recvAddress
     * @param amount
     * @param prvKeys
     * @return
     */
    @PostMapping(value = "/confirm/{accountName}")
    public RestResp confirmTransaction(@PathVariable String accountName, String recvAddress,double amount,String prvKeys) {
        return accountService.confirmTransaction(recvAddress,amount,Arrays.asList(prvKeys.split(",")),1);
    }

    /**
     * 取消交易
     * @param accountName
     * @param recvAddress
     * @param amount
     * @param prvKeys
     * @return
     */
    @PostMapping(value = "/cancel/{accountName}")
    public RestResp cancelTransaction(@PathVariable String accountName, String recvAddress,double amount,String prvKeys) {
        return accountService.confirmTransaction(recvAddress,amount,Arrays.asList(prvKeys.split(",")),0);
    }

    /**
     * 获取公私钥
     * @param accountName
     * @return
     */
    @GetMapping(value = "/key/{accountName}")
    public RestResp getKeys(@PathVariable String accountName){
        return accountService.getKeys(accountName);
    }
}
