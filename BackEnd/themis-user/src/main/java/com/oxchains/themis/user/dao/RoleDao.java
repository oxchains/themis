package com.oxchains.themis.user.dao;

import com.oxchains.themis.user.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2017-10-26 10:16
 * @name RoleDao
 * @desc:
 */
@Repository
public interface RoleDao extends CrudRepository<Role,Integer> {
    /**
     * find role info by id
     * @param id
     * @return
     */
    Role findById(Long id);
}
