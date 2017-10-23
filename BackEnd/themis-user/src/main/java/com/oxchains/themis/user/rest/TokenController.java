package com.oxchains.themis.user.rest;

<<<<<<< HEAD:BackEnd/themis-user/src/main/java/com/oxchains/themisuser/rest/TokenController.java
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themisuser.auth.JwtService;
import com.oxchains.themisuser.domain.User;
import com.oxchains.themisuser.service.UserService;
=======
import com.oxchains.common.model.RestResp;
import com.oxchains.themis.user.auth.JwtService;
import com.oxchains.themis.user.domain.User;
import com.oxchains.themis.user.service.UserService;
>>>>>>> ffe9dab4d05c462f166e3295cebed61ff970bd49:BackEnd/themis-user/src/main/java/com/oxchains/themis/user/rest/TokenController.java
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
