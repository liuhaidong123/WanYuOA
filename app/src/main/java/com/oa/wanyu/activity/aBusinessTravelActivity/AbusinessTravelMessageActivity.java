package com.oa.wanyu.activity.aBusinessTravelActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.currencyApply.CurrencyApplyActivityMessageActivity;
import com.oa.wanyu.bean.AbusinessTravelInformationItems;
import com.oa.wanyu.bean.AbusinessTravelInfromationRoot;
import com.oa.wanyu.bean.CurrencyInformationRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.util.ArrayList;
import java.util.List;

//出差详情
public class AbusinessTravelMessageActivity extends AppCompatActivity {

    private ImageView mback,show_img;
    private ListView listView;
    private List<AbusinessTravelInformationItems> mList = new ArrayList<>();
    private View footer;
    private EditText reason;
    private TravelMessAdapter travelMessAdapter;
    private LinearLayout agree_disagree_ll;
    private TextView agree_btn, disagree_btn, withdraw_btn;//同意，驳回，撤回
    private Intent intent;
    long referId;
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private AlertDialog.Builder builder_withdraw, builder_agree, builder_disagree;
    private AlertDialog alertDialog_withdraw, alertDialog_agree, alertDialog_disagree;

    private String url, agree_url, withdraw_url;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String s = (String) msg.obj;
                    Object o = gson.fromJson(s, AbusinessTravelInfromationRoot.class);
                    if (o != null && o instanceof AbusinessTravelInfromationRoot) {
                        AbusinessTravelInfromationRoot abusinessTravelInfromationRoot = (AbusinessTravelInfromationRoot) o;
                        if (abusinessTravelInfromationRoot != null) {
                            if ("0".equals(abusinessTravelInfromationRoot.getCode())) {
                                agree_disagree_ll.setVisibility(View.VISIBLE);
                                if (abusinessTravelInfromationRoot.getApplyTrip() != null) {
                                    reason.setText(abusinessTravelInfromationRoot.getApplyTrip().getCause() + "");
                                    if (abusinessTravelInfromationRoot.getApplyTrip().getItems() != null) {
                                        mList = abusinessTravelInfromationRoot.getApplyTrip().getItems();
                                        travelMessAdapter.notifyDataSetChanged();
                                    }
                                }

                            } else if ("-1".equals(abusinessTravelInfromationRoot.getCode())) {
                                Toast.makeText(AbusinessTravelMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                show_img.setVisibility(View.GONE);
                                agree_disagree_ll.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(AbusinessTravelMessageActivity.this, "此数据不存在", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(AbusinessTravelMessageActivity.this, "后台数据错误", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    show_img.setVisibility(View.GONE);
                    Toast.makeText(AbusinessTravelMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                    agree_disagree_ll.setVisibility(View.GONE);
                }

            } else if (msg.what == 2) {//撤回
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(AbusinessTravelMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 3) {//同意或者驳回
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(AbusinessTravelMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                    agree_disagree_ll.setVisibility(View.GONE);
                }

            } else {
                //数据错误
                show_img.setVisibility(View.GONE);
                Toast.makeText(AbusinessTravelMessageActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
                agree_disagree_ll.setVisibility(View.GONE);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abusiness_travel_message);

        show_img=(ImageView) findViewById(R.id.show_sign);

        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_abusiness_travel_information;
        agree_url = URLTools.urlBase + URLTools.apply_abusiness_travel_agree;
        withdraw_url = URLTools.urlBase + URLTools.apply_abusiness_travel_withdraw;

        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_abusiness_travel_message);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (referId != -1) {//请求详情
                    BallProgressUtils.showLoading(AbusinessTravelMessageActivity.this, mAll_RL);
                    okHttpManager.getMethod(false, url + "id=" + referId, "请求出差详情", handler, 1);
                } else {//传过来的详情ID错误
                    Toast.makeText(AbusinessTravelMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


        footer = LayoutInflater.from(this).inflate(R.layout.chucha_mess_footer, null);
        reason = footer.findViewById(R.id.edit_reason);
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
                if (referId != -1) {//请求详情
                    alertDialog_agree.show();
                } else {//传过来的详情ID错误
                    Toast.makeText(AbusinessTravelMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        disagree_btn = (TextView) findViewById(R.id.travel_disagree_btn);
        disagree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (referId != -1) {//请求详情
                    alertDialog_disagree.show();
                } else {//传过来的详情ID错误
                    Toast.makeText(AbusinessTravelMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        withdraw_btn = (TextView) findViewById(R.id.withdraw_submit);
        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (referId != -1) {//请求详情
                    alertDialog_withdraw.show();
                } else {//传过来的详情ID错误
                    Toast.makeText(AbusinessTravelMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //撤回
        builder_withdraw = new AlertDialog.Builder(this);
        builder_withdraw.setTitle("是否撤回");
        builder_withdraw.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_withdraw.dismiss();
                BallProgressUtils.showLoading(AbusinessTravelMessageActivity.this, mAll_RL);
                okHttpManager.getMethod(false, withdraw_url + "id=" + referId, "撤回接口", handler, 2);
            }
        });
        builder_withdraw.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_withdraw.dismiss();
            }
        });
        alertDialog_withdraw = builder_withdraw.create();

        //同意
        builder_agree = new AlertDialog.Builder(this);
        builder_agree.setTitle("是否同意");
        builder_agree.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_agree.dismiss();
                BallProgressUtils.showLoading(AbusinessTravelMessageActivity.this, mAll_RL);
                okHttpManager.getMethod(false, agree_url + "id=" + referId + "&isAdopt=" + 1, "同意接口", handler, 3);
            }
        });
        builder_agree.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_agree.dismiss();
            }
        });
        alertDialog_agree = builder_agree.create();

        //驳回
        builder_disagree = new AlertDialog.Builder(this);
        builder_disagree.setTitle("是否驳回");
        builder_disagree.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_disagree.dismiss();
                BallProgressUtils.showLoading(AbusinessTravelMessageActivity.this, mAll_RL);
                okHttpManager.getMethod(false, agree_url + "id=" + referId + "&isAdopt=" + 0, "驳回接口", handler, 3);
            }
        });
        builder_disagree.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_disagree.dismiss();
            }
        });
        alertDialog_disagree = builder_disagree.create();


        intent = getIntent();
        referId = intent.getLongExtra("id", -1);
        if (referId != -1) {//请求详情
            okHttpManager.getMethod(false, url + "id=" + referId, "请求出差详情", handler, 1);
        } else {//传过来的详情ID错误
            Toast.makeText(this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
        }
        //0表示待审批（显示同意，驳回），1表示已审批，什么都不显示，2表示申请中的审批中跳转过来的，（显示撤回按钮）
        int flag = intent.getIntExtra("flag", -1);
       int show_flag=intent.getIntExtra("show_flag",-1);
        if (flag == 0) {
            agree_disagree_ll.setVisibility(View.VISIBLE);
            agree_btn.setVisibility(View.VISIBLE);
            disagree_btn.setVisibility(View.VISIBLE);
            withdraw_btn.setVisibility(View.GONE);
        } else if (flag == 1) {

            if (show_flag==100){//已完成图片
                show_img.setVisibility(View.VISIBLE);
                show_img.setImageResource(R.mipmap.t_img);
            }else if (show_flag==200){//被驳回图片
                show_img.setVisibility(View.VISIBLE);
                show_img.setImageResource(R.mipmap.b_img);
            }else {
                show_img.setVisibility(View.GONE);
            }

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
            return mList.size();
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
            travelholder.startTime.setText(mList.get(i).getBeginDateString() + "");
            travelholder.endTime.setText(mList.get(i).getEndDateString() + "");
            travelholder.day.setText(mList.get(i).getDuration() + "");
            travelholder.place.setText(mList.get(i).getPlace() + "");

            return view;
        }

        class Travelholder {
            TextView mingxi, startTime, endTime, day, place;

        }

    }

}
