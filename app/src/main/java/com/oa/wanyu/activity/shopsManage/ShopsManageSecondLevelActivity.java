package com.oa.wanyu.activity.shopsManage;

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
import com.oa.wanyu.bean.ShopsManageRoot;
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

import java.util.ArrayList;
import java.util.List;

//商铺查看第二页
public class ShopsManageSecondLevelActivity extends AppCompatActivity {

    private ImageView mback_img;
    private SmartRefreshLayout smartRefreshLayout;
    private ListView listView;
    private ShopsSecondLevelAdapter shopsSecondLevelAdapter;
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private List<ShopsManageSecondBean> mList = new ArrayList<>();
    private int refresh = 0;//0默认刷新，1为加载数据
    private int start = 0, limit = 40;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private long mID;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ShopsManageSecondRoot.class);
                    if (o != null && o instanceof ShopsManageSecondRoot) {
                        ShopsManageSecondRoot shopsManageSecondRoot = (ShopsManageSecondRoot) o;
                        if (shopsManageSecondRoot != null) {
                            if ("0".equals(shopsManageSecondRoot.getCode())) {
                                if (shopsManageSecondRoot.getRows() != null) {

                                    if (refresh == 0) {//刷新
                                        if (shopsManageSecondRoot.getRows().size() == 0) {
                                            Toast.makeText(ShopsManageSecondLevelActivity.this, "暂无最新商铺", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("空空如也");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = shopsManageSecondRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < shopsManageSecondRoot.getRows().size(); i++) {
                                            mList.add(shopsManageSecondRoot.getRows().get(i));
                                        }

                                        Toast.makeText(ShopsManageSecondLevelActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    shopsSecondLevelAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(shopsManageSecondRoot.getCode())) {
                                Toast.makeText(ShopsManageSecondLevelActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(ShopsManageSecondLevelActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(ShopsManageSecondLevelActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_manage_second_level);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_shops_manage_second_level);
        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okHttpManager = OkHttpManager.getInstance();
        Intent intent = getIntent();
        mID = intent.getLongExtra("id", -1);
        url = URLTools.urlBase + URLTools.residential_list + "buildingId=" + mID + "&start=" + start + "&limit=" + limit;
        if (mID != -1) {
            BallProgressUtils.showLoading(this,mAll_RL);
            okHttpManager.getMethod(false, url, "店铺列表", handler, 1);
        } else {
            Toast.makeText(this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
        }


        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh = 0;
                start = 0;

                if (mID != -1) {
                    BallProgressUtils.showLoading(ShopsManageSecondLevelActivity.this, mAll_RL);
                    url = URLTools.urlBase + URLTools.residential_list + "buildingId=" + mID + "&start=" + start + "&limit=" + limit;
                    okHttpManager.getMethod(false, url, "店铺列表", handler, 1);
                } else {
                    Toast.makeText(ShopsManageSecondLevelActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);


        listView = (ListView) findViewById(R.id.my_listview_id);
        shopsSecondLevelAdapter = new ShopsSecondLevelAdapter();
        listView.setAdapter(shopsSecondLevelAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShopsManageSecondLevelActivity.this, ShopsManageMessageActivity.class);
                intent.putExtra("id",mList.get(i).getId());
                intent.putExtra("name",mList.get(i).getRoomNum()+"");
                startActivityForResult(intent,1);
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
                if (mID != -1) {
                    //BallProgressUtils.showLoading(ShopsManageSecondLevelActivity.this,mAll_RL);
                    url = URLTools.urlBase + URLTools.residential_list + "buildingId=" + mID + "&start=" + start + "&limit=" + limit;
                    okHttpManager.getMethod(false, url, "店铺列表", handler, 1);
                } else {
                    Toast.makeText(ShopsManageSecondLevelActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }

            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh = 1;
                start += 40;
                if (mID != -1) {
                    //BallProgressUtils.showLoading(ShopsManageSecondLevelActivity.this,mAll_RL);
                     url = URLTools.urlBase + URLTools.residential_list + "buildingId=" + mID + "&start=" + start + "&limit=" + limit;
                    okHttpManager.getMethod(false, url, "店铺列表", handler, 1);
                } else {
                    Toast.makeText(ShopsManageSecondLevelActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK){
            refresh = 0;
            start = 0;
            if (mID != -1) {
                //BallProgressUtils.showLoading(ShopsManageSecondLevelActivity.this,mAll_RL);
                url = URLTools.urlBase + URLTools.residential_list + "buildingId=" + mID + "&start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "店铺列表", handler, 1);
            } else {
                Toast.makeText(ShopsManageSecondLevelActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class ShopsSecondLevelAdapter extends BaseAdapter {

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
            ShopsSecondLevelHolder shopsSecondLevelHolder = null;
            if (view == null) {
                shopsSecondLevelHolder = new ShopsSecondLevelHolder();
                view = LayoutInflater.from(ShopsManageSecondLevelActivity.this).inflate(R.layout.shops_select_second_level_item, null);
                shopsSecondLevelHolder.shops_name = view.findViewById(R.id.shops_name);
                shopsSecondLevelHolder.rent_status = view.findViewById(R.id.shops_status);

                view.setTag(shopsSecondLevelHolder);
            } else {
                shopsSecondLevelHolder = (ShopsSecondLevelHolder) view.getTag();
            }
            if (mList.get(i).getState() == 40) {
                shopsSecondLevelHolder.shops_name.setTextColor(ContextCompat.getColor(ShopsManageSecondLevelActivity.this, R.color.color_8bc34a));
                shopsSecondLevelHolder.rent_status.setTextColor(ContextCompat.getColor(ShopsManageSecondLevelActivity.this, R.color.color_8bc34a));
            } else {
                shopsSecondLevelHolder.shops_name.setTextColor(ContextCompat.getColor(ShopsManageSecondLevelActivity.this, R.color.color_b9b9b9));
                shopsSecondLevelHolder.rent_status.setTextColor(ContextCompat.getColor(ShopsManageSecondLevelActivity.this, R.color.color_b9b9b9));
            }
            shopsSecondLevelHolder.shops_name.setText(mList.get(i).getRoomNum()+"");
            shopsSecondLevelHolder.rent_status.setText(mList.get(i).getStateText()+"");

            return view;
        }

        class ShopsSecondLevelHolder {
            TextView shops_name, rent_status;
        }
    }
}
