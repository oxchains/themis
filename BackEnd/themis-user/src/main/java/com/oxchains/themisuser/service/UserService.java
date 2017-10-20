package com.oxchains.themisuser.service;

import com.oxchains.common.model.RestResp;
import com.oxchains.common.util.ConstantUtils;
import com.oxchains.common.util.EncryptUtils;
import com.oxchains.themisuser.auth.JwtService;
import com.oxchains.themisuser.dao.UserDao;
import com.oxchains.themisuser.domain.User;
import com.oxchains.themisuser.domain.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @Author ccl
 * @Time 2017-10-12 17:24
 * @Name UserService
 * @Desc:
 */

@Transactional
@Service
public class UserService extends BaseService{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserDao userDao;

    @Resource
    JwtService jwtService;

    @Resource
    AccountService accountService;

    public RestResp addUser(User user){
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        Optional<User> optional=findUser(user);
        if (optional.isPresent()){
            return RestResp.fail("操作失败");
        }
        user = userDao.save(user);
        if (user == null){
            return RestResp.fail("操作失败");
        }
        //注册比特币账户
        String address = accountService.enrollAccount(user.getLoginname());

        return RestResp.success("操作成功");
    }
    public RestResp updateUser(User user){
        User u=userDao.findByLoginname(user.getLoginname());
        u.setUsername(user.getUsername());
        user = userDao.save(u);
        if (user == null){
            return RestResp.fail("操作失败");
        }
        return RestResp.success("操作成功");
    }

    public RestResp login(User user) {
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        Optional<User> optional=findUser(user);
        return optional.map(u -> {
            String token="Bearer "+jwtService.generate(user);
            logger.info("token = "+token);
            ConstantUtils.USER_TOKEN.put(u.getLoginname(),token);
            return RestResp.success("登录成功",new UserToken(u.getUsername(),token));
        }).orElse(RestResp.fail("登录失败"));
    }

    public Optional<User> findUser(User user){
        Optional<User> optional = null;
        if(null != user.getLoginname()){
            optional = userDao.findByLoginnameAndPassword(user.getLoginname(),user.getPassword());
        }
        if(null != user.getEmail()){
            optional = userDao.findByEmailAndPassword(user.getEmail(),user.getPassword());
        }
        if(null != user.getMobilephone()){
            optional = userDao.findByMobilephoneAndPassword(user.getMobilephone(),user.getPassword());
        }
        return optional;
    }
    public RestResp findUsers(){
        return RestResp.success(newArrayList(userDao.findAll()));
    }

}
