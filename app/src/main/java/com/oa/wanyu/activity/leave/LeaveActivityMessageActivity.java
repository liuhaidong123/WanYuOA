package com.oa.wanyu.activity.leave;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.currencyApply.CurrencyApplyActivityMessageActivity;
import com.oa.wanyu.bean.CurrencyInformationRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.leaveType.LeaveInformationRoot;
import com.oa.wanyu.myutils.BallProgressUtils;

//请假详情
public class LeaveActivityMessageActivity extends AppCompatActivity {
    private ImageView mBack,show_img;
    long referId;
    private int withdraw = -1;//判断是否显示撤回按钮
    private RelativeLayout mAll_RL, no_data_rl;
    private TextView no_mess_tv;
    private TextView mLeaveType, startTime, endTime, dayTv,person_and_department;
    private EditText mReason_edit;
    private LinearLayout agree_disagree_ll;
    private TextView agree_btn, disagree_btn, withdraw_btn;//同意，驳回，撤回
    private Intent intent;
    private AlertDialog.Builder builder_withdraw, builder_agree, builder_disagree;
    private AlertDialog alertDialog_withdraw, alertDialog_agree, alertDialog_disagree;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url, agree_url, withdraw_url;//同意或驳回，撤回
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            if (msg.what == 1) {
                try {
                    String s = (String) msg.obj;
                    Object o = gson.fromJson(s, LeaveInformationRoot.class);
                    if (o != null && o instanceof LeaveInformationRoot) {
                        LeaveInformationRoot leaveInformationRoot = (LeaveInformationRoot) o;
                        if (leaveInformationRoot != null) {
                            if ("0".equals(leaveInformationRoot.getCode())) {
                                agree_disagree_ll.setVisibility(View.VISIBLE);
                                if (leaveInformationRoot.getApplyLeave() != null) {
                                    person_and_department.setText(leaveInformationRoot.getApplyLeave().getDepartmentName()+" "+leaveInformationRoot.getApplyLeave().getTrueName());
                                    mLeaveType.setText(leaveInformationRoot.getApplyLeave().getTyp()+"");
                                    startTime.setText(leaveInformationRoot.getApplyLeave().getBeginDateString()+"");
                                    endTime.setText(leaveInformationRoot.getApplyLeave().getEndDateString()+"");
                                    dayTv.setText(leaveInformationRoot.getApplyLeave().getDuration()+"");
                                    mReason_edit.setText(leaveInformationRoot.getApplyLeave().getReason()+"");

                                    //status=1  驳回   ；2 已同意  ；0 待审批 ； 3 失效;10变示不显示撤回按钮，20表示显示撤回按钮
                                    int status =leaveInformationRoot.getApplyLeave().getStatus();
                                    if (status == 0) {

                                        if (withdraw==10){//不显示撤回按钮
                                            agree_disagree_ll.setVisibility(View.VISIBLE);
                                            agree_btn.setVisibility(View.VISIBLE);
                                            disagree_btn.setVisibility(View.VISIBLE);
                                            withdraw_btn.setVisibility(View.GONE);
                                        }else if (withdraw==20){//显示撤回按钮
                                            agree_disagree_ll.setVisibility(View.VISIBLE);
                                            agree_btn.setVisibility(View.GONE);
                                            disagree_btn.setVisibility(View.GONE);
                                            withdraw_btn.setVisibility(View.VISIBLE);
                                        }else {//
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

                                    } else {//失效
                                        agree_disagree_ll.setVisibility(View.GONE);
                                        agree_btn.setVisibility(View.GONE);
                                        disagree_btn.setVisibility(View.GONE);
                                        withdraw_btn.setVisibility(View.GONE);
                                    }
                                }else {
                                    Toast.makeText(LeaveActivityMessageActivity.this, "此数据不存在", Toast.LENGTH_SHORT).show();
                                }


                            } else if ("-1".equals(leaveInformationRoot.getCode())) {
                                Toast.makeText(LeaveActivityMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                show_img.setVisibility(View.GONE);
                                agree_disagree_ll.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(LeaveActivityMessageActivity.this, "失败信息："+leaveInformationRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("失败信息："+leaveInformationRoot.getMessage());
                                show_img.setVisibility(View.GONE);
                                agree_disagree_ll.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(LeaveActivityMessageActivity.this, "此数据不存在", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(LeaveActivityMessageActivity.this, "后台数据错误", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(LeaveActivityMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                    show_img.setVisibility(View.GONE);
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
                                Toast.makeText(LeaveActivityMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(LeaveActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LeaveActivityMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 3) {//同意或者驳回
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivityMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(LeaveActivityMessageActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LeaveActivityMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else {
                //数据错误
                Toast.makeText(LeaveActivityMessageActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
                show_img.setVisibility(View.GONE);
                agree_disagree_ll.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_message);
        show_img=(ImageView) findViewById(R.id.show_sign);
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_leave_information;
        agree_url = URLTools.urlBase + URLTools.apply_leave_agree;
        withdraw_url = URLTools.urlBase + URLTools.apply_leave_withdraw;

        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_leave_message);
        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (referId != -1) {//请求详情
                    no_data_rl.setEnabled(false);
                    BallProgressUtils.showLoading(LeaveActivityMessageActivity.this, mAll_RL);
                    okHttpManager.getMethod(false, url + "id=" + referId, "请求请假详情", handler, 1);
                } else {//传过来的详情ID错误
                    Toast.makeText(LeaveActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        person_and_department= (TextView) findViewById(R.id.person_and_department);

        //请假类型
        mLeaveType = (TextView) findViewById(R.id.leave_type_tv);

        //开始时间
        startTime = (TextView) findViewById(R.id.leave_start_time_tv);

        //结束时间
        endTime = (TextView) findViewById(R.id.leave_end_time_tv);
        //时长
        dayTv = (TextView) findViewById(R.id.day_tv);
        //请假理由
        mReason_edit = (EditText) findViewById(R.id.edit_leave_reason);

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
                    Toast.makeText(LeaveActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LeaveActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LeaveActivityMessageActivity.this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
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
                BallProgressUtils.showLoading(LeaveActivityMessageActivity.this,mAll_RL);
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
                BallProgressUtils.showLoading(LeaveActivityMessageActivity.this,mAll_RL);
                okHttpManager.getMethod(false, agree_url + "id=" + referId+"&isAdopt="+1, "同意接口", handler, 3);
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
                BallProgressUtils.showLoading(LeaveActivityMessageActivity.this,mAll_RL);
                okHttpManager.getMethod(false, agree_url + "id=" + referId+"&isAdopt="+0, "驳回接口", handler, 3);
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
            okHttpManager.getMethod(false, url + "id=" + referId, "请求请假详情", handler, 1);
        } else {//传过来的详情ID错误
            Toast.makeText(this, "请求详情ID错误", Toast.LENGTH_SHORT).show();
        }

    }
}
