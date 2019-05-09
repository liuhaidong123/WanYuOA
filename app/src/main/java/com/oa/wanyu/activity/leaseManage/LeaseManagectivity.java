package com.oa.wanyu.activity.leaseManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

//出租管理
public class LeaseManagectivity extends AppCompatActivity {
    private ImageView mbcak;
    private EditText editText;
    private ListView mListView;
    private SmartRefreshLayout smartRefreshLayout;
    private LeaseAdapter leaseAdapter;//出租管理适配器
    private TextView already_lease_tv, no_lease_tv;//已租，未租
    private View already_lease_line, no_lease_line;
    private int flag = 40;//40表示已租，10表示未租

    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private List<ShopsManageSecondBean> mList = new ArrayList<>();
    private int refresh = 0;//0默认刷新，1为加载数据
    private int start = 0, limit = 20;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            already_lease_tv.setEnabled(true);
            no_lease_tv.setEnabled(true);
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
                                            Toast.makeText(LeaseManagectivity.this, "暂无商铺", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("暂无商铺");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = shopsManageSecondRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < shopsManageSecondRoot.getRows().size(); i++) {
                                            mList.add(shopsManageSecondRoot.getRows().get(i));
                                        }

                                        Toast.makeText(LeaseManagectivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    leaseAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(shopsManageSecondRoot.getCode())) {
                                Toast.makeText(LeaseManagectivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(LeaseManagectivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(LeaseManagectivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_managectivity);
        initUI();
    }

    private void initUI() {
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_lease_managectivity);
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
        BallProgressUtils.showLoading(LeaseManagectivity.this, mAll_RL);
        okHttpManager.getMethod(false, url+"&name="+"", "出租管理", handler, 1);

        editText= (EditText) findViewById(R.id.edit_id);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //按下的时候会会执行：手指按下和手指松开俩个过程
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        //do something;
                        if ("".equals(editText.getText().toString().trim())){
                            Toast.makeText(LeaseManagectivity.this,"请输入查询商铺名称",Toast.LENGTH_SHORT).show();
                        }else {
                            start = 0;
                            refresh=0;
                            url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                            BallProgressUtils.showLoading(LeaseManagectivity.this, mAll_RL);
                            try {
                                String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                                okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                BallProgressUtils.dismisLoading();
                            }

                        }

                    }

                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ("".equals(editable.toString().trim())){
                    refresh=0;
                    start = 0;
                    url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                    BallProgressUtils.showLoading(LeaseManagectivity.this, mAll_RL);
                    okHttpManager.getMethod(false, url+"&name="+"", "出租管理", handler, 1);
                }
            }
        });
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                BallProgressUtils.showLoading(LeaseManagectivity.this, mAll_RL);
                try {
                    String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                    okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    BallProgressUtils.dismisLoading();
                }

            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);

        mbcak = (ImageView) findViewById(R.id.back_img);
        mbcak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.my_listview_id);
        leaseAdapter = new LeaseAdapter();
        mListView.setAdapter(leaseAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (flag==40){//已租
                    Intent intent=new Intent(LeaseManagectivity.this,AlreadyLeaseActivity.class);
                    intent.putExtra("id",mList.get(i).getId());
                    startActivityForResult(intent,40);
                }else if (flag==10){//未租
                    Intent intent=new Intent(LeaseManagectivity.this,NoLeaseActivity.class);
                    intent.putExtra("id",mList.get(i).getId());
                    startActivityForResult(intent,10);
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
                url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                try {
                    String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                    okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    BallProgressUtils.dismisLoading();
                }


            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh = 1;
                start += 20;
                url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                try {
                    String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                    okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    BallProgressUtils.dismisLoading();
                }
            }
        });

        //已租
        already_lease_tv = (TextView) findViewById(R.id.already_lease);
        already_lease_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                already_lease_tv.setEnabled(false);
                flag = 40;
                already_lease_tv.setTextColor(ContextCompat.getColor(LeaseManagectivity.this, R.color.color_3dacfe));
                already_lease_line.setBackgroundResource(R.color.color_3dacfe);

                no_lease_tv.setTextColor(ContextCompat.getColor(LeaseManagectivity.this, R.color.color_b9b9b9));
                no_lease_line.setBackgroundResource(R.color.color_b9b9b9);

                refresh = 0;
                start = 0;
                BallProgressUtils.showLoading(LeaseManagectivity.this, mAll_RL);
                url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                try {
                    String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                    okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    already_lease_tv.setEnabled(true);
                    BallProgressUtils.dismisLoading();
                }


            }
        });
        //未租
        no_lease_tv = (TextView) findViewById(R.id.no_lease);
        no_lease_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_lease_tv.setEnabled(false);
                flag = 10;
                no_lease_tv.setTextColor(ContextCompat.getColor(LeaseManagectivity.this, R.color.color_3dacfe));
                no_lease_line.setBackgroundResource(R.color.color_3dacfe);

                already_lease_tv.setTextColor(ContextCompat.getColor(LeaseManagectivity.this, R.color.color_b9b9b9));
                already_lease_line.setBackgroundResource(R.color.color_b9b9b9);

                refresh = 0;
                start = 0;
                BallProgressUtils.showLoading(LeaseManagectivity.this, mAll_RL);
                url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
                try {
                    String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                    okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
                } catch (UnsupportedEncodingException e) {
                    no_lease_tv.setEnabled(true);
                    e.printStackTrace();
                    BallProgressUtils.dismisLoading();
                }

            }
        });
        already_lease_line = findViewById(R.id.already_lease_line);
        no_lease_line = findViewById(R.id.no_lease_line);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==40&&resultCode==RESULT_OK){//已租
            refresh = 0;
            start = 0;
            flag=40;
            url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
            try {
                String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
        }else if (requestCode==10&&resultCode==RESULT_OK){//未租
            refresh = 0;
            start = 0;
            flag=10;
            url = URLTools.urlBase + URLTools.residential_list + "state=" + flag + "&start=" + start + "&limit=" + limit;
            try {
                String str=URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                okHttpManager.getMethod(false, url+"&name="+str, "出租管理", handler, 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    class LeaseAdapter extends BaseAdapter {

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
            LeaseHolder leaseHolder = null;
            if (view == null) {
                leaseHolder = new LeaseHolder();
                view = LayoutInflater.from(LeaseManagectivity.this).inflate(R.layout.lease_manage_item, null);
                leaseHolder.shops_name = view.findViewById(R.id.floor_name);
                leaseHolder.area = view.findViewById(R.id.area_tv);
                leaseHolder.status = view.findViewById(R.id.status_tv);
                leaseHolder.type = view.findViewById(R.id.type_money_tv);
                leaseHolder.name = view.findViewById(R.id.name_tv);
                leaseHolder.money = view.findViewById(R.id.money_tv);
                leaseHolder.location = view.findViewById(R.id.location_tv);
                view.setTag(leaseHolder);
            } else {
                leaseHolder = (LeaseHolder) view.getTag();
            }
            if (!"".equals(mList.get(i).getBuildingName())) {
                leaseHolder.shops_name.setText(mList.get(i).getBuildingName() + ""+mList.get(i).getRoomNum());
            } else {
                leaseHolder.shops_name.setText("未知商铺名称");
            }

            leaseHolder.status.setText("状态："+mList.get(i).getStateText() + "");

            if (!"".equals(mList.get(i).getArea())) {
                leaseHolder.area.setText("面积：" + mList.get(i).getArea() + "平米");
            } else {
                leaseHolder.area.setText("面积：未知");
            }

            if (!"".equals(mList.get(i).getAddress())) {
                leaseHolder.location.setText("位置：" + mList.get(i).getAddress() + "");
            } else {
                leaseHolder.location.setText("位置：未知");
            }

            if (!"".equals(mList.get(i).getNam())) {
                leaseHolder.name.setText("姓名：" + mList.get(i).getNam() + "");
            } else {
                leaseHolder.name.setText("姓名：---");
            }

            if (!"".equals(mList.get(i).getRental())) {
                leaseHolder.money.setText("租金：" + mList.get(i).getRental() + "元");
            } else {
                leaseHolder.money.setText("租金：" + "---");
            }

            if (!"".equals(mList.get(i).getEndDateString())){
                leaseHolder.type.setText("到租日期："+mList.get(i).getEndDateString());
            }else {
                leaseHolder.type.setText("到租日期：未知");
            }


            return view;
        }

        class LeaseHolder {
            TextView shops_name, area, status, type, name, money, location;
        }
    }

}
