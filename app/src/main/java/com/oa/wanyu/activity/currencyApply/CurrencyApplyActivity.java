package com.oa.wanyu.activity.currencyApply;

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
import com.oa.wanyu.activity.goodsBuyActivity.GoodsBuyActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.util.HashMap;
import java.util.Map;

//通用申请
public class CurrencyApplyActivity extends AppCompatActivity {

    private ImageView mback;
    private EditText content_edit, mess_edit;
    private TextView submit;

    private RelativeLayout mAll;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            submit.setEnabled(true);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(CurrencyApplyActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(CurrencyApplyActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(CurrencyApplyActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }  else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(CurrencyApplyActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(CurrencyApplyActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(CurrencyApplyActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_apply);
        mAll= (RelativeLayout) findViewById(R.id.activity_currency_apply);
        okHttpManager=OkHttpManager.getInstance();
        url= URLTools.urlBase+URLTools.apply_currency;

        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        content_edit = (EditText) findViewById(R.id.apply_content_edit);
        mess_edit = (EditText) findViewById(R.id.apply_mess_edit);


        submit = (TextView) findViewById(R.id.apply_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(content_edit.getText().toString())){

                    if (!"".equals(mess_edit.getText().toString())){
                        submit.setEnabled(false);
                        BallProgressUtils.showLoading(CurrencyApplyActivity.this,mAll);
                        Map<Object,Object> map=new HashMap<Object, Object>();
                        map.put("content",content_edit.getText().toString());
                        map.put("detail",mess_edit.getText().toString());
                        okHttpManager.postMethod(false,url,"提交通用申请",map,handler,1);
                    }else {
                        Toast.makeText(CurrencyApplyActivity.this, "请填写申请详情", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CurrencyApplyActivity.this, "请填写申请内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
