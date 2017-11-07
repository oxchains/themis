package com.oxchains.themis.order.repo.message;

import com.oxchains.themis.order.entity.message.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuqi on 2017/11/7.
 */
@Repository
public interface MessageRepo extends CrudRepository<Message,Long> {
    List<Message> findByIdAndMessageTextId(Long id,Long textid);
}
