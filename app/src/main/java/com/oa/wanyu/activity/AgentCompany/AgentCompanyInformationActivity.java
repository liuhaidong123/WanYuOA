package com.oa.wanyu.activity.AgentCompany;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.Customer.CustomerActivity;
import com.oa.wanyu.bean.AgentCompanyInformationRoot;
import com.oa.wanyu.bean.AgentCompanyInformationRows;
import com.oa.wanyu.bean.AgentCompanyRoot;
import com.oa.wanyu.bean.CustomerListRows;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CircleImg;
import com.oa.wanyu.myutils.SharedPrefrenceTools;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//代理公司人员列表
public class AgentCompanyInformationActivity extends AppCompatActivity {
    private SharedPrefrenceTools sharedPrefrenceTools;
    private String companyId="-1",isAdmin="-1";
    private TextView mAdd_btn, mAgent_Information_tv, agent_name;
    private CircleImg agent_company_img;
    private AgentCompanyPersonAdapter agentCompanyPersonAdapter;
    private ListView mListview;
    private List<AgentCompanyInformationRows> mList = new ArrayList<>();
    private SmartRefreshLayout mSmartRefreshLayout;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private int mPosition;//删除代理公司人员时的下标
    private RelativeLayout mAll_Rl, no_data_rl;
    private TextView no_mess_tv;
    private Gson gson = new Gson();
    private OkHttpManager mOkHttpManager;
    private String url, url_delete;
    private long id = -1;
    private int limit = 40, start = 0;
    private int refreshFlag = 0;//0表示刷新，1表示加载更多
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, AgentCompanyInformationRoot.class);
                    if (o != null && o instanceof AgentCompanyInformationRoot) {
                        AgentCompanyInformationRoot companyRoot = (AgentCompanyInformationRoot) o;
                        if (companyRoot != null) {
                            if ("0".equals(companyRoot.getCode())) {
                                if (companyRoot.getUserList() != null) {

                                    if (companyRoot.getCompany() != null) {
                                        mAdd_btn.setText("添加");

                                        if ("".equals(companyRoot.getCompany().getName())) {
                                            agent_name.setText("-------");
                                        } else {
                                            agent_name.setText(companyRoot.getCompany().getName() + "");
                                        }

                                        if ("".equals(companyRoot.getCompany().getLogo())) {
                                            agent_company_img.setImageResource(R.mipmap.agent);
                                        } else {
                                            Picasso.with(AgentCompanyInformationActivity.this).load(URLTools.urlBase + companyRoot.getCompany().getLogo()).error(R.mipmap.agent).into(agent_company_img);
                                        }

                                        if ("".equals(companyRoot.getCompany().getIntroduction())) {
                                            mAgent_Information_tv.setText("--------");
                                        } else {
                                            mAgent_Information_tv.setText("公司简介：" + companyRoot.getCompany().getIntroduction() + "");
                                        }

                                    } else {
                                        no_data_rl.setVisibility(View.VISIBLE);
                                        no_mess_tv.setText("暂未获取代理公司资料，请重新尝试。");
                                        agent_name.setVisibility(View.GONE);
                                        mAdd_btn.setVisibility(View.GONE);
                                        agent_company_img.setVisibility(View.GONE);
                                        mAgent_Information_tv.setVisibility(View.GONE);
                                    }


                                    if (refreshFlag == 0) {//刷新
                                        if (companyRoot.getUserList().size() == 0) {
                                            Toast.makeText(AgentCompanyInformationActivity.this, "暂无销售人员", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("暂无销售人员");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = companyRoot.getUserList();

                                    } else {//加载
                                        for (int i = 0; i < companyRoot.getUserList().size(); i++) {
                                            mList.add(companyRoot.getUserList().get(i));
                                        }

                                        Toast.makeText(AgentCompanyInformationActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                                    }


                                    agentCompanyPersonAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(companyRoot.getCode())) {
                                Toast.makeText(AgentCompanyInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(AgentCompanyInformationActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else if (msg.what == 2) {//提交删除
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(AgentCompanyInformationActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                mList.remove(mPosition);
                                agentCompanyPersonAdapter.notifyDataSetChanged();

                            } else if ("-1".equals(successBean.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(AgentCompanyInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(AgentCompanyInformationActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AgentCompanyInformationActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(AgentCompanyInformationActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(AgentCompanyInformationActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_company_information);
        sharedPrefrenceTools=SharedPrefrenceTools.getSharedPrefrenceToolsInstance(this);
        Object companyID=SharedPrefrenceTools.getValueByKey("companyID","-1");
        Object adminID=SharedPrefrenceTools.getValueByKey("idAdmin","-1");
        companyId=String.valueOf(companyID);
        isAdmin=String.valueOf(adminID);
        initUi();
    }

    private void initUi() {

        //添加代理公司人员
        mAdd_btn = (TextView) findViewById(R.id.agent_add);
        mAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (companyId.equals(String.valueOf(id))&&isAdmin.equals("1")){
                    Intent intent = new Intent(AgentCompanyInformationActivity.this, AddAgentCompanyPersonActivity.class);
                    intent.putExtra("id", id);
                    startActivityForResult(intent,123);
                }else {
                    Toast.makeText(AgentCompanyInformationActivity.this, "抱歉，您没有添加人员的权限，请联系管理员。", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mAgent_Information_tv = (TextView) findViewById(R.id.agent_information);
        mAgent_Information_tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        agent_name = (TextView) findViewById(R.id.agent_name);
        agent_company_img = (CircleImg) findViewById(R.id.agent_img);

        //删除代理公司人员
        builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除此销售人员?请谨慎操作。");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();

                if (id != -1) {

                    if (companyId.equals(String.valueOf(id))&&isAdmin.equals("1")){
                        BallProgressUtils.showLoading(AgentCompanyInformationActivity.this, mAll_Rl);
                        mOkHttpManager.getMethod(false, url_delete + "id=" + mList.get(mPosition).getId(), "删除销售人员", handler, 2);
                    }else {
                        Toast.makeText(AgentCompanyInformationActivity.this, "抱歉，您没有权限删除人员，请联系管理员。", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AgentCompanyInformationActivity.this, "获取id失败，请重新尝试", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        mListview = (ListView) findViewById(R.id.listview_agent_person);
        agentCompanyPersonAdapter = new AgentCompanyPersonAdapter();
        mListview.setAdapter(agentCompanyPersonAdapter);
        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPosition = i;
                alertDialog.show();
                return true;
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
                // start = 0;
                mOkHttpManager.getMethod(false, url + "id=" + id, "代理公司详情列表", handler, 1);

            }
        });

        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refreshFlag = 1;
                //  start += 40;
                // mOkHttpManager.getMethod(false, url + "id=" + id, "代理公司详情列表", handler, 1);
            }
        });


        mAll_Rl = (RelativeLayout) findViewById(R.id.activity_agent_company_information);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != -1) {
                    no_data_rl.setEnabled(false);
                    BallProgressUtils.showLoading(AgentCompanyInformationActivity.this, mAll_Rl);
                    refreshFlag = 0;
                    // start = 0;
                    mOkHttpManager.getMethod(false, url + "id=" + id, "代理公司详情列表", handler, 1);
                } else {
                    Toast.makeText(AgentCompanyInformationActivity.this, "获取id失败，请重新尝试", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mOkHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.agent_cpmpany_information_list;
        url_delete = URLTools.urlBase + URLTools.agent_company_delete_person;
        id = getIntent().getLongExtra("id", -1);
        if (id != -1) {
            BallProgressUtils.showLoading(AgentCompanyInformationActivity.this, mAll_Rl);
            mOkHttpManager.getMethod(false, url + "id=" + id, "代理公司详情列表", handler, 1);
        } else {
            Toast.makeText(this, "获取id失败，请重新尝试", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123&&resultCode==RESULT_OK){
            if (id != -1) {
                refreshFlag = 0;
                mOkHttpManager.getMethod(false, url + "id=" + id, "代理公司详情列表", handler, 1);
            } else {
                Toast.makeText(this, "获取id失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AgentCompanyPersonAdapter extends BaseAdapter {

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
            AgentPersonHolder agentPersonHolder = null;
            if (view == null) {
                agentPersonHolder = new AgentPersonHolder();
                view = LayoutInflater.from(AgentCompanyInformationActivity.this).inflate(R.layout.agent_company_person_item, null);
                agentPersonHolder.head = view.findViewById(R.id.agent_person_head);
                agentPersonHolder.name = view.findViewById(R.id.agent_person_name);
                agentPersonHolder.phone = view.findViewById(R.id.agent_person_phone);
                agentPersonHolder.num = view.findViewById(R.id.agent_company_num);
                view.setTag(agentPersonHolder);
            } else {
                agentPersonHolder = (AgentPersonHolder) view.getTag();
            }

            if ("".equals(mList.get(i).getAvatar())) {
                agentPersonHolder.head.setImageResource(R.mipmap.head_img_icon);
            } else {
                Picasso.with(AgentCompanyInformationActivity.this).load(URLTools.urlBase + mList.get(i).getAvatar()).error(R.mipmap.head_img_icon).into(agentPersonHolder.head);
            }

            if ("".equals(mList.get(i).getTrueName())) {
                agentPersonHolder.name.setText("姓名：未知");
            } else {
                agentPersonHolder.name.setText(mList.get(i).getTrueName() + "");
            }

            if ("".equals(mList.get(i).getMobile())) {
                agentPersonHolder.phone.setText("电话：未知");
            } else {
                agentPersonHolder.phone.setText(mList.get(i).getMobile() + "");
            }

            agentPersonHolder.num.setText("成交量：" + mList.get(i).getVolume() + "套");


            return view;
        }

        class AgentPersonHolder {
            CircleImg head;
            TextView name, phone, num;
        }
    }
}
