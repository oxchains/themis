package com.oxchains.themis.common.bitcoin;

/**
 * @author ccl
 * @time 2017-10-31 14:10
 * @name BitcoinConst
 * @desc:
 */
public interface BitcoinConst {
    enum VoutHashType implements BitcoinConst{
        PUB_KEY_HASH("pubkeyhash",1),SCRIPT_HASH("scripthash",2);
        private String name;
        private int index;

        VoutHashType(String name, int index){
            this.name=name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
