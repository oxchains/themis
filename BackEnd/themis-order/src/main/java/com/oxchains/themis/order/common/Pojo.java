package com.oxchains.themis.order.common;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Data
public class Pojo implements Serializable {
    private Long userId;        //yonghu id
    private String id;    //订单id
    private Integer successId; // 判断仲裁那个胜利 1 买家  2 卖家
    private Long noticeId;
    private String txId;
    private String content;
    private Integer status;
    private String amount;
    private BigDecimal money;
    private String fileName;
    private String thumbUrl;
    private Integer pageNum;
    private Integer pageSize;
    private String token;
    public Integer getPageNum() {
        return pageNum;
    }
}
