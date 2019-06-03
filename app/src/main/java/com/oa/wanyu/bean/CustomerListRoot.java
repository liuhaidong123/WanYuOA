package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/5/28.
 */

public class CustomerListRoot {
    private int total;

    private String code;

    private String message;

    private List<CustomerListRows> rows;

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

    public List<CustomerListRows> getRows() {
        return rows;
    }

    public void setRows(List<CustomerListRows> rows) {
        this.rows = rows;
    }

    public List<String> getColmodel() {
        return colmodel;
    }

    public void setColmodel(List<String> colmodel) {
        this.colmodel = colmodel;
    }
}
