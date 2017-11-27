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
    private Long userId;
    private String id;
    private Integer successId;
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
    public Integer getPageNum() {
        return pageNum;
    }
    @Override
    public String toString() {
        return "Pojo{" +
                "userId=" + userId +
                ", id='" + id + '\'' +
                ", successId=" + successId +
                ", noticeId=" + noticeId +
                ", txId='" + txId + '\'' +
                '}';
    }
}
