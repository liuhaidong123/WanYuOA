package com.oa.wanyu.activity.reimbursementActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.leave.LeaveActivityMessageActivity;
import com.oa.wanyu.bean.ReimbursementInformationBean;
import com.oa.wanyu.bean.ReimbursementInformationItems;
import com.oa.wanyu.bean.ReimbursementInformationRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.leaveType.LeaveInformationRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.MyGridView;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

//报销详情
public class ReimbursementActivityMessageActivity extends AppCompatActivity {
    private ImageView mBack, show_img,show_icon;
    long referId;
    private int withdraw = -1;//判断是否显示撤回按钮
    private String person_name="",department_name="";
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private ListView mListview;
    private ReimbursementMessAda reimbursementMessAda;
    private View footer;
    private MyGridView myGridView;
    private PictureMessAda pictureMessAda;
    private List<String> imgList = new ArrayList<>();//返回图片集合
    private List<ReimbursementInformationItems> mList = new ArrayList<>();
    private TextView all_money;
    private LinearLayout agree_disagree_ll;
    private TextView agree_btn, disagree_btn, withdraw_btn;//同意，驳回，撤回
    private Intent intent;
    private AlertDialog.Builder builder_withdraw, builder_agree, builder_disagree;
    private AlertDialog alertDialog_withdraw, alertDialog_agree, alertDialog_disagree;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url, agree_url, withdraw_url;//同意或驳回，撤回
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            if (msg.what == 1) {
                try {
                    String s = (String) msg.obj;
                    Object o = gson.fromJson(s, ReimbursementInformationRoot.class);
                    if (o != null && o instanceof ReimbursementInformationRoot) {
                        ReimbursementInformationRoot reimbursementInformationRoot = (ReimbursementInformationRoot) o;
                        if (reimbursementInformationRoot != null) {
                            if ("0".equals(reimbursementInformationRoot.getCode())) {
                                agree_disagree_ll.setVisibility(View.VISIBLE);
                                if (reimbursementInformationRoot.getApplyReimburse() != null) {
                                    ReimbursementInformationBean reimbursementInformationBean = reimbursementInformationRoot.getApplyReimburse();
                                    all_money.setText(reimbursementInformationBean.getAmount()+"");
                                    person_name=reimbursementInformationBean.getTrueName();
                                    department_name=reimbursementInformationBean.getDepartmentName();

                                    //status=1  驳回   ；2 已同意  ；0 待审批 3失效；withdraw=10变示不显示撤回按钮，20表示显示撤回按钮
                                    int status = reimbursementInformationRoot.getApplyReimburse().getStatus();
                                    if (status == 0) {
                                        if (withdraw == 10) {//不显示撤回按钮
                                            agree_disagree_ll.setVisibility(View.VISIBLE);
                                            agree_btn.setVisibility(View.VISIBLE);
                                            disagree_btn.setVisibility(View.VISIBLE);
                                            withdraw_btn.setVisibility(View.GONE);
                                        } else if (withdraw == 20) {//显示撤回按钮
                                            agree_disagree_ll.setVisibility(View.VISIBLE);
                                            agree_btn.setVisibility(View.GONE);
                                            disagree_btn.setVisibility(View.GONE);
                                            withdraw_btn.setVisibility(View.VISIBLE);
                                        } else {//
                                            agree_disagree_ll.setVisibility(View.GONE);
                                            agree_btn.setVisibility(View.GONE);
                                            disagree_btn.setVisibility(View.GONE);
                                            withdraw_btn.setVisibility(View.GONE);
                                        }


                                    } else if (status == 1) {

                                        show_img.setVisibility(View.VISIBLE);
                                        show_img.setImageResource(R.mipmap.b_img);

                                        agree_disagree_ll.setVisibility(View.GONE);
                                        agree_btn.setVisibility(View.GONE);
                                        disagree_btn.setVisibility(View.GONE);
                                        withdraw_btn.setVisibility(View.GONE);
                                    } else if (status == 2) {
                                        show_img.setVisibility(View.VISIBLE);
                                        show_img.setImageResource(R.mipmap.t_img);
                                        agree_disagree_ll.setVisibility(View.GONE);
                                        agree_btn.setVisibility(View.GONE);
                                        disagree_btn.setVisibility(View.GONE);
                                        withdraw_btn.setVisibility(View.GONE);

                                    } else {// 3 失效
                                        agree_disagree_ll.setVisibility(View.GONE);
                                        agree_btn.setVisibility(View.GONE);
                                        disagree_btn.setVisibility(View.GONE);
                                        withdraw_btn.setVisibility(View.GONE);
                                    }



                                    if (!"".equals(reimbursementInformationBean.getPictures())) {
                                        String[] strings = reimbursementInformationBean.getPictures().split(";");
                                        for (int i = 0; i < strings.length; i++) {
                                            imgList.add(strings[i]);
                                        }
                                        Log.e("图片几张", imgList.size() + "");
                                        pictureMessAda.notifyDataSetChanged();
                                    }

                                    if (reimbursementInformationBean.getItems() != null) {
                                        mList = reimbursementInformationBean.getItems();
                                        reimbursementMessAda.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ReimbursementActivityMessageActivity.this, "此数据不存在", Toast.LENGTH_SHORT).show();

                                    }


                                } else {
                                    Toast.makeText(ReimbursementActivityMessageActivity.this, "此数据不存在", Toast.LENGTH_SHORT).show();
                                }


                            } else if ("-1".equals(reimbursementInformationRoot.getCode())) {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                show_icon.setVisibility(View.GONE);
                                agree_disagree_ll.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, "错误信息："+reimbursementInformationRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("错误信息："+reimbursementInformationRoot.getMessage());
                                show_icon.setVisibility(View.GONE);
                                agree_disagree_ll.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(ReimbursementActivityMessageActivity.this, "此数据不存在", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(ReimbursementActivityMessageActivity.this, "后台数据错误", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                    show_icon.setVisibility(View.GONE);
                    agree_disagree_ll.setVisibility(View.GONE);
                }

            } else if (msg.what == 2) {//撤回
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 3) {//同意或者驳回
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(ReimbursementActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else {
                //数据错误
                Toast.makeText(ReimbursementActivityMessageActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                show_icon.setVisibility(View.GONE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
                agree_disagree_ll.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement_message);

        show_icon=(ImageView) findViewById(R.id.show_sign);

        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //显示发票大图
        show_img = (ImageView) findViewById(R.id.show_img);
        show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_img.setVisibility(View.GONE);
            }
        });
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_reimbursement_information;
        agree_url = URLTools.urlBase + URLTools.apply_reimbursement_agree;
        withdraw_url = URLTools.urlBase + URLTools.apply_reimbursement_withdraw;

        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_reimbursement_message);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (referId != -1) {//请求详情
                    no_data_rl.setEnabled(false);
                    BallProgressUtils.showLoading(ReimbursementActivityMessageActivity.this, mAll_RL);
                    okHttpManager.getMethod(false, url + "id=" + referId, "请求报销详情", handler, 1);
                } else {//传过来的详情ID错误
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mListview = (ListView) findViewById(R.id.bx_listview_id);
        reimbursementMessAda = new ReimbursementMessAda();
        mListview.setAdapter(reimbursementMessAda);
        footer = LayoutInflater.from(this).inflate(R.layout.baoxiao_message_footer, null);
        mListview.addFooterView(footer);
        //报销总金额
        all_money = footer.findViewById(R.id.bx_tv);
        myGridView = footer.findViewById(R.id.img_gridview);
        pictureMessAda = new PictureMessAda();
        myGridView.setAdapter(pictureMessAda);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                show_img.setVisibility(View.VISIBLE);
                com.squareup.picasso.Picasso.with(ReimbursementActivityMessageActivity.this).load(URLTools.urlBase+imgList.get(i)).into(show_img);
            }
        });

        //同意，驳回，撤销
        //同意
        agree_disagree_ll = (LinearLayout) findViewById(R.id.agree_disagree_ll);
        agree_btn = (TextView) findViewById(R.id.travel_agree_btn);
        agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (referId != -1) {//请求详情
                    alertDialog_agree.show();
                } else {//传过来的详情ID错误
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //，驳回
        disagree_btn = (TextView) findViewById(R.id.travel_disagree_btn);
        disagree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (referId != -1) {//请求详情
                    alertDialog_disagree.show();
                } else {//传过来的详情ID错误
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //撤回
        withdraw_btn = (TextView) findViewById(R.id.withdraw_submit);
        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (referId != -1) {//请求详情
                    alertDialog_withdraw.show();
                } else {//传过来的详情ID错误
                    Toast.makeText(ReimbursementActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //撤回
        builder_withdraw = new AlertDialog.Builder(this);
        builder_withdraw.setTitle("是否撤回");
        builder_withdraw.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_withdraw.dismiss();
                BallProgressUtils.showLoading(ReimbursementActivityMessageActivity.this, mAll_RL);
                okHttpManager.getMethod(false, withdraw_url + "id=" + referId, "撤回接口", handler, 2);
            }
        });
        builder_withdraw.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_withdraw.dismiss();
            }
        });
        alertDialog_withdraw = builder_withdraw.create();


