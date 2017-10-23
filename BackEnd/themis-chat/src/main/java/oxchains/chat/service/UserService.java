package oxchains.chat.service;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oxchains.chat.auth.JwtService;
import oxchains.chat.auth.UserToken;
import oxchains.chat.common.EncryptUtils;
import oxchains.chat.common.User;
import oxchains.chat.repo.UserRepo;
import oxchains.chat.repo.UserTokenRepo;

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
             LOG.debug("faild generate token:",e.getMessage());
        }

        return userToken;
    }

    public List<User> findUser(){
        return IteratorUtils.toList(userRepo.findAll().iterator());
    }

}
