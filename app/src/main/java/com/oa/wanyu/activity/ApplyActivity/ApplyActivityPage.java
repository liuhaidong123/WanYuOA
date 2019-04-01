package com.oa.wanyu.activity.ApplyActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oa.wanyu.R;
import com.oa.wanyu.activity.aBusinessTravelActivity.AbusinessTravelActivity;
import com.oa.wanyu.activity.approvalActivity.ApprovalActivity;
import com.oa.wanyu.activity.completeActivity.CompleteActivity;
import com.oa.wanyu.activity.currencyApply.CurrencyApplyActivity;
import com.oa.wanyu.activity.goodsBuyActivity.GoodsBuyActivity;
import com.oa.wanyu.activity.goodsUseActivity.GoodsUseActivity;
import com.oa.wanyu.activity.invalidActivity.InvalidActivity;
import com.oa.wanyu.activity.leave.LeaveActivity;
import com.oa.wanyu.activity.outActivity.OutActivity;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivity;
import com.oa.wanyu.activity.rejectActivity.RejectActivity;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

//申请页面
public class ApplyActivityPage extends AppCompatActivity implements View.OnClickListener {

    private ImageView mback;
    private LinearLayout mChuCha_ll,mLeave_ll,mOut_ll,mReimbursement_ll,goods_use_ll,goods_buy_ll,currency_ll;
    private LinearLayout spz_ll,reject_ll,complete_ll,invalid_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_page);
        iniUI();
    }

    private void iniUI() {
        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(this);

        spz_ll= (LinearLayout) findViewById(R.id.spz_ll);
        spz_ll.setOnClickListener(this);

        mChuCha_ll = (LinearLayout) findViewById(R.id.chucha_ll);
        mChuCha_ll.setOnClickListener(this);

        mLeave_ll = (LinearLayout) findViewById(R.id.qingjia_ll);
        mLeave_ll.setOnClickListener(this);

        mOut_ll= (LinearLayout) findViewById(R.id.waichu_ll);
        mOut_ll.setOnClickListener(this);

        mReimbursement_ll= (LinearLayout) findViewById(R.id.baoxiao_ll);
        mReimbursement_ll.setOnClickListener(this);

        goods_use_ll= (LinearLayout) findViewById(R.id.wu_lingyong_ll);
        goods_use_ll.setOnClickListener(this);

        goods_buy_ll= (LinearLayout) findViewById(R.id.wu_shenggou_ll);
        goods_buy_ll.setOnClickListener(this);

        currency_ll= (LinearLayout) findViewById(R.id.tongyong_ll);
        currency_ll.setOnClickListener(this);

        reject_ll= (LinearLayout) findViewById(R.id.bbh_ll);
        reject_ll.setOnClickListener(this);

        complete_ll= (LinearLayout) findViewById(R.id.ywc_ll);
        complete_ll.setOnClickListener(this);

        invalid_ll= (LinearLayout) findViewById(R.id.ysx_ll);
        invalid_ll.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mback.getId()) {
            finish();
            //出差
        } else if (id == mChuCha_ll.getId()) {
            Intent i = new Intent(this, AbusinessTravelActivity.class);
            startActivity(i);
//            请假
        }else if (id == mLeave_ll.getId()) {
            Intent i = new Intent(this, LeaveActivity.class);
            startActivity(i);
            //外出
        }else if (id == mOut_ll.getId()) {
            Intent i = new Intent(this, OutActivity.class);
            startActivity(i);
//            报销
        }else if (id == mReimbursement_ll.getId()) {
            Intent i = new Intent(this, ReimbursementActivity.class);
            startActivity(i);

            //物品领用
        }else if (id == goods_use_ll.getId()) {
            Intent i = new Intent(this, GoodsUseActivity.class);
            startActivity(i);
            //物品申购
        }else if (id == goods_buy_ll.getId()) {
            Intent i = new Intent(this, GoodsBuyActivity.class);
            startActivity(i);
        }else if (id == currency_ll.getId()) {
            Intent i = new Intent(this, CurrencyApplyActivity.class);
            startActivity(i);
           //审批中
        }else if (id == spz_ll.getId()) {
            Intent i = new Intent(this, ApprovalActivity.class);
            startActivity(i);
            //被驳回
        }else if (id ==reject_ll.getId()) {
            Intent i = new Intent(this, RejectActivity.class);
            startActivity(i);
            //已完成
        }else if (id ==complete_ll.getId()) {
            Intent i = new Intent(this, CompleteActivity.class);
            startActivity(i);
//            已失效
        }else if (id ==invalid_ll.getId()) {
            Intent i = new Intent(this, InvalidActivity.class);
            startActivity(i);
        }
    }


}
