package oxchains.chat.service;

import org.apache.commons.collections.IteratorUtils;
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
        User users = userRepo.findUserByUsernameAndPassword(user.getUsername(), EncryptUtils.encodeSHA256(user.getPassword()));
        if(users==null){
            return null;
        }
        UserToken userToken = new UserToken(users, jwtService.generate(users));
        userTokenRepo.save(userToken);
        return userToken;
    }

    public List<User> findUser(){
        return IteratorUtils.toList(userRepo.findAll().iterator());
    }

}
