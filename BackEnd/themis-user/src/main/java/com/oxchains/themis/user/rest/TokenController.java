package com.oxchains.themis.user.rest;

import com.oxchains.common.model.RestResp;
import com.oxchains.themis.user.auth.JwtService;
import com.oxchains.themis.user.domain.User;
import com.oxchains.themis.user.service.UserService;
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
