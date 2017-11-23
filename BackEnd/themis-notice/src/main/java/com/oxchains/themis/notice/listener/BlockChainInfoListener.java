package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.notice.dao.BlockChainInfoDao;
import com.oxchains.themis.notice.dao.CNYDetailDao;
import com.oxchains.themis.notice.domain.BlockChainInfo;
import com.oxchains.themis.notice.domain.CNYDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时获取比特币价格的监听器 （获取来源：blockchain.info）
 * @author luoxuri
 * @create 2017-11-02 10:57
 **/
@Component
@Transactional
public class BlockChainInfoListener {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static String CNY_TAG = "¥";
    @Resource private CNYDetailDao cnyDetailDao;
    @Resource private BlockChainInfoDao blockChainInfoDao;

    /**
     * 调度器
     * 每间隔 12 分钟执行一次
     *
     * 如果行情获取失败，就return
     * 如果行情获取成功，就保存，且数据库只保存一条btc-cny的信息，新得信息就update
     */
    @Scheduled(fixedRate = 1000 * 720)
    public void blockChainInfoListener(){
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            LOG.info("Timed tasks begin to run：{} BTC market", currentTime);

            String url = "https://blockchain.info/ticker";
            String result = HttpUtils.sendGet(url);
            BlockChainInfo blockChainInfo = (BlockChainInfo) JsonUtil.fromJson(result, BlockChainInfo.class);
            LOG.info("比特币行情: {}", blockChainInfo);
            if (null != blockChainInfo){
                List<BlockChainInfo> bciList = blockChainInfoDao.findBySymbol(CNY_TAG);
                CNYDetail cnyDetail = cnyDetailDao.findBySymbol(CNY_TAG);
                if (bciList.size() != 0 && cnyDetail != null){
                    for (BlockChainInfo b : bciList) {
                        if (CNY_TAG.equals(b.getSymbol())){
                            b.setSaveTime(currentTime);
                            b.setSymbol(CNY_TAG);
                            blockChainInfoDao.save(b);
                        }else {
                            LOG.error("********** 实时获取比特币价格异常：货币符号异常 **********");
                            return;
                        }
                    }
                    if (CNY_TAG.equals(cnyDetail.getSymbol())){
                        cnyDetail.setSaveTime(currentTime);
                        cnyDetail.setBuy(blockChainInfo.getCNY().getBuy());
                        cnyDetail.setLast(blockChainInfo.getCNY().getLast());
                        cnyDetail.setSell(blockChainInfo.getCNY().getSell());
                        cnyDetail.setSymbol(blockChainInfo.getCNY().getSymbol());
                        cnyDetailDao.save(cnyDetail);
                    }else {
                        LOG.error("********** 实时获取比特币价格异常：货币符号异常 **********");
                        return;
                    }
                }else {
                    if (CNY_TAG.equals(blockChainInfo.getSymbol()) && CNY_TAG.equals(blockChainInfo.getCNY().getSymbol())){
                        blockChainInfo.setSymbol(CNY_TAG);
                        blockChainInfo.setSaveTime(currentTime);
                        blockChainInfoDao.save(blockChainInfo);

                        blockChainInfo.getCNY().setSaveTime(currentTime);
                        cnyDetailDao.save(blockChainInfo.getCNY());
                    }else {
                        LOG.error("********** 实时获取比特币价格异常：货币符号异常 **********");
                        return;
                    }
                }
            }else {
                LOG.error("********** 实时获取比特币价格错误 **********");
                return;
            }
            LOG.info("This timed tasks has been completed");
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("定时任务：获取比特币价格异常", e.getMessage());
        }
    }
}
