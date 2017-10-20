package com.oxchains.themisuser.rest;

import com.oxchains.themisuser.domain.RestResp;
import com.oxchains.themisuser.domain.User;
import com.oxchains.themisuser.service.UserService;
import com.oxchains.themisuser.util.VerifyCodeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author ccl
 * @Time 2017-10-12 18:19
 * @Name UserController
 * @Desc:
 */
@RestController
public class UserController {
    @Resource
    UserService userService;

    @PostMapping(value = "/register")
    public RestResp register(User user){
        return userService.addUser(user);
    }

    @PostMapping(value = "/login")
    public RestResp login(User user){
        return userService.login(user);
    }

    @PostMapping(value = "/update")
    public RestResp update(User user){
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
