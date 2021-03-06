package com.oa.wanyu.OkHttpUtils;

/**
 * Created by liuhaidong on 2019/3/19.
 */

public class URLTools {
    public static String urlBase="http://59.110.169.148:8183";
    //public static String urlBase = "http://192.168.1.168:8086";
    public static String login_url = "/admin/login?";//登录接口 post 参数name手机号，password密码
    public static String isLogin_url = "/admin/isLogin";//  判断是否登录接口：返回 code 说明： 0、已登录 -1、您还没有登录！
    public static String apply_all_status = "/mobileapi/convergeApply/findPage.do?";//msgStatus=   申请》 0=审批中，1=被驳回，2=已审批，3=已撤销
    public static String approval_status = "/mobileapi/convergeApproval/findPage.do?";// msgStatus=：消息状态, 0=待审批，1=已审批
    public static String apply_abusiness_travel = "/mobileapi/applyTrip/save.do?";//提交出差申请cause=出差事由&items=
    public static String apply_leave = "/mobileapi/applyLeave/save.do?";//提交请假申请beginDate=2018-08-17&endDate=2018-08-17&duration=1&typ=事假&reason=请假事由
    public static String apply_out = "/mobileapi/applyOut/save.do?";//   提交外出申请beginDate=2018-08-17&endDate=2018-08-17&duration=1&reason=外出事由
    public static String leave_type = "/mobileapi/applyLeave/getType.do";//请假类型
    public static String apply_use = "/mobileapi/applyRecipients/save.do?";//提交资产领用申请purpose=物品用途&items=
    public static String apply_buy = "/mobileapi/applyBuy/save.do?";//提交物品申购申请items=
    public static String apply_currency = "/mobileapi/applyUniversal/save.do?";//提交通用申请content=""&detail=""
    public static String apply_reimbursement = "/mobileapi/applyReimburse/save.do?";// 提交报销申请items=
    public static String apply_reimbursement_type = "/mobileapi/applyReimburse/getType.do";//报销类型

    public static String apply_currency_information = "/mobileapi/applyUniversal/get.do?";//通用详情id=
    public static String apply_currency_agree = "/mobileapi/applyUniversal/doApproval.do?";//id=2&isAdopt=1 通用详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_currency_withdraw = "/mobileapi/applyUniversal/cancel.do?";// 撤销通用接口id=1

    public static String apply_abusiness_travel_information = "/mobileapi/applyTrip/get.do?";//出差详情id=
    public static String apply_abusiness_travel_agree = "/mobileapi/applyTrip/doApproval.do?";//id=2&isAdopt=1 出差详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_abusiness_travel_withdraw = "/mobileapi/applyTrip/cancel.do?";// 撤销出差接口id=1

    public static String apply_leave_information = "/mobileapi/applyLeave/get.do?";//请假详情id=
    public static String apply_leave_agree = "/mobileapi/applyLeave/doApproval.do?";//id=2&isAdopt=1 请假详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_leave_withdraw = "/mobileapi/applyLeave/cancel.do?";// 撤销请假接口id=1

    public static String apply_out_information = "/mobileapi/applyOut/get.do?";//外出详情id=
    public static String apply_out_agree = "/mobileapi/applyOut/doApproval.do?";//id=2&isAdopt=1 外出详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_out_withdraw = "/mobileapi/applyOut/cancel.do?";// 撤销外出接口id=1

    public static String apply_reimbursement_information = "/mobileapi/applyReimburse/get.do?";//报销详情id=
    public static String apply_reimbursement_agree = "/mobileapi/applyReimburse/doApproval.do?";//id=2&isAdopt=1 报销详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_reimbursement_withdraw = "/mobileapi/applyReimburse/cancel.do?";// 撤销报销接口id=1

    public static String apply_use_information = "/mobileapi/applyRecipients/get.do?";//领用详情id=
    public static String apply_use_agree = "/mobileapi/applyRecipients/doApproval.do?";//id=2&isAdopt=1 领用详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_use_withdraw = "/mobileapi/applyRecipients/cancel.do?";// 撤销领用接口id=1

