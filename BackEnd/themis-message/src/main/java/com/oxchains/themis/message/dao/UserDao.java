package com.oxchains.themis.message.dao;

import com.oxchains.themis.message.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ccl
 * @time 2017-10-12 17:20
 * @nameUserDao
 * @desc:
 */
@Repository
public interface UserDao extends CrudRepository<User,Long> {
    User findByLoginname(String loginname);
    Optional<User> findByLoginnameAndPassword(String loginname, String password);
    Optional<User> findByEmailAndPassword(String loginname, String password);
    Optional<User> findByMobilephoneAndPassword(String loginname, String password);
}
