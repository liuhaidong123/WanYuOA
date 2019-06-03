package com.oa.wanyu.activity.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.floorManage.FloorHouseInformationActivity;
import com.oa.wanyu.bean.CurrencyInformationBean;
import com.oa.wanyu.bean.CustomerInformaionRoot;
import com.oa.wanyu.bean.CustomerInformationBean;
import com.oa.wanyu.bean.CustomerListRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CheckPhone;
import com.oa.wanyu.myutils.CircleImg;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

//客户详情
public class CustomerMessageActivity extends AppCompatActivity {

    private ImageView mBack, star_img;
    private TextView edit_btn, sex_tv, call1_btn, call2_btn, xing_tv, delete_btn;
    private CircleImg circleImg;
    private EditText name_edit, age_edit, phone1_edit, phone2_edit, live_edit, way_edit, like_edit;
    private RelativeLayout mAll;

    private AlertDialog.Builder oneBuilder;
    private AlertDialog oneAlertDialog;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private long id = -1;
    private int starFlag = -1;
    private String delete_url, sign_star_url, information_url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, CustomerInformaionRoot.class);
                    if (o != null && o instanceof CustomerInformaionRoot) {
                        CustomerInformaionRoot customerInformaionRoot = (CustomerInformaionRoot) o;
                        if (customerInformaionRoot != null) {
                            if ("0".equals(customerInformaionRoot.getCode())) {
                                if (customerInformaionRoot.getCustomer() != null) {
                                    CustomerInformationBean customer = customerInformaionRoot.getCustomer();
                                    edit_btn.setVisibility(View.VISIBLE);
                                    star_img.setVisibility(View.VISIBLE);
                                    xing_tv.setVisibility(View.VISIBLE);
                                    call1_btn.setVisibility(View.VISIBLE);
                                    call2_btn.setVisibility(View.VISIBLE);
                                    delete_btn.setVisibility(View.VISIBLE);

                                    name_edit.setText(customer.getTrueName() + "");
                                    sex_tv.setText(customer.getGender() + "");
                                    age_edit.setText(customer.getAge() + "");
                                    phone1_edit.setText(customer.getMobile() + "");
                                    if ("".equals(customer.getTelephone())) {
                                        call2_btn.setVisibility(View.GONE);
                                        phone2_edit.setText("----------");
                                    } else {
                                        call2_btn.setVisibility(View.VISIBLE);
                                        phone2_edit.setText(customer.getTelephone() + "");
                                    }

                                    if ("".equals(customer.getArea())) {
                                        live_edit.setText("----------");
                                    } else {
                                        live_edit.setText(customer.getArea() + "");
                                    }

                                    if ("".equals(customer.getChannel())) {
                                        way_edit.setText("----------");
                                    } else {
                                        way_edit.setText(customer.getChannel() + "");
                                    }

                                    if ("".equals(customer.getIntention())) {
                                        like_edit.setText("----------");
                                    } else {
                                        like_edit.setText(customer.getIntention() + "");
                                    }

                                    if (0 == customer.getStar()) {//非星级
                                        starFlag = 0;
                                        star_img.setImageResource(R.mipmap.xing);
                                    } else if (1 == customer.getStar()) {
                                        starFlag = 1;
                                        star_img.setImageResource(R.mipmap.xiangqing);
                                    }

                                    if ("".equals(customer.getAvatar())) {
                                        circleImg.setImageResource(R.mipmap.head_img_icon);
                                    } else {
                                        Picasso.with(CustomerMessageActivity.this).load(URLTools.urlBase + customer.getAvatar()).error(R.mipmap.head_img_icon).into(circleImg);
                                    }
                                } else {
                                    Toast.makeText(CustomerMessageActivity.this, "获取客户信息失败，请重新尝试", Toast.LENGTH_SHORT).show();
                                    edit_btn.setVisibility(View.GONE);
                                    star_img.setVisibility(View.GONE);
                                    xing_tv.setVisibility(View.GONE);
                                    call1_btn.setVisibility(View.GONE);
                                    call2_btn.setVisibility(View.GONE);
                                    delete_btn.setVisibility(View.GONE);
                                }


                            } else if ("-1".equals(customerInformaionRoot.getCode())) {
                                Toast.makeText(CustomerMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                edit_btn.setVisibility(View.GONE);
                                star_img.setVisibility(View.GONE);
                                xing_tv.setVisibility(View.GONE);
                                call1_btn.setVisibility(View.GONE);
                                call2_btn.setVisibility(View.GONE);
                                delete_btn.setVisibility(View.GONE);
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(CustomerMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    edit_btn.setVisibility(View.GONE);
                    star_img.setVisibility(View.GONE);
                    xing_tv.setVisibility(View.GONE);
                    call1_btn.setVisibility(View.GONE);
                    call2_btn.setVisibility(View.GONE);
                    delete_btn.setVisibility(View.GONE);
                }
            } else if (msg.what == 2) {//标记星星
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                //Toast.makeText(CustomerMessageActivity.this, "添加客户成功", Toast.LENGTH_SHORT).show();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(CustomerMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CustomerMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(CustomerMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 3) {//删除客户
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(CustomerMessageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, getIntent());
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(CustomerMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CustomerMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(CustomerMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {//连接服务器失败
                Toast.makeText(CustomerMessageActivity.this, "连接服务器失败,请重新尝试", Toast.LENGTH_SHORT).show();
                edit_btn.setVisibility(View.GONE);
                star_img.setVisibility(View.GONE);
                xing_tv.setVisibility(View.GONE);
                call1_btn.setVisibility(View.GONE);
                call2_btn.setVisibility(View.GONE);
                delete_btn.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_message);
        mAll = (RelativeLayout) findViewById(R.id.activity_customer_message);
        initUI();
        okHttpManager = OkHttpManager.getInstance();
        id = getIntent().getLongExtra("id", -1);
        information_url = URLTools.urlBase + URLTools.customer_message;
        sign_star_url = URLTools.urlBase + URLTools.customer_add;//标记星级客户接口
        delete_url = URLTools.urlBase + URLTools.customer_delete;//删除客户
        if (id != -1) {
            BallProgressUtils.showLoading(this, mAll);
            okHttpManager.getMethod(false, information_url + "id=" + id, "获取客户详情", handler, 1);
        } else {
            Toast.makeText(CustomerMessageActivity.this, "获取ID错误，请重新尝试", Toast.LENGTH_SHORT).show();
            edit_btn.setVisibility(View.GONE);
            star_img.setVisibility(View.GONE);
            xing_tv.setVisibility(View.GONE);
            call1_btn.setVisibility(View.GONE);
            call2_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
        }


    }

    private void initUI() {
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //点击星星
        star_img = (ImageView) findViewById(R.id.xing);
        star_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (starFlag == 0) {
                    starFlag = 1;
                    star_img.setImageResource(R.mipmap.xiangqing);
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    map.put("id", id);
                    map.put("star", starFlag);
                    okHttpManager.postMethod(false, sign_star_url, "标记星级客户", map, handler, 2);

                } else {
                    star_img.setImageResource(R.mipmap.xing);
                    starFlag = 0;
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    map.put("id", id);
                    map.put("star", starFlag);
                    okHttpManager.postMethod(false, sign_star_url, "标记星级客户", map, handler, 2);

                }
            }
        });
        xing_tv = (TextView) findViewById(R.id.xing_tv);
        //电话1
        call1_btn = (TextView) findViewById(R.id.call_phone1_id);
        call1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPhone.checkPhone(phone1_edit.getText().toString().trim())){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone1_edit.getText().toString().trim());
                    intent.setData(data);
                    startActivity(intent);
                }else {
                    Toast.makeText(CustomerMessageActivity.this, "电话错误，请重新尝试", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //电话2
        call2_btn = (TextView) findViewById(R.id.call_phone2_id);
        call2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPhone.checkPhone(phone2_edit.getText().toString().trim())){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone2_edit.getText().toString().trim());
                    intent.setData(data);
                    startActivity(intent);
                }else {
                    Toast.makeText(CustomerMessageActivity.this, "电话错误，请重新尝试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //编辑
        edit_btn = (TextView) findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CustomerMessageActivity.this,CustomerAddActivity.class);
                intent.putExtra("id",id);
                startActivityForResult(intent,1);
            }
        });

        //头像
        circleImg = (CircleImg) findViewById(R.id.add_head_img);
        sex_tv = (TextView) findViewById(R.id.sex_edit);
        name_edit = (EditText) findViewById(R.id.name_edit);
        age_edit = (EditText) findViewById(R.id.age_edit);
        phone1_edit = (EditText) findViewById(R.id.phone1_edit);
        phone2_edit = (EditText) findViewById(R.id.phone2_edit);
        live_edit = (EditText) findViewById(R.id.live_edit);
        way_edit = (EditText) findViewById(R.id.way_edit);
        like_edit = (EditText) findViewById(R.id.like_edit);

        //删除客户
        delete_btn = (TextView) findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBuilder = new AlertDialog.Builder(CustomerMessageActivity.this);
                oneBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        oneAlertDialog.dismiss();
                    }
                });

                oneBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //删除客户
                        okHttpManager.getMethod(false, delete_url + "id=" + id, "删除", handler, 3);
                    }
                });
                oneBuilder.setTitle("确认删除此客户？请谨慎操作");
                oneAlertDialog = oneBuilder.create();
                oneAlertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK){
            if (id != -1) {
                BallProgressUtils.showLoading(this, mAll);
                okHttpManager.getMethod(false, information_url + "id=" + id, "获取客户详情", handler, 1);
            } else {
                Toast.makeText(CustomerMessageActivity.this, "获取ID错误，请重新尝试", Toast.LENGTH_SHORT).show();
                edit_btn.setVisibility(View.GONE);
                star_img.setVisibility(View.GONE);
                xing_tv.setVisibility(View.GONE);
                call1_btn.setVisibility(View.GONE);
                call2_btn.setVisibility(View.GONE);
                delete_btn.setVisibility(View.GONE);
            }
        }
    }
}
