package com.oa.wanyu.activity.aBusinessTravelActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oa.wanyu.R;

//出差详情
public class AbusinessTravelMessageActivity extends AppCompatActivity {

    private ImageView mback;
    private ListView listView;
    private View footer;
    private TravelMessAdapter travelMessAdapter;
    private LinearLayout agree_disagree_ll;
    private TextView agree_btn, disagree_btn, withdraw_btn;//同意，驳回，撤回
    private Intent intent;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abusiness_travel_message);


        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        footer = LayoutInflater.from(this).inflate(R.layout.chucha_mess_footer, null);
        listView = (ListView) findViewById(R.id.travel_message_listview_id);
        travelMessAdapter = new TravelMessAdapter();
        listView.setAdapter(travelMessAdapter);
        listView.addFooterView(footer);

        //同意，驳回，撤销
        agree_disagree_ll = (LinearLayout) findViewById(R.id.agree_disagree_ll);
        agree_btn = (TextView) findViewById(R.id.travel_agree_btn);
        agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        disagree_btn = (TextView) findViewById(R.id.travel_disagree_btn);
        disagree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //撤回
        builder = new AlertDialog.Builder(this);
        builder.setTitle("是否撤回");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        withdraw_btn = (TextView) findViewById(R.id.withdraw_submit);
        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        intent = getIntent();
        //0表示待审批（显示同意，驳回），1表示已审批，什么都不显示，2表示申请中的审批中跳转过来的，（显示撤回按钮）
        int flag = intent.getIntExtra("flag", -1);
        if (flag == 0) {
            agree_disagree_ll.setVisibility(View.VISIBLE);
            agree_btn.setVisibility(View.VISIBLE);
            disagree_btn.setVisibility(View.VISIBLE);
            withdraw_btn.setVisibility(View.GONE);
        } else if (flag == 1) {
            agree_disagree_ll.setVisibility(View.GONE);
            agree_btn.setVisibility(View.GONE);
            disagree_btn.setVisibility(View.GONE);
            withdraw_btn.setVisibility(View.GONE);
        } else if (flag == 2) {
            //撤销时需要弹框
            agree_disagree_ll.setVisibility(View.VISIBLE);
            agree_btn.setVisibility(View.GONE);
            disagree_btn.setVisibility(View.GONE);
            withdraw_btn.setVisibility(View.VISIBLE);
        } else {//错误
            agree_disagree_ll.setVisibility(View.GONE);
            agree_btn.setVisibility(View.GONE);
            disagree_btn.setVisibility(View.GONE);
            withdraw_btn.setVisibility(View.GONE);
        }
    }


    class TravelMessAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Travelholder travelholder = null;
            if (view == null) {
                travelholder = new Travelholder();
                view = LayoutInflater.from(AbusinessTravelMessageActivity.this).inflate(R.layout.chucha_mess_item, null);
                travelholder.mingxi = view.findViewById(R.id.mingxi_num_tv);
                travelholder.place = view.findViewById(R.id.edit_didian);
                travelholder.startTime = view.findViewById(R.id.start_time_tv);
                travelholder.endTime = view.findViewById(R.id.end_time_tv);
                travelholder.day = view.findViewById(R.id.day_tv);
                view.setTag(travelholder);
            } else {
                travelholder = (Travelholder) view.getTag();
            }
            int a = i + 1;
            travelholder.mingxi.setText("行程明细" + "(" + a + ")");
            return view;
        }

        class Travelholder {
            TextView mingxi, startTime, endTime, day, place;

        }

    }

}
