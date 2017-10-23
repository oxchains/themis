package com.oxchains.themis.user.rest;

<<<<<<< HEAD:BackEnd/themis-user/src/main/java/com/oxchains/themisuser/rest/UserController.java
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.VerifyCodeUtils;
import com.oxchains.themisuser.domain.User;
import com.oxchains.themisuser.service.UserService;
=======
import com.oxchains.common.model.RestResp;
import com.oxchains.common.util.VerifyCodeUtils;
import com.oxchains.themis.user.domain.User;
import com.oxchains.themis.user.service.UserService;
>>>>>>> ffe9dab4d05c462f166e3295cebed61ff970bd49:BackEnd/themis-user/src/main/java/com/oxchains/themis/user/rest/UserController.java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public RestResp register(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping(value = "/login")
    public RestResp login(@RequestBody User user){
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
