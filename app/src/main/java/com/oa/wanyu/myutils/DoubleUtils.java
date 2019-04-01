package com.oa.wanyu.myutils;

/**
 * Created by liuhaidong on 2019/3/21.
 */

public class DoubleUtils {

    public static boolean myDoubleC(String mPrice_edit){
        if (!"".equals(mPrice_edit.toString())) {
            //以小数点开头不行
            if (mPrice_edit.toString().indexOf(".") == 0) {
                //填写正确价格
                return false;
            }else{
                //以0开头并且小数点没有紧跟在0的后面，也不行
                if (mPrice_edit.toString().indexOf(".") > 1 && mPrice_edit.toString().startsWith("0")) {
                    return false;
                }else{
                    //以0开头并且没有小数点，也不行
                    if (mPrice_edit.toString().indexOf(".") == -1 && mPrice_edit.toString().startsWith("0")) {

                        return false;
                    }else{
                        //执行操作
                        return true;

                    }

                }

            }

        }else {
            return false;

        }
    }
}
