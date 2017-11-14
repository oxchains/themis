package com.oxchains.themis.order.repo;

import com.oxchains.themis.order.entity.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/24.
 * @author huohuo
 */
@Repository
public interface NoticeRepo extends CrudRepository<Notice,Long> {
    Notice findNoticeByUserIdAndTxStatusIsNotAndNoticeType(Long userid,Integer status,Long typeid);
}
