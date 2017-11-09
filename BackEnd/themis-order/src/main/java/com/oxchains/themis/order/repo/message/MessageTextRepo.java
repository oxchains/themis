package com.oxchains.themis.order.repo.message;

import com.oxchains.themis.order.entity.message.MessageText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/11/7.
 */
@Repository
public interface MessageTextRepo extends CrudRepository<MessageText,Long>{
}
