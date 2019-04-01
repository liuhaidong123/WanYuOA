package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/3/18.
 */

public class GoodsUseBean {
    private String nam;
    private int num;

    public GoodsUseBean(String nam, int num) {
        this.nam = nam;
        this.num = num;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
