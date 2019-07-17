package com.oa.wanyu.activity.floorManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.oa.wanyu.activity.leaseManage.AlreadyLeaseActivity;
import com.oa.wanyu.activity.leaseManage.NoLeaseActivity;
import com.oa.wanyu.activity.notice_activity.NoticeActivity;
import com.oa.wanyu.activity.statistic.StatisticActivity;
import com.oa.wanyu.bean.FloorAddressRoot;
import com.oa.wanyu.bean.FloorAddressRows;
import com.oa.wanyu.bean.FloorHouseInformation;
import com.oa.wanyu.bean.FloorHouseInformationRoot;
import com.oa.wanyu.bean.SalespersonRoot;
import com.oa.wanyu.bean.SalespersonRows;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.login.Permission;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.SharedPrefrenceTools;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//房屋户型图详情
public class FloorHouseInformationActivity extends AppCompatActivity {
    private SharedPrefrenceTools sharedPrefrenceTools;
    private Toolbar toolbar;
    private ImageView house_img, back_img;
    private TextView title_name;
    private RelativeLayout mAll_rl, no_data_rl;
    private TextView no_mess_tv;

    private AlertDialog.Builder salesperson_builder;
    private AlertDialog salesperson_alertDialog;
    private ListView salesListview;
    private SalespersonAdapter salespersonAdapter;
    private List<SalespersonRows> salesPersonList = new ArrayList();

