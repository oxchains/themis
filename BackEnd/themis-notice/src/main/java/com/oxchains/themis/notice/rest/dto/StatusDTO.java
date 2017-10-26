package com.oxchains.themis.notice.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * 所在地，货币类型，支付方式等DTO
 *
 * @author luoxuri
 * @create 2017-10-26 19:29
 **/
@Data
public class StatusDTO <T> {

    private Iterable<T> LocationList;
    private Iterable<T> CurrencyList;
    //private Iterable<T> NoticeTypeList;
    private Iterable<T> PaymentList;

    private Iterable<T> BTCMarketList;
}
