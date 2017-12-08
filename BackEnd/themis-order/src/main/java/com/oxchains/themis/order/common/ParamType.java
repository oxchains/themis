package com.oxchains.themis.order.common;

/**
 * Created by xuqi on 2017/11/3.
 * @author huohuo
 */
public interface ParamType {
    enum ArbitrateStatus {
        //仲裁表状态
        NOARBITRATE(0,"未仲裁"),ARBITRATEING(1,"仲裁中"),ARBITRATEEND(2,"仲裁结束");
        private Integer status;
        private String description;
        ArbitrateStatus(Integer status, String description) {
            this.status = status;
            this.description = description;
        }
        public Integer getStatus() {
            return status;
        }
        public String getDescription() {
            return description;
        }
    }

    enum NoticeStatus {
        //公告的类型
        BUY(1L,"购买"),SELL(2L,"出售");
        private Long status;
        private String name;

        NoticeStatus(Long status, String name) {
            this.status = status;
            this.name = name;
        }
        public Long getStatus() {
            return status;
        }
        public String getName() {
            return name;
        }
    }
    enum  NoticeTxStatus {
        //公告的交易状态
        NOTX(0,"未交易"),TXING(1,"交易中"),TXEND(2,"交易结束");
        private int status;
        private String description;

        NoticeTxStatus(int status, String description) {
            this.status = status;
            this.description = description;
        }
        public int getStatus() {
            return status;
        }
        public String getDescription() {
            return description;
        }
    }
    enum OrderStatus {
        //订单状态
        WAIT_CONFIRM(1L, "待确认"),
        WAIT_PAY(2L, "待付款"),
        WAIT_SEND(3L, "待收货"),
        WAIT_RECIVE(4L, "待收货"),
        WAIT_COMMENT(5L, "待评价"),
        FINISH(6L, "已完成"),
        CANCEL(7L, "已取消"),
        WAIT_REFUND(8L, "退款中");

        private Long status;
        private String name;
        OrderStatus(Long value, String name) {
            this.status = value;
            this.name = name;
        }
        public Long getStatus(){
            return this.status;
        }

        public static String getName(Long status) {
            if (status != null) {
                for (OrderStatus item : OrderStatus.values()) {
                    if (item.status == status.longValue()) {
                        return item.name;
                    }
                }
            }
            return "";
        }
    }
    enum RoleStatus {
        //角色
        ADMIN(1L),CUSTOM_SERVICE(2L),ABRITRATEER(3L),USER(4L);
        private Long status;

        RoleStatus(Long status) {
            this.status = status;
        }

        public Long getStatus() {
            return status;
        }
    }
    enum  VcurrencyStatus {
        //支付类型
        BTC(1L,"BTC");
        private Long id;
        private String vcurrencyName;

        VcurrencyStatus(Long id, String vcurrencyName) {
            this.id = id;
            this.vcurrencyName = vcurrencyName;
        }
        public Long getId() {
            return id;
        }
        public String getVcurrencyName() {
            return vcurrencyName;
        }
    }
    enum CommentStatus{
        GOOD(1,"好评"),BAD(2,"差评");
        private Integer status;
        private String desc;

        CommentStatus(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {

            return status;
        }

        public String getDesc() {
            return desc;
        }
    }
    enum UserTxDetailHandle{
        BELIEVE(1,"信任"),FIRST_BUY_TIME(2,"验证第一次购买时间"),DESC(3,"评价"),TX_NUM_AMOUNT(4,"交易次数和交易总量");
        private Integer status;
        private String desc;

        UserTxDetailHandle(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {
            return status;
        }
        public String getDesc() {
            return desc;
        }
    }
}