    private long id = -1,  userId = -1;
    private String companyId ="";
    private int state = -1;
    private String url, sign_url, salesperson_url;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            if (msg.what == 1) {//详情
                try {
                    String str = (String) msg.obj;
                    Object o = gson.fromJson(str, FloorHouseInformationRoot.class);
                    if (o != null && o instanceof FloorHouseInformationRoot) {
                        FloorHouseInformationRoot floorHouseInformationRoot = (FloorHouseInformationRoot) o;
                        if (floorHouseInformationRoot != null) {
                            if ("0".equals(floorHouseInformationRoot.getCode())) {
                                no_data_rl.setVisibility(View.GONE);
                                no_mess_tv.setText("");
                                FloorHouseInformation floorHouseInformation = floorHouseInformationRoot.getHouse();
                                title_name.setText(floorHouseInformation.getBuildingName() + "-" + floorHouseInformation.getHouseNum() + "号楼-" + floorHouseInformation.getUint() + "单元-" + floorHouseInformation.getRoomNum());
                                Picasso.with(FloorHouseInformationActivity.this).load(URLTools.urlBase + floorHouseInformation.getCover()).error(R.mipmap.errorpicture).into(house_img);

                            } else if ("-1".equals(floorHouseInformationRoot.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(FloorHouseInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }else {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("错误信息："+floorHouseInformationRoot.getMessage());
                                Toast.makeText(FloorHouseInformationActivity.this, "错误信息："+floorHouseInformationRoot.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Toast.makeText(FloorHouseInformationActivity.this, "获取楼房信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析出错，请重新尝试");
                    Toast.makeText(FloorHouseInformationActivity.this, "数据解析出错，请重新尝试 ", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 2) {//
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(FloorHouseInformationActivity.this, "标记成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, getIntent());
                                finish();

                            } else if ("-1".equals(successBean.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(FloorHouseInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }else {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("错误信息："+successBean.getMessage());
                                Toast.makeText(FloorHouseInformationActivity.this, "错误信息："+successBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(FloorHouseInformationActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 3) {//销售人员列表接口
                try {
                    String str = (String) msg.obj;
                    Object o = gson.fromJson(str, SalespersonRoot.class);
                    if (o != null && o instanceof SalespersonRoot) {
                        SalespersonRoot salespersonRoot = (SalespersonRoot) o;
                        if (salespersonRoot != null) {
                            if ("0".equals(salespersonRoot.getCode())) {

                                if (salespersonRoot.getRows() != null) {

                                    if (salespersonRoot.getRows().size() == 0) {
                                        Toast.makeText(FloorHouseInformationActivity.this, "暂未获取销售人员列表，请重新尝试", Toast.LENGTH_SHORT).show();

                                    }
                                    salesPersonList = salespersonRoot.getRows();
                                    salespersonAdapter.notifyDataSetChanged();

                                }


                            } else if ("-1".equals(salespersonRoot.getCode())) {
                                Toast.makeText(FloorHouseInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(FloorHouseInformationActivity.this,salespersonRoot.getMessage()+"" , Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        Toast.makeText(FloorHouseInformationActivity.this, "获取销售人员列表失败，请重新尝试", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FloorHouseInformationActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else {
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("连接服务器失败，请重新尝试");
                Toast.makeText(FloorHouseInformationActivity.this, "连接服务器失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AlertDialog.Builder oneBuilder, tooBuilder, threeBuilder;
    private AlertDialog oneAlertDialog, twoAlertDialog, threeAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_house_information);
        sharedPrefrenceTools = SharedPrefrenceTools.getSharedPrefrenceToolsInstance(this);
        mAll_rl = (RelativeLayout) findViewById(R.id.activity_floor_house_information);
        toolbar = (Toolbar) findViewById(R.id.bx_rl);
        setSupportActionBar(toolbar);
        title_name = (TextView) findViewById(R.id.sq_title);

        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        house_img = (ImageView) findViewById(R.id.house_img);
        url = URLTools.urlBase + URLTools.floor_house_information;//详情
        sign_url = URLTools.urlBase + URLTools.house_sign_sell;//标记楼房状态
        salesperson_url = URLTools.urlBase + URLTools.salesperson_list;//销售人员列表
        okHttpManager = OkHttpManager.getInstance();
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        state = intent.getIntExtra("state", -1);
        if (id != -1) {
            BallProgressUtils.showLoading(this, mAll_rl);
            okHttpManager.getMethod(false, url + "id=" + id, "房屋户型图详情", handler, 1);
        } else {
            Toast.makeText(FloorHouseInformationActivity.this, "id错误", Toast.LENGTH_SHORT).show();
        }


        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id != -1) {
                    no_data_rl.setEnabled(false);
                    BallProgressUtils.showLoading(FloorHouseInformationActivity.this, mAll_rl);
                    okHttpManager.getMethod(false, url + "id=" + id, "房屋户型图详情", handler, 1);
                } else {
                    Toast.makeText(FloorHouseInformationActivity.this, "获取房屋id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        companyId = (String) sharedPrefrenceTools.getValueByKey("companyID","");
        if ("".equals(companyId)) {
            Toast.makeText(FloorHouseInformationActivity.this, "获取代理公司id错误，请重新尝试", Toast.LENGTH_SHORT).show();
        } else {
            okHttpManager.getMethod(false, salesperson_url + "companyId=" + companyId, "销售人员列表", handler, 3);//获取销售人员列表
        }

        //==楼盘地址
        salesperson_builder = new AlertDialog.Builder(this);
        salesperson_alertDialog = salesperson_builder.create();
        View addressview = LayoutInflater.from(this).inflate(R.layout.address_alert, null);
        salesperson_alertDialog.setView(addressview);
        salesListview = addressview.findViewById(R.id.address_listview_id);
        salespersonAdapter = new SalespersonAdapter();
        salesListview.setAdapter(salespersonAdapter);
        salesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userId = salesPersonList.get(i).getId();
                salesperson_alertDialog.dismiss();

                oneBuilder = new AlertDialog.Builder(FloorHouseInformationActivity.this);
                oneBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        oneAlertDialog.dismiss();
                    }
                });

                oneBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //标记为已售
                        okHttpManager.getMethod(false, sign_url + "id=" + id + "&state=" + 50 + "&stateText=已售" +"&companyId="+companyId+"&userId="+userId, "标记为已售", handler, 2);
                    }
                });
                oneBuilder.setTitle("确认此楼房为" + salesPersonList.get(i).getTrueName() + "售出的楼房？请谨慎操作！");
                oneAlertDialog = oneBuilder.create();
                oneAlertDialog.show();
            }
        });
    }

    /**
     * 标记楼房
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_1://标记为已售
                if (!"".equals(companyId)) {
                    if (salesPersonList.size()!=0){
                        salesperson_alertDialog.show();
                    }else {
                        okHttpManager.getMethod(false, salesperson_url + "companyId=" + companyId, "销售人员列表", handler, 3);//获取销售人员列表
                    }
                } else {
                    Toast.makeText(FloorHouseInformationActivity.this, "获取代理公司id错误，请重新尝试", Toast.LENGTH_SHORT).show();
                }


//                oneBuilder = new AlertDialog.Builder(FloorHouseInformationActivity.this);
//                oneBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        oneAlertDialog.dismiss();
//                    }
//                });
//
//                oneBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //标记为已售
//                        okHttpManager.getMethod(false, sign_url + "id=" + id + "&state=" + 50 + "&stateText=已售", "标记为已售", handler, 2);
//                    }
//                });
//                oneBuilder.setTitle("确认此楼房标记为已售？请谨慎操作");
//                oneAlertDialog = oneBuilder.create();
//                oneAlertDialog.show();

                break;
            case R.id.menu_2:
                if (state == 30) {//标记为预定
                    tooBuilder = new AlertDialog.Builder(FloorHouseInformationActivity.this);

                    tooBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            twoAlertDialog.dismiss();
                        }
                    });

                    tooBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            okHttpManager.getMethod(false, sign_url + "id=" + id + "&state=" + 45 + "&stateText=预定", "标记为预定", handler, 2);

                        }
                    });
                    tooBuilder.setTitle("确认此楼房标记为预定？请谨慎操作");
                    twoAlertDialog = tooBuilder.create();
                    twoAlertDialog.show();

                } else if (state == 45) {//标记为可售

                    threeBuilder = new AlertDialog.Builder(FloorHouseInformationActivity.this);

                    threeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            threeAlertDialog.dismiss();
                        }
                    });

                    threeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            okHttpManager.getMethod(false, sign_url + "id=" + id + "&state=" + 30 + "&stateText=可售", "标记为可售", handler, 2);

                        }
                    });
                    threeBuilder.setTitle("确认此楼房标记为可售？请谨慎操作");
                    threeAlertDialog = threeBuilder.create();
                    threeAlertDialog.show();
                    ;
                }

                break;

            default:
                break;
        }

        return true;
    }

    /**
     * 显示标记的列表
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean permissionC = false;
       Object o= SharedPrefrenceTools.getValueByKey("idAdmin","-1");
        Log.e("o=",o.toString());
        if (o.toString().equals(String.valueOf(0))){
            permissionC = false;//无权限
        }else if (o.toString().equals(String.valueOf(1))){
            permissionC = true;//有权限
        }else {
            permissionC = false;//无权限
        }
        //如果有权限，才显示可售，预定，已售
        if (permissionC) {
            //如果是预定跳过来，弹框中显示的是（已售，可售），如果是可售跳过来，弹框中显示的是（已售，预定），如果是已售跳过来，不显示
            if (state != -1) {
                if (state == 30) {//可售
                    getMenuInflater().inflate(R.menu.ke_shou, menu);
                } else if (state == 50) {//已售

                } else if (state == 45) {//预定
                    getMenuInflater().inflate(R.menu.yu_ding, menu);
                }
            }
        } else {
            Toast.makeText(FloorHouseInformationActivity.this, "抱歉，您没有操作楼房的权限", Toast.LENGTH_SHORT).show();

        }

        return true;
    }


    //销售人员列表接口
    class SalespersonAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return salesPersonList.size();
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
            SalesHolder salesHolder = null;
            if (view == null) {
                salesHolder = new SalesHolder();
                view = LayoutInflater.from(FloorHouseInformationActivity.this).inflate(R.layout.leave_item, null);
                salesHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(salesHolder);
            } else {
                salesHolder = (SalesHolder) view.getTag();
            }
            salesHolder.tv.setText(salesPersonList.get(i).getTrueName() + "");

            return view;
        }

        class SalesHolder {
            TextView tv;
        }
    }

}
