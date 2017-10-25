package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/10/25.
 */
@Repository
public interface UserRepo extends CrudRepository<User,Long> {
}
