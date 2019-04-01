package com.oa.wanyu.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2019/3/15.
 */

public class ReimbursementBean {
    private String money;
    private String type;

    public ReimbursementBean(String money, String type) {
        this.money = money;
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
