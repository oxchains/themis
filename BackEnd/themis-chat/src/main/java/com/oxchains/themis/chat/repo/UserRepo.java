package com.oxchains.themis.chat.repo;

import com.oxchains.themis.chat.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * create by huohuo
 * @author huohuo
 */
@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByUsernameAndEmail(String username, String email);

}
