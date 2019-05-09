package com.oa.wanyu.activity.shopsManage;

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
import com.oa.wanyu.bean.ShopsManageBean;
import com.oa.wanyu.bean.ShopsManageRoot;
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

//商铺查看首页
public class ShopsManagectivity extends AppCompatActivity {

    private ImageView mback_img;
    private SmartRefreshLayout smartRefreshLayout;
    private ListView listView;
    private ShopsAdapter shopsAdapter;
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private List<ShopsManageBean> mList = new ArrayList<>();
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
                    Object o = gson.fromJson(mes, ShopsManageRoot.class);
                    if (o != null && o instanceof ShopsManageRoot) {
                        ShopsManageRoot shopsManageRoot = (ShopsManageRoot) o;
                        if (shopsManageRoot != null) {
                            if ("0".equals(shopsManageRoot.getCode())) {
                                if (shopsManageRoot.getRows() != null) {

                                    if (refresh == 0) {//刷新
                                        if (shopsManageRoot.getRows().size() == 0) {
                                            Toast.makeText(ShopsManagectivity.this, "暂无最新商铺", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("空空如也");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = shopsManageRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < shopsManageRoot.getRows().size(); i++) {
                                            mList.add(shopsManageRoot.getRows().get(i));
                                        }

                                        Toast.makeText(ShopsManagectivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    shopsAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(shopsManageRoot.getCode())) {
                                Toast.makeText(ShopsManagectivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(ShopsManagectivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(ShopsManagectivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_managectivity);
        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_shops_managectivity);

        BallProgressUtils.showLoading(this, mAll_RL);
        url = URLTools.urlBase + URLTools.shops_list;
        okHttpManager = OkHttpManager.getInstance();
        okHttpManager.getMethod(false, url, "商铺列表", handler, 1);

        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh = 0;
                start = 0;
                BallProgressUtils.showLoading(ShopsManagectivity.this, mAll_RL);
                okHttpManager.getMethod(false, url, "商铺列表", handler, 1);

            }
        });


        listView = (ListView) findViewById(R.id.my_listview_id);
        shopsAdapter = new ShopsAdapter();
        listView.setAdapter(shopsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShopsManagectivity.this, ShopsManageSecondLevelActivity.class);
               intent.putExtra("id",mList.get(i).getId());
                startActivity(intent);
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
                okHttpManager.getMethod(false, url, "商铺列表", handler, 1);

            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh = 1;
                start += 20;

            }
        });

    }


    class ShopsAdapter extends BaseAdapter {

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
            ShopsManageHolder shopsManageHolder = null;
            if (view == null) {
                shopsManageHolder = new ShopsManageHolder();
                view = LayoutInflater.from(ShopsManagectivity.this).inflate(R.layout.shops_select_item, null);
                shopsManageHolder.shops_name = view.findViewById(R.id.shops_name);
                shopsManageHolder.rent = view.findViewById(R.id.shops_rent);
                shopsManageHolder.rent_already = view.findViewById(R.id.shops_rent_already);
                view.setTag(shopsManageHolder);
            } else {
                shopsManageHolder = (ShopsManageHolder) view.getTag();
            }
            shopsManageHolder.shops_name.setText(mList.get(i).getName() + "");
            shopsManageHolder.rent.setText("可租 " + mList.get(i).getTenantable() + "");
            shopsManageHolder.rent_already.setText("已租 " + mList.get(i).getRented() + "");

            return view;
        }

        class ShopsManageHolder {
            TextView shops_name, rent, rent_already;
        }
    }
}
