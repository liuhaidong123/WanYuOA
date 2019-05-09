package com.oa.wanyu.activity.leaseManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
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
import com.oa.wanyu.bean.ExpireRemindRoot;
import com.oa.wanyu.bean.ExpireremindRows;
import com.oa.wanyu.bean.ShopsManageSecondBean;
import com.oa.wanyu.bean.ShopsManageSecondRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//到期提醒
public class ExpireRemindctivity extends AppCompatActivity {

    private ImageView mbcak;
    private ListView mListView;
    private SmartRefreshLayout smartRefreshLayout;
    private ExpireRemindAdapter remindAdapter;//出租管理适配器
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private List<ExpireremindRows> mList = new ArrayList<>();
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
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ExpireRemindRoot.class);
                    if (o != null && o instanceof ExpireRemindRoot) {
                        ExpireRemindRoot expireRemindRoot = (ExpireRemindRoot) o;
                        if (expireRemindRoot != null) {
                            if ("0".equals(expireRemindRoot.getCode())) {
                                if (expireRemindRoot.getRows() != null) {

                                    if (refresh == 0) {//刷新
                                        if (expireRemindRoot.getRows().size() == 0) {
                                            Toast.makeText(ExpireRemindctivity.this, "暂无商铺", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("暂无商铺");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = expireRemindRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < expireRemindRoot.getRows().size(); i++) {
                                            mList.add(expireRemindRoot.getRows().get(i));
                                        }

                                        Toast.makeText(ExpireRemindctivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    remindAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(expireRemindRoot.getCode())) {
                                Toast.makeText(ExpireRemindctivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(ExpireRemindctivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(ExpireRemindctivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire_remindctivity);
        mbcak = (ImageView) findViewById(R.id.back_img);
        mbcak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAll_RL = (RelativeLayout) findViewById(R.id.activity_expire_remindctivity);
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.expire_remind + "start=" + start + "&limit=" + limit;
        BallProgressUtils.showLoading(ExpireRemindctivity.this, mAll_RL);
        okHttpManager.getMethod(false, url, "到期提醒接口", handler, 1);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.expire_remind + "start=" + start + "&limit=" + limit;
                BallProgressUtils.showLoading(ExpireRemindctivity.this, mAll_RL);
                okHttpManager.getMethod(false, url, "到期提醒接口", handler, 1);

            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);

        mListView = (ListView) findViewById(R.id.my_listview_id);
        remindAdapter = new ExpireRemindAdapter();
        mListView.setAdapter(remindAdapter);
        //到期提醒详情
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ExpireRemindctivity.this, ExpireRemindMessageActivity.class);
                intent.putExtra("id", mList.get(i).getId());
                startActivityForResult(intent, 40);
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
                url = URLTools.urlBase + URLTools.expire_remind + "start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "到期提醒接口", handler, 1);

            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh = 1;
                start += 20;
                url = URLTools.urlBase + URLTools.expire_remind + "start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "到期提醒接口", handler, 1);

            }
        });

    }


    class ExpireRemindAdapter extends BaseAdapter {

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
            ExpireHolder expireHolder = null;
            if (view == null) {
                expireHolder = new ExpireHolder();
                view = LayoutInflater.from(ExpireRemindctivity.this).inflate(R.layout.lease_manage_item, null);
                expireHolder.floor_name_rl = view.findViewById(R.id.floor_name_rl);
                expireHolder.shops_name = view.findViewById(R.id.floor_name);
                expireHolder.area = view.findViewById(R.id.area_tv);
                expireHolder.status = view.findViewById(R.id.status_tv);
                expireHolder.type = view.findViewById(R.id.type_money_tv);
                expireHolder.name = view.findViewById(R.id.name_tv);
                expireHolder.money = view.findViewById(R.id.money_tv);
                expireHolder.location = view.findViewById(R.id.location_tv);
                expireHolder.reming_date = view.findViewById(R.id.notify_date);
                expireHolder.reming_date.setVisibility(View.VISIBLE);
                view.setTag(expireHolder);
            } else {
                expireHolder = (ExpireHolder) view.getTag();
            }

            if (!"".equals(mList.get(i).getBuildingName())) {
                expireHolder.shops_name.setText(mList.get(i).getBuildingName() + "" + mList.get(i).getRoomNum());
            } else {
                expireHolder.shops_name.setText("未知商铺名称");
            }

            expireHolder.status.setText("状态：" + mList.get(i).getStateText() + "");

            if (!"".equals(mList.get(i).getArea())) {
                expireHolder.area.setText("面积：" + mList.get(i).getArea() + "平米");
            } else {
                expireHolder.area.setText("面积：未知");
            }

            if (!"".equals(mList.get(i).getAddress())) {
                expireHolder.location.setText("位置：" + mList.get(i).getAddress() + "");
            } else {
                expireHolder.location.setText("位置：未知");
            }

            if (!"".equals(mList.get(i).getNam())) {
                expireHolder.name.setText("姓名：" + mList.get(i).getNam() + "");
            } else {
                expireHolder.name.setText("姓名：---");
            }

            if (!"".equals(mList.get(i).getRental())) {
                expireHolder.money.setText("租金：" + mList.get(i).getRental() + "元");
            } else {
                expireHolder.money.setText("租金：" + "---");
            }

            if (!"".equals(mList.get(i).getEndDateString())) {
                expireHolder.type.setText("到租日期：" + mList.get(i).getEndDateString());
            } else {
                expireHolder.type.setText("到租日期：未知");
            }

            if (mList.get(i).getRemaining() == 0) {
                expireHolder.reming_date.setText("今天到期");
                expireHolder.floor_name_rl.setBackgroundResource(R.drawable.zu_top_bg);
            } else if (mList.get(i).getRemaining() > 0) {
                expireHolder.reming_date.setText(mList.get(i).getRemaining() + "后到期");
                expireHolder.floor_name_rl.setBackgroundResource(R.drawable.zu_top_bg);
            } else {
                expireHolder.reming_date.setText("逾期" + mList.get(i).getRemaining() + "天");
                expireHolder.floor_name_rl.setBackgroundResource(R.drawable.remind_bg);
            }

            return view;
        }

        class ExpireHolder {
            RelativeLayout floor_name_rl;
            TextView shops_name, area, status, type, name, money, location;
            TextView reming_date;
        }
    }
}
