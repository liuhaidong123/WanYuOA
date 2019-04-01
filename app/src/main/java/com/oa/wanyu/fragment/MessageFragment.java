package com.oa.wanyu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oa.wanyu.R;
import com.oa.wanyu.myutils.CircleImg;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 消息
 */
public class MessageFragment extends Fragment {

    private SmartRefreshLayout smartRefreshLayout;
    private ListView mListView;
    private MyAdapter myAdapter;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mListView = view.findViewById(R.id.listview_notify);
        myAdapter=new MyAdapter();
        mListView.setAdapter(myAdapter);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        smartRefreshLayout.setRefreshHeader(new CircleHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(getContext(), R.color.color_1c82d4)));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
                view = LayoutInflater.from(getContext()).inflate(R.layout.mess_item, null);
                myHolder.img = view.findViewById(R.id.img);
                myHolder.title = view.findViewById(R.id.title);
                myHolder.mess = view.findViewById(R.id.message);
                myHolder.date = view.findViewById(R.id.date);
                view.setTag(myHolder);
            } else {
                myHolder = (MyHolder) view.getTag();
            }


            return view;
        }

        class MyHolder {
            CircleImg img;
            TextView title, mess, date;

        }
    }
}
