package com.oa.wanyu.activity.reimbursementActivity;

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
import com.oa.wanyu.myutils.MyGridView;

import it.sephiroth.android.library.picasso.Picasso;

//报销详情
public class ReimbursementActivityMessageActivity extends AppCompatActivity {
    private ImageView mBack;
    private ListView mListview;
    private ReimbursementMessAda reimbursementMessAda;
    private View footer;
    private MyGridView myGridView;
    private PictureMessAda pictureMessAda;
    private TextView all_money;
    private LinearLayout agree_disagree_ll;
    private TextView agree_btn, disagree_btn, withdraw_btn;//同意，驳回，撤回
    private Intent intent;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement_message);

        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListview = (ListView) findViewById(R.id.bx_listview_id);
        reimbursementMessAda=new ReimbursementMessAda();
        mListview.setAdapter(reimbursementMessAda);
        footer=LayoutInflater.from(this).inflate(R.layout.baoxiao_message_footer,null);
        mListview.addFooterView(footer);
        //报销总金额
        all_money = footer.findViewById(R.id.bx_tv);
        myGridView = footer.findViewById(R.id.img_gridview);
        pictureMessAda = new PictureMessAda();
        myGridView.setAdapter(pictureMessAda);
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

    class ReimbursementMessAda extends BaseAdapter {

        @Override
        public int getCount() {
            return 7;
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
            ReimHolder reimHolder = null;
            if (view == null) {
                reimHolder = new ReimHolder();
                view = LayoutInflater.from(ReimbursementActivityMessageActivity.this).inflate(R.layout.reimbursment_message_item, null);
                reimHolder.money = view.findViewById(R.id.edit_money);
                reimHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
                reimHolder.type = view.findViewById(R.id.type_mess_tv);
                view.setTag(reimHolder);
            } else {
                reimHolder = (ReimHolder) view.getTag();
            }
            int a = i + 1;
            reimHolder.mingxi.setText("报销明细" + "(" + a + ")");
            return view;
        }

        class ReimHolder {
            EditText money;
            TextView type, mingxi;
        }
    }


    class PictureMessAda extends BaseAdapter{

        @Override
        public int getCount() {
            return 6;
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
            PHolder pHolder=null;
            if (view==null){
                pHolder=new PHolder();
                view = LayoutInflater.from(ReimbursementActivityMessageActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
                pHolder.imageView = view.findViewById(R.id.picture);
                view.setTag(pHolder);
            }else {
                pHolder= (PHolder) view.getTag();
            }
            Picasso.with(ReimbursementActivityMessageActivity.this).load(R.mipmap.head_img_icon).into(pHolder.imageView);

            return view;
        }

        class PHolder {
            ImageView imageView;
        }
    }
}
