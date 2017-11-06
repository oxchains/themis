package com.oxchains.themis.common.param;

/**
 * @author ccl
 * @time 2017-11-01 16:50
 * @name ParamType
 * @desc:
 */
public interface ParamType {

    enum UpdateUserInfoType implements ParamType {
        ADD(1),INFO(2),PWD(3),EMAIL(4),PHONE(5),FPWD(6);

        UpdateUserInfoType(int type){
            this.type=type;
        }
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    enum TrustTabType implements ParamType{
        TRUSTED(1),TRUST(2),SHIELD(3);

        TrustTabType(int type) {
            this.type = type;
        }

        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
