package com.oxchains.themisuser.dao;


import com.oxchains.themisuser.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author ccl
 * @Time 2017-10-12 17:20
 * @Name UserDao
 * @Desc:
 */
@Repository
public interface UserDao extends CrudRepository<User,Integer> {
    User findByLoginname(String loginname);
    Optional<User> findByLoginnameAndPassword(String loginname,String password);
    Optional<User> findByEmailAndPassword(String loginname,String password);
    Optional<User> findByMobilephoneAndPassword(String loginname,String password);
}
