package com.oa.wanyu.activity.floorManage;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

//楼盘详情
public class FloorMessageActivity extends AppCompatActivity {

    private ImageView mback_img;
    private TextView mTitle_tv;
    private SmartRefreshLayout smartRefreshLayout;
    private GridView mGridView;
    private MyAdapter myAdapter;
    private int num = 20;

    private ListView mListview;
    private MyNumAdapter myNumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_message);

        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle_tv = (TextView) findViewById(R.id.sq_title);
//        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.floor_refresh_id);
//        smartRefreshLayout.setRefreshHeader(new CircleHeader(this));
//        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(this, R.color.color_1c82d4)));
//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//
//
//            }
//        });
//
//        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
//
//            }
//        });


        mGridView = (GridView) findViewById(R.id.floor_gridview_id);
        myAdapter = new MyAdapter();
        mGridView.setAdapter(myAdapter);


        mListview = (ListView) findViewById(R.id.floor_listview);
        myNumAdapter = new MyNumAdapter();
        mListview.setAdapter(myNumAdapter);

    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyHolder myHolder = null;
            if (view == null) {
                myHolder = new MyHolder();
                view = LayoutInflater.from(FloorMessageActivity.this).inflate(R.layout.floor_gridview_item, null);
                myHolder.tv = view.findViewById(R.id.floor_num);
                view.setTag(myHolder);
            } else {
                myHolder = (MyHolder) view.getTag();
            }
            int a = 100 - i;
            myHolder.tv.setText(a + "");
            return view;
        }

        class MyHolder {
            TextView tv, num;
        }
    }

    class MyNumAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyHolder myHolder = null;
            if (view == null) {
                myHolder = new MyHolder();
                view = LayoutInflater.from(FloorMessageActivity.this).inflate(R.layout.num_item, null);

                myHolder.num = view.findViewById(R.id.floor_num);
                view.setTag(myHolder);
            } else {
                myHolder = (MyHolder) view.getTag();
            }
            int a = 20 - i;
            myHolder.num.setText(a + "");
            return view;
        }

        class MyHolder {
            TextView num;
        }
    }
}
