package com.oxchains.themis.common.constant;

/**
 * @author ccl
 * @time 2017-11-06 14:12
 * @name Status
 * @desc:
 */
public interface Status {
    enum TrustStatus{
        TRUST(1,"信任"),SHIELD(2,"屏蔽");
        private Integer status;
        private String name;

        TrustStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
