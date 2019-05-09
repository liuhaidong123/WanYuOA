package com.oa.wanyu.activity.notice_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oa.wanyu.R;

public class NoticeMessageActivity extends AppCompatActivity {

    private ImageView mback;
    private TextView mtitle, mcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_message);

        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mtitle = (TextView) findViewById(R.id.title);
        mcontent = (TextView) findViewById(R.id.content);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        mtitle.setText("主题 "+title + "");
        mcontent.setText("内容 "+content + "");
    }
}
