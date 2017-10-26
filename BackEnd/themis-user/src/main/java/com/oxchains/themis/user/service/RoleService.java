package com.oxchains.themis.user.service;

import com.oxchains.themis.user.dao.RoleDao;
import com.oxchains.themis.user.domain.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ccl
 * @Time 2017-10-26 10:19
 * @Name RoleService
 * @Desc:
 */
@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;

    public Role findById(Long id){
        return roleDao.findById(id);
    }
}
