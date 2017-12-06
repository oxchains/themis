package com.oxchains.themis.order.entity.ValidaPojo;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Created by xuqi on 2017/12/4.
 */
@Data
public class AddOrderPojo {
    @NotNull(message = "服务器繁忙，请稍后再试")
    private Long userId;
    @NotNull(message = "服务器繁忙,请稍后再试")
    private Long noticeId;
    @DecimalMax(value = "10000", message = "最大交易限额为10000")
    @DecimalMin(value = "0.000001" ,message = "最小交易限额为0.000001")
    @NotNull(message = "交易数量不可为空")
    private BigDecimal amount;
    @NotNull(message = "交易金额不可为空")
    private BigDecimal money;

}
