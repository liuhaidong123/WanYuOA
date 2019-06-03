package com.oa.wanyu.activity.AgentCompany;

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
import com.oa.wanyu.activity.Customer.CustomerActivity;
import com.oa.wanyu.activity.leaseManage.AlreadyLeaseActivity;
import com.oa.wanyu.activity.leaseManage.LeaseManagectivity;
import com.oa.wanyu.activity.leaseManage.NoLeaseActivity;
import com.oa.wanyu.bean.AgentCompanyRoot;
import com.oa.wanyu.bean.AgentCompanyRows;
import com.oa.wanyu.bean.CustomerListRoot;
import com.oa.wanyu.bean.ShopsManageSecondBean;
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
import java.util.List;

//代理公司列表
public class AgentCompanyActivity extends AppCompatActivity {

    private ImageView mBack;
    private ListView mListView;
    private AgentAdapter agentAdapter;
    private SmartRefreshLayout smartRefreshLayout;

    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private List<AgentCompanyRows> mList = new ArrayList<>();
    private int refreshFlag = 0;//0默认刷新，1为加载数据
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
                    Object o = gson.fromJson(mes, AgentCompanyRoot.class);
                    if (o != null && o instanceof AgentCompanyRoot) {
                        AgentCompanyRoot companyRoot = (AgentCompanyRoot) o;
                        if (companyRoot != null) {
                            if ("0".equals(companyRoot.getCode())) {
                                if (companyRoot.getRows() != null) {

                                    if (refreshFlag == 0) {//刷新
                                        if (companyRoot.getRows().size() == 0) {
                                            Toast.makeText(AgentCompanyActivity.this, "暂无代理公司", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("暂无代理公司");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = companyRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < companyRoot.getRows().size(); i++) {
                                            mList.add(companyRoot.getRows().get(i));
                                        }

                                        Toast.makeText(AgentCompanyActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();

                                    }

                                    agentAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(companyRoot.getCode())) {
                                Toast.makeText(AgentCompanyActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(AgentCompanyActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(AgentCompanyActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_company);

        mAll_RL = (RelativeLayout) findViewById(R.id.activity_agent_company);
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.agent_cpmpany_list;
        BallProgressUtils.showLoading(AgentCompanyActivity.this, mAll_RL);
        okHttpManager.getMethod(false, url + "&start=" + start + "&limit=" + limit, "代理公司列表", handler, 1);

        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshFlag = 0;
                start = 0;
                BallProgressUtils.showLoading(AgentCompanyActivity.this, mAll_RL);
                okHttpManager.getMethod(false, url + "&start=" + start + "&limit=" + limit, "代理公司列表", handler, 1);

            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);

        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.my_listview_id);
        agentAdapter = new AgentAdapter();
        mListView.setAdapter(agentAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(AgentCompanyActivity.this, AgentCompanyInformationActivity.class);
                intent.putExtra("id", mList.get(i).getId());
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
                refreshFlag = 0;
                start = 0;
                okHttpManager.getMethod(false, url + "&start=" + start + "&limit=" + limit, "代理公司列表", handler, 1);

            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refreshFlag = 1;
                start += 20;
                okHttpManager.getMethod(false, url + "&start=" + start + "&limit=" + limit, "代理公司列表", handler, 1);

            }
        });
    }

    class AgentAdapter extends BaseAdapter {

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
            AgentHolder agentHolder = null;
            if (view == null) {
                agentHolder = new AgentHolder();
                view = LayoutInflater.from(AgentCompanyActivity.this).inflate(R.layout.agent_company_item, null);
                agentHolder.agent_name = view.findViewById(R.id.agent_name);//代理公司名称
                agentHolder.agent_manager = view.findViewById(R.id.agent_manager);//管理员
                agentHolder.agent_people_num = view.findViewById(R.id.agent_people_num);//代理公司人数
                agentHolder.agent_num = view.findViewById(R.id.agent_num);//成交量
                agentHolder.agent_date = view.findViewById(R.id.agent_date);//签约日期
                agentHolder.agent_img = view.findViewById(R.id.agent_img);
                view.setTag(agentHolder);
            } else {
                agentHolder = (AgentHolder) view.getTag();
            }

            agentHolder.agent_name.setText(mList.get(i).getName() + "");
            if ("".equals(mList.get(i).getLogo())) {
                agentHolder.agent_img.setImageResource(R.mipmap.agent);
            } else {
                Picasso.with(AgentCompanyActivity.this).load(URLTools.urlBase + mList.get(i).getLogo()).error(R.mipmap.agent).into(agentHolder.agent_img);

            }

            if ("".equals(mList.get(i).getTrueName())) {//管理员
                agentHolder.agent_manager.setText("未知");
            } else {
                agentHolder.agent_manager.setText(mList.get(i).getTrueName() + "");
            }

            agentHolder.agent_people_num.setText(mList.get(i).getEmployees() + "");//人数

            agentHolder.agent_num.setText(mList.get(i).getVolume() + "");//成交量

            if ("".equals(mList.get(i).getSignDateString())) {
                agentHolder.agent_date.setText("未知");
            } else {
                agentHolder.agent_date.setText(mList.get(i).getSignDateString() + "");
            }


            return view;
        }

        class AgentHolder {
            TextView agent_name, agent_manager, agent_people_num, agent_num, agent_date;
            CircleImg agent_img;
        }
    }
}
