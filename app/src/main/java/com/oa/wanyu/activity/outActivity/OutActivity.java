package com.oa.wanyu.activity.outActivity;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.leave.LeaveActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//外出
public class OutActivity extends AppCompatActivity {

    private ImageView mback;
    private TextView startTime, endTime, outTime, out_submit;
    private EditText out_reason;

    private AlertDialog.Builder mStartTimeBuilder, mEndTimeBuilder;//开始，结束时间builder
    private AlertDialog mStartTimeAlertDialog, mEndTimeAlertDialog;//开始，结束时间
    private TimePicker startTimepiker, endTimepicker;
    private int year, month, day;
    private String month2, day2;
    private RelativeLayout mAll;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            out_submit.setEnabled(true);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("3".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("4".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("5".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("6".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(OutActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(OutActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(OutActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_out;
        mAll = (RelativeLayout) findViewById(R.id.activity_out);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (month < 10) {
            month2 = "0" + month;
        } else {
            month2 = month + "";
        }

        if (day < 10) {
            day2 = "0" + day;
        } else {
            day2 = day + "";
        }
        initUI();
    }

    private void initUI() {
        mback = (ImageView) findViewById(R.id.back_img);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //开始时间
        startTime = (TextView) findViewById(R.id.out_start_time_tv);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimeAlertDialog.show();
            }
        });
        mStartTimeBuilder = new AlertDialog.Builder(this);
        mStartTimeAlertDialog = mStartTimeBuilder.create();
        View start_view = LayoutInflater.from(this).inflate(R.layout.out_start_time_alert, null);
        mStartTimeAlertDialog.setView(start_view);
        TextView sure = start_view.findViewById(R.id.sure_btn);
        TextView cancle = start_view.findViewById(R.id.cancle_btn);
        startTimepiker = start_view.findViewById(R.id.out_start_timepicker);
        startTimepiker.setIs24HourView(true);
        sure.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int hour = startTimepiker.getHour();
                int minite = startTimepiker.getMinute();
                String hour2 = "";
                if (hour < 10) {
                    if (hour == 0) {
                        hour2 = "00";
                    } else {
                        hour2 = "0" + hour;
                    }

                } else {
                    hour2 = hour + "";
                }
                String minite2 = "";
                if (minite < 10) {
                    if (minite == 0) {
                        minite2 = "00";
                    } else {
                        minite2 = "0" + minite;
                    }

                } else {
                    minite2 = minite + "";
                }

                startTime.setText(year + "-" + month2 + "-" + day2 + " " + hour2 + ":" + minite2 + ":00");
                mStartTimeAlertDialog.dismiss();
                checkTime();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimeAlertDialog.dismiss();
            }
        });

        //结束时间
        endTime = (TextView) findViewById(R.id.out_end_time_tv);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndTimeAlertDialog.show();
            }
        });

        mEndTimeBuilder = new AlertDialog.Builder(this);
        mEndTimeAlertDialog = mEndTimeBuilder.create();
        View end_view = LayoutInflater.from(this).inflate(R.layout.out_end_time_alert, null);
        mEndTimeAlertDialog.setView(end_view);
        TextView sure2 = end_view.findViewById(R.id.sure2_btn);
        TextView cancle2 = end_view.findViewById(R.id.cancle2_btn);
        endTimepicker = end_view.findViewById(R.id.out_end_timepicker);
        endTimepicker.setIs24HourView(true);
        sure2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int hour = endTimepicker.getHour();
                int minite = endTimepicker.getMinute();
                String hour2 = "";
                if (hour < 10) {
                    if (hour == 0) {
                        hour2 = "00";
                    } else {
                        hour2 = "0" + hour;
                    }

                } else {
                    hour2 = hour + "";
                }
                String minite2 = "";
                if (minite < 10) {
                    if (minite == 0) {
                        minite2 = "00";
                    } else {
                        minite2 = "0" + minite;
                    }

                } else {
                    minite2 = minite + "";
                }
                endTime.setText(year + "-" + month2 + "-" + day2 + " " + hour2 + ":" + minite2 + ":00");
                mEndTimeAlertDialog.dismiss();
                checkTime();
            }
        });
        cancle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndTimeAlertDialog.dismiss();
            }
        });
        //外出时间
        outTime = (TextView) findViewById(R.id.time_tv);

        out_submit = (TextView) findViewById(R.id.out_submit);
        out_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(out_reason.getText().toString())) {
                    if (checkTime()) {
                        BallProgressUtils.showLoading(OutActivity.this, mAll);
                        out_submit.setEnabled(false);
                        Map<Object, Object> map = new HashMap<Object, Object>();
                        map.put("beginDate", startTime.getText().toString());
                        map.put("endDate", endTime.getText().toString());
                        map.put("duration", outTime.getText().toString());
                        map.put("reason", out_reason.getText().toString());

                        okHttpManager.postMethod(false, url, "提交外出", map, handler, 1);
                    }
                } else {
                    Toast.makeText(OutActivity.this, "请填写外出理由", Toast.LENGTH_SHORT).show();
                }
            }
        });

        out_reason = (EditText) findViewById(R.id.edit_out_reason);
    }


    private boolean checkTime() {

        if ("".equals(startTime.getText().toString())) {
            Toast.makeText(this, "请选择外出开始时间", Toast.LENGTH_SHORT).show();
        } else {

            if ("".equals(endTime.getText().toString())) {
                Toast.makeText(this, "请选择外出结束时间", Toast.LENGTH_SHORT).show();
            } else {

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date startT = dateformat.parse(startTime.getText().toString());
                    Date endT = dateformat.parse(endTime.getText().toString());

                    if (startT.getTime() - System.currentTimeMillis() > 0) {
                        if (endT.getTime() - System.currentTimeMillis() > 0) {

                            if (endT.getTime() - startT.getTime() > 0) {
                                double betweenHours = ((endT.getTime() - startT.getTime()) / (1000 * 60 * 60));
                                if ((endT.getTime() - startT.getTime()) % (1000 * 60 * 60) > 0) {
                                    outTime.setText(betweenHours + 1 + "");//设置时间
                                } else {
                                    outTime.setText(betweenHours + "");//设置时间
                                }

                                return true;
                            } else {
                                Toast.makeText(OutActivity.this, "结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
                                outTime.setText("0");//如果时间选择错误，时间归0
                                return false;
                            }
                        } else {
                            Toast.makeText(OutActivity.this, "结束时间必须大于当前时间", Toast.LENGTH_SHORT).show();
                            outTime.setText("0");//如果时间选择错误，时间归0
                            return false;
                        }

                    } else {
                        Toast.makeText(OutActivity.this, "开始时间必须大于当前时间", Toast.LENGTH_SHORT).show();
                        outTime.setText("0");//如果时间选择错误，时间归0
                        return false;
                    }


//                    if (startT.getTime() - System.currentTimeMillis() > 0 && endT.getTime() - System.currentTimeMillis() > 0 && endT.getTime() - startT.getTime() > 0) {
//                        double betweenHours = ((endT.getTime() - startT.getTime()) / (1000 * 60 * 60));
//                        outTime.setText(betweenHours + 1 + "");//设置时间
//                        return true;
//                    } else {
//                        Toast.makeText(OutActivity.this, "请选择正确的时间", Toast.LENGTH_SHORT).show();
//                        outTime.setText("0");//如果时间选择错误，时间归0
//                        return false;
//                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "时间解析错误", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return false;
    }
}
