package com.oxchains.themis.user.dao;

import com.oxchains.themis.user.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ccl
 * @Time 2017-10-26 10:16
 * @Name RoleDao
 * @Desc:
 */
@Repository
public interface RoleDao extends CrudRepository<Role,Integer> {
    Role findById(Long id);
}
