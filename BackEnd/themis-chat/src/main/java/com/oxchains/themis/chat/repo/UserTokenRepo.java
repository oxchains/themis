package com.oxchains.themis.chat.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.oxchains.themis.chat.auth.UserToken;

/**
 * Created by xuqi on 2017/10/13.
 */
@Repository
public interface UserTokenRepo extends CrudRepository<UserToken,String> {

}
