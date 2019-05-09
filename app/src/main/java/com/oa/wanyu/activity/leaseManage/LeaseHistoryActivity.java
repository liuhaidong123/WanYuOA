package com.oa.wanyu.activity.leaseManage;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.oa.wanyu.bean.LeaseHistoryBean;
import com.oa.wanyu.bean.LeaseHistoryRoot;
import com.oa.wanyu.bean.ShopsManageSecondRoot;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//出租历史
public class LeaseHistoryActivity extends AppCompatActivity {
    private ListView listView;
    private HistoryAdapter historyAdapter;
    private ImageView back_img;
    private List<LeaseHistoryBean> mList = new ArrayList<>();
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, LeaseHistoryRoot.class);
                    if (o != null && o instanceof LeaseHistoryRoot) {
                        LeaseHistoryRoot leaseHistoryRoot = (LeaseHistoryRoot) o;
                        if (leaseHistoryRoot != null) {
                            if ("0".equals(leaseHistoryRoot.getCode())) {
                                if (leaseHistoryRoot.getRows() != null) {

                                    if (leaseHistoryRoot.getRows().size() == 0) {
                                        Toast.makeText(LeaseHistoryActivity.this, "此商铺暂无出租历史", Toast.LENGTH_SHORT).show();
                                        no_data_rl.setVisibility(View.VISIBLE);
                                        no_mess_tv.setText("暂无出租历史");
                                    } else {
                                        no_data_rl.setVisibility(View.GONE);
                                        no_mess_tv.setText("");
                                    }
                                    mList = leaseHistoryRoot.getRows();


                                    historyAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(leaseHistoryRoot.getCode())) {
                                Toast.makeText(LeaseHistoryActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(LeaseHistoryActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(LeaseHistoryActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_history);


        listView = (ListView) findViewById(R.id.history_lease_listview_id);
        historyAdapter = new HistoryAdapter();
        listView.setAdapter(historyAdapter);

        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAll_RL = (RelativeLayout) findViewById(R.id.activity_lease_history);
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.select_history_lease;
        id = getIntent().getLongExtra("id", -1);
        if (id != -1) {
            BallProgressUtils.showLoading(LeaseHistoryActivity.this, mAll_RL);
            okHttpManager.getMethod(false, url + "shopId=" + id, "商铺历史列表", handler, 1);
        } else {
            Toast.makeText(this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
        }


        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id != -1) {
                    BallProgressUtils.showLoading(LeaseHistoryActivity.this, mAll_RL);
                    okHttpManager.getMethod(false, url + "shopId=" + id, "商铺历史列表", handler, 1);
                } else {
                    Toast.makeText(LeaseHistoryActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);

    }

    class HistoryAdapter extends BaseAdapter {

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
            LeaseHistoryHolder leaseHistoryHolder = null;
            if (view == null) {
                leaseHistoryHolder = new LeaseHistoryHolder();
                view = LayoutInflater.from(LeaseHistoryActivity.this).inflate(R.layout.lease_history_item, null);
                leaseHistoryHolder.name = view.findViewById(R.id.name_history_tv);
                leaseHistoryHolder.money = view.findViewById(R.id.money_history_tv);
                leaseHistoryHolder.phone = view.findViewById(R.id.phone_history_tv);
                leaseHistoryHolder.time = view.findViewById(R.id.time_history_tv);
                view.setTag(leaseHistoryHolder);
            } else {
                leaseHistoryHolder = (LeaseHistoryHolder) view.getTag();
            }
            leaseHistoryHolder.name.setText("姓名：" + mList.get(i).getNam());
            leaseHistoryHolder.money.setText("租金：" + mList.get(i).getRental()+"元");
            leaseHistoryHolder.phone.setText("电话：" + mList.get(i).getTell());
            leaseHistoryHolder.time.setText("时间：" + mList.get(i).getBeginDateString() + "至" + mList.get(i).getEndDateString());

            return view;
        }

        class LeaseHistoryHolder {
            TextView name, money, phone, time;
        }
    }
}
