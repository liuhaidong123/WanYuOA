package com.oa.wanyu.fragmentAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.oa.wanyu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2018/5/28.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mList = new ArrayList<>();
    private String[] str = new String[]{"消息", "工作", "联系人", "我的"};
    private int[] iconImg = {R.mipmap.mess_icon_select, R.mipmap.job_icon_no_select, R.mipmap.contacts_icon_no_select, R.mipmap.my_icon_no_select};

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> list, Context context) {
        super(fm);
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    //自定义tablayout布局

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tablayout_item, null);
        TextView tv = view.findViewById(R.id.icon_tv);
        ImageView img = view.findViewById(R.id.icon_img);
        tv.setText(str[position]);
        img.setImageResource(iconImg[position]);
        if (position == 0) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_3dacfe));
            view.setBackgroundResource(R.color.color_ffffff);
        } else {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_b9b9b9));
            view.setBackgroundResource(R.color.color_f9f9f9);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
