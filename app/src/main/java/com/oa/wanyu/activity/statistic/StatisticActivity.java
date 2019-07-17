package com.oa.wanyu.activity.statistic;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.oa.wanyu.activity.floorManage.FloorHouseInformationActivity;
import com.oa.wanyu.bean.FloorAddressRoot;
import com.oa.wanyu.bean.FloorAddressRows;
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

//销售统计
public class StatisticActivity extends AppCompatActivity {
    private RelativeLayout mAll, no_data_rl;
    private ImageView mback;
    private TextView area_tv, no_mess_tv;
    private ListView mListview, mAddressListView;
    private StatisticAdapter statisticAdapter;
    private List<StatisticRows> mList = new ArrayList<>();
    private SmartRefreshLayout mSmartRefreshLayout;
    private AlertDialog.Builder address_builder;
    private AlertDialog address_alertDialog;
    private AddressAdapter addressAdapter;
    private List<FloorAddressRows> addressList = new ArrayList();

    private int numberAll = 0;//每年销售的总楼房数量
    private long buildingID = -1;
    private int refreshFlag = 0;//0表示刷新1表示加载
    private String url_area, url_statistic;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            area_tv.setEnabled(true);
            if (msg.what == 1) {//小区接口
                try {
                    String str = (String) msg.obj;
                    Object o = gson.fromJson(str, FloorAddressRoot.class);
                    if (o != null && o instanceof FloorAddressRoot) {
                        FloorAddressRoot floorAddressRoot = (FloorAddressRoot) o;
                        if (floorAddressRoot != null) {
                            if ("0".equals(floorAddressRoot.getCode())) {

                                if (floorAddressRoot.getRows() != null) {

                                    if (floorAddressRoot.getRows().size() == 0) {
                                        Toast.makeText(StatisticActivity.this, "暂未获取小区列表", Toast.LENGTH_SHORT).show();
                                        area_tv.setText("暂未获取小区");
                                    } else {
                                        buildingID = floorAddressRoot.getRows().get(0).getId();//默认显示第一个小区下的统计列表
                                        area_tv.setText(floorAddressRoot.getRows().get(0).getName());
                                        okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID, "楼房销售统计接口", handler, 2);
                                    }
                                    addressList = floorAddressRoot.getRows();
                                    addressAdapter.notifyDataSetChanged();

                                }


                            } else if ("-1".equals(floorAddressRoot.getCode())) {
                                Toast.makeText(StatisticActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(StatisticActivity.this, floorAddressRoot.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        Toast.makeText(StatisticActivity.this, "获取小区列表失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(StatisticActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 2) {//楼房统计
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, StatisticRoot.class);
                    if (o != null && o instanceof StatisticRoot) {
                        StatisticRoot statisticRoot = (StatisticRoot) o;
                        if (statisticRoot != null) {
                            if ("0".equals(statisticRoot.getCode())) {
                                if (statisticRoot.getRows() != null) {

                                    if (refreshFlag == 0) {//刷新
                                        if (statisticRoot.getRows().size() == 0) {
                                            Toast.makeText(StatisticActivity.this, "暂无楼房销售统计列表", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("暂无楼房销售统计列表");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = statisticRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < statisticRoot.getRows().size(); i++) {
                                            mList.add(statisticRoot.getRows().get(i));
                                        }

                                        Toast.makeText(StatisticActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    statisticAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(statisticRoot.getCode())) {
                                Toast.makeText(StatisticActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            } else {
                                Toast.makeText(StatisticActivity.this, statisticRoot.getMessage() + "", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("楼房统计数据错误");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(StatisticActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }


            } else {
                //数据错误
                Toast.makeText(StatisticActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    //销售统计
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

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
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StatisticActivity.this, StatisticInformationActivity.class);
                intent.putExtra("buildId", buildingID);
                intent.putExtra("year", mList.get(i).getYear());
                intent.putExtra("month", mList.get(i).getMonth());
                intent.putExtra("area", area_tv.getText().toString());
                startActivity(intent);
            }
        });
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
                //refreshFlag = 1;
                //  start += 40;
                // okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID, "楼房销售统计接口", handler, 2);
            }
        });


        mAll = (RelativeLayout) findViewById(R.id.activity_statistic);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buildingID != -1) {
                    no_data_rl.setEnabled(false);
                    BallProgressUtils.showLoading(StatisticActivity.this, no_data_rl);
                    refreshFlag = 0;
                    // start = 0;
                    okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID, "楼房销售统计接口", handler, 2);
                } else {
                    no_data_rl.setEnabled(true);
                    Toast.makeText(StatisticActivity.this, "获取id失败，请重新尝试", Toast.LENGTH_SHORT).show();
                }

            }
        });

        url_statistic = URLTools.urlBase + URLTools.floor_statistic_number;//楼房销售统计接口
        url_area = URLTools.urlBase + URLTools.floor_address_list;//小区接口
        okHttpManager = OkHttpManager.getInstance();
        okHttpManager.getMethod(false, url_area, "地址接口", handler, 1);
        area_tv = (TextView) findViewById(R.id.statistic_tv);
        area_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressList.size() == 0) {
                    area_tv.setEnabled(false);
                    BallProgressUtils.showLoading(StatisticActivity.this, mAll);
                    okHttpManager.getMethod(false, url_area, "小区接口", handler, 1);
                    Toast.makeText(StatisticActivity.this, "正在查询数据，请稍等。。。", Toast.LENGTH_SHORT).show();
                } else {
                    address_alertDialog.show();
                }

            }
        });

