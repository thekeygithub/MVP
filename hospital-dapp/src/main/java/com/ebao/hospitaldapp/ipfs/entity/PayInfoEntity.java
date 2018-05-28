package com.ebao.hospitaldapp.ipfs.entity;

import java.util.List;

public class PayInfoEntity {

    /**
     * apiType : Ebao01
     * E01_INP_NO01 : 2018042300001
     * E01_INP_NO02 : 2018-04-03
     * E01_INP_NO03 : 常爱梅
     * E01_INP_NO04 : 410205197204080024
     * E01_INP_NO05 : 2
     * E01_INP_NO06 : 1974-12-16
     * E01_INP_NO07 : 人民医院
     * E01_INP_NO08 : 3
     * E01_INP_NO09 : 3
     * E01_INP_NO10 : A100
     * E01_INP_LIST01 : [{"E01_INP_LIST01_NO01":"xxx","E01_INP_LIST01_NO02":2},{"E01_INP_LIST01_NO01":"xxx","E01_INP_LIST01_NO02":2}]
     * E01_INP_LIST02 : [{"E01_INP_LIST02_NO01":"yyy","E01_INP_LIST02_NO02":2},{"E01_INP_LIST02_NO01":"yyy","E01_INP_LIST02_NO02":2}]
     */

    private String apiType = "Ebao01";
    private String E01_INP_NO01;
    private String E01_INP_NO02;
    private String E01_INP_NO03;
    private String E01_INP_NO04;
    private int E01_INP_NO05;
    private String E01_INP_NO06;
    private String E01_INP_NO07;
    private int E01_INP_NO08;
    private int E01_INP_NO09;
    private String E01_INP_NO10;
    private List<E01INPLIST01Bean> E01_INP_LIST01;
    private List<E01INPLIST02Bean> E01_INP_LIST02;

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getE01_INP_NO01() {
        return E01_INP_NO01;
    }

    public void setE01_INP_NO01(String E01_INP_NO01) {
        this.E01_INP_NO01 = E01_INP_NO01;
    }

    public String getE01_INP_NO02() {
        return E01_INP_NO02;
    }

    public void setE01_INP_NO02(String E01_INP_NO02) {
        this.E01_INP_NO02 = E01_INP_NO02;
    }

    public String getE01_INP_NO03() {
        return E01_INP_NO03;
    }

    public void setE01_INP_NO03(String E01_INP_NO03) {
        this.E01_INP_NO03 = E01_INP_NO03;
    }

    public String getE01_INP_NO04() {
        return E01_INP_NO04;
    }

    public void setE01_INP_NO04(String E01_INP_NO04) {
        this.E01_INP_NO04 = E01_INP_NO04;
    }

    public int getE01_INP_NO05() {
        return E01_INP_NO05;
    }

    public void setE01_INP_NO05(int E01_INP_NO05) {
        this.E01_INP_NO05 = E01_INP_NO05;
    }

    public String getE01_INP_NO06() {
        return E01_INP_NO06;
    }

    public void setE01_INP_NO06(String E01_INP_NO06) {
        this.E01_INP_NO06 = E01_INP_NO06;
    }

    public String getE01_INP_NO07() {
        return E01_INP_NO07;
    }

    public void setE01_INP_NO07(String E01_INP_NO07) {
        this.E01_INP_NO07 = E01_INP_NO07;
    }

    public int getE01_INP_NO08() {
        return E01_INP_NO08;
    }

    public void setE01_INP_NO08(int E01_INP_NO08) {
        this.E01_INP_NO08 = E01_INP_NO08;
    }

    public int getE01_INP_NO09() {
        return E01_INP_NO09;
    }

    public void setE01_INP_NO09(int E01_INP_NO09) {
        this.E01_INP_NO09 = E01_INP_NO09;
    }

    public String getE01_INP_NO10() {
        return E01_INP_NO10;
    }

    public void setE01_INP_NO10(String E01_INP_NO10) {
        this.E01_INP_NO10 = E01_INP_NO10;
    }

    public List<E01INPLIST01Bean> getE01_INP_LIST01() {
        return E01_INP_LIST01;
    }

    public void setE01_INP_LIST01(List<E01INPLIST01Bean> E01_INP_LIST01) {
        this.E01_INP_LIST01 = E01_INP_LIST01;
    }

    public List<E01INPLIST02Bean> getE01_INP_LIST02() {
        return E01_INP_LIST02;
    }

    public void setE01_INP_LIST02(List<E01INPLIST02Bean> E01_INP_LIST02) {
        this.E01_INP_LIST02 = E01_INP_LIST02;
    }

    public static class E01INPLIST01Bean {
        /**
         * E01_INP_LIST01_NO01 : xxx
         * E01_INP_LIST01_NO02 : 2
         */

        private String E01_INP_LIST01_NO01;
        private int E01_INP_LIST01_NO02;

        public String getE01_INP_LIST01_NO01() {
            return E01_INP_LIST01_NO01;
        }

        public void setE01_INP_LIST01_NO01(String E01_INP_LIST01_NO01) {
            this.E01_INP_LIST01_NO01 = E01_INP_LIST01_NO01;
        }

        public int getE01_INP_LIST01_NO02() {
            return E01_INP_LIST01_NO02;
        }

        public void setE01_INP_LIST01_NO02(int E01_INP_LIST01_NO02) {
            this.E01_INP_LIST01_NO02 = E01_INP_LIST01_NO02;
        }
    }

    public static class E01INPLIST02Bean {
        /**
         * E01_INP_LIST02_NO01 : yyy
         * E01_INP_LIST02_NO02 : 2
         */

        private String E01_INP_LIST02_NO01;
        private int E01_INP_LIST02_NO02;

        public String getE01_INP_LIST02_NO01() {
            return E01_INP_LIST02_NO01;
        }

        public void setE01_INP_LIST02_NO01(String E01_INP_LIST02_NO01) {
            this.E01_INP_LIST02_NO01 = E01_INP_LIST02_NO01;
        }

        public int getE01_INP_LIST02_NO02() {
            return E01_INP_LIST02_NO02;
        }

        public void setE01_INP_LIST02_NO02(int E01_INP_LIST02_NO02) {
            this.E01_INP_LIST02_NO02 = E01_INP_LIST02_NO02;
        }
    }
}
