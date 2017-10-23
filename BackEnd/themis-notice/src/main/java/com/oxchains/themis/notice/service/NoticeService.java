package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.dao.NoticeDao;
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
            List<Notice> noticeListUnDone = noticeDao.findByLoginnameAndNoticeTypeAndTxStatus(notice.getLoginname(), notice.getNoticeType(), 0);
            List<Notice> noticeListDoing = noticeDao.findByLoginnameAndNoticeTypeAndTxStatus(notice.getLoginname(), notice.getNoticeType(), 1);
            List<Notice> noticeListDone = noticeDao.findByLoginnameAndNoticeTypeAndTxStatus(notice.getLoginname(), notice.getNoticeType(), 2);

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
            List<Notice> buyNoticeList = noticeDao.findByNoticeType("购买");
            List<Notice> sellNoticeList = noticeDao.findByNoticeType("出售");

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

    public RestResp querMeNotice(String loginname, String noticeType){
        try {
            List<Notice> noticeList = noticeDao.findByLoginnameAndNoticeType(loginname, noticeType);
            return RestResp.success("操作成功", noticeList);
        }catch (Exception e){
            return RestResp.fail("操作失败", e.getMessage());
        }
    }
}
