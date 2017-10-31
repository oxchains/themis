package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.notice.dao.*;
import com.oxchains.themis.notice.domain.*;
import com.oxchains.themis.notice.domain.Currency;
import com.oxchains.themis.notice.rest.dto.HomeDTO;
import com.oxchains.themis.notice.rest.dto.PageDTO;
import com.oxchains.themis.notice.rest.dto.StatusDTO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.hibernate.jpa.HibernateEntityManager;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Service
@Transactional
public class NoticeService {

    @Resource private NoticeDao noticeDao;
    @Resource private BTCTickerDao btcTickerDao;
    @Resource private BTCResultDao btcResultDao;
    @Resource private BTCMarketDao btcMarketDao;
    @Resource private CountryDao countryDao;
    @Resource private CurrencyDao currencyDao;
    @Resource private PaymentDao paymentDao;
    @Resource private SearchTypeDao searchTypeDao;
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
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), 0);
            List<Notice> noticeListDoing = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), 1);
            List<Notice> noticeListDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), 2);

            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            for (BTCTicker btcTicker : btcTickerList) {
                Double low = btcTicker.getLow().doubleValue();
                Double minPrice = notice.getMinPrice().doubleValue();
                if (null == notice.getMinPrice()){
                    notice.setMinPrice(btcTicker.getLow());
                }else { // 市场价低于定义的最低价，那么价格就是自己定义的最低价
                    if (ArithmeticUtils.minus(low, minPrice) < 0) notice.setPrice(notice.getMinPrice());
                }
            }

            if (notice.getPremium() > 10 && notice.getPremium() < 0) {
                return RestResp.fail("请按规输入溢价");
            }

            if (!noticeListDone.isEmpty() && noticeListDoing.isEmpty()){
                notice.setCreateTime(createTime);
                Notice n = noticeDao.save(notice);
                return RestResp.success("操作成功", n);
            }else {
                if (!noticeListDoing.isEmpty()){
                    return RestResp.fail("已经有一条此类型公告且正在交易");
                } else if (!noticeListUnDone.isEmpty()) {
                    return RestResp.fail("已经有一条此类型公告");
                } else {
                    notice.setCreateTime(createTime);
                    Notice n = noticeDao.save(notice);
                    return RestResp.success("操作成功", n);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败", e.getMessage());
        }

    }

    public RestResp queryRandomNotice(){
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeType(1L);
            List<Notice> sellNoticeList = noticeDao.findByNoticeType(2L);

            if (buyNoticeList.size() > 2 && sellNoticeList.size() > 2){
                int buySize = getRandom(buyNoticeList);
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = 0; i < subBuyList.size(); i++){ setUserTxDetail(subBuyList, i);}
                for (int i = 0; i < subSellList.size(); i++){setUserTxDetail(subSellList, i);}
                partList.addAll(subBuyList);
                partList.addAll(subSellList);
            }else if (buyNoticeList.size() <= 2 && sellNoticeList.size() > 2){
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = 0; i < buyNoticeList.size(); i++){setUserTxDetail(buyNoticeList, i);}
                for (int i = 0; i < subSellList.size(); i++){setUserTxDetail(subSellList, i);}
                partList.addAll(buyNoticeList);
                partList.addAll(subSellList);
            }else if (buyNoticeList.size() > 2 && sellNoticeList.size() <= 2){
                int buySize = getRandom(buyNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                for (int i = 0; i < subBuyList.size(); i++){setUserTxDetail(subBuyList, i);}
                for (int i = 0; i < sellNoticeList.size(); i++){setUserTxDetail(sellNoticeList, i);}
                partList.addAll(subBuyList);
                partList.addAll(sellNoticeList);
            }else {
                for (int i = 0; i < buyNoticeList.size(); i++){setUserTxDetail(buyNoticeList, i);}
                for (int i = 0; i < sellNoticeList.size(); i++){setUserTxDetail(sellNoticeList, i);}
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList);
            }
            return RestResp.success("操作成功", partList);
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail("操作失败");
        }
    }

    public RestResp queryPartNotice(){
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeType(1L);
            List<Notice> sellNoticeList = noticeDao.findByNoticeType(2L);

            // 设置交易次数，信任人数，信誉度（随机）
            int randomTxNum = new Random().nextInt(1000); // 随机交易次数
            int randomTtustNum = new Random().nextInt(randomTxNum); // 随机信任人数(不超过交易次数)
            int randomTrustPercent = new Random().nextInt(10) + 90; // 随机信任度(90-100)
            for (int i = 0; i < buyNoticeList.size(); i++){
                buyNoticeList.get(i).setTxNum(randomTxNum);
                buyNoticeList.get(i).setTrustNum(randomTtustNum);
                buyNoticeList.get(i).setTrustPercent(randomTrustPercent);

            }
            for (int i = 0; i < sellNoticeList.size(); i++){
                sellNoticeList.get(i).setTxNum(randomTxNum);
                sellNoticeList.get(i).setTrustNum(randomTtustNum);
                sellNoticeList.get(i).setTrustPercent(randomTrustPercent);
            }

            // 公告总数量判断
            if (buyNoticeList.size() > 2 && sellNoticeList.size() > 2){
                int buySize = new Random().nextInt(buyNoticeList.size() - 2);
                int sellSize = new Random().nextInt(sellNoticeList.size() - 2);
                partList.addAll(buyNoticeList.subList(buySize, buySize + 2));
                partList.addAll(sellNoticeList.subList(sellSize, sellSize + 2));
            }else if (buyNoticeList.size() <= 2 && sellNoticeList.size() > 2){
                int sellSize = new Random().nextInt(sellNoticeList.size() - 2);
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList.subList(sellSize, sellSize + 2));
            }else if (buyNoticeList.size() > 2 && sellNoticeList.size() <= 2){
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

    // 搜索购买公告
    public RestResp searchPage_buy(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = 2L;
            Integer pageNum = notice.getPageNum();

            Pageable pageable = buildPageRequest(pageNum, 8, "createTime");
            Page<Notice> page = null;

            // 对所在地，货币类型，支付方式判断，可为null
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

            // 将好评度等值添加到list中返回
            for (int i = 0; i < resultList.size(); i++){
                Long userId = resultList.get(i).getUserId();
                UserTxDetail utdInfo = userTxDetailDao.findOne(userId);
                resultList.get(i).setTxNum(utdInfo.getTxNum());
                resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                double goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal,2);
                resultList.get(i).setGoodPercent((int)(goodP*100));
            }

            // 将page相关参数设置到DTO中返回
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

    // 搜索出售公告
    public RestResp searchPage_sell(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = 1L;
            Integer pageNum = notice.getPageNum();
            if (null == pageNum) pageNum = 1;
            // Integer pageSize = notice.getPageSize();
            // Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pageable = buildPageRequest(pageNum, 8, "createTime");
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

            // 将好评度等值添加到list中返回
            for (int i = 0; i < resultList.size(); i++){
                Long userId = resultList.get(i).getUserId();
                UserTxDetail utdInfo = userTxDetailDao.findOne(userId);
                resultList.get(i).setTxNum(utdInfo.getTxNum());
                resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                double goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal,2);
                resultList.get(i).setGoodPercent((int)(goodP*100));
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

    // 作废
    public RestResp defaultSearch_buy(Long noticeType, Integer pageNum){
        try {
            Pageable pageable = buildPageRequest(pageNum, 8, null);
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

    // 作废
    public RestResp defaultSearch_sell(Long noticeType, Integer pageNum){
        try {
            Pageable pageable = buildPageRequest(pageNum, 8, null);
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

    public RestResp stopNotice(Long id){
        try {
            Notice noticeInfo = noticeDao.findOne(id);
            if (null != noticeInfo) {
                if (noticeInfo.getTxStatus() == 2) return RestResp.fail("公告已下架");
                if (noticeInfo.getTxStatus() == 1) return RestResp.fail("交易中公告，禁止下架");
                List<Notice> noticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(noticeInfo.getUserId(), noticeInfo.getNoticeType(), noticeInfo.getTxStatus());
                if (!noticeList.isEmpty()){
                    Notice save = null;
                    for (Notice n : noticeList) {
                        n.setTxStatus(2);
                        save = noticeDao.save(n);
                    }
                    return RestResp.success("操作成功", save);
                }else {
                    return RestResp.fail("操作失败");
                }
            }else {
                return RestResp.fail("操作失败");
            }
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
            Iterable<SearchType> searchType = searchTypeDao.findAll();
            Iterable<BTCTicker> btcTiker = btcTickerDao.findBTCTickerBySymbol("btccny");

            if (location.iterator().hasNext() && currency.iterator().hasNext() && payment.iterator().hasNext() && searchType.iterator().hasNext()){
                StatusDTO statusDTO = new StatusDTO<>();
                statusDTO.setLocationList(location);
                statusDTO.setCurrencyList(currency);
                statusDTO.setPaymentList(payment);
                statusDTO.setSearchTypeList(searchType);
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


    // =================================================================

    /**
     * 根据list大小-2获取一个随机数
     * @param list
     * @return
     */
    private int getRandom(List list){
        return new Random().nextInt(list.size() - 2);
    }

    /**
     * 对list截取，获取size为2的新list
     * @param list
     * @param size
     * @return
     */
    private List getSubList(List list, int size){
        return list.subList(size, size + 2);
    }

    /**
     * 设置用户交易详情数据
     * @param subList
     * @param i
     */
    private void setUserTxDetail(List<Notice> subList, int i) {
        Long userId = subList.get(i).getUserId();
        UserTxDetail userTxDetail = userTxDetailDao.findOne(userId);
        subList.get(i).setTxNum(userTxDetail.getTxNum());
        subList.get(i).setTrustNum(userTxDetail.getBelieveNum());
        double trustP = ArithmeticUtils.divide(userTxDetail.getBelieveNum(), userTxDetail.getTxNum(), 2);
        subList.get(i).setTrustPercent((int)ArithmeticUtils.multiply(trustP, (double) 100, 0));
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
        } else if ("createTime".equals(sortType)){
            sort = new Sort(Sort.Direction.ASC, "noticeContent");
        }
        return new PageRequest(pageNum - 1, pageSize, sort);
    }

}
