package com.oa.wanyu.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oa.wanyu.R;
import com.oa.wanyu.fragment.ContactsFragment;
import com.oa.wanyu.fragment.JobFragment;
import com.oa.wanyu.fragment.MessageFragment;
import com.oa.wanyu.fragment.MyFragment;
import com.oa.wanyu.fragmentAdapter.MainFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
        private ViewPager mViewpager;
    private MainFragmentAdapter mainFragmentAdapter;
    private MessageFragment mMessageFragment;
    private JobFragment mJobFragment;
    private ContactsFragment mContactsFragment;
    private MyFragment mMyFragment;
    private FragmentManager mFragmentManager;
    private List<Fragment> mListFragment=new ArrayList<>();
    private TabLayout mTablayout;
    private  long time = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        mViewpager = (ViewPager) findViewById(R.id.main_viewpager);
        mFragmentManager = getSupportFragmentManager();

        mMessageFragment=new MessageFragment();
        mJobFragment=new JobFragment();
        mContactsFragment=new ContactsFragment();
        mMyFragment=new MyFragment();

        mListFragment.add(mMessageFragment);
        mListFragment.add(mJobFragment);
        mListFragment.add(mContactsFragment);
        mListFragment.add(mMyFragment);
        mainFragmentAdapter=new MainFragmentAdapter(mFragmentManager, mListFragment, this);
        mViewpager.setAdapter(mainFragmentAdapter);

        mTablayout = (TabLayout) findViewById(R.id.main_tablayout);
        mTablayout.setupWithViewPager(mViewpager);//setupWithViewPager必须在ViewPager.setAdapter()之后调用
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTablayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTablayout.getTabAt(i);
            tab.setCustomView(mainFragmentAdapter.getTabView(i));
        }
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.icon_tv);
                ImageView img2 = view.findViewById(R.id.icon_img);
                tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_3dacfe));
                if (tab.getPosition() == 0) {
                    img2.setImageResource(R.mipmap.mess_icon_select);
                    view.setBackgroundResource(R.color.color_ffffff);
                } else if (tab.getPosition() == 1) {
                    img2.setImageResource(R.mipmap.job_icon_select);
                    view.setBackgroundResource(R.color.color_ffffff);
                } else if (tab.getPosition() == 2) {
                    img2.setImageResource(R.mipmap.contacts_icon_select);
                    view.setBackgroundResource(R.color.color_ffffff);
                } else if (tab.getPosition() == 3) {
                    img2.setImageResource(R.mipmap.my_icon_select);
                    view.setBackgroundResource(R.color.color_ffffff);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.icon_tv);
                tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_b9b9b9));
                ImageView img2 = view.findViewById(R.id.icon_img);
                if (tab.getPosition() == 0) {
                    view.setBackgroundResource(R.color.color_f9f9f9);
                    img2.setImageResource(R.mipmap.mess_icon_no_select);
                } else if (tab.getPosition() == 1) {
                    img2.setImageResource(R.mipmap.job_icon_no_select);
                    view.setBackgroundResource(R.color.color_f9f9f9);
                } else if (tab.getPosition() == 2) {
                    img2.setImageResource(R.mipmap.contacts_icon_no_select);
                    view.setBackgroundResource(R.color.color_f9f9f9);
                } else if (tab.getPosition() == 3) {
                    img2.setImageResource(R.mipmap.my_icon_no_select);
                    view.setBackgroundResource(R.color.color_f9f9f9);
                }
            }

            //再次被选中
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (time > 0) {
            if (System.currentTimeMillis() - time < 2000) {
                super.onBackPressed();
            } else {
                time = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }

        } else {
            time = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();

        }
    }
}
