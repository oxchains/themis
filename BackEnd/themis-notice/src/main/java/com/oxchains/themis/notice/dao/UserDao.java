package com.oxchains.themis.notice.dao;


import com.oxchains.themis.notice.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ccl
 * @time 2017-10-12 17:20
 * @nameUserDao
 * @desc:
 */
@Repository
public interface UserDao extends CrudRepository<User,Integer> {
    User findByLoginname(String loginname);
    Optional<User> findByLoginnameAndPassword(String loginname, String password);
    Optional<User> findByEmailAndPassword(String loginname, String password);
    Optional<User> findByMobilephoneAndPassword(String loginname, String password);
}
