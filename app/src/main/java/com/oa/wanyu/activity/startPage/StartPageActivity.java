package com.oa.wanyu.activity.startPage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.MainActivity;
import com.oa.wanyu.activity.login.LoginActivity;
import com.oa.wanyu.bean.start.StartBean;
import com.oa.wanyu.myutils.SharedPrefrenceTools;

import java.util.Timer;
import java.util.TimerTask;

//启动
public class StartPageActivity extends AppCompatActivity {
    private SharedPrefrenceTools sharedPrefrenceTools;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, StartBean.class);
                    if (o != null && o instanceof StartBean) {
                        StartBean startBean = (StartBean) o;
                        if (startBean != null) {

                            if ("0".equals(startBean.getCode())) {//账号已登录


                                final Timer timer = new Timer(true);
                                TimerTask timerTask = new TimerTask() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                };
                                timer.schedule(timerTask, 2000);

                            } else if ("-1".equals(startBean.getCode())) {//账号未登录或登录过期

                                final Timer timer = new Timer(true);
                                TimerTask timerTask = new TimerTask() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(StartPageActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                };
                                timer.schedule(timerTask, 2000);

                            }
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(StartPageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StartPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(StartPageActivity.this, "连接服务器失败，请重新尝试", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartPageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        sharedPrefrenceTools = SharedPrefrenceTools.getSharedPrefrenceToolsInstance(this);
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.isLogin_url;
        okHttpManager.getMethod(false, url, "判断登录接口", handler, 1);


    }
}
