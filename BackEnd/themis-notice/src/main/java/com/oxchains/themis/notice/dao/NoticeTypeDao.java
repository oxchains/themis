package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Currency;
import com.oxchains.themis.notice.domain.NoticeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Luo_xuri on 2017/10/26.
 */
@Repository
public interface NoticeTypeDao extends CrudRepository<NoticeType, Long> {
}
