package com.oxchains.themis.notice.rest;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.domain.Notice;
import com.oxchains.themis.notice.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 发布公告
     * @param notice
     * @return
     */
    @PostMapping(value = "/broadcast")
    public RestResp broadcastNotice(@RequestBody Notice notice){
        return noticeService.broadcastNotice(notice);
    }

    /**
     * 首页随机获取公告，新得返回形式
     * @return
     */
    /*@GetMapping(value = "/query/random")
    public RestResp queryRandomNotice(){
        return noticeService.queryRandomNotice();
    }*/

    /**
     * 随机查询两条购买公告、两条出售公告
     * @return
     */
    @GetMapping(value = "/query/part")
    public RestResp queryPartNotice(){
        return noticeService.queryPartNotice();
    }

    /**
     * 查询所有公告
     * @return
     */
    @GetMapping(value = "/query/all")
    public RestResp queryAllNotice(){
        return noticeService.queryAllNotice();
    }

    /**
     * 查询所有非交易状态公告
     * @return
     */
    @GetMapping(value = "/query/unDone")
    public RestResp queryAllUnDone(){
        return noticeService.querAllUnDone();
    }

    /**
     * 搜索公告(未分页)
     * @param notice
     * @return
     */
    @PostMapping(value = "/search")
    public RestResp searchNotice(@RequestBody Notice notice){
        if (notice.getSearchType() == 1){
            // 1 默认是搜公告
            return noticeService.searchNotice(notice);
        }else {
            // 不是1 就是搜用户，暂时都是搜公告
            return noticeService.searchNotice(notice);
        }
    }

    /**
     * 查询自己的公告
     * @param userId     登录id
     * @param noticeType    公告类型
     * @return
     */
    @GetMapping(value = "/query/me")
    public RestResp queryMeNotice(@RequestParam Long userId, @RequestParam Long noticeType){
        return noticeService.querMeNotice(userId, noticeType);
    }

    /**
     * 根据交易状态查询自己的公告
     * @param userId
     * @param noticeType
     * @param txStatus
     * @return
     */
    @GetMapping(value = "/query/me2")
    public RestResp queryMeAllNotice(@RequestParam Long userId, @RequestParam Long noticeType, @RequestParam Integer txStatus){
        return noticeService.queryMeAllNotice(userId, noticeType, txStatus);
    }

    /**
     * 实时获取(火币网)BTC价格
     * @return
     */
    @GetMapping(value = "/query/BTCPrice")
    public RestResp queryBTCPrice(){
        return noticeService.queryBTCPrice();
    }

    /**
     * 实时获取(火币网)BTC行情信息
     * @return
     */
    @GetMapping(value = "/query/BTCMarket")
    public RestResp queryBTCMarket(){
        return noticeService.queryBTCMarket();
    }

    /**
     * 分页搜索所有公告
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/search/pageAll")
    public RestResp searchPageAll(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return noticeService.searchPageAll(pageNum, pageSize);
    }

    /**
     * 分页搜索公告-购买
     * @param notice
     * @return
     */
    @PostMapping(value = "search/page/buy")
    public RestResp searchPage_buy(@RequestBody Notice notice){
        System.out.println("1----"+notice.getSearchType());
        // if (null == notice.getSearchType()) notice.setSearchType(1);
        if (notice.getSearchType() == 1){// 1 默认是搜公告
            return noticeService.searchPage_buy(notice);
        }else {
            return noticeService.searchPage_buy(notice);
        }
    }

    /**
     * 分页搜索公告-出售
     * @param notice
     * @return
     */
    @PostMapping(value = "search/page/sell")
    public RestResp searchPage_sell(@RequestBody Notice notice){
        if (notice.getSearchType() == 1){// 1 默认是搜公告
            return noticeService.searchPage_sell(notice);
        }else {
            return noticeService.searchPage_sell(notice);
        }
    }

    /**
     * 点击 购买/出售公告 显示的默认第一页数据
     * @param noticeType
     * @return
     */
    @GetMapping(value = "/search/default/buy")
    public RestResp DefaultSearch_buy(@RequestParam Long noticeType, @RequestParam Integer pageNum){
        return noticeService.defaultSearch_buy(noticeType, pageNum);
    }

    /**
     * 点击 购买/出售公告 显示的默认第一页数据
     * @param noticeType
     * @return
     */
    @GetMapping(value = "/search/default/sell")
    public RestResp DefaultSearch_sell(@RequestParam Long noticeType, @RequestParam Integer pageNum){
        return noticeService.defaultSearch_sell(noticeType, pageNum);
    }

    /**
     * 发布公告页面要返回的状态列表
     * @return
     */
    @GetMapping(value = "/query/statusKV")
    public RestResp queryStatusKV(){
        return noticeService.queryStatusKV();
    }

}
