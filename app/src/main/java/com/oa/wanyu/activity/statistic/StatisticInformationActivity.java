package com.oa.wanyu.activity.statistic;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.oa.wanyu.bean.FloorAddressRoot;
import com.oa.wanyu.bean.StatisticInformationRoot;
import com.oa.wanyu.bean.StatisticInformationRows;
import com.oa.wanyu.bean.StatisticRoot;
import com.oa.wanyu.bean.StatisticRows;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

//每个月楼房的信息
public class StatisticInformationActivity extends AppCompatActivity {
    private RelativeLayout mAll, no_data_rl;
    private ImageView mback;
    private TextView area_tv, no_mess_tv;
    private ListView mListview;
    private StatisticAdapter statisticAdapter;
    private List<StatisticInformationRows> mList = new ArrayList<>();
    private SmartRefreshLayout mSmartRefreshLayout;

    private int year = -1, month = -1;
    private String area = "";
    private int numberAll = 0;//每月销售的总楼房数量
    private long buildingID = -1;
    private int refreshFlag = 0;//0表示刷新1表示加载
    private String url_statistic;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            if (msg.what == 1) {//楼房月统计统计
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, StatisticInformationRoot.class);
                    if (o != null && o instanceof StatisticInformationRoot) {
                        StatisticInformationRoot statisticInformationRoot = (StatisticInformationRoot) o;
                        if (statisticInformationRoot != null) {
                            if ("0".equals(statisticInformationRoot.getCode())) {
                                if (statisticInformationRoot.getRows() != null) {

                                    if (refreshFlag == 0) {//刷新
                                        if (statisticInformationRoot.getRows().size() == 0) {
                                            Toast.makeText(StatisticInformationActivity.this, "暂无楼房销售统计列表", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("暂无楼房销售统计列表");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = statisticInformationRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < statisticInformationRoot.getRows().size(); i++) {
                                            mList.add(statisticInformationRoot.getRows().get(i));
                                        }

                                        Toast.makeText(StatisticInformationActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    statisticAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(statisticInformationRoot.getCode())) {
                                Toast.makeText(StatisticInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            } else {
                                Toast.makeText(StatisticInformationActivity.this, statisticInformationRoot.getMessage() + "", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("楼房统计数据错误");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(StatisticInformationActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }


            } else {
                //数据错误
                Toast.makeText(StatisticInformationActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_information);
        initUI();
    }

    private void initUI() {
        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListview = (ListView) findViewById(R.id.listview_statistic);
        statisticAdapter = new StatisticAdapter();
        mListview.setAdapter(statisticAdapter);

        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mSmartRefreshLayout.setRefreshHeader(new CircleHeader(this));
        mSmartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(this, R.color.color_1c82d4)));
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshFlag = 0;
                //start = 0;
                okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID, "楼房销售统计接口", handler, 2);

            }
        });

        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败

            }
        });


        mAll = (RelativeLayout) findViewById(R.id.activity_statistic_information);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buildingID != -1) {
                    no_data_rl.setEnabled(false);
                    BallProgressUtils.showLoading(StatisticInformationActivity.this, no_data_rl);
                    refreshFlag = 0;
                    // start = 0;
                    okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID, "楼房销售统计接口", handler, 2);
                } else {
                    no_data_rl.setEnabled(true);
                    Toast.makeText(StatisticInformationActivity.this, "获取id失败，请重新尝试", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buildingID = getIntent().getLongExtra("buildId", -1);
        year = getIntent().getIntExtra("year", -1);
        month = getIntent().getIntExtra("month", -1);
        area = getIntent().getStringExtra("area");
        url_statistic = URLTools.urlBase + URLTools.floor_statistic_month_number;//楼房销售统计接口
        okHttpManager = OkHttpManager.getInstance();
        area_tv = (TextView) findViewById(R.id.statistic_tv);
        area_tv.setText(area + year + "年" + month + "月楼房销售明细");
        okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID + "&year=" + year + "&month=" + month + "&countMode=day", "楼房销售月明细", handler, 1);
        BallProgressUtils.showLoading(this, mAll);
    }


    class StatisticAdapter extends BaseAdapter {

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

            StatisticHolder statisticHolder = null;
            if (view == null) {
                statisticHolder = new StatisticHolder();
                view = LayoutInflater.from(StatisticInformationActivity.this).inflate(R.layout.statistic_item, null);
                statisticHolder.date = view.findViewById(R.id.date_tv);
                statisticHolder.floor_number = view.findViewById(R.id.statistic_number);
                statisticHolder.floor_progress = view.findViewById(R.id.statistic_bg_tv);
                statisticHolder.all_number = view.findViewById(R.id.all_number);
                view.setTag(statisticHolder);
            } else {
                statisticHolder = (StatisticHolder) view.getTag();
            }

            if (i == mList.size() - 1) {//总数据最后一条数据时
                statisticHolder.all_number.setVisibility(View.VISIBLE);
                numberAll += mList.get(i).getVolume();
                statisticHolder.all_number.setText(mList.get(i).getYear() + "年"+mList.get(i).getMonth()+"月共售楼" + numberAll + "套");
                numberAll = 0;
            } else {

                statisticHolder.all_number.setVisibility(View.GONE);
                numberAll += mList.get(i).getVolume();

            }

            statisticHolder.date.setText(mList.get(i).getYear() + "年" + mList.get(i).getMonth() + "月" + mList.get(i).getDay() + "日");
            statisticHolder.floor_number.setText(mList.get(i).getVolume() + "套");

            statisticHolder.floor_progress.setWidth(mList.get(i).getVolume() * 8);//显示楼房销售多少的颜色长度
            if (mList.get(i).getDay() == 1 || mList.get(i).getDay() == 4 || mList.get(i).getDay() == 7 || mList.get(i).getDay() == 10 || mList.get(i).getDay() == 14 || mList.get(i).getDay() == 17 || mList.get(i).getDay() == 20 || mList.get(i).getDay() == 23 || mList.get(i).getDay() == 26 || mList.get(i).getDay() == 29) {
                statisticHolder.floor_progress.setBackgroundResource(R.drawable.statistic_bg3);
            } else if (mList.get(i).getDay() == 2 || mList.get(i).getDay() == 5 || mList.get(i).getDay() == 8 || mList.get(i).getDay() == 11 || mList.get(i).getDay() == 15 || mList.get(i).getDay() == 18 || mList.get(i).getDay() == 21 || mList.get(i).getDay() == 24 || mList.get(i).getDay() == 27 || mList.get(i).getDay() == 30) {
                statisticHolder.floor_progress.setBackgroundResource(R.drawable.statistic_bg2);
            } else if (mList.get(i).getDay() == 3 || mList.get(i).getDay() == 6 || mList.get(i).getDay() == 9 || mList.get(i).getDay() == 12 || mList.get(i).getDay() == 16 || mList.get(i).getDay() == 19 || mList.get(i).getDay() == 22 || mList.get(i).getDay() == 25 || mList.get(i).getDay() == 28 || mList.get(i).getDay() == 31) {
                statisticHolder.floor_progress.setBackgroundResource(R.drawable.statistic_bg1);
            } else {
                statisticHolder.floor_progress.setBackgroundResource(R.color.color_090a0f);
            }

            return view;
        }

        class StatisticHolder {
            TextView date, floor_number, floor_progress, all_number;
        }

    }
}
