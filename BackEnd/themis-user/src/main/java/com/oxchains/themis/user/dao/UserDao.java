package com.oxchains.themis.user.dao;


import com.oxchains.themis.user.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ccl
 * @time 2017-10-12 17:20
 * @name UserDao
 * @desc:
 */
@Repository
public interface UserDao extends CrudRepository<User,Long> {
    /**
     * find by loginname
     * @param loginname
     * @return User
     */
    User findByLoginname(String loginname);

    /**
     * 通过手机查找
     * @param mobilephone
     * @return User
     */
    User findByMobilephone(String mobilephone);

    /**
     * find by login and password
     * @param loginname
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByLoginnameAndPassword(String loginname,String password);

    /**
     * find by email and password
     * @param loginname
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByEmailAndPassword(String loginname,String password);

    /**
     * find by phone and password
     * @param loginname
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByMobilephoneAndPassword(String loginname,String password);
}
