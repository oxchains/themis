package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Repository
public interface NoticeDao extends CrudRepository<Notice,Long> {

    // 发布公告需要提前判断的接口

    List<Notice> findByUserIdAndNoticeType(Long loginName, Long noticeType);
    List<Notice> findByUserIdAndNoticeTypeAndTxStatus(Long loginName, Long noticeType, Integer txStatus);

    // 搜索广告用到的接口

    List<Notice> findByNoticeType(Long noticeType);
    List<Notice> findByLocationAndNoticeType(Long location, Long noticeType);
    List<Notice> findByCurrencyAndNoticeType(Long currency, Long noticeType);
    List<Notice> findByPayTypeAndNoticeType(Long payType, Long noticeType);
    List<Notice> findByLocationAndCurrencyAndNoticeType(Long location, Long currency, Long noticeType);
    List<Notice> findByLocationAndPayTypeAndNoticeType(Long location, Long payType, Long noticeType);
    List<Notice> findByCurrencyAndPayTypeAndNoticeType(Long currency, Long payType, Long noticeType);
    List<Notice> findByLocationAndCurrencyAndPayTypeAndNoticeType(Long location, Long currency, Long payType, Long noticeType);

    // 查询所有未完成订单

    List<Notice> findByTxStatus(Integer txStatus);

}