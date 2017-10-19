package com.oxchains.themisuser.rest;

import com.oxchains.themisuser.domain.RestResp;
import com.oxchains.themisuser.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @GetMapping(value = "/balance/{accountName}")
    public RestResp getBalance(@PathVariable String accountName) {
        return RestResp.success(accountService.getBalance(accountName));
    }

    @GetMapping(value = "/address/{accountName}")
    public RestResp getNewAddress(@PathVariable String accountName) {
        return RestResp.success(accountService.getAddress(accountName));
    }

    @PostMapping(value = "/transfer/{accountName}")
    public RestResp transferAccounts(@PathVariable String accountName, String recvAddress,double amount,String pubKeys,int nRequired) {
        return accountService.createTransaction(accountName,recvAddress,amount,Arrays.asList(pubKeys.split(",")),nRequired);
    }

    @PostMapping(value = "/confirm/{accountName}")
    public RestResp confirmTransaction(@PathVariable String accountName, String recvAddress,double amount,String prvKeys) {
        return accountService.confirmTransaction(recvAddress,amount,Arrays.asList(prvKeys.split(",")),1);
    }

    @PostMapping(value = "/cancel/{accountName}")
    public RestResp cancelTransaction(@PathVariable String accountName, String recvAddress,double amount,String prvKeys) {
        return accountService.confirmTransaction(recvAddress,amount,Arrays.asList(prvKeys.split(",")),0);
    }
}
