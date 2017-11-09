package com.oxchains.themis.notice.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @author luoxuri
 * @create 2017-11-03 19:11
 **/
public interface NoticeConst {
    /**
     * 公告类型
     */
    enum NoticeType{
        BUY(1L, "购买"), SELL(2L, "出售");
        private Long status;
        private String name;
        NoticeType(Long status, String name){
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

    /**
     * 集合大小
     */
    enum ListSize{
        ZERO(0, "0"), ONE(1, "1"), TWO(2, "2");
        private Integer value;
        private String name;
        ListSize(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public Integer getValue() {
            return value;
        }
    }

    /**
     * 交易状态
     */
    enum TxStatus{
        ZERO(0, "未交易"), ONE(1, "交易中"), TWO(2, "交易完成");
        private Integer status;
        private String name;
        TxStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }
        public Integer getStatus() {
            return status;
        }
        public String getName() {
            return name;
        }
    }

    /**
     * 用于判断的常量
     */
    enum Constant{
        ZERO(0), ONE(1), TWO(2), FIVE(5), TEN(10), HUNDRED(100);
        private Integer value;

        Constant(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 搜索类型
     */
    enum SearchType{
        ONE(1L, "搜公告"), TWO(2L, "搜用户");
        private Long status;
        private String name;

        SearchType(Long status, String name) {
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
}
