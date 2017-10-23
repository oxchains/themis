package com.oxchains.themis.chat.repo;

import com.oxchains.themis.chat.common.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/10/12.
 */
@Repository
public interface UserRepo extends CrudRepository<User,Integer> {
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByUsernameAndEmail(String username, String email);

}
