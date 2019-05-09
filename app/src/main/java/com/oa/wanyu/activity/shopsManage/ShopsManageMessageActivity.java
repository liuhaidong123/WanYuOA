package com.oa.wanyu.activity.shopsManage;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.leave.LeaveActivity;
import com.oa.wanyu.bean.ShopsManageSecondRoot;
import com.oa.wanyu.bean.ShopsMessageBean;
import com.oa.wanyu.bean.ShopsMessageRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.ImgUitls;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//商铺详情
public class ShopsManageMessageActivity extends AppCompatActivity {

    private ImageView mback;
    private TextView shops_name, address, type, floor, area, status, sign_btn;
    private RelativeLayout mAll, no_data_rl;
    private TextView no_mess_tv;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url, sign_url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sign_btn.setEnabled(true);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ShopsMessageRoot.class);
                    if (o != null && o instanceof ShopsMessageRoot) {
                        ShopsMessageRoot shopsMessageRoot = (ShopsMessageRoot) o;
                        if (shopsMessageRoot != null) {
                            if ("0".equals(shopsMessageRoot.getCode())) {
                                ShopsMessageBean shopsMessageBean = shopsMessageRoot.getShop();
                                if (shopsMessageBean != null) {
                                    no_data_rl.setVisibility(View.GONE);
                                    no_mess_tv.setText("");
                                    if (!"".equals(shopsMessageBean.getAddress())) {
                                        address.setText(shopsMessageBean.getAddress() + "");
                                    } else {
                                        address.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getHouseType().trim())) {
                                        type.setText(shopsMessageBean.getHouseType() + "");
                                    } else {
                                        type.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getFloor())) {
                                        floor.setText(shopsMessageBean.getFloor() + "");
                                    } else {
                                        floor.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getArea())) {
                                        area.setText(shopsMessageBean.getArea() + "平米");
                                    } else {
                                        area.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getStateText())) {
                                        status.setText(shopsMessageBean.getStateText() + "");
                                    } else {
                                        status.setText("--------");
                                    }

                                    if (shopsMessageBean.getState() == 50) {
                                        sign_btn.setVisibility(View.GONE);
                                    } else {
                                        sign_btn.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    Toast.makeText(ShopsManageMessageActivity.this, "商铺信息错误", Toast.LENGTH_SHORT).show();
                                    no_data_rl.setVisibility(View.VISIBLE);
                                    no_mess_tv.setText("商铺信息错误，请刷新");
                                }


                            } else if ("-1".equals(shopsMessageRoot.getCode())) {
                                Toast.makeText(ShopsManageMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(ShopsManageMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else if (msg.what == 2) {//提交标记
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(ShopsManageMessageActivity.this, "标记成功", Toast.LENGTH_SHORT).show();
                               setResult(RESULT_OK,getIntent());
                                finish();

                            } else if ("-1".equals(successBean.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(ShopsManageMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(ShopsManageMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(ShopsManageMessageActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    private long id;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_manage_message);
        okHttpManager = OkHttpManager.getInstance();
        id = getIntent().getLongExtra("id", -1);
        name = getIntent().getStringExtra("name");
        sign_url = URLTools.urlBase + URLTools.sign_sell;//标记为已售
        url = URLTools.urlBase + URLTools.shops_message + "id=" + id;
        if (id != -1) {
            okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
        } else {
            Toast.makeText(this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
        }
        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id != -1) {
                    BallProgressUtils.showLoading(ShopsManageMessageActivity.this, mAll);
                    okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
                } else {
                    Toast.makeText(ShopsManageMessageActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll = (RelativeLayout) findViewById(R.id.activity_shops_manage_message);

        shops_name = (TextView) findViewById(R.id.sq_title);
        if (!"".equals(name)) {
            shops_name.setText(name);
        } else {
            shops_name.setText("未获取商铺名称");
        }
        address = (TextView) findViewById(R.id.shops_location_mess);
        type = (TextView) findViewById(R.id.shops_type_mess);
        floor = (TextView) findViewById(R.id.shops_floor_mess);
        area = (TextView) findViewById(R.id.shops_area_mess);
        status = (TextView) findViewById(R.id.shops_status_mess);
        //标记为已售
        sign_btn = (TextView) findViewById(R.id.sign_btn);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != -1) {
                    BallProgressUtils.showLoading(ShopsManageMessageActivity.this,mAll);
                    sign_btn.setEnabled(false);
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    map.put("id", id);
                    map.put("state", 50);
                    map.put("stateText", "已售");
                    okHttpManager.postMethod(false, sign_url, "标记为已售", map, handler, 2);
                } else {
                    Toast.makeText(ShopsManageMessageActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
