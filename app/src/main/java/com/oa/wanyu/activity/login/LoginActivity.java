package com.oa.wanyu.activity.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.MainActivity;
import com.oa.wanyu.bean.login.LoginRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CheckPhone;
import com.oa.wanyu.myutils.SharedPrefrenceTools;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

//登录
public class LoginActivity extends AppCompatActivity {
    private SharedPrefrenceTools sharedPrefrenceTools;
    private TextView mLogin_select, mRegister_select, mLogin_or_Register_btn, forget_password_btn;
    private View login_view, register_view;
    private EditText edit_phone, edit_password;
    private OkHttpManager okHttpManager;
    private String login_url;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            mLogin_or_Register_btn.setEnabled(true);
            if (msg.what == 1) {
                String mes = (String) msg.obj;
                Log.e("登录返回数据=", mes);
                Object o = gson.fromJson(mes, LoginRoot.class);
                if (o != null && o instanceof LoginRoot) {
                    LoginRoot loginRoot = (LoginRoot) o;
                    if (loginRoot != null) {
                        if ("0".equals(loginRoot.getCode())) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            sharedPrefrenceTools.put("phone", loginRoot.getUser().getMobile());//手机号
                            sharedPrefrenceTools.put("truename", loginRoot.getUser().getTrueName() + "");//姓名
                            sharedPrefrenceTools.put("avatar", loginRoot.getUser().getAvatar() + "");//头像
                            sharedPrefrenceTools.put("age", loginRoot.getUser().getAge() + "");//年龄
                            sharedPrefrenceTools.put("gender", loginRoot.getUser().getGender() + "");//性别
                            sharedPrefrenceTools.put("companyID", loginRoot.getUser().getCompanyId() + "");//销售的公司id
                            sharedPrefrenceTools.put("idAdmin", loginRoot.getUser().getIsAdmin()+"");//权限id
                           // Log.e("isadmin",loginRoot.getUser().getIsAdmin()+"");
                            for (int i = 0; i < loginRoot.getUser().getPermission().size(); i++) {//存放权限实体类
                                sharedPrefrenceTools.saveObject("Permission" + i, loginRoot.getUser().getPermission().get(i));
                            }
                            sharedPrefrenceTools.put("PermissionNum", loginRoot.getUser().getPermission().size());//存放功能数量

                            //极光推送设置别名
                            JPushInterface.setAlias(LoginActivity.this, 111, loginRoot.getUser().getMobile());

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } else if ("1".equals(loginRoot.getCode())) {
                            Toast.makeText(LoginActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                        } else if ("2".equals(loginRoot.getCode())) {
                            Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                        } else if ("3".equals(loginRoot.getCode())) {
                            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                        } else if ("4".equals(loginRoot.getCode())) {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败，请重新尝试", Toast.LENGTH_SHORT).show();

                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "服务器数据错误", Toast.LENGTH_SHORT).show();

                }

            } else {
                //数据错误
                Toast.makeText(LoginActivity.this, "连接服务器失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private RelativeLayout mAll_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAll_rl = (RelativeLayout) findViewById(R.id.activity_login);
        sharedPrefrenceTools = SharedPrefrenceTools.getSharedPrefrenceToolsInstance(this);
        okHttpManager = OkHttpManager.getInstance();
        login_url = URLTools.urlBase + URLTools.login_url;
        //登录（或者注册，目前只是登录）
        mLogin_or_Register_btn = (TextView) findViewById(R.id.login_or_register_btn);
        mLogin_or_Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cPhone();
            }
        });
        forget_password_btn = (TextView) findViewById(R.id.forget_password);
        forget_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "请联系后台管理员", Toast.LENGTH_SHORT).show();
            }
        });

        mRegister_select = (TextView) findViewById(R.id.register_btn);
        mRegister_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "注册功能暂未开通", Toast.LENGTH_SHORT).show();
            }
        });


        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
    }

    //判断手机号和密码
    public boolean cPhone() {

        if (!"".equals(edit_phone.getText().toString())) {

            if (CheckPhone.checkPhone(edit_phone.getText().toString())) {

                if (!"".equals(edit_password.getText().toString())) {
                    mLogin_or_Register_btn.setEnabled(false);
                    BallProgressUtils.showLoading(LoginActivity.this, mAll_rl);
                    Map<Object, Object> map = new HashMap<>();
                    map.put("name", edit_phone.getText().toString());
                    map.put("password", edit_password.getText().toString());
                    //登录接口
                    okHttpManager.postMethod(true, login_url, "登录", map, handler, 1);

                } else {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return false;
                }


            } else {
                Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return false;

            }


        } else {

            Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        return false;
    }
}
