package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.dao.BTCTickerDao;
import com.oxchains.themis.notice.dao.NoticeDao;
import com.oxchains.themis.notice.domain.BTCTicker;
import com.oxchains.themis.notice.domain.Notice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Service
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private BTCTickerDao btcTickerDao;

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
            List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), 0);
            List<Notice> noticeListDoing = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), 1);
            List<Notice> noticeListDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), 2);

            if (!noticeListDone.isEmpty() && noticeListDoing.isEmpty()){
                Notice n = noticeDao.save(notice);
                return RestResp.success("操作成功", n);
            }else {
                if (!noticeListDoing.isEmpty()){
                    return RestResp.fail("已经有一条此类型公告且正在交易");
                } else if (!noticeListUnDone.isEmpty()) {
                    return RestResp.fail("已经有一条此类型公告");
                } else {
                    Notice n = noticeDao.save(notice);
                    return RestResp.success("操作成功", n);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryPartNotice(){
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeType(1L);
            List<Notice> sellNoticeList = noticeDao.findByNoticeType(2L);

            int buySize = new Random().nextInt(buyNoticeList.size() - 2);
            int sellSize = new Random().nextInt(sellNoticeList.size() - 2);

            if (buyNoticeList.size() > 2 && sellNoticeList.size() > 2){
                partList.addAll(buyNoticeList.subList(buySize, buySize + 2));
                partList.addAll(sellNoticeList.subList(sellSize, sellSize + 2));
            }else if (buyNoticeList.size() < 2 && sellNoticeList.size() > 2){
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList.subList(sellSize, sellSize + 2));
            }else if (buyNoticeList.size() > 2 && sellNoticeList.size() < 2){
                partList.addAll(buyNoticeList.subList(buySize, buySize + 2));
                partList.addAll(sellNoticeList);
            }else {
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList);
            }
            return RestResp.success("操作成功", partList);
        }catch (Exception e){
            return RestResp.fail("操作失败", e.getMessage());
        }
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

    public RestResp querAllUnDone(){
        try {
            List<Notice> noticeList = noticeDao.findByTxStatus(0);
            if (!noticeList.isEmpty()){
                return RestResp.success("操作成功", noticeList);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e) {
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    /**
     * 搜索公告需要传递的数据：
     * searchType   搜索类型(搜公告:0/搜用户:非0)
     * noticeType   公告类型(出售/购买)
     * location     地区(可选)
     * currency     货币(可选)
     * payType      支付方式(可选)
     * @param notice
     * @return
     */
    public RestResp searchNotice(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = notice.getNoticeType();
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

    public RestResp querMeNotice(Long userId, Long noticeType){
        try {
            List<Notice> noticeList = noticeDao.findByUserIdAndNoticeType(userId, noticeType);
            return RestResp.success("操作成功", noticeList);
        }catch (Exception e){
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp queryBTCMarket(){
        try {
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            if (!btcTickerList.isEmpty()){
                return RestResp.success("操作成功", btcTickerList);
            }else {
                return RestResp.fail("操作失败");
            }

            // TODO 关于价格之间的计算
            /*Double premium = notice.getPremium();// 溢价
            BTCTicker btcTicker = btcTickerDao.findBySymbol("btccny");
            BigDecimal last = btcTicker.getLast();
            String newLast = last.toString();

            Double m = premium/100;
            System.out.println("溢价倍数：" + m);
            Double n = 1 + m;
            String per = n.toString();

            // 基于溢价之后的价格
            BigDecimal price = ArithmeticUtils.multiply(newLast, per);*/
        }catch (Exception e){
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

}
