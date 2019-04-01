package com.oa.wanyu.activity.notice_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oa.wanyu.R;

//公告
public class NoticeActivity extends AppCompatActivity {
    private ImageView mback;
    private EditText title_notice, content_notice;
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

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

            }
        });

    }
}
