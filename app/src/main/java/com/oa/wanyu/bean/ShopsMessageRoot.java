package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/9.
 */

public class ShopsMessageRoot {

    private String code;

    private ShopsMessageBean shop;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ShopsMessageBean getShop() {
        return shop;
    }

    public void setShop(ShopsMessageBean shop) {
        this.shop = shop;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
