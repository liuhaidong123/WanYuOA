package com.oa.wanyu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oa.wanyu.R;
import com.oa.wanyu.activity.AgentCompany.AgentCompanyActivity;
import com.oa.wanyu.activity.ApplyActivity.ApplyActivityPage;
import com.oa.wanyu.activity.Customer.CustomerActivity;
import com.oa.wanyu.activity.SP_Activity.SPActivity;
import com.oa.wanyu.activity.floorManage.FloorManageActivity;
import com.oa.wanyu.activity.leaseManage.ExpireRemindctivity;
import com.oa.wanyu.activity.leaseManage.LeaseManagectivity;
import com.oa.wanyu.activity.notice_activity.NoticeActivity;
import com.oa.wanyu.activity.shopsManage.ShopsManagectivity;
import com.oa.wanyu.activity.statistic.StatisticActivity;
import com.oa.wanyu.bean.login.Permission;
import com.oa.wanyu.myutils.SharedPrefrenceTools;

/**
 * 工作
 */
public class JobFragment extends Fragment implements View.OnClickListener {
    private LinearLayout mShengQin_ll, mSP_ll, mGongGao_ll, mShang_Pu_ll, mChu_Zu_ll, Expire_ll, floorManage_ll, ke_hu_ll, agent_ll, tongji_ll;

    private SharedPrefrenceTools sharedPrefrenceTools;

    public JobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        intiUI(view);
        return view;
    }

    private void intiUI(View view) {
        sharedPrefrenceTools = SharedPrefrenceTools.getSharedPrefrenceToolsInstance(getActivity());

        mShengQin_ll = view.findViewById(R.id.shenqing_ll);
        mShengQin_ll.setOnClickListener(this);

        mSP_ll = view.findViewById(R.id.shenpi_ll);
        mSP_ll.setOnClickListener(this);

        mGongGao_ll = view.findViewById(R.id.gonggao_ll);
        mGongGao_ll.setOnClickListener(this);

        mShang_Pu_ll = view.findViewById(R.id.shangpu_ll);
        mShang_Pu_ll.setOnClickListener(this);

        mChu_Zu_ll = view.findViewById(R.id.shangpu_chuzu_ll);
        mChu_Zu_ll.setOnClickListener(this);

        Expire_ll = view.findViewById(R.id.tixing_ll);
        Expire_ll.setOnClickListener(this);

        floorManage_ll = view.findViewById(R.id.loupan_ll);
        floorManage_ll.setOnClickListener(this);

        ke_hu_ll = view.findViewById(R.id.kehu_ll);
        ke_hu_ll.setOnClickListener(this);

        agent_ll = view.findViewById(R.id.daili_ll);
        agent_ll.setOnClickListener(this);

        tongji_ll = view.findViewById(R.id.xiaoshou_tongji_ll);
        tongji_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mShengQin_ll.getId()) {//跳转申请页面
            Intent i = new Intent(getActivity(), ApplyActivityPage.class);
            startActivity(i);
        } else if (id == mSP_ll.getId()) {//跳转审批页面
            Intent i = new Intent(getActivity(), SPActivity.class);
            startActivity(i);
        } else if (id == mGongGao_ll.getId()) {//跳转公告页面

            boolean permissionC = false;
            for (int k = 0; k < (int) SharedPrefrenceTools.getValueByKey("PermissionNum", 0); k++) {
                Permission permission = (Permission) SharedPrefrenceTools.getObject("Permission" + k);
                if (permission != null) {
                    //如果有这个权限
                    if ("notice".equals(permission.getTarget())) {
                        //如果权限等于*，就有盘点权限
                        if ("*".equals(permission.getOperaton()) || "create".equals(permission.getOperaton())) {
                            permissionC = true;
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "存储权限对象为空", Toast.LENGTH_SHORT).show();
                }

            }
            if (permissionC) {
                Intent i = new Intent(getActivity(), NoticeActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getActivity(), "抱歉，您没有发布通知权限", Toast.LENGTH_SHORT).show();

            }


        } else if (id == mShang_Pu_ll.getId()) {//商铺查看

            boolean permissionA = false;
            for (int k = 0; k < (int) SharedPrefrenceTools.getValueByKey("PermissionNum", 0); k++) {
                Permission permission = (Permission) SharedPrefrenceTools.getObject("Permission" + k);
                if (permission != null) {
                    //如果有这个权限
                    if ("shop".equals(permission.getTarget())) {
                        //如果权限等于*，就有权限
                        if ("*".equals(permission.getOperaton())) {
                            permissionA = true;
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "存储权限对象为空", Toast.LENGTH_SHORT).show();
                }

            }
            if (permissionA) {
                Intent i = new Intent(getActivity(), ShopsManagectivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getActivity(), "抱歉，您没有查看商铺的权限", Toast.LENGTH_SHORT).show();

            }

        } else if (id == mChu_Zu_ll.getId()) {//出租管理
            boolean permissionB = false;
            for (int k = 0; k < (int) SharedPrefrenceTools.getValueByKey("PermissionNum", 0); k++) {
                Permission permission = (Permission) SharedPrefrenceTools.getObject("Permission" + k);
                if (permission != null) {
                    //如果有这个权限
                    if ("shop".equals(permission.getTarget())) {
                        //如果权限等于*，就有权限
                        if ("*".equals(permission.getOperaton())) {
                            permissionB = true;
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "存储权限对象为空", Toast.LENGTH_SHORT).show();
                }

            }
            if (permissionB) {
                Intent i = new Intent(getActivity(), LeaseManagectivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getActivity(), "抱歉，您没有查看出租管理的权限", Toast.LENGTH_SHORT).show();

            }


        } else if (id == Expire_ll.getId()) {//到期提醒

            boolean permissionD = false;
            for (int k = 0; k < (int) SharedPrefrenceTools.getValueByKey("PermissionNum", 0); k++) {
                Permission permission = (Permission) SharedPrefrenceTools.getObject("Permission" + k);
                if (permission != null) {
                    //如果有这个权限
                    if ("shop".equals(permission.getTarget())) {
                        //如果权限等于*，就有权限
                        if ("*".equals(permission.getOperaton())) {
                            permissionD = true;
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "存储权限对象为空", Toast.LENGTH_SHORT).show();
                }

            }
            if (permissionD) {
                Intent i = new Intent(getActivity(), ExpireRemindctivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getActivity(), "抱歉，您没有查看到期提醒的权限", Toast.LENGTH_SHORT).show();

            }


        } else if (id == floorManage_ll.getId()) {//楼盘管理
            Intent i = new Intent(getActivity(), FloorManageActivity.class);
            startActivity(i);
        } else if (id == ke_hu_ll.getId()) {//跳转客户管理
            Intent i = new Intent(getActivity(), CustomerActivity.class);
            startActivity(i);
        } else if (id == agent_ll.getId()) {//跳转代理公司
            Intent i = new Intent(getActivity(), AgentCompanyActivity.class);
            startActivity(i);
        } else if (id == tongji_ll.getId()) {//跳转销售统计
            Intent i = new Intent(getActivity(), StatisticActivity.class);
            startActivity(i);
        }
    }
}
