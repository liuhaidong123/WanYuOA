package com.oa.wanyu.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2019/3/15.
 */

public class ReimbursementBean {
    private String amount;
    private String typ;

    public ReimbursementBean(String amount, String typ) {
        this.amount = amount;
        this.typ = typ;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
