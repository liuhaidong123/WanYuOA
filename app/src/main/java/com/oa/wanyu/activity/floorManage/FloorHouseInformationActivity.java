package com.oa.wanyu.activity.floorManage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oa.wanyu.R;

//房屋户型图详情
public class FloorHouseInformationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView house_img, back_img;
    private TextView title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_house_information);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()) {
//            case R.id.a:
//
//                break;
//            case R.id.icon_tv:
//
//                break;
//
//            default:
//                break;
//        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //如果是预定跳过来，弹框中显示的是（可售，预售），如果是可售跳过来，弹框中显示的是（已售，预定），如果是已售跳过来，不显示
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
