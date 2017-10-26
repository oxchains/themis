package com.oxchains.themis.notice.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.domain.Notice;
import com.oxchains.themis.notice.service.NoticeService;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.springframework.data.jpa.domain.Specification;
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
        if (notice.getSearchType() == 0){
            // 0 默认是搜公告
            return noticeService.searchNotice(notice);
        }else {
            // 不是0 就是搜用户，暂时都是搜公告
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
    public RestResp searchPageAll(@RequestParam("pageNum") Integer pageNum, @RequestParam Integer pageSize){
        return noticeService.searchPageAll(pageNum, pageSize);
    }

    /**
     * 分页搜索公告
     * @param location
     * @param currency
     * @param payType
     * @param noticeType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "search/page")
    public RestResp searchPage(@RequestParam Long location,
                               @RequestParam Long currency,
                               @RequestParam Long payType,
                               @RequestParam Long noticeType,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize){
        return noticeService.searchPage(location, currency, payType, noticeType, pageNum, pageSize);
    }

}
