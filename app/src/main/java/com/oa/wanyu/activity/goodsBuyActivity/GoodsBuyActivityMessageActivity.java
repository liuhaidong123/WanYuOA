package com.oa.wanyu.activity.goodsBuyActivity;

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

//物品申购详情
public class GoodsBuyActivityMessageActivity extends AppCompatActivity {
    private ImageView mBack;
    private ListView mListview;
    private GoodsBuyMessageAda goodsBuyMessageAda;
    private View footer;
    private LinearLayout agree_disagree_ll;
    private TextView agree_btn, disagree_btn, withdraw_btn,all_price;//同意，驳回，撤回
    private Intent intent;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_buy_message);

        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListview= (ListView) findViewById(R.id.goods_buy_message_listview_id);
        goodsBuyMessageAda=new GoodsBuyMessageAda();
        footer = LayoutInflater.from(this).inflate(R.layout.goods_buy_message_footer, null);
        mListview.addFooterView(footer);
        mListview.setAdapter(goodsBuyMessageAda);
        //所有申购物品的总价
        all_price=footer.findViewById(R.id.goods_buy_all_price_tv);


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

    class  GoodsBuyMessageAda extends BaseAdapter{

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
            GoodsBuyMessageHolder goodsBuyMessageHolder=null;
            if (view==null){
                goodsBuyMessageHolder=new GoodsBuyMessageHolder();
                view = LayoutInflater.from(GoodsBuyActivityMessageActivity.this).inflate(R.layout.goods_buy_message_item, null);
                goodsBuyMessageHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
                goodsBuyMessageHolder.name = view.findViewById(R.id.edit_goods_buy_name);
                goodsBuyMessageHolder.model = view.findViewById(R.id.edit_goods_buy_model);
                goodsBuyMessageHolder.unitprice = view.findViewById(R.id.edit_goods_buy_price);
                goodsBuyMessageHolder.num = view.findViewById(R.id.edit_goods_buy_num);
                goodsBuyMessageHolder.allprice = view.findViewById(R.id.goods_all_price);
                goodsBuyMessageHolder.mess = view.findViewById(R.id.edit_goods_use_reason);

                view.setTag(goodsBuyMessageHolder);
            }else {
                goodsBuyMessageHolder= (GoodsBuyMessageHolder) view.getTag();
            }

            int a = i + 1;
            goodsBuyMessageHolder.mingxi.setText("物品申购明细" + "(" + a + ")");
            return view;
        }

        class  GoodsBuyMessageHolder{
            TextView mingxi, allprice;
            EditText name, model, unitprice, num, mess;
        }
    }
}