        //==楼盘地址
        address_builder = new AlertDialog.Builder(this);
        address_alertDialog = address_builder.create();
        View addressview = LayoutInflater.from(this).inflate(R.layout.address_alert, null);
        address_alertDialog.setView(addressview);
        mAddressListView = addressview.findViewById(R.id.address_listview_id);
        addressAdapter = new AddressAdapter();
        mAddressListView.setAdapter(addressAdapter);
        mAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                area_tv.setText(addressList.get(i).getName());
                buildingID = addressList.get(i).getId();
                if (buildingID != -1) {
                    okHttpManager.getMethod(false, url_statistic + "buildingId=" + buildingID, "楼房销售统计接口", handler, 2);
                } else {
                    Toast.makeText(StatisticActivity.this, "小区ID错误，请重新尝试", Toast.LENGTH_SHORT).show();
                }

                address_alertDialog.dismiss();
            }
        });

    }

    //小区列表
    class AddressAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return addressList.size();
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
            AddressHolder addressHolder = null;
            if (view == null) {
                addressHolder = new AddressHolder();
                view = LayoutInflater.from(StatisticActivity.this).inflate(R.layout.leave_item, null);
                addressHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(addressHolder);
            } else {
                addressHolder = (AddressHolder) view.getTag();
            }
            addressHolder.tv.setText(addressList.get(i).getName() + "");

            return view;
        }

        class AddressHolder {
            TextView tv;
        }
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
                view = LayoutInflater.from(StatisticActivity.this).inflate(R.layout.statistic_item, null);
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
                statisticHolder.all_number.setText(mList.get(i).getYear() + "年共售楼" + numberAll + "套");
                numberAll = 0;
            } else {
                //每年的最后一条数据时
                if (mList.get(i).getYear() != mList.get(i + 1).getYear()) {
                    statisticHolder.all_number.setVisibility(View.VISIBLE);
                    numberAll += mList.get(i).getVolume();
                    statisticHolder.all_number.setText(mList.get(i).getYear() + "年共售楼" + numberAll + "套");
                    numberAll = 0;
                } else {
                    statisticHolder.all_number.setVisibility(View.GONE);
                    numberAll += mList.get(i).getVolume();
                }
            }

            statisticHolder.date.setText(mList.get(i).getYear() + "年" + mList.get(i).getMonth() + "月");
            statisticHolder.floor_number.setText(mList.get(i).getVolume() + "套");

            statisticHolder.floor_progress.setWidth(mList.get(i).getVolume() * 8);//显示楼房销售多少的颜色长度
            if (mList.get(i).getMonth() == 1 || mList.get(i).getMonth() == 4 || mList.get(i).getMonth() == 7 || mList.get(i).getMonth() == 10) {
                statisticHolder.floor_progress.setBackgroundResource(R.drawable.statistic_bg3);
            } else if (mList.get(i).getMonth() == 2 || mList.get(i).getMonth() == 5 || mList.get(i).getMonth() == 8 || mList.get(i).getMonth() == 11) {
                statisticHolder.floor_progress.setBackgroundResource(R.drawable.statistic_bg2);
            } else if (mList.get(i).getMonth() == 3 || mList.get(i).getMonth() == 6 || mList.get(i).getMonth() == 9 || mList.get(i).getMonth() == 12) {
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
