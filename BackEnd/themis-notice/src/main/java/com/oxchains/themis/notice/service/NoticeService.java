package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.notice.dao.*;
import com.oxchains.themis.notice.domain.*;
import com.oxchains.themis.notice.domain.Currency;
import com.oxchains.themis.notice.rest.dto.PageDTO;
import com.oxchains.themis.notice.rest.dto.StatusDTO;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Service
public class NoticeService {

    @Resource private NoticeDao noticeDao;
    @Resource private BTCTickerDao btcTickerDao;
    @Resource private BTCResultDao btcResultDao;
    @Resource private BTCMarketDao btcMarketDao;
    @Resource private UserDao userDao;
    @Resource private CountryDao countryDao;
    @Resource private CurrencyDao currencyDao;
    @Resource private NoticeTypeDao noticeTypeDao;
    @Resource private PaymentDao paymentDao;
    @Resource private UserTxDetailDao userTxDetailDao;

    /**
     * 发布公告需要传递的参数：
     * userId           关联user表的id
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

            User userInfo = userDao.findOne(notice.getUserId().intValue());
            String loginname = userInfo.getLoginname();

            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            for (BTCTicker btcTicker : btcTickerList) {
                Double low = btcTicker.getLow().doubleValue();
                Double minPrice = notice.getMinPrice().doubleValue();
                if (null == notice.getMinPrice()){
                    notice.setMinPrice(btcTicker.getLow());
                }else {
                    // 市场价低于定义的最低价，那么价格就是自己定义的最低价
                    if (ArithmeticUtils.minus(low, minPrice) < 0){
                        notice.setPrice(notice.getMinPrice());
                    }
                }
            }


            if (!noticeListDone.isEmpty() && noticeListDoing.isEmpty()){
                notice.setLoginname(loginname);
                Notice n = noticeDao.save(notice);
                return RestResp.success("操作成功", n);
            }else {
                if (!noticeListDoing.isEmpty()){
                    return RestResp.fail("已经有一条此类型公告且正在交易");
                } else if (!noticeListUnDone.isEmpty()) {
                    return RestResp.fail("已经有一条此类型公告");
                } else {
                    notice.setLoginname(loginname);
                    Notice n = noticeDao.save(notice);
                    return RestResp.success("操作成功", n);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }

    }

    public RestResp queryPartNotice(){
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeType(1L);
            List<Notice> sellNoticeList = noticeDao.findByNoticeType(2L);

            // ====== start 获取交易次数，信任人数，信誉度 ======
            int randomTxNum = new Random().nextInt(1000); // 随机交易次数
            int randomTtustNum = new Random().nextInt(randomTxNum); // 随机信任人数(不超过交易次数)
            int randomTrustPercent = new Random().nextInt(10) + 90; // 随机信任度(90-100)
            for (int i = 0; i < buyNoticeList.size(); i++){
                buyNoticeList.get(i).setTxNum(randomTxNum);
                buyNoticeList.get(i).setTrustNum(randomTtustNum);
                buyNoticeList.get(i).setTrustPercent(randomTrustPercent);

                // ====== start 获取姓名 ======user表没有数据
                /*Long userId = buyNoticeList.get(i).getUserId();
                User userInfo = userDao.findOne(userId.intValue());
                buyNoticeList.get(i).setLoginname(userInfo.getLoginname());*/

            }
            for (int i = 0; i < sellNoticeList.size(); i++){
                sellNoticeList.get(i).setTxNum(randomTxNum);
                sellNoticeList.get(i).setTrustNum(randomTtustNum);
                sellNoticeList.get(i).setTrustPercent(randomTrustPercent);
            }
            // ====== end ======

            if (buyNoticeList.size() > 2 && sellNoticeList.size() > 2){
                int buySize = new Random().nextInt(buyNoticeList.size() - 2);
                int sellSize = new Random().nextInt(sellNoticeList.size() - 2);
                // ====== start 从对应表中获取数据 ======
                /*List<Notice> subBuyList = buyNoticeList.subList(buySize, buySize + 2);
                for (int i = 0; i < subBuyList.size(); i++){
                    UserTxDetail userTxDetailInfo = userTxDetailDao.findOne(subBuyList.get(i).getUserId());
                    Integer txNum = userTxDetailInfo.getTxNum();//交易次数
                    Integer goodDesc = userTxDetailInfo.getGoodDesc();//好评
                    Integer badDesc = userTxDetailInfo.getBadDesc();//差评
                    Integer believNum = userTxDetailInfo.getBelieveNum();//信任数

                    subBuyList.get(i).setTxNum(txNum);
                    subBuyList.get(i).setTrustNum(believNum);
                    subBuyList.get(i).setTrustPercent((believNum/txNum)*100);
                }*/
                // List<Notice> subSellList = sellNoticeList.subList(sellSize, sellSize + 2);

                // 将下面addAll中的换成subBuyList
                // ====== end ======
                partList.addAll(buyNoticeList.subList(buySize, buySize + 2));
                partList.addAll(sellNoticeList.subList(sellSize, sellSize + 2));
            }else if (buyNoticeList.size() < 2 && sellNoticeList.size() > 2){
                int sellSize = new Random().nextInt(sellNoticeList.size() - 2);
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList.subList(sellSize, sellSize + 2));
            }else if (buyNoticeList.size() > 2 && sellNoticeList.size() < 2){
                int buySize = new Random().nextInt(buyNoticeList.size() - 2);
                partList.addAll(buyNoticeList.subList(buySize, buySize + 2));
                partList.addAll(sellNoticeList);
            }else {
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList);
            }
            return RestResp.success("操作成功", partList);
        }catch (Exception e){
            e.printStackTrace();
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
            return RestResp.fail("操作失败", e.getMessage());
        }
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
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

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
            return RestResp.fail("操作失败", e.getMessage());
        }

    }

    public RestResp querMeNotice(Long userId, Long noticeType){
        try {
            List<Notice> noticeList = noticeDao.findByUserIdAndNoticeType(userId, noticeType);
            return RestResp.success("操作成功", noticeList);
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp queryMeAllNotice(Long userId, Long noticeType, Integer txStatus){
        try {
            List<Notice> resultList = new ArrayList<>();
            if (txStatus == 2){
                List<Notice> noticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, 2);
                resultList.addAll(noticeList);
            } else {
                List<Notice> unDoneNoticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, 0);
                List<Notice> doingNoticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, 1);
                resultList.addAll(unDoneNoticeList);
                resultList.addAll(doingNoticeList);
            }
            return RestResp.success("操作成功", resultList);
        }catch (Exception e){
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp queryBTCPrice(){
        try {
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            if (!btcTickerList.isEmpty()){
                return RestResp.success("操作成功", btcTickerList);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp queryBTCMarket(){
        try {
            List<BTCResult> btcResultList = btcResultDao.findByIsSuc("true");
            List<BTCMarket> btcMarketList = btcMarketDao.findBySymbol("huobibtccny");
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            BTCResult btcResult = null;
            BTCMarket btcMarket = null;
            BTCTicker btcTicker = null;
            for (int i = 0; i < btcResultList.size(); i++){
                btcResult = btcResultList.get(i);
            }
            for (int i = 0; i < btcMarketList.size(); i++){
                btcMarket = btcMarketList.get(i);
            }
            for (int i = 0; i < btcTickerList.size(); i++){
                btcTicker = btcTickerList.get(i);
            }
            btcMarket.setTicker(btcTicker);
            btcResult.setDatas(btcMarket);
            return RestResp.success("操作成功", btcResultList);
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp searchPageAll(Integer pageNum, Integer pageSize){
        try {
            Pageable pageable = buildPageRequest(pageNum, pageSize, null);
            Page<Notice> page = noticeDao.findAll(pageable);
            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }
            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    /**
     * 创建分页请求
     * @param pageNum   当前第几页
     * @param pageSize  每页显示的数量
     * @param sortType  排序
     * @return
     */
    private PageRequest buildPageRequest(Integer pageNum, Integer pageSize, String sortType){
        Sort sort = null;
        if("auto".equals(sortType)){
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("noticeContent".equals(sortType)){
            sort = new Sort(Sort.Direction.ASC, "noticeContent");
        }
        return new PageRequest(pageNum - 1, pageSize, sort);
    }

    public RestResp searchPage(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = notice.getNoticeType();
            Integer pageNum = notice.getPageNum();
            // Integer pageSize = notice.getPageSize();
            // Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pageable = buildPageRequest(pageNum, 8, null);
            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeType(location, currency, payType, noticeType, pageable);
            }else if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeType(location, currency, noticeType, pageable);
            } else if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeType(location, payType, noticeType, pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeType(currency, payType, noticeType, pageable);
            } else if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeType(location, noticeType, pageable);
            } else if (null == location && null == currency && null != payType) {
                page = noticeDao.findByPayTypeAndNoticeType(payType, noticeType, pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndNoticeType(currency, noticeType, pageable);
            } else if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeType(noticeType, pageable);
            }else {
                return RestResp.fail("操作失败");
            }
            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }

            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(8);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp defaultSearch(Long noticeType){
        try {
            Pageable pageable = buildPageRequest(1, 8, null);
            Page<Notice> page = noticeDao.findByNoticeType(noticeType, pageable);

            // ====== start 获取交易次数，信任人数，信誉度 ======
            List<Notice> buyNoticeList = noticeDao.findByNoticeType(1L);
            List<Notice> sellNoticeList = noticeDao.findByNoticeType(2L);
            int randomTxNum = new Random().nextInt(1000); // 随机交易次数
            int randomTtustNum = new Random().nextInt(randomTxNum); // 随机信任人数(不超过交易次数)
            int randomTrustPercent = new Random().nextInt(10) + 90; // 随机信任度(90-100)
            for (int i=0; i<buyNoticeList.size();i++){
                buyNoticeList.get(i).setTxNum(randomTxNum);
                buyNoticeList.get(i).setTrustNum(randomTtustNum);
                buyNoticeList.get(i).setTrustPercent(randomTrustPercent);
            }
            for (int i = 0; i < sellNoticeList.size(); i++){
                sellNoticeList.get(i).setTxNum(randomTxNum);
                sellNoticeList.get(i).setTrustNum(randomTtustNum);
                sellNoticeList.get(i).setTrustPercent(randomTrustPercent);
            }
            // ====== end ======

            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }
            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(1);
            pageDTO.setPageSize(8);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

    public RestResp queryStatusKV(){
        try {
            Iterable<Country> location = countryDao.findAll();
            Iterable<Currency> currency = currencyDao.findAll();
            Iterable<Payment> payment = paymentDao.findAll();
            Iterable<BTCTicker> btcTiker = btcTickerDao.findBTCTickerBySymbol("btccny");
            if (location.iterator().hasNext() && currency.iterator().hasNext() && payment.iterator().hasNext()){
                StatusDTO statusDTO = new StatusDTO<>();
                statusDTO.setLocationList(location);
                statusDTO.setCurrencyList(currency);
                statusDTO.setPaymentList(payment);
                statusDTO.setBTCMarketList(btcTiker);
                return RestResp.success("操作成功", statusDTO);
            } else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }
    }

}
