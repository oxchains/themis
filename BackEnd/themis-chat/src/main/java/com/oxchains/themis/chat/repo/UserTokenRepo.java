package com.oxchains.themis.chat.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.oxchains.themis.chat.auth.UserToken;

/**
 * create by huohuo
 * @author huohuo
 */
@Repository
public interface UserTokenRepo extends CrudRepository<UserToken,String> {

}
