package com.oa.wanyu.activity.Customer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.leaseManage.LeaseManagectivity;
import com.oa.wanyu.bean.CustomerListRoot;
import com.oa.wanyu.bean.CustomerListRows;
import com.oa.wanyu.bean.ShopsManageSecondRoot;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//客户管理
public class CustomerActivity extends AppCompatActivity {

    private ImageView mBack_img;
    private TextView add_btn, mNo_mess_tv;
    private EditText search_btn;
    private ListView mListView;
    private CustomerAdapter customerAdapter;
    private List<CustomerListRows> mList = new ArrayList<>();
    private SmartRefreshLayout mSmartRefreshLayout;
    private RelativeLayout mAll_Rl, mNo_Data_rl;
    private Gson gson = new Gson();
    private OkHttpManager mOkHttpManager;
    private String urlList;
    private int limit = 40, start = 0;
    private int refreshFlag = 0;//0表示刷新，1表示加载更多
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, CustomerListRoot.class);
                    if (o != null && o instanceof CustomerListRoot) {
                        CustomerListRoot customerListRoot = (CustomerListRoot) o;
                        if (customerListRoot != null) {
                            if ("0".equals(customerListRoot.getCode())) {
                                if (customerListRoot.getRows() != null) {

                                    if (refreshFlag == 0) {//刷新
                                        if (customerListRoot.getRows().size() == 0) {
                                            Toast.makeText(CustomerActivity.this, "暂无客户", Toast.LENGTH_SHORT).show();
                                            mNo_Data_rl.setVisibility(View.VISIBLE);
                                            mNo_mess_tv.setText("暂无客户");
                                        } else {
                                            mNo_Data_rl.setVisibility(View.GONE);
                                            mNo_mess_tv.setText("");
                                        }
                                        mList = customerListRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < customerListRoot.getRows().size(); i++) {
                                            mList.add(customerListRoot.getRows().get(i));
                                        }

                                        Toast.makeText(CustomerActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    customerAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(customerListRoot.getCode())) {
                                Toast.makeText(CustomerActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                mNo_Data_rl.setVisibility(View.VISIBLE);
                                mNo_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(CustomerActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    mNo_Data_rl.setVisibility(View.VISIBLE);
                    mNo_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(CustomerActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                mNo_Data_rl.setVisibility(View.VISIBLE);
                mNo_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initUI();
    }

    private void initUI() {
        mAll_Rl = (RelativeLayout) findViewById(R.id.activity_customer);

        mBack_img = (ImageView) findViewById(R.id.back_img);
        mBack_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //添加客户
        add_btn = (TextView) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, CustomerAddActivity.class);

                startActivityForResult(intent, 1);
            }
        });

        search_btn = (EditText) findViewById(R.id.edit_id);
        search_btn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //按下的时候会会执行：手指按下和手指松开俩个过程
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        //do something;
                        if ("".equals(search_btn.getText().toString().trim())) {
                            Toast.makeText(CustomerActivity.this, "请输入查询客户的姓名", Toast.LENGTH_SHORT).show();
                        } else {
                            BallProgressUtils.showLoading(CustomerActivity.this, mAll_Rl);
                            refreshFlag = 0;
                            start = 0;
                            Map<Object, Object> map = new HashMap<>();
                            map.put("start", start);
                            map.put("limit", limit);
                            map.put("name", search_btn.getText().toString().trim());
                            mOkHttpManager.postMethod(false, urlList, "客户列表", map, mHandler, 1);
                        }

                    }

                    return true;
                }
                return false;
            }
        });
        mListView = (ListView) findViewById(R.id.listview_customer);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CustomerActivity.this, CustomerMessageActivity.class);
                intent.putExtra("id",mList.get(i).getId());
                startActivityForResult(intent,1);
            }
        });
        customerAdapter = new CustomerAdapter();
        mListView.setAdapter(customerAdapter);


        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mSmartRefreshLayout.setRefreshHeader(new CircleHeader(this));
        mSmartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(this, R.color.color_1c82d4)));
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshFlag = 0;
                start = 0;
                Map<Object, Object> map = new HashMap<>();
                map.put("name", search_btn.getText().toString().trim());
                map.put("start", start);
                map.put("limit", limit);
                mOkHttpManager.postMethod(false, urlList, "客户列表", map, mHandler, 1);

            }
        });

        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败

                refreshFlag = 1;
                start += 40;
                Map<Object, Object> map = new HashMap<>();
                map.put("name", search_btn.getText().toString().trim());
                map.put("start", start);
                map.put("limit", limit);
                mOkHttpManager.postMethod(false, urlList, "客户列表", map, mHandler, 1);
            }
        });

        mAll_Rl = (RelativeLayout) findViewById(R.id.activity_customer);
        mNo_Data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        mNo_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mNo_Data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BallProgressUtils.showLoading(CustomerActivity.this, mAll_Rl);
                refreshFlag = 0;
                start = 0;
                Map<Object, Object> map = new HashMap<>();
                map.put("start", start);
                map.put("limit", limit);
                map.put("name", search_btn.getText().toString().trim());
                mOkHttpManager.postMethod(false, urlList, "客户列表", map, mHandler, 1);
            }
        });
        BallProgressUtils.showLoading(this, mAll_Rl);
        mOkHttpManager = OkHttpManager.getInstance();
        urlList = URLTools.urlBase + URLTools.customer_list;//客户列表
        Map<Object, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("limit", limit);
        map.put("name", search_btn.getText().toString().trim());
        mOkHttpManager.postMethod(false, urlList, "客户列表", map, mHandler, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            refreshFlag = 0;
            start = 0;
            Map<Object, Object> map = new HashMap<>();
            map.put("name", search_btn.getText().toString().trim());
            mOkHttpManager.postMethod(false, urlList, "客户列表", map, mHandler, 1);
        }
    }

    class CustomerAdapter extends BaseAdapter {

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

            CustomerHolder customerHolder = null;
            if (view == null) {
                customerHolder = new CustomerHolder();
                view = LayoutInflater.from(CustomerActivity.this).inflate(R.layout.customer_item, null);
                customerHolder.head = view.findViewById(R.id.customer_head);
                customerHolder.ximg = view.findViewById(R.id.xing);
                customerHolder.name = view.findViewById(R.id.customer_name);
                customerHolder.phone = view.findViewById(R.id.customer_phone);
                customerHolder.house = view.findViewById(R.id.customer_house);
                view.setTag(customerHolder);
            } else {
                customerHolder = (CustomerHolder) view.getTag();
            }
            if ("".equals(mList.get(i).getAvatar())) {
                customerHolder.head.setImageResource(R.mipmap.head_img_icon);
            } else {
                Picasso.with(CustomerActivity.this).load(URLTools.urlBase + mList.get(i).getAvatar()).error(R.mipmap.head_img_icon).into(customerHolder.head);
            }

            customerHolder.name.setText(mList.get(i).getTrueName() + "");
            customerHolder.phone.setText("电话：" + mList.get(i).getMobile() + "");

            if ("".equals(mList.get(i).getIntention())) {
                customerHolder.house.setText("意向房源：-------");
            } else {
                customerHolder.house.setText("意向房源: " + mList.get(i).getIntention() + "");
            }

            if (0 == mList.get(i).getStar()) {//非星级客户
                customerHolder.ximg.setVisibility(View.GONE);
            } else if (1 == mList.get(i).getStar()) {//星级客户
                customerHolder.ximg.setVisibility(View.VISIBLE);
            } else {
                customerHolder.ximg.setVisibility(View.GONE);
            }
            return view;
        }

        class CustomerHolder {
            ImageView ximg;
            CircleImg head;
            TextView name, phone, house;
        }
    }
}
