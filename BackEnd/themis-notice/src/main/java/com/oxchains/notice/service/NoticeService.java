package com.oxchains.notice.service;

import com.oxchains.notice.common.RestResp;
import com.oxchains.notice.dao.NoticeDao;
import com.oxchains.notice.domain.Notice;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Service
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;

    /**
     * 发布公告需要传递的参数：
     * loginname        登录名
     * noticeType       公告类型(购买/出售)
     * location         地区
     * currency         货币类型
     * premium          溢价
     * price            价格
     * minPrice         最低价
     * minTxLimit       最小交易额度
     * maxTxLimit       最大交易额度
     * payType          支付方式
     * noticeContent    公告内容
     * @param notice
     * @return
     */
    public RestResp broadcastNotice(Notice notice){
        try {
            Optional<Notice> optional_1 = noticeDao.findByLoginnameAndNoticeType(notice.getLoginname(), notice.getNoticeType());
            Optional<Notice> optional_2 = noticeDao.findByLoginnameAndNoticeTypeAndTxStatus(notice.getLoginname(), notice.getNoticeType(), 1);
            if (optional_2.isPresent()){
                return RestResp.fail("已经有一条此类型公告且正在交易");
            } else if (optional_1.isPresent()) {
                return RestResp.fail("已经有一条此类型公告");
            } else {
                Notice n = noticeDao.save(notice);
                return RestResp.success("操作成功", n);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryAllNotice(){
        try {
            Iterable<Notice> it = noticeDao.findAll();
            if(it.iterator().hasNext()){
                return RestResp.success("操作成功", it);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 搜索公告需要传递的数据：
     * searchType   搜索类型(搜用户:0/搜公告)
     * noticeType   公告类型(出售/购买)
     * location     地区
     * currency     货币
     * payType      支付方式
     * @param notice
     * @return
     */
    public RestResp searchNotice(Notice notice){
        try {
            String location = notice.getLocation();
            String currency = notice.getCurrency();
            String payType = notice.getPayType();
            String noticeType = notice.getNoticeType();
            List<Notice> noticeList = null;
            if (null != location && null != currency && null != payType) {
                noticeList = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeType(location, currency, payType, noticeType);
            } else if (null != location && null != currency && null == payType) {
                noticeList = noticeDao.findByLocationAndCurrencyAndNoticeType(location, currency, noticeType);
            } else if (null != location && null == currency && null != payType) {
                noticeList = noticeDao.findByLocationAndPayTypeAndNoticeType(location, payType, noticeType);
            } else if (null == location && null != currency && null != payType) {
                noticeList = noticeDao.findByCurrencyAndPayTypeAndNoticeType(currency, payType, noticeType);
            } else if (null != location && null == currency && null == payType) {
                noticeList = noticeDao.findByLocationAndNoticeType(location, noticeType);
            } else if (null == location && null == currency && null != payType) {
                noticeList = noticeDao.findByPayTypeAndNoticeType(payType, noticeType);
            } else if (null == location && null != currency && null != payType) {
                noticeList = noticeDao.findByCurrencyAndNoticeType(currency, noticeType);
            } else if (null == location && null == currency && null == payType) {
                noticeList = noticeDao.findByNoticeType(noticeType);
            } else {
                return RestResp.fail("操作失败");
            }
            return RestResp.success(noticeList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail("操作失败");
    }
}
