package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/5/29.
 */

public class CustomerInformaionRoot {

    private String code;

    private String message;

    private CustomerInformationBean customer;

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

    public CustomerInformationBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInformationBean customer) {
        this.customer = customer;
    }
}