    public static String apply_buy_information = "/mobileapi/applyBuy/get.do?";//申购详情id=
    public static String apply_buy_agree = "/mobileapi/applyBuy/doApproval.do?";//id=2&isAdopt=1 申购详情同意，驳回isAdopt=是否同意，1同意，0驳回
    public static String apply_buy_withdraw = "/mobileapi/applyBuy/cancel.do?";// 撤销申购接口id=1

    public static String shops_list = "/mobileapi/building/findList.do";//商铺列表
    public static String residential_list = "/mobileapi/shop/findPage.do?";//商铺下的小区列表buildingId=10&state=40(不需要status这个参数)
    public static String shops_message = "/mobileapi/shop/get.do?";//商铺详情id=97

    public static String sign_sell = "/mobileapi/shop/save.do?";//商铺标记为已售id=1&state=50&stateText=%E5%B7%B2%E5%94%AE

    public static String notice_mess = "/mobileapi/message/save.do?";//发布公告title&content&msgStatus
    public static String notice_list = "/mobileapi/message/findPage.do?"; //消息》分页查询消息列表接口
    public static String notice_delete = "/mobileapi/message/setDeleted.do?";//删除通知id=消息编号
    public static String notice_sign = "/mobileapi/message/setReaded.do?";//标记为已读id=消息编号

    public static String user_message = "/mobileapi/user/get.do";//获取用户信息

    public static String select_contacts = "/mobileapi/contact/findList.do?";//  查询联系人列表
    public static String select_contacts2 = "/mobileapi/user/findContact.do?";//  查询联系人列表2



    public static String select_history_lease = "/mobileapi/shopLog/findList.do?";//  查询出租历史记录接口shopId=1

    public static String expire_remind = "/mobileapi/shop/findRemaining.do?";//到期提醒列表
    public static String sure_money="/mobileapi/shop/save.do?";//确认交租接口id=1&lastBeginDate=2019-05-09&lastEndDate=2020-05-09&lastPaidIn=1234

    public static String floor_address_list="/mobileapi/building/findList.do?";//楼盘地址列表
    public static String floor_num_list="/mobileapi/house/findHouse.do?";//楼号列表buildingId=1
    public static String floor_unit_list="/mobileapi/house/findUnit.do?";//单元列表buildingId=1&houseNum=1
    public static String floor_all_house="/mobileapi/house/findList.do?";//单元中所有的房屋buildingId=1&houseNum=1&uint=1
    public static String floor_house_information="/mobileapi/house/get.do?";//户型图

    public static String house_sign_sell = "/mobileapi/house/save.do?";//房屋标记为已售id=1&state=50&stateText=%E5%B7%B2%E5%94%AE

    public static String customer_list="/mobileapi/customer/findPage.do?";//客户列表加字段name为搜索接口
    public static String customer_add="/mobileapi/customer/save.do?";//添加客户(也是标记为星级客户接口，需要id,star=0,非星级，1星级)
    public static String customer_message="/mobileapi/customer/get.do?";//客户详情id="";

    public static String customer_delete="/mobileapi/customer/delete.do?";//删除客户id="";

    public static String agent_cpmpany_list="/mobileapi/company/findPage.do?";//代理公司接口
    public static String agent_cpmpany_information_list="/mobileapi/company/get.do?";//代理公司详情接口id=""
    public static String agent_company_add_person="/mobileapi/user/save.do?";//添加销售人员接口
    public static String agent_company_delete_person="/mobileapi/user/delete.do?";//删除销售人员接口
    public static String salesperson_list="/mobileapi/user/findList.do?";//销售人员列表companyId=1此id是销售人员登录时保存下来的id



    public static String floor_statistic_number="/mobileapi/houseSale/findList.do?";//楼房统计(按月统计)
    public static String floor_statistic_month_number="/mobileapi/houseSale/findList.do?";//楼房统计(按每个月数量明细)


    public static String set_password="/mobileapi/user/modPwd.do?";//修改密码接口password=旧密码&password2=新密码
    public static String exit_app="/admin/logoutAjax?";//退出登录
    public static String viewpager_img="/mobileapi/carousel/findList.do?";//首页轮播图接口
    public static String check_verson_code="/mobileapi/dict/get.do?id=6";//检测版本号

}
