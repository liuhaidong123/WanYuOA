package com.oa.wanyu.OkHttpUtils;

/**
 * Created by liuhaidong on 2019/3/19.
 */

public class URLTools {
    public static String urlBase = "http://192.168.1.168:8086";
    public static String login_url = "/admin/login";//登录接口 post 参数name手机号，password密码
    public static String isLogin_url = "/admin/isLogin";//  判断是否登录接口：返回 code 说明： 0、已登录 -1、您还没有登录！
    public static String apply_all_status = "/mobileapi/convergeApply/findPage.do?";//msgStatus=   申请》 0=审批中，1=被驳回，2=已审批，3=已撤销
    public static String approval_status = "/mobileapi/convergeApproval/findPage.do?";// msgStatus=：消息状态, 0=待审批，1=已审批
    public static String apply_abusiness_travel = "/mobileapi/applyTrip/save.do?";//提交出差申请cause=出差事由&items=
    public static String apply_leave = "/mobileapi/applyLeave/save.do?";//提交请假申请beginDate=2018-08-17&endDate=2018-08-17&duration=1&typ=事假&reason=请假事由
    public static String apply_out = "/mobileapi/applyOut/save.do?";//   提交外出申请beginDate=2018-08-17&endDate=2018-08-17&duration=1&reason=外出事由
    public static String leave_type="/mobileapi/applyLeave/getType.do";//请假类型
    public static String apply_use="/mobileapi/applyRecipients/save.do?";//提交资产领用申请purpose=物品用途&items=
    public static String apply_buy="/mobileapi/applyBuy/save.do?";//提交物品申购申请items=
    public static String apply_currency="/mobileapi/applyUniversal/save.do?";//提交通用申请content=""&detail=""
    public static String apply_reimbursement="/mobileapi/applyReimburse/save.do?";// 提交报销申请items=


}
