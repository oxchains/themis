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
    List<Notice> findByLoginnameAndNoticeType(String loginName, String noticeType);
    List<Notice> findByLoginnameAndNoticeTypeAndTxStatus(String loginName, String noticeType, int txStatus);

    // 搜索广告用到的接口
    List<Notice> findByNoticeType(String noticeType);
    List<Notice> findByLocationAndNoticeType(String location, String noticeType);
    List<Notice> findByCurrencyAndNoticeType(String currency, String noticeType);
    List<Notice> findByPayTypeAndNoticeType(String payType, String noticeType);
    List<Notice> findByLocationAndCurrencyAndNoticeType(String location, String currency, String noticeType);
    List<Notice> findByLocationAndPayTypeAndNoticeType(String location, String payType, String noticeType);
    List<Notice> findByCurrencyAndPayTypeAndNoticeType(String currency, String payType, String noticeType);
    List<Notice> findByLocationAndCurrencyAndPayTypeAndNoticeType(String location, String currency, String payType, String noticeType);

    // 查询所有未完成订单
    List<Notice> findByTxStatus(Integer txStatus);
}