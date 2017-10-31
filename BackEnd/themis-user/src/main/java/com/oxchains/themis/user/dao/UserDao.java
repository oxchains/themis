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
public interface UserDao extends CrudRepository<User,Integer> {
    /**
     * find by loginname
     * @param loginname
     * @return
     */
    User findByLoginname(String loginname);

    /**
     *
     * @param loginname
     * @param password
     * @return
     */
    Optional<User> findByLoginnameAndPassword(String loginname,String password);

    /**
     *
     * @param loginname
     * @param password
     * @return
     */
    Optional<User> findByEmailAndPassword(String loginname,String password);

    /**
     *
     * @param loginname
     * @param password
     * @return
     */
    Optional<User> findByMobilephoneAndPassword(String loginname,String password);
}
