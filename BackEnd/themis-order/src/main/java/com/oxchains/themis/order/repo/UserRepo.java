package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    List<User> findUserByRoleId(Long id);
}
