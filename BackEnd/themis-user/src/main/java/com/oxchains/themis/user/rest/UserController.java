package com.oxchains.themis.user.rest;


import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.VerifyCodeUtils;

import com.oxchains.themis.user.domain.User;
import com.oxchains.themis.user.service.UserService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2017-10-12 18:19
 * @name UserController
 * @desc:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping(value = "/register")
    public RestResp register(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping(value = "/login")
    public RestResp login(@RequestBody User user){
        return userService.login(user);
    }



    @PostMapping(value = "/update")
    public RestResp update(@RequestBody User user){
        return userService.updateUser(user);
    }
    @GetMapping(value = "/user")
    public RestResp list(){
        return userService.findUsers();
    }

    /**
     * Verification Code
     * @return
     */
    @GetMapping(value = "/verifyCode")
    public RestResp verifyCode(){
        return RestResp.success(VerifyCodeUtils.getRandCode(6));
    }

}
