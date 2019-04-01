package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/3/5.
 */

public class TestBean {
    private String name;
    private String pinying;

    public String getPinying() {
        return pinying;
    }

    public void setPinying(String pinying) {
        this.pinying = pinying;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public TestBean(String name, String pinying) {
        this.name = name;
        this.pinying = pinying;
    }
}
