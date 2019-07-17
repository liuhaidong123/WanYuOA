package com.oa.wanyu.activity.SP_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.aBusinessTravelActivity.AbusinessTravelMessageActivity;
import com.oa.wanyu.activity.currencyApply.CurrencyApplyActivityMessageActivity;
import com.oa.wanyu.activity.goodsBuyActivity.GoodsBuyActivityMessageActivity;
import com.oa.wanyu.activity.goodsUseActivity.GoodsUseActivityMessageActivity;
import com.oa.wanyu.activity.leave.LeaveActivityMessageActivity;
import com.oa.wanyu.activity.outActivity.OutActivityMessageActivity;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivityMessageActivity;
import com.oa.wanyu.bean.approvastatus.ApprovalRoot;
import com.oa.wanyu.bean.approvastatus.ApprovalRows;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CircleImg;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//审批（待审批，已审批）
public class SPActivity extends AppCompatActivity {
    private ImageView mbcak;
    private ListView mListView;
    private SmartRefreshLayout smartRefreshLayout;
    private SPAdapter spAdapter;
    private TextView wait_tv, already_tv;
    private View wait_line, already_line;
    private int flag = 0;//0表示待审批，1表示已审批

    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private List<ApprovalRows> mList = new ArrayList<>();
    private int refresh = 0;//0默认刷新，1为加载数据
    private int start = 0, limit = 20;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            already_tv.setEnabled(true);
            wait_tv.setEnabled(true);
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ApprovalRoot.class);
                    if (o != null && o instanceof ApprovalRoot) {
                        ApprovalRoot approvalRoot = (ApprovalRoot) o;
                        if (approvalRoot != null) {
                            if ("0".equals(approvalRoot.getCode())) {
                                if (approvalRoot.getRows() != null) {

                                    if (refresh == 0) {//刷新
                                        if (approvalRoot.getRows().size() == 0) {
                                            Toast.makeText(SPActivity.this, "暂无审批项目", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("空空如也");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = approvalRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < approvalRoot.getRows().size(); i++) {
                                            mList.add(approvalRoot.getRows().get(i));
                                        }

                                        Toast.makeText(SPActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                                    }

                                    spAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(approvalRoot.getCode())) {
                                Toast.makeText(SPActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }else {
                                Toast.makeText(SPActivity.this, "错误信息："+approvalRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("错误信息："+approvalRoot.getMessage());
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(SPActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(SPActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp);

        url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
        okHttpManager = OkHttpManager.getInstance();
        okHttpManager.getMethod(false, url, "审批列表", handler, 1);

        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_data_rl.setEnabled(false);
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
                BallProgressUtils.showLoading(SPActivity.this, mAll_RL);
                okHttpManager.getMethod(false, url, "审批列表", handler, 1);
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_sp);

        mbcak = (ImageView) findViewById(R.id.back_img);
        mbcak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.my_listview_id);
        spAdapter = new SPAdapter();
        mListView.setAdapter(spAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mList.get(i).getMsgType() == 35) {//出差详情页面 flag=0表示待审批，1表示已审批
                    Intent intent = new Intent(SPActivity.this, AbusinessTravelMessageActivity.class);
                    //intent.putExtra("flag", flag);
                    intent.putExtra("id", mList.get(i).getReferId());
                    intent.putExtra("withdraw_flag",10);
                    startActivityForResult(intent, 1);

                } else if (mList.get(i).getMsgType() == 15) {//请假
                    Intent intent = new Intent(SPActivity.this, LeaveActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                     startActivityForResult(intent, 1);

                } else if (mList.get(i).getMsgType() == 20) {//外出
                    Intent intent = new Intent(SPActivity.this, OutActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivityForResult(intent, 1);

                } else if (mList.get(i).getMsgType() == 30) {//报销
                    Intent intent = new Intent(SPActivity.this, ReimbursementActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivityForResult(intent, 1);

                } else if (mList.get(i).getMsgType() == 25) {//物品领用
                    Intent intent = new Intent(SPActivity.this, GoodsUseActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivityForResult(intent, 1);

                } else if (mList.get(i).getMsgType() == 10) {//物品申购
                    Intent intent = new Intent(SPActivity.this, GoodsBuyActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivityForResult(intent, 1);

                } else if (mList.get(i).getMsgType() == 40) {//通用申请
                    Intent intent = new Intent(SPActivity.this, CurrencyApplyActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivityForResult(intent, 1);

                }
            }
        });


        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.my_smartrefresh_id);
        smartRefreshLayout.setRefreshHeader(new CircleHeader(this));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(this, R.color.color_1c82d4)));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "审批列表", handler, 1);

            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh = 1;
                start += 20;
                url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "审批列表", handler, 1);
            }
        });

        //待审批
        wait_tv = (TextView) findViewById(R.id.wait_sp);
        wait_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wait_tv.setEnabled(false);
                flag = 0;
                wait_tv.setTextColor(ContextCompat.getColor(SPActivity.this, R.color.color_3dacfe));
                wait_line.setBackgroundResource(R.color.color_3dacfe);
                BallProgressUtils.showLoading(SPActivity.this, mAll_RL);
                already_tv.setTextColor(ContextCompat.getColor(SPActivity.this, R.color.color_b9b9b9));
                already_line.setBackgroundResource(R.color.color_b9b9b9);
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "审批列表", handler, 1);

            }
        });
        //已审批
        already_tv = (TextView) findViewById(R.id.already_sp);
        already_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                already_tv.setTextColor(ContextCompat.getColor(SPActivity.this, R.color.color_3dacfe));
                already_line.setBackgroundResource(R.color.color_3dacfe);
                BallProgressUtils.showLoading(SPActivity.this, mAll_RL);
                wait_tv.setTextColor(ContextCompat.getColor(SPActivity.this, R.color.color_b9b9b9));
                wait_line.setBackgroundResource(R.color.color_b9b9b9);
                already_tv.setEnabled(false);
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "审批列表", handler, 1);
            }
        });
        wait_line = findViewById(R.id.wait_line);
        already_line = findViewById(R.id.already_line);


    }

    class SPAdapter extends BaseAdapter {

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
            SPlHolder sPlHolder = null;
            if (view == null) {
                sPlHolder = new SPlHolder();
                view = LayoutInflater.from(SPActivity.this).inflate(R.layout.approval_listview_item, null);
                sPlHolder.type_img = view.findViewById(R.id.type_img);
                sPlHolder.status = view.findViewById(R.id.status_tv);
                sPlHolder.name = view.findViewById(R.id.persson_name);
                sPlHolder.bumen = view.findViewById(R.id.bumen_name);
                sPlHolder.job = view.findViewById(R.id.job_name);
                sPlHolder.time = view.findViewById(R.id.time_name);
                sPlHolder.circleImg = view.findViewById(R.id.person_head_img);
                sPlHolder.title_rl = view.findViewById(R.id.title_rl);
                view.setTag(sPlHolder);
            } else {
                sPlHolder = (SPlHolder) view.getTag();
            }

            if (flag == 0) {
                sPlHolder.status.setText("审批中");
                sPlHolder.title_rl.setBackgroundResource(R.drawable.spz_bg);
            } else {
                if (1 == mList.get(i).getApprovalState()) {//1=同意
                    sPlHolder.status.setText("已同意");
                    sPlHolder.title_rl.setBackgroundResource(R.drawable.ywc_bg);
                }
                if (2 == mList.get(i).getApprovalState()) {//2=被驳回
                    sPlHolder.status.setText("被驳回");
                    sPlHolder.title_rl.setBackgroundResource(R.drawable.bbh_bg);
                }

            }

            if (mList.get(i).getMsgType() == 35) {//出差申请图片
                sPlHolder.type_img.setImageResource(R.mipmap.chucha_item);
            } else if (mList.get(i).getMsgType() == 15) {//请假图片
                sPlHolder.type_img.setImageResource(R.mipmap.jia_item);
            } else if (mList.get(i).getMsgType() == 20) {//外出图片
                sPlHolder.type_img.setImageResource(R.mipmap.waichu_item);
            } else if (mList.get(i).getMsgType() == 30) {//报销
                sPlHolder.type_img.setImageResource(R.mipmap.baoxiao_item);
            } else if (mList.get(i).getMsgType() == 25) {//物品领用图片
                sPlHolder.type_img.setImageResource(R.mipmap.lingyong_item);
            } else if (mList.get(i).getMsgType() == 10) {//物品申购图片
                sPlHolder.type_img.setImageResource(R.mipmap.shengou_item);
            } else if (mList.get(i).getMsgType() == 40) {//通用申请图片
                sPlHolder.type_img.setImageResource(R.mipmap.tongyong_item);
            }


            sPlHolder.name.setText(mList.get(i).getTrueName() + "");
            if ("".equals(mList.get(i).getDepartmentName())) {
                sPlHolder.bumen.setText("暂无");
            } else {
                sPlHolder.bumen.setText(mList.get(i).getDepartmentName() + "");
            }

            if ("".equals(mList.get(i).getPosition())) {
                sPlHolder.job.setText("暂无");
            } else {
                sPlHolder.job.setText(mList.get(i).getPosition() + "");
            }

            sPlHolder.time.setText(mList.get(i).getCreateTimeString() + "");
            Picasso.with(SPActivity.this).load(URLTools.urlBase + mList.get(i).getAvatar()).error(R.mipmap.head_img_icon).into(sPlHolder.circleImg);

            return view;
        }

        class SPlHolder {
            ImageView type_img;
            TextView status, name, bumen, job, time;
            CircleImg circleImg;
            RelativeLayout title_rl;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            refresh = 0;
            start = 0;
            url = URLTools.urlBase + URLTools.approval_status + "msgStatus=" + flag + "&start=" + start + "&limit=" + limit;
            okHttpManager.getMethod(false, url, "审批列表", handler, 1);
        }
    }
}
