package com.oxchains.themis.chat.service;

import com.oxchains.themis.chat.entity.User;
import com.oxchains.themis.chat.repo.UserRepo;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * create by huohuo
 * @author huohuo
 */
@Service
public class UserService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserRepo userRepo;
    public List<User> findUser(){
        return IteratorUtils.toList(userRepo.findAll().iterator());
    }

}
