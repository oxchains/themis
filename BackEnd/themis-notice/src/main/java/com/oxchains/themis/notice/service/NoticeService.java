package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.notice.common.NoticeConst;
import com.oxchains.themis.notice.dao.*;
import com.oxchains.themis.notice.domain.*;
import com.oxchains.themis.notice.domain.Currency;
import com.oxchains.themis.notice.rest.dto.PageDTO;
import com.oxchains.themis.notice.rest.dto.StatusDTO;
import org.omg.CORBA.INTERNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@Service
@Transactional
public class NoticeService {

    private static final Logger LOG = LoggerFactory.getLogger(NoticeService.class);

    @Resource private NoticeDao noticeDao;
    @Resource private BTCTickerDao btcTickerDao;
    @Resource private BTCResultDao btcResultDao;
    @Resource private BTCMarketDao btcMarketDao;
    @Resource private CNYDetailDao cnyDetailDao;
    @Resource private CountryDao countryDao;
    @Resource private CurrencyDao currencyDao;
    @Resource private PaymentDao paymentDao;
    @Resource private SearchTypeDao searchTypeDao;
    @Resource private UserTxDetailDao userTxDetailDao;
    @Resource private UserDao userDao;

    public RestResp broadcastNotice(Notice notice){
        try {
            // 必填项判断
            if (null == notice.getNoticeType() && null == notice.getLocation() && null == notice.getCurrency()
                    && null == notice.getPrice() && null == notice.getMinTxLimit() && null == notice.getMaxTxLimit()
                    && null == notice.getPayType() && null == notice.getNoticeContent() && null == notice.getPremium()) {
                return RestResp.fail("必填项不能为空");
            }

            // 选填项(最低价)判断-11.1中国又禁止一部分btc相关平台，此价格获取失败
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            if (btcTickerList.size() != NoticeConst.ListSize.ZERO.getValue()){
                for (BTCTicker btcTicker : btcTickerList) {
                    Double low = btcTicker.getLow().doubleValue();
                    Double minPrice = notice.getMinPrice().doubleValue();
                    if (null == notice.getMinPrice()){
                        notice.setMinPrice(btcTicker.getLow());
                    }else { // 市场价低于定义的最低价，那么价格就是自己定义的最低价
                        if (ArithmeticUtils.minus(low, minPrice) < NoticeConst.Constant.ZERO.getValue()) {
                            notice.setPrice(notice.getMinPrice());
                        }
                    }
                }
            }else {
                // 选填项（最低价判断）
                CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");
                if (cnyDetail != null){
                    if (null == notice.getMinPrice()){
                        notice.setMinPrice(new BigDecimal(cnyDetail.getLast()));
                    }else {
                        Double low = Double.valueOf(cnyDetail.getLast());
                        Double minPrice = notice.getMinPrice().doubleValue();
                        if (ArithmeticUtils.minus(low, minPrice) < NoticeConst.Constant.ZERO.getValue()){
                            notice.setPrice(notice.getMinPrice());
                        }
                    }
                }else {
                    return RestResp.fail("比特币价格获取失败，请手动查询实时价格慎重");
                }
            }

            // 溢价判断
            if (notice.getPremium() < NoticeConst.Constant.ZERO.getValue() && notice.getPremium() > NoticeConst.Constant.TEN.getValue()) {
                return RestResp.fail("请按规定输入溢价（0~10）");
            }

            // 两种不能发布公告得判断
            List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), NoticeConst.TxStatus.ZERO.getStatus());
            if (noticeListUnDone.size() != NoticeConst.ListSize.ZERO.getValue()){
                return RestResp.fail("已经有一条此类型公告");
            }
            List<Notice> noticeListDoing = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), NoticeConst.TxStatus.ONE.getStatus());
            if (noticeListDoing.size() != NoticeConst.ListSize.ZERO.getValue()){
                return RestResp.fail("已经有一条此类型公告且正在交易");
            }

            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            notice.setCreateTime(createTime);
            Notice n = noticeDao.save(notice);
            return RestResp.success("操作成功", n);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("发布公告异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryRandomNotice(){
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeTypeAndTxStatus(NoticeConst.NoticeType.BUY.getStatus(), NoticeConst.TxStatus.ZERO.getStatus());
            List<Notice> sellNoticeList = noticeDao.findByNoticeTypeAndTxStatus(NoticeConst.NoticeType.SELL.getStatus(), NoticeConst.TxStatus.ZERO.getStatus());

            if (buyNoticeList.size() == NoticeConst.ListSize.ZERO.getValue() && sellNoticeList.size() == NoticeConst.ListSize.ZERO.getValue()){
                return RestResp.success("没有数据", new ArrayList<>());
            }
            if (buyNoticeList.size() > NoticeConst.ListSize.TWO.getValue() && sellNoticeList.size() > NoticeConst.ListSize.TWO.getValue()){
                int buySize = getRandom(buyNoticeList);
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < subBuyList.size(); i++){ setUserTxDetail(subBuyList, i);}
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < subSellList.size(); i++){setUserTxDetail(subSellList, i);}
                partList.addAll(subBuyList);
                partList.addAll(subSellList);
            }else if (buyNoticeList.size() <= NoticeConst.ListSize.TWO.getValue() && sellNoticeList.size() > NoticeConst.ListSize.TWO.getValue()){
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < buyNoticeList.size(); i++){setUserTxDetail(buyNoticeList, i);}
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < subSellList.size(); i++){setUserTxDetail(subSellList, i);}
                partList.addAll(buyNoticeList);
                partList.addAll(subSellList);
            }else if (buyNoticeList.size() > NoticeConst.ListSize.TWO.getValue() && sellNoticeList.size() <= NoticeConst.ListSize.TWO.getValue()){
                int buySize = getRandom(buyNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < subBuyList.size(); i++){setUserTxDetail(subBuyList, i);}
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < sellNoticeList.size(); i++){setUserTxDetail(sellNoticeList, i);}
                partList.addAll(subBuyList);
                partList.addAll(sellNoticeList);
            }else {
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < buyNoticeList.size(); i++){setUserTxDetail(buyNoticeList, i);}
                for (int i = NoticeConst.Constant.ZERO.getValue(); i < sellNoticeList.size(); i++){setUserTxDetail(sellNoticeList, i);}
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList);
            }
            return RestResp.success("操作成功", partList);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("获取4条随机公告异常", e.getMessage());

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
            LOG.error("查询所有公告异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryMeAllNotice(Long userId, Integer pageNum, Long noticeType, Integer txStatus){
        try {
            List<Notice> resultList = new ArrayList<>();
            Pageable pageable = buildPageRequest(pageNum, NoticeConst.Constant.FIVE.getValue(), "auto");
            Page<Notice> page = null;
            if (txStatus.equals(NoticeConst.TxStatus.TWO.getStatus())){
                page = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeConst.TxStatus.TWO.getStatus(), pageable);
                Iterator<Notice> it = page.iterator();
                while (it.hasNext()){
                    resultList.add(it.next());
                }
            }else {
                List<Notice> unDoneNoticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeConst.TxStatus.ZERO.getStatus());
                List<Notice> doingNoticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeConst.TxStatus.ONE.getStatus());
                resultList.addAll(unDoneNoticeList);
                resultList.addAll(doingNoticeList);
            }
            PageDTO<Notice> pageDTO = new PageDTO<>();
            if (page == null){
                pageDTO.setCurrentPage(NoticeConst.Constant.ONE.getValue());
                pageDTO.setPageSize(NoticeConst.Constant.FIVE.getValue());
                pageDTO.setRowCount((long)resultList.size());
                pageDTO.setTotalPage(NoticeConst.Constant.ONE.getValue());
                pageDTO.setPageList(resultList);
            }else {
                pageDTO.setCurrentPage(pageNum);
                pageDTO.setPageSize(NoticeConst.Constant.FIVE.getValue());
                pageDTO.setRowCount(page.getTotalElements());
                pageDTO.setTotalPage(page.getTotalPages());
                pageDTO.setPageList(resultList);
            }
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("查询我的公告异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 火币网API接口停止服务，获取行情失败
     */
    @Deprecated
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
            LOG.error("查询BTC价格异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 火币网API接口停止服务，获取行情失败
     */
    @Deprecated
    public RestResp queryBTCMarket(){
        try {
            List<BTCResult> btcResultList = btcResultDao.findByIsSuc("true");
            List<BTCMarket> btcMarketList = btcMarketDao.findBySymbol("huobibtccny");
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            BTCResult btcResult = null;
            BTCMarket btcMarket = null;
            BTCTicker btcTicker = null;
            for (int i = NoticeConst.Constant.ZERO.getValue(); i < btcResultList.size(); i++){
                btcResult = btcResultList.get(i);
            }
            for (int i = NoticeConst.Constant.ZERO.getValue(); i < btcMarketList.size(); i++){
                btcMarket = btcMarketList.get(i);
            }
            for (int i = NoticeConst.Constant.ZERO.getValue(); i < btcTickerList.size(); i++){
                btcTicker = btcTickerList.get(i);
            }
            btcMarket.setTicker(btcTicker);
            btcResult.setDatas(btcMarket);
            return RestResp.success("操作成功", btcResultList);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("查询BTC深度行情异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryBlockChainInfo(){
        try {
            CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");
            if (cnyDetail != null){
                return RestResp.success("操作成功", cnyDetail);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("获取BTC价格异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp searchBuyPage(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = NoticeConst.NoticeType.SELL.getStatus();
            Integer pageNum = notice.getPageNum();

            Pageable pageable = buildPageRequest(pageNum, NoticeConst.Constant.FIVE.getValue(), "auto");

            // 对所在地，货币类型，支付方式判断，可为null
            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatus(location, currency, payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            }else if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeTypeAndTxStatus(location, currency, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeTypeAndTxStatus(location, payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeTypeAndTxStatus(currency, payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeTypeAndTxStatus(location, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null == currency && null != payType) {
                page = noticeDao.findByPayTypeAndNoticeTypeAndTxStatus(payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndNoticeTypeAndTxStatus(currency, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeTypeAndTxStatus(noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            }else {
                return RestResp.fail("操作失败");
            }

            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }

            // 将好评度等值添加到list中返回
            for (int i = NoticeConst.Constant.ZERO.getValue(); i < resultList.size(); i++){
                Long userId = resultList.get(i).getUserId();
                UserTxDetail utdInfo = userTxDetailDao.findOne(userId);
                if (utdInfo == null){
                    resultList.get(i).setTxNum(NoticeConst.Constant.ZERO.getValue());
                    resultList.get(i).setTrustNum(NoticeConst.Constant.ZERO.getValue());
                    resultList.get(i).setGoodPercent(NoticeConst.Constant.ZERO.getValue());
                }else {
                    resultList.get(i).setTxNum(utdInfo.getTxNum());
                    resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                    double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                    double goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal, NoticeConst.Constant.TWO.getValue());
                    resultList.get(i).setGoodPercent((int)(goodP * NoticeConst.Constant.HUNDRED.getValue()));
                }

            }

            // 将page相关参数设置到DTO中返回
            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(NoticeConst.Constant.FIVE.getValue());
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("搜索购买公告异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp searchSellPage(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = NoticeConst.NoticeType.BUY.getStatus();
            Integer pageNum = notice.getPageNum();

            Pageable pageable = buildPageRequest(pageNum, NoticeConst.Constant.FIVE.getValue(), "createTime");

            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatus(location, currency, payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            }else if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeTypeAndTxStatus(location, currency, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeTypeAndTxStatus(location, payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeTypeAndTxStatus(currency, payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeTypeAndTxStatus(location, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null == currency && null != payType) {
                page = noticeDao.findByPayTypeAndNoticeTypeAndTxStatus(payType, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndNoticeTypeAndTxStatus(currency, noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            } else if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeTypeAndTxStatus(noticeType, NoticeConst.TxStatus.ZERO.getStatus(), pageable);
            }else {
                return RestResp.fail("操作失败");
            }

            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }

            // 将好评度等值添加到list中返回
            for (int i = NoticeConst.ListSize.ZERO.getValue(); i < resultList.size(); i++){
                Long userId = resultList.get(i).getUserId();
                UserTxDetail utdInfo = userTxDetailDao.findOne(userId);
                if (null == utdInfo){
                    resultList.get(i).setTxNum(NoticeConst.Constant.ZERO.getValue());
                    resultList.get(i).setTrustNum(NoticeConst.Constant.ZERO.getValue());
                    resultList.get(i).setGoodPercent(NoticeConst.Constant.ZERO.getValue());
                }else {
                    resultList.get(i).setTxNum(utdInfo.getTxNum());
                    resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                    double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                    double goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal,NoticeConst.Constant.TWO.getValue());
                    resultList.get(i).setGoodPercent((int)(goodP * NoticeConst.Constant.HUNDRED.getValue()));
                }

            }

            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(NoticeConst.Constant.FIVE.getValue());
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("搜索出售公告异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp stopNotice(Long id){
        try {
            Notice noticeInfo = noticeDao.findOne(id);
            if (null == noticeInfo) {
                return RestResp.fail("操作失败");
            }
            if (noticeInfo.getTxStatus().equals(NoticeConst.TxStatus.TWO.getStatus())) {
                return RestResp.fail("公告已下架");
            }
            if (noticeInfo.getTxStatus().equals(NoticeConst.TxStatus.ONE.getStatus())) {
                return RestResp.fail("交易中公告，禁止下架");
            }
            List<Notice> noticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(noticeInfo.getUserId(), noticeInfo.getNoticeType(), noticeInfo.getTxStatus());
            if (noticeList.size() == NoticeConst.ListSize.ZERO.getValue()){
                return RestResp.fail("操作失败");
            }
            for (Notice n : noticeList) {
                n.setTxStatus(NoticeConst.TxStatus.TWO.getStatus());
                noticeDao.save(n);
            }
            return RestResp.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("下架公告异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryStatusKV(){
        try {
            Iterable<Country> location = countryDao.findAll();
            Iterable<Currency> currency = currencyDao.findAll();
            Iterable<Payment> payment = paymentDao.findAll();
            Iterable<SearchType> searchType = searchTypeDao.findAll();
            Iterable<BTCTicker> btcTiker = btcTickerDao.findBTCTickerBySymbol("btccny");
            CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");

            if (location.iterator().hasNext() && currency.iterator().hasNext() && payment.iterator().hasNext() && searchType.iterator().hasNext()){
                StatusDTO statusDTO = new StatusDTO<>();
                statusDTO.setLocationList(location);
                statusDTO.setCurrencyList(currency);
                statusDTO.setPaymentList(payment);
                statusDTO.setSearchTypeList(searchType);
                statusDTO.setBTCMarketList(btcTiker);
                statusDTO.setCnyDetailList(cnyDetail);
                return RestResp.success("操作成功", statusDTO);
            } else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("查询状态异常", e.getMessage());
        }
        return RestResp.fail("操作失败");
    }

    // =================================================================

    /**
     * 根据list大小-2获取一个随机数
     */
    private int getRandom(List list){
        return new Random().nextInt(list.size() - NoticeConst.ListSize.TWO.getValue());
    }

    /**
     * 对list截取，获取size为2的新list
     */
    private List getSubList(List list, int size){
        return list.subList(size, size + NoticeConst.ListSize.TWO.getValue());
    }

    /**
     * 设置用户交易详情数据
     */
    private void setUserTxDetail(List<Notice> subList, int i) {
        Long userId = subList.get(i).getUserId();
        UserTxDetail userTxDetail = userTxDetailDao.findOne(userId);
        if (null == userTxDetail){
            subList.get(i).setTxNum(NoticeConst.Constant.ZERO.getValue());
            subList.get(i).setTrustNum(NoticeConst.Constant.ZERO.getValue());
            subList.get(i).setTrustPercent(NoticeConst.Constant.ZERO.getValue());
        }else {
            subList.get(i).setTxNum(userTxDetail.getTxNum());
            subList.get(i).setTrustNum(userTxDetail.getBelieveNum());
            double trustP = ArithmeticUtils.divide(userTxDetail.getBelieveNum(), userTxDetail.getTxNum(), NoticeConst.Constant.TWO.getValue());
            subList.get(i).setTrustPercent((int) ArithmeticUtils.multiply(trustP, NoticeConst.Constant.HUNDRED.getValue(), NoticeConst.Constant.ZERO.getValue()));
        }

    }

    /**
     * 创建分页请求
     */
    private PageRequest buildPageRequest(Integer pageNum, Integer pageSize, String sortType){
        Sort sort = null;
        if("auto".equals(sortType)){
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("createTime".equals(sortType)){
            sort = new Sort(Sort.Direction.ASC, "createTime");
        }
        return new PageRequest(pageNum - NoticeConst.Constant.ONE.getValue(), pageSize, sort);
    }

}
