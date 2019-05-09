package com.oa.wanyu.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.bean.ContactsBean;
import com.oa.wanyu.bean.ContactsRoot;
import com.oa.wanyu.bean.approvastatus.ApprovalRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CheckPhone;
import com.oa.wanyu.myutils.CircleImg;
import com.oa.wanyu.myutils.MyLetterListView;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    private List<ContactsBean> mList = new ArrayList<>();
    private ListView mListView;
    private int mPosition;
    private SmartRefreshLayout smartRefreshLayout;
    private MyConcatcsAdapter myConcatcsAdapter;
    private List<Boolean> isVisi = new ArrayList<>();//默认集合中都是true为隐藏，false为显示打电话按钮
//    private Comparator comparator = new Comparator<ContactsBean>() {
//        @Override
//        public int compare(ContactsBean o1, ContactsBean o2) {
//            String a = o1.getPhoneticize().substring(0, 1);
//            String b = o2.getPhoneticize().substring(0, 1);
//            //如果前面的大那么返回1，后面的大返回-1；此位置相同，继续比较下一位，直到最后一位，如果都相同的话，就返回0；
//            int flag = a.compareTo(b);
//            return flag;
//        }
//    };

    private RelativeLayout no_data_rl;
    private TextView no_mess_tv;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ContactsRoot.class);
                    if (o != null && o instanceof ContactsRoot) {
                        ContactsRoot contactsRoot = (ContactsRoot) o;
                        if (contactsRoot != null) {
                            if ("0".equals(contactsRoot.getCode())) {
                                if (contactsRoot.getRows() != null) {
                                    if (contactsRoot.getRows().size() == 0) {
                                        Toast.makeText(getActivity(), "暂无联系人", Toast.LENGTH_SHORT).show();
                                        no_data_rl.setVisibility(View.VISIBLE);
                                        no_mess_tv.setText("暂无联系人");
                                    } else {
                                        no_data_rl.setVisibility(View.GONE);
                                        no_mess_tv.setText("");
                                    }
                                    mList = contactsRoot.getRows();
                                    for (int i = 0; i < mList.size(); i++) {
                                        mAlphaIndexer.put(getAlpha(mList.get(i).getPhoneticize()), i);
                                        isVisi.add(i, true);
                                    }
                                    myConcatcsAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(contactsRoot.getCode())) {
                                Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else {
                //数据错误
                Toast.makeText(getActivity(), "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }

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

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.select_contacts;
        okHttpManager.getMethod(false, url + "name=" + "", "查询联系人列表", handler, 1);

        no_data_rl = view1.findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                BallProgressUtils.showLoading(getActivity(), no_data_rl);
                okHttpManager.getMethod(false, url + "name=" + "", "查询联系人列表", handler, 1);
            }
        });
        no_mess_tv = view1.findViewById(R.id.no_mess_tv);


        editText = view1.findViewById(R.id.edit_id);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //按下的时候会会执行：手指按下和手指松开俩个过程
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        //do something;
                        if ("".equals(editText.getText().toString().trim())){
                            Toast.makeText(getActivity(),"请输入查询联系人的姓名",Toast.LENGTH_SHORT).show();
                        }else {
                            BallProgressUtils.showLoading(getActivity(), smartRefreshLayout);
                            try {
                                String str= URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                                okHttpManager.getMethod(false, url+ "name=" + str, "查询联系人列表", handler, 1);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                BallProgressUtils.dismisLoading();
                            }

                        }

                    }

                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
              if ("".equals(editable.toString().trim())) {
                  okHttpManager.getMethod(false, url+ "name=" + "", "查询联系人列表", handler, 1);
              }
            }
        });

        myLetterListView = view1.findViewById(R.id.a_to_z_listview_id);
        myLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

        mListView = view1.findViewById(R.id.listview_contacts);

        // Collections.sort(mList, comparator);//集合按照字母A到Z排序

        myConcatcsAdapter = new MyConcatcsAdapter();
        mListView.setAdapter(myConcatcsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mPosition = i;
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

        smartRefreshLayout = view1.findViewById(R.id.smart_refresh);
        smartRefreshLayout.setRefreshHeader(new CircleHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(getContext(), R.color.color_1c82d4)));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                try {
                    String str= URLEncoder.encode(editText.getText().toString().trim(),"utf-8");
                    okHttpManager.getMethod(false, url+ "name=" + str, "查询联系人列表", handler, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    BallProgressUtils.dismisLoading();
                }

            }
        });
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            MyHolder myHolder = null;
            if (view == null) {
                myHolder = new MyHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.contacts_item, null);
                myHolder.img = view.findViewById(R.id.contacts_head);
                myHolder.name = view.findViewById(R.id.contacts_name);
                myHolder.job = view.findViewById(R.id.contacts_job);
                myHolder.department = view.findViewById(R.id.contacts_department);
                myHolder.call_phone_btn = view.findViewById(R.id.call_phone_id);
                myHolder.call_hone_num = view.findViewById(R.id.telephone_num);
                myHolder.call_phone_rl = view.findViewById(R.id.call_phone_rl);
                view.setTag(myHolder);

            } else {
                myHolder = (MyHolder) view.getTag();
            }

            Picasso.with(getActivity()).load(URLTools.urlBase + mList.get(i).getAvatar()).error(R.mipmap.head_img_icon).into(myHolder.img);
            myHolder.name.setText(mList.get(i).getUserName() + "");

            if (!"".equals(mList.get(i).getDepartmentName())) {
                myHolder.department.setText("部门：" + mList.get(i).getDepartmentName() + "");
            } else {
                myHolder.department.setText("部门：未知");
            }

            if (!"".equals(mList.get(i).getPosition())) {
                myHolder.job.setText("职位：" + mList.get(i).getPosition() + "");
            } else {
                myHolder.job.setText("职位：未知");
            }

            if (!"".equals(mList.get(i).getMobile())) {
                myHolder.call_hone_num.setText("电话：" + mList.get(i).getMobile());
            } else {
                myHolder.call_hone_num.setText("电话：未知");
            }
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
                            intentPhone(mList.get(i).getMobile());
                        }
                    } else {
                        intentPhone(mList.get(i).getMobile());
                    }


                }
            });
            return view;
        }

        class MyHolder {
            CircleImg img;
            TextView name, department, job, call_phone_btn, call_hone_num;
            RelativeLayout call_phone_rl;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "电话授权成功", Toast.LENGTH_SHORT).show();
                    //注意这里电话需要传过来
                    intentPhone(mList.get(mPosition).getMobile());
                } else {
                    Toast.makeText(getActivity(), "电话授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    private void intentPhone(String phone) {
        if (CheckPhone.checkPhone(phone)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "电话错误", Toast.LENGTH_SHORT).show();
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
