package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.notice.dao.BlockChainInfoDao;
import com.oxchains.themis.notice.dao.CNYDetailDao;
import com.oxchains.themis.notice.domain.BlockChainInfo;
import com.oxchains.themis.notice.domain.CNYDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            LOG.info("BTC market: {}", blockChainInfo);
            if (null != blockChainInfo){
                List<BlockChainInfo> bciList = blockChainInfoDao.findBySymbol("¥");
                List<CNYDetail> cdList = cnyDetailDao.findBySymbol("¥");
                if (bciList.size() != 0 && cdList.size() != 0){
                    for (BlockChainInfo b : bciList) {
                        b.setSaveTime(currentTime);
                        b.setSymbol("¥");
                        blockChainInfoDao.save(b);
                    }
                    for (CNYDetail c : cdList) {
                        c.setSaveTime(currentTime);
                        c.setBuy(blockChainInfo.getCNY().getBuy());
                        c.setLast(blockChainInfo.getCNY().getLast());
                        c.setSell(blockChainInfo.getCNY().getSell());
                        c.setSymbol(blockChainInfo.getCNY().getSymbol());
                        cnyDetailDao.save(c);
                    }
                }else {
                    blockChainInfo.setSymbol("¥");
                    blockChainInfo.setSaveTime(currentTime);
                    blockChainInfoDao.save(blockChainInfo);

                    blockChainInfo.getCNY().setSaveTime(currentTime);
                    cnyDetailDao.save(blockChainInfo.getCNY());
                }
            }else {
                LOG.error("query BTC market failed");
                return;

            }
            LOG.info("This timed tasks has been completed");
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("定时任务：获取比特币价格异常", e.getMessage());
        }
    }
}
