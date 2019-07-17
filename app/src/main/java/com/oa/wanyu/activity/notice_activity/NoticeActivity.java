package com.oa.wanyu.activity.notice_activity;

import android.os.Handler;
import android.os.Message;
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
import com.oa.wanyu.activity.shopsManage.ShopsManageMessageActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.util.HashMap;
import java.util.Map;

//公告
public class NoticeActivity extends AppCompatActivity {
    private ImageView mback;
    private EditText title_notice, content_notice;
    private TextView submit;

    private OkHttpManager okHttpManager;
    private Gson gson=new Gson();
    private String url;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            submit.setEnabled(true);
            if (msg.what == 1) {//提交标记
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(NoticeActivity.this, "发布成功", Toast.LENGTH_SHORT).show();

                                finish();

                            } else if ("-1".equals(successBean.getCode())) {

                                Toast.makeText(NoticeActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(NoticeActivity.this, "失败信息："+successBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(NoticeActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(NoticeActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();

            }
        }
    };
    private RelativeLayout mAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        mAll= (RelativeLayout) findViewById(R.id.activity_notice);
        okHttpManager=OkHttpManager.getInstance();
        url= URLTools.urlBase+URLTools.notice_mess;

        mback= (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title_notice= (EditText) findViewById(R.id.edit_notice_title_name);
        content_notice= (EditText) findViewById(R.id.edit_notice_content);

        submit= (TextView) findViewById(R.id.notice_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(title_notice.getText().toString().trim())){
                    if (!"".equals(content_notice.getText().toString().trim())){
                        submit.setEnabled(false);
                        BallProgressUtils.showLoading(NoticeActivity.this,mAll);
                        Map<Object,Object> map=new HashMap<Object, Object>();
                        map.put("title",title_notice.getText().toString().trim());
                        map.put("content",content_notice.getText().toString().trim());
                        map.put("msgStatus",0);
                        okHttpManager.postMethod(false,url,"发布通知",map,handler,1);
                    }else {
                        Toast.makeText(NoticeActivity.this, "请填写内容", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(NoticeActivity.this, "请填写标题", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
