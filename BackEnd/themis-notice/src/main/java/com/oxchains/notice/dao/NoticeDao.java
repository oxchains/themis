package com.oxchains.notice.dao;

import com.oxchains.notice.domain.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Repository
public interface NoticeDao extends CrudRepository<Notice,Long> {

    // 发布公告需要提前判断的接口
    Optional<Notice> findByLoginnameAndNoticeType(String loginName, String noticeType);
    Optional<Notice> findByLoginnameAndNoticeTypeAndTxStatus(String loginName, String noticeType, int txStatus);

    // 搜索广告用到的接口
    List<Notice> findByNoticeType(String noticeType);
    List<Notice> findByLocationAndNoticeType(String location, String noticeType);
    List<Notice> findByCurrencyAndNoticeType(String currency, String noticeType);
    List<Notice> findByPayTypeAndNoticeType(String payType, String noticeType);
    List<Notice> findByLocationAndCurrencyAndNoticeType(String location, String currency, String noticeType);
    List<Notice> findByLocationAndPayTypeAndNoticeType(String location, String payType, String noticeType);
    List<Notice> findByCurrencyAndPayTypeAndNoticeType(String currency, String payType, String noticeType);
    List<Notice> findByLocationAndCurrencyAndPayTypeAndNoticeType(String location, String currency, String payType, String noticeType);
}