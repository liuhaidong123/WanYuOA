package com.oa.wanyu.activity.floorManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.leaseManage.AlreadyLeaseActivity;
import com.oa.wanyu.activity.leaseManage.NoLeaseActivity;
import com.oa.wanyu.bean.FloorHouseInformation;
import com.oa.wanyu.bean.FloorHouseInformationRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.squareup.picasso.Picasso;

//房屋户型图详情
public class FloorHouseInformationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView house_img, back_img;
    private TextView title_name;
    private RelativeLayout mAll_rl, no_data_rl;
    private TextView no_mess_tv;
    ;
    private long id = -1;
    private int state = -1;
    private String url, sign_url;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {//详情
                try{
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
                                Picasso.with(FloorHouseInformationActivity.this).load(URLTools.urlBase+floorHouseInformation.getCover()).error(R.mipmap.errorpicture).into(house_img);

                            } else if ("-1".equals(floorHouseInformationRoot.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(FloorHouseInformationActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Toast.makeText(FloorHouseInformationActivity.this, "获取楼房信息失败", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
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
                            }
                        }
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
                    BallProgressUtils.showLoading(FloorHouseInformationActivity.this, mAll_rl);
                    okHttpManager.getMethod(false, url + "id=" + id, "房屋户型图详情", handler, 1);
                } else {
                    Toast.makeText(FloorHouseInformationActivity.this, "获取房屋id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
    }

    /**
     * 标记楼房
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_1://标记为已售
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
                        okHttpManager.getMethod(false, sign_url + "id=" + id + "&state=" + 50 + "&stateText=已售", "标记为已售", handler, 2);
                    }
                });
                oneBuilder.setTitle("确认此楼房标记为已售？请谨慎操作");
                oneAlertDialog = oneBuilder.create();
                oneAlertDialog.show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //如果是预定跳过来，弹框中显示的是（已售，可售），如果是可售跳过来，弹框中显示的是（已售，预定），如果是已售跳过来，不显示
        if (state != -1) {
            if (state == 30) {//可售
                getMenuInflater().inflate(R.menu.ke_shou, menu);
            } else if (state == 50) {//已售

            } else if (state == 45) {//预定
                getMenuInflater().inflate(R.menu.yu_ding, menu);
            }
        }

        return true;
    }
}
