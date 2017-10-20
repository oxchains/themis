package com.oxchains.themisuser.rest;

import com.oxchains.common.model.RestResp;
import com.oxchains.themisuser.auth.JwtService;
import com.oxchains.themisuser.domain.User;
import com.oxchains.themisuser.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author ccl
 * @Time 2017-10-13 10:32
 * @Name TokenController
 * @Desc:
 */
@RestController
@RequestMapping(value = "/token")
public class TokenController {
    @Resource
    JwtService jwtService;

    @Resource
    UserService userService;

    @PostMapping
    public RestResp token(User user){
        return userService.findUser(user).map(u -> {
          String token = jwtService.generate(u);
          return RestResp.success(token);
        }).orElse(RestResp.fail());
    }
}
