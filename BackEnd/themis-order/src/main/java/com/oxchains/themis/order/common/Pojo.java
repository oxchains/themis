package com.oxchains.themis.order.common;

import lombok.Data;

import javax.validation.constraints.Size;
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
    private String txId;     //交易id
    @Size(max = 200,message = "评论上限200字")
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

    @Override
    public String toString() {
        return "Pojo{" +
                "userId=" + userId +
                ", id='" + id + '\'' +
                ", successId=" + successId +
                ", noticeId=" + noticeId +
                ", txId='" + txId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", amount='" + amount + '\'' +
                ", money=" + money +
                ", fileName='" + fileName + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", token='" + token + '\'' +
                '}';
    }
}
