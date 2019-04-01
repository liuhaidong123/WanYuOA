package com.oa.wanyu.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oa.wanyu.R;
import com.oa.wanyu.bean.TestBean;
import com.oa.wanyu.myutils.CircleImg;
import com.oa.wanyu.myutils.MyLetterListView;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 联系人
 */
public class ContactsFragment extends Fragment {
    private EditText editText;
    private MyLetterListView myLetterListView;
    private HashMap<String, Integer> mAlphaIndexer = new HashMap<String, Integer>();//当前汉语拼音首字母和与之对应的列表位
    private List<TestBean> mList = new ArrayList<>();
    private ListView mListView;
    private SmartRefreshLayout smartRefreshLayout;
    private MyConcatcsAdapter myConcatcsAdapter;

    private List<Boolean> isVisi = new ArrayList<>();//默认集合中都是true为隐藏，false为显示打电话按钮
    private Comparator comparator = new Comparator<TestBean>() {
        @Override
        public int compare(TestBean o1, TestBean o2) {
            String a = o1.getName().substring(0, 1);
            String b = o2.getName().substring(0, 1);
            //如果前面的大那么返回1，后面的大返回-1；此位置相同，继续比较下一位，直到最后一位，如果都相同的话，就返回0；
            int flag = a.compareTo(b);
            return flag;
        }
    };

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view1) {
        editText = view1.findViewById(R.id.edit_id);

        myLetterListView = view1.findViewById(R.id.a_to_z_listview_id);
        myLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

        mListView = view1.findViewById(R.id.listview_contacts);
        mList.add(new TestBean("张三", "zhangshan"));
        mList.add(new TestBean("王三", "wangshan"));
        mList.add(new TestBean("李三", "lishan"));
        mList.add(new TestBean("赵三", "zhaoshan"));
        mList.add(new TestBean("刘三", "liushan"));
        mList.add(new TestBean("Jams", "Jams"));
        mList.add(new TestBean("Tom", "Tom"));
        mList.add(new TestBean("Mary", "Mary"));
        mList.add(new TestBean("kardy", "kardy"));
        mList.add(new TestBean("张三", "zhangshan"));
        mList.add(new TestBean("王三", "wangshan"));
        mList.add(new TestBean("李三", "lishan"));
        mList.add(new TestBean("赵三", "zhaoshan"));
        mList.add(new TestBean("刘三", "liushan"));
        mList.add(new TestBean("Jams", "Jams"));
        mList.add(new TestBean("Tom", "Tom"));
        mList.add(new TestBean("Mary", "Mary"));
        mList.add(new TestBean("kardy", "kardy"));
        mList.add(new TestBean("张三", "zhangshan"));
        mList.add(new TestBean("王三", "wangshan"));
        mList.add(new TestBean("李三", "lishan"));
        mList.add(new TestBean("赵三", "zhaoshan"));
        mList.add(new TestBean("刘三", "liushan"));
        mList.add(new TestBean("Jams", "Jams"));
        mList.add(new TestBean("Tom", "Tom"));
        mList.add(new TestBean("Mary", "Mary"));
        mList.add(new TestBean("kardy", "kardy"));


        // Collections.sort(mList, comparator);//集合按照字母A到Z排序
        for (int i = 0; i < mList.size(); i++) {
            mAlphaIndexer.put(getAlpha(mList.get(i).getPinying()), i);
            isVisi.add(i, true);
        }
        myConcatcsAdapter = new MyConcatcsAdapter();
        mListView.setAdapter(myConcatcsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int k = 0; k < isVisi.size(); k++) {
                    if (k == i) {
                        isVisi.set(i, false);
                    } else {
                        isVisi.set(k, true);
                    }
                }

                myConcatcsAdapter.notifyDataSetChanged();
            }
        });

//        smartRefreshLayout = view1.findViewById(R.id.smart_refresh);
//        smartRefreshLayout.setRefreshHeader(new CircleHeader(getContext()));
//        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(getContext(), R.color.color_1c82d4)));
//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//            }
//        });
//
//        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
//            }
//        });
    }


    //联系人适配器
    class MyConcatcsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
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

            myHolder = new MyHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.contacts_item, null);
            myHolder.img = view.findViewById(R.id.contacts_head);
            myHolder.name = view.findViewById(R.id.contacts_name);
            myHolder.job = view.findViewById(R.id.contacts_job);
            myHolder.call_phone_btn = view.findViewById(R.id.call_phone_id);
            myHolder.call_hone_num = view.findViewById(R.id.telephone_num);
            myHolder.call_phone_rl = view.findViewById(R.id.call_phone_rl);


            myHolder.name.setText(mList.get(i).getName());

            //判断打电话按钮是否显示隐藏
            if (isVisi.get(i)) {
                myHolder.call_phone_rl.setVisibility(View.GONE);
            } else {
                myHolder.call_phone_rl.setVisibility(View.VISIBLE);
            }

            myHolder.call_phone_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                            return;
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:18335277251"));
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:18335277251"));
                        startActivity(intent);
                    }


                }
            });
            return view;
        }

        class MyHolder {
            CircleImg img;
            TextView name, job, call_phone_btn, call_hone_num;
            RelativeLayout call_phone_rl;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(), "电话授权成功", Toast.LENGTH_SHORT).show();
                    //注意这里电话需要传过来
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:18335277251"));
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "电话授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    //触摸右侧拼音的监听事件
    public class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(String s) {

            if (mAlphaIndexer.get(s) != null) {
                int position = mAlphaIndexer.get(s);
                mListView.setSelection(position);

            }
        }
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }
}
