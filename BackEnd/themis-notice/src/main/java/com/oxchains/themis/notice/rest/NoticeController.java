package com.oxchains.themis.notice.rest;

import com.oxchains.themis.notice.common.RestResp;
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
     * 查询所有公告
     * @return
     */
    @GetMapping(value = "/queryAll")
    public RestResp queryAllNotice(){
        return noticeService.queryAllNotice();
    }

    /**
     * 查询所有非交易状态公告
     * @return
     */
    @GetMapping(value = "/queryUnDone")
    public RestResp queryAllUnDone(){
        return noticeService.querAllUnDone();
    }

    /**
     * 搜索公告
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
}
