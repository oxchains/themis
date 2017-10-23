package com.oxchains.themis.chat.rest;

import com.oxchains.themis.chat.common.User;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oxchains.themis.chat.auth.UserToken;
import com.oxchains.themis.chat.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xuqi on 2017/10/12.
 */
@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user/login")
    public RestResp enroll(User user, HttpServletResponse response) {
      UserToken userToken = userService.tokenForUser(user);
      if(userToken!=null){
      }
      return userToken!=null? RestResp.success(userToken):RestResp.fail();
    }

    @RequestMapping("/user/findUser")
    public RestResp finduser(){
        List<User> list =  userService.findUser();
        return list==null?null:RestResp.success(list);
    }


}
