package com.oa.wanyu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.bean.VersonCodeRoot;
import com.oa.wanyu.bean.VersonCodeRows;
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

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private int mVersonCode;
    private Gson gson=new Gson();
    private OkHttpManager okHttpManager;
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try{
                    String s = (String) msg.obj;
                    Object o = gson.fromJson(s, VersonCodeRoot.class);
                    if (o != null && o instanceof VersonCodeRoot) {
                        VersonCodeRoot versonCodeRoot = (VersonCodeRoot) o;
                        if (versonCodeRoot != null && "0".equals(versonCodeRoot.getCode())) {
                            //判断服务器版本号与本地版本号
                            VersonCodeRows versonCodeRows = versonCodeRoot.getDict();
                            if (versonCodeRows != null) {

                                String versonçode = versonCodeRows.getValue();
                                Integer code = Integer.valueOf(versonçode);
                                if (code>mVersonCode){
                                    //弹框更新(等待上线后再测试)
                                    alertDialog.show();
                                }
                            }


                        } else {
                            Toast.makeText(MainActivity.this, "获取版本号失败", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(MainActivity.this, "获取版本号错误", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){

                }


            } else {
                Toast.makeText(MainActivity.this, "连接服务器失败,请重新尝试", Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){


        //获取本地版本号
        mVersonCode=getVersionCode();
        //检测版本号
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.check_verson_code;
        okHttpManager.getMethod(false, url, "检测版本号", handler, 1);
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("检测到最新版本,请更新");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://59.110.169.148:8183/static/app-release.apk"));
                startActivity(intent);
                alertDialog.dismiss();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();



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

    private int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionCode = packInfo.versionCode;
        Log.e("本地软件版本号=", versionCode + "");
        return versionCode;
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