        //同意
        builder_agree = new AlertDialog.Builder(this);
        builder_agree.setTitle("是否同意");
        builder_agree.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_agree.dismiss();
                BallProgressUtils.showLoading(ReimbursementActivityMessageActivity.this, mAll_RL);
                okHttpManager.getMethod(false, agree_url + "id=" + referId + "&isAdopt=" + 1, "同意接口", handler, 3);
            }
        });
        builder_agree.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_agree.dismiss();
            }
        });
        alertDialog_agree = builder_agree.create();

        //驳回
        builder_disagree = new AlertDialog.Builder(this);
        builder_disagree.setTitle("是否驳回");
        builder_disagree.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_disagree.dismiss();
                BallProgressUtils.showLoading(ReimbursementActivityMessageActivity.this, mAll_RL);
                okHttpManager.getMethod(false, agree_url + "id=" + referId + "&isAdopt=" + 0, "驳回接口", handler, 3);
            }
        });
        builder_disagree.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog_disagree.dismiss();
            }
        });
        alertDialog_disagree = builder_disagree.create();


        intent = getIntent();
        referId = intent.getLongExtra("id", -1);
        withdraw = intent.getIntExtra("withdraw_flag", -1);
        if (referId != -1) {//请求详情
            okHttpManager.getMethod(false, url + "id=" + referId, "请求报销详情", handler, 1);
        } else {//传过来的详情ID错误
            Toast.makeText(this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
        }

    }

    class ReimbursementMessAda extends BaseAdapter {

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
            ReimHolder reimHolder = null;
            if (view == null) {
                reimHolder = new ReimHolder();
                view = LayoutInflater.from(ReimbursementActivityMessageActivity.this).inflate(R.layout.reimbursment_message_item, null);
                reimHolder.money = view.findViewById(R.id.edit_money);
                reimHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
                reimHolder.type = view.findViewById(R.id.type_mess_tv);
                reimHolder.person_and_department= view.findViewById(R.id.person_and_department);
                view.setTag(reimHolder);
            } else {
                reimHolder = (ReimHolder) view.getTag();
            }
            int a = i + 1;
            reimHolder.mingxi.setText("报销明细" + "(" + a + ")");
            reimHolder.money.setText(mList.get(i).getAmount() + "");
            reimHolder.type.setText(mList.get(i).getTyp() + "");
            reimHolder.person_and_department.setText(department_name+"  " +person_name);
            return view;
        }

        class ReimHolder {
            EditText money,person_and_department;
            TextView type, mingxi;
        }
    }


    class PictureMessAda extends BaseAdapter {

        @Override
        public int getCount() {
            return imgList.size();
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
            PHolder pHolder = null;
            if (view == null) {
                pHolder = new PHolder();
                view = LayoutInflater.from(ReimbursementActivityMessageActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
                pHolder.imageView = view.findViewById(R.id.picture);
                view.setTag(pHolder);
            } else {
                pHolder = (PHolder) view.getTag();
            }
            Picasso.with(ReimbursementActivityMessageActivity.this).load(URLTools.urlBase + imgList.get(i)).error(R.mipmap.errorpicture).into(pHolder.imageView);

            return view;
        }

        class PHolder {
            ImageView imageView;
        }
    }
}
