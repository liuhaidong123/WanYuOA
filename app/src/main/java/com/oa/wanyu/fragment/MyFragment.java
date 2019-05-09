package com.oa.wanyu.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.bean.UserMessageRoot;
import com.oa.wanyu.myutils.CircleImg;
import com.squareup.picasso.Picasso;

/**
 * 我的
 */
public class MyFragment extends Fragment {
    private TextView m_name, m_company, m_department, m_job, m_phone;
    private CircleImg head_img;
    private ImageView m_edit_img;

    private OkHttpManager okhttpmanager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, UserMessageRoot.class);
                    if (o != null && o instanceof UserMessageRoot) {
                        UserMessageRoot userMessageRoot = (UserMessageRoot) o;
                        if (userMessageRoot != null) {

                            if ("".equals(userMessageRoot.getTrueName())) {
                                m_name.setText("未知姓名");
                            } else {
                                m_name.setText(userMessageRoot.getTrueName() + "");
                            }

                            if ("".equals(userMessageRoot.getDepartmentName())) {
                                m_department.setText("部门： -----");
                            } else {
                                m_department.setText(userMessageRoot.getDepartmentName() + "");
                            }

                            if ("".equals(userMessageRoot.getPosition())) {
                                m_job.setText("职位： -----");
                            } else {
                                m_job.setText("职位： " + userMessageRoot.getPosition() + "");
                            }

                            if ("".equals(userMessageRoot.getTelephone())) {
                                m_phone.setText("电话： -----");
                            } else {
                                m_phone.setText("电话： " + userMessageRoot.getTelephone() + "");
                            }
                            m_company.setText(userMessageRoot.getCompanyName() + "");


                            Picasso.with(getActivity()).load(URLTools.urlBase + userMessageRoot.getAvatar()).error(R.mipmap.head_img_icon).into(head_img);

                        } else {
                            Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getActivity(), "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {

        m_name = view.findViewById(R.id.my_name);
        m_company = view.findViewById(R.id.my_company);
        m_department = view.findViewById(R.id.my_bumen);
        m_job = view.findViewById(R.id.my_job);
        m_phone = view.findViewById(R.id.my_phone);
        head_img = view.findViewById(R.id.my_head_img);
        m_edit_img = view.findViewById(R.id.my_edit);

        okhttpmanager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.user_message;
        okhttpmanager.getMethod(false, url, "用户信息", handler, 1);
    }

}
