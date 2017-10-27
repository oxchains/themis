package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/25.
 */
@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    List<User> findUserByRoleId(Long id);
}
