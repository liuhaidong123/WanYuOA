package com.oa.wanyu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oa.wanyu.R;
import com.oa.wanyu.activity.ApplyActivity.ApplyActivityPage;
import com.oa.wanyu.activity.SP_Activity.SPActivity;
import com.oa.wanyu.activity.floorManage.FloorManageActivity;
import com.oa.wanyu.activity.leaseManage.ExpireRemindctivity;
import com.oa.wanyu.activity.leaseManage.LeaseManagectivity;
import com.oa.wanyu.activity.notice_activity.NoticeActivity;
import com.oa.wanyu.activity.shopsManage.ShopsManagectivity;

/**
 * 工作
 */
public class JobFragment extends Fragment implements View.OnClickListener {
    private LinearLayout mShengQin_ll,mSP_ll,mGongGao_ll,mShang_Pu_ll,mChu_Zu_ll,Expire_ll,floorManage_ll;

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

        mShengQin_ll = view.findViewById(R.id.shenqing_ll);
        mShengQin_ll.setOnClickListener(this);

        mSP_ll= view.findViewById(R.id.shenpi_ll);
        mSP_ll.setOnClickListener(this);

        mGongGao_ll= view.findViewById(R.id.gonggao_ll);
        mGongGao_ll.setOnClickListener(this);

        mShang_Pu_ll= view.findViewById(R.id.shangpu_ll);
        mShang_Pu_ll.setOnClickListener(this);

        mChu_Zu_ll= view.findViewById(R.id.shangpu_chuzu_ll);
        mChu_Zu_ll.setOnClickListener(this);

        Expire_ll= view.findViewById(R.id.tixing_ll);
        Expire_ll.setOnClickListener(this);

        floorManage_ll= view.findViewById(R.id.loupan_ll);
        floorManage_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==mShengQin_ll.getId()){//跳转申请页面
            Intent i=new Intent(getActivity(), ApplyActivityPage.class);
            startActivity(i);
        }else if (id==mSP_ll.getId()){//跳转审批页面
            Intent i=new Intent(getActivity(),SPActivity.class);
            startActivity(i);
        }else if (id==mGongGao_ll.getId()){//跳转公告页面
            Intent i=new Intent(getActivity(),NoticeActivity.class);
            startActivity(i);
        }else if (id==mShang_Pu_ll.getId()){//商铺查看
            Intent i=new Intent(getActivity(),ShopsManagectivity.class);
            startActivity(i);
        }
        else if (id==mChu_Zu_ll.getId()){//出租管理
            Intent i=new Intent(getActivity(),LeaseManagectivity.class);
            startActivity(i);
        }else if (id==Expire_ll.getId()){//到期提醒
            Intent i=new Intent(getActivity(),ExpireRemindctivity.class);
            startActivity(i);
        }else if (id==floorManage_ll.getId()){//楼盘管理
            Intent i=new Intent(getActivity(),FloorManageActivity.class);
            startActivity(i);
        }
    }
}
