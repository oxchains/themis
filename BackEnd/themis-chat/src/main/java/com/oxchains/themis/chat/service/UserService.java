package com.oxchains.themis.chat.service;

import com.oxchains.themis.chat.auth.JwtService;
import com.oxchains.themis.chat.auth.UserToken;
import com.oxchains.themis.chat.common.User;
import com.oxchains.themis.chat.repo.UserRepo;
import com.oxchains.themis.chat.repo.UserTokenRepo;
import com.oxchains.themis.common.util.EncryptUtils;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuqi on 2017/10/12.
 */
@Service
public class UserService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private UserRepo userRepo;
    private UserTokenRepo userTokenRepo;
    private JwtService jwtService;

    public UserService(UserRepo userRepo, UserTokenRepo userTokenRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
        this.jwtService = jwtService;
    }

    public User findUserByUsernameAndPassword(String username, String password){
       return userRepo.findUserByUsernameAndPassword(username, EncryptUtils.encodeSHA256(password));
    };
    public UserToken tokenForUser(User user){
        User users = null;
        UserToken userToken =null;
        try {
            users = userRepo.findUserByUsernameAndPassword(user.getUsername(), EncryptUtils.encodeSHA256(user.getPassword()));
            if(users==null){
                return null;
            }
            userToken = new UserToken(users, jwtService.generate(users));
            userTokenRepo.save(userToken);
        }catch (Exception e){
             LOG.error("faild generate token : {}",e.getMessage(),e);
        }

        return userToken;
    }

    public List<User> findUser(){
        return IteratorUtils.toList(userRepo.findAll().iterator());
    }

}
