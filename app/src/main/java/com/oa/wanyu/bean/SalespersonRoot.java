package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/6/13.
 */

public class SalespersonRoot {
    private int total;

    private String code;

    private String message;

    private List<SalespersonRows> rows;

    private List<String> colmodel;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SalespersonRows> getRows() {
        return rows;
    }

    public void setRows(List<SalespersonRows> rows) {
        this.rows = rows;
    }

    public List<String> getColmodel() {
        return colmodel;
    }

    public void setColmodel(List<String> colmodel) {
        this.colmodel = colmodel;
    }
}
