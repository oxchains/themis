package com.oxchains.themis.arbitrate.repo.message;

import com.oxchains.themis.arbitrate.entity.message.MessageText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/11/7.
 */
@Repository
public interface MessageTextRepo extends CrudRepository<MessageText,Long>{
}
