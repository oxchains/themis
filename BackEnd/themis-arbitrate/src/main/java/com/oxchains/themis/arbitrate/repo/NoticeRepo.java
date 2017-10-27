package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/24.
 */
@Repository
public interface NoticeRepo extends CrudRepository<Notice,Long> {
    List<Notice> findNoticeByUserIdAndTxStatusIsNot(Long id, Integer status);
    Notice findNoticeByUserIdAndTxStatusIsNotAndNoticeType(Long userid, Integer status, Long typeid);
}
