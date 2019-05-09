package com.oa.wanyu.activity.leave;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.aBusinessTravelActivity.AbusinessTravelActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.leaveType.LeaveRoot;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//请假
public class LeaveActivity extends AppCompatActivity {
    private ImageView mBack;
    private TextView mLeaveType, startTime, endTime, dayTv, mSubmit_btn;
    private EditText mReason_edit;
    private AlertDialog.Builder mLeaveTypeBuilder, mStartTimeBuilder, mEndTimeBuilder;//开始，归还时间builder
    private AlertDialog mLeaveTypeAlertDialog, mStartTimeAlertDialog, mEndTimeAlertDialog;//开始，归还时间alertdialog
    private DatePicker datePickerEnd, datePickerStart;

    private ListView mLeave_ListView;
    private LeaveAdapter leaveAdapter;
    private List<String> mLeaveList = new ArrayList<>();

    private RelativeLayout mAll;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String urlType, urlSubmit;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            mSubmit_btn.setEnabled(true);
            if (msg.what == 1) {//提交
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("3".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("4".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("5".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(LeaveActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(LeaveActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 1010) {
                //数据错误
                Toast.makeText(LeaveActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {//请假类型
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, LeaveRoot.class);
                    if (o != null && o instanceof LeaveRoot) {
                        LeaveRoot leaveRoot = (LeaveRoot) o;
                        if ("0".equals(leaveRoot.getCode())&&leaveRoot != null) {
                            if (leaveRoot.getLeaveType() != null) {
                                mLeaveList = leaveRoot.getLeaveType();
                                leaveAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(LeaveActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LeaveActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        mAll = (RelativeLayout) findViewById(R.id.activity_leave);
        initUI();
    }

    private void initUI() {
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okHttpManager = OkHttpManager.getInstance();
        urlSubmit = URLTools.urlBase + URLTools.apply_leave;//提交请假
        urlType = URLTools.urlBase + URLTools.leave_type;//请假类型
        okHttpManager.getMethod(false, urlType, "请假类型", handler, 2);

        mLeaveTypeBuilder = new AlertDialog.Builder(this);
        mLeaveTypeAlertDialog = mLeaveTypeBuilder.create();
        View l_alert = LayoutInflater.from(this).inflate(R.layout.leave_alert_listview, null);
        mLeaveTypeAlertDialog.setView(l_alert);
        mLeave_ListView = l_alert.findViewById(R.id.leave_listview_id);
        leaveAdapter = new LeaveAdapter();
        mLeave_ListView.setAdapter(leaveAdapter);
        mLeave_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mLeaveType.setText(mLeaveList.get(i));
                mLeaveTypeAlertDialog.dismiss();
            }
        });
        //请假类型
        mLeaveType = (TextView) findViewById(R.id.leave_type_tv);
        mLeaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLeaveList.size()==0){
                    Toast.makeText(LeaveActivity.this, "正在请求数据，请稍等。。。", Toast.LENGTH_SHORT).show();

                    okHttpManager.getMethod(false, urlType, "请假类型", handler, 2);
                }else {
                    mLeaveTypeAlertDialog.show();
                }

            }
        });

        //开始时间
        startTime = (TextView) findViewById(R.id.leave_start_time_tv);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimeAlertDialog.show();
            }
        });
        //结束时间
        endTime = (TextView) findViewById(R.id.leave_end_time_tv);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndTimeAlertDialog.show();
            }
        });
        //时长
        dayTv = (TextView) findViewById(R.id.day_tv);
        //请假理由
        mReason_edit = (EditText) findViewById(R.id.edit_leave_reason);
        //  提交
        mSubmit_btn = (TextView) findViewById(R.id.leave_submit);
        mSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(mLeaveType.getText().toString())) {
                    if (checkTime()) {
                        if (!"".equals(mReason_edit.getText().toString())) {

                            mSubmit_btn.setEnabled(false);
                            BallProgressUtils.showLoading(LeaveActivity.this, mAll);
                            Map<Object, Object> map = new HashMap<Object, Object>();
                            map.put("typ", mLeaveType.getText().toString());
                            map.put("beginDate", startTime.getText().toString());
                            map.put("endDate", endTime.getText().toString());
                            map.put("duration", dayTv.getText().toString());
                            map.put("reason", mReason_edit.getText().toString());

                            okHttpManager.postMethod(false, urlSubmit, "提交请假", map, handler, 1);
                        } else {
                            Toast.makeText(LeaveActivity.this, "请填写请假理由", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(LeaveActivity.this, "请选择请假类型", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //开始时间弹框
        mStartTimeBuilder = new AlertDialog.Builder(this);
        mStartTimeAlertDialog = mStartTimeBuilder.create();

        View startView = LayoutInflater.from(this).inflate(R.layout.select_start_time, null);
        mStartTimeAlertDialog.setView(startView);
        datePickerStart = startView.findViewById(R.id.start_datePicker);
        TextView cancleStartTimeBtn = startView.findViewById(R.id.cancle_btn);
        TextView sureStartTimeBtn = startView.findViewById(R.id.sure_btn);
        cancleStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimeAlertDialog.dismiss();
            }
        });
        //确定，请假开始时间
        sureStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePickerStart.getYear();
                int month = datePickerStart.getMonth() + 1;

                String month2 = "";
                if (month < 10) {
                    month2 = "0" + month;
                } else {
                    month2 = month + "";
                }
                int day = datePickerStart.getDayOfMonth();
                String day2 = "";
                if (day < 10) {
                    day2 = "0" + day;
                } else {
                    day2 = day + "";
                }
                //
                startTime.setText(year + "-" + month2 + "-" + day2);
                mStartTimeAlertDialog.dismiss();
                checkTime();
            }
        });


        //结束时间弹框
        mEndTimeBuilder = new AlertDialog.Builder(this);
        mEndTimeAlertDialog = mEndTimeBuilder.create();
        View endView = LayoutInflater.from(this).inflate(R.layout.select_end_time, null);
        mEndTimeAlertDialog.setView(endView);
        datePickerEnd = endView.findViewById(R.id.end_datePicker);
        TextView cancleEndTimeBtn = endView.findViewById(R.id.cancle_btn);
        TextView sureEndTimeBtn = endView.findViewById(R.id.sure_btn);
        cancleEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndTimeAlertDialog.dismiss();
            }
        });
        //确定，请假结束时间
        sureEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePickerEnd.getYear();
                int month = datePickerEnd.getMonth() + 1;
                String month2 = "";
                if (month < 10) {
                    month2 = "0" + month;
                } else {
                    month2 = month + "";
                }
                int day = datePickerEnd.getDayOfMonth();
                String day2 = "";
                if (day < 10) {
                    day2 = "0" + day;
                } else {
                    day2 = day + "";
                }
                endTime.setText(year + "-" + month2 + "-" + day2);
                mEndTimeAlertDialog.dismiss();

                checkTime();
            }
        });

    }

    /**
     * 判断开始时间.结束时间.当前时间
     *
     * @return
     */
    private boolean checkTime() {
        if ("".equals(startTime.getText().toString())) {
            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if ("".equals(endTime.getText().toString())) {
                Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    Date startDate = dateformat.parse(startTime.getText().toString() + " 00:00:00");
                    Date endDate = dateformat.parse(endTime.getText().toString() + " 00:00:00");
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    String month2 = "";
                    if (month < 10) {
                        month2 = "0" + month;
                    } else {
                        month2 = month + "";
                    }
                    String day2 = "";
                    if (day < 10) {
                        day2 = "0" + day;
                    } else {
                        day2 = day + "";
                    }

                    Date currentDate = dateformat.parse(year + "-" + month2 + "-" + day2 + " 00:00:00");
                    //开始时间必须大于等于当前时间，结束时间必须大于当前时间，结束时间必须大于等于开始时间
                    if (startDate.getTime() - currentDate.getTime() >= 0 && endDate.getTime() - currentDate.getTime() >= 0 && endDate.getTime() - startDate.getTime() >= 0) {//判断开始时间是否大于当前时间
                        long betweenDays = ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
                        dayTv.setText(betweenDays + 1 + "");//设置天数
                        return true;
                    } else {
                        Toast.makeText(LeaveActivity.this, "请选择正确的时间", Toast.LENGTH_SHORT).show();
                        dayTv.setText("0");//如果时间选择错误，天数归0
                        return false;
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "时间解析错误", Toast.LENGTH_SHORT).show();
                }

                return false;

            }
        }


    }

    //请假类型适配器
    class LeaveAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLeaveList.size();
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
            LeaveHolder leaveHolder = new LeaveHolder();
            view = LayoutInflater.from(LeaveActivity.this).inflate(R.layout.leave_item, null);
            leaveHolder.leave_tv = view.findViewById(R.id.leave_);
            leaveHolder.leave_tv.setText(mLeaveList.get(i));

            return view;
        }

        class LeaveHolder {
            TextView leave_tv;
        }
    }
}
