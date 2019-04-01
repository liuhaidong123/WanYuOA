package com.oa.wanyu.activity.aBusinessTravelActivity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.oa.wanyu.activity.completeActivity.CompleteActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.TravelBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//出差页面
public class AbusinessTravelActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mAll;
    private ImageView mBack;
    private ListView mListview;
    private TextView mSubmit_btn;
    private View footer;
    private TextView mAdd_Travel_btn;//添加行程
    private EditText mReson_edit;//出差理由
    private List<TravelBean> mList = new ArrayList<>();
    private TravelAdapter travelAdapter;
    private int mPosition;
    private AlertDialog.Builder mStartTimeBuilder, mEndTimeBuilder;//开始，归还时间builder
    private AlertDialog mStartTimeAlertDialog, mEndTimeAlertDialog;//开始，归还时间alertdialog
    private DatePicker datePickerEnd, datePickerStart;

    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSubmit_btn.setEnabled(true);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("3".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("4".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("5".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("6".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(AbusinessTravelActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(AbusinessTravelActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(AbusinessTravelActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abusiness_travel);
        mAll = (RelativeLayout) findViewById(R.id.activity_abusiness_travel);
        initUI();
    }


    private void initUI() {
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(this);

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_abusiness_travel;


        mSubmit_btn = (TextView) findViewById(R.id.chucha_submit);
        mSubmit_btn.setOnClickListener(this);

        mListview = (ListView) findViewById(R.id.chucha_listview_id);
        footer = LayoutInflater.from(this).inflate(R.layout.chucha_footer, null);
        mList.add(new TravelBean("", "", "", 0));
        mListview.addFooterView(footer);
        travelAdapter = new TravelAdapter();
        mListview.setAdapter(travelAdapter);
        //添加item
        mAdd_Travel_btn = footer.findViewById(R.id.add_trip);
        mAdd_Travel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                travelAdapter.addItem();
            }
        });

        mReson_edit = footer.findViewById(R.id.edit_reason);
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
        //确定，出差开始时间
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
                mList.get(mPosition).setBeginDate(year + "-" + month2 + "-" + day2);//保存开始时间数据
                checkTime(mPosition);//判断时间
                travelAdapter.notifyDataSetChanged();//更新数据
                mStartTimeAlertDialog.dismiss();
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
        //确定，出差结束时间
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
                mList.get(mPosition).setEndDate(year + "-" + month2 + "-" + day2);//保存结束时间数据
                checkTime(mPosition);//判断开始时间与结束时间
                travelAdapter.notifyDataSetChanged();//更新数据
                mEndTimeAlertDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            finish();
        } else if (id == mSubmit_btn.getId()) {

            String s = gson.toJson(mList);
            Log.e("json字符串", s);
            try {
                mSubmit_btn.setEnabled(false);
                BallProgressUtils.showLoading(AbusinessTravelActivity.this, mAll);
                String ss = URLEncoder.encode(s, "UTF-8");
                Log.e("json字符串编码之后", ss);
                Map<Object, Object> map = new HashMap<>();
                map.put("items", ss);
                map.put("cause", mReson_edit.getText().toString());
                okHttpManager.postMethod(false, url, "出差提交", map, handler, 1);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(AbusinessTravelActivity.this, "编码错误", Toast.LENGTH_SHORT).show();
                BallProgressUtils.dismisLoading();
            }
        }
    }


    class TravelAdapter extends BaseAdapter {
        public void addItem() {
            mList.add(new TravelBean("", "", "", 0));
            this.notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            mList.remove(position);
            this.notifyDataSetChanged();
        }

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
            TravelHolder travelHolder = null;

            travelHolder = new TravelHolder();
            view = LayoutInflater.from(AbusinessTravelActivity.this).inflate(R.layout.chucha_item, null);
            travelHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
            travelHolder.delete = view.findViewById(R.id.delete_btn);
            travelHolder.place = view.findViewById(R.id.edit_didian);
            travelHolder.startTime = view.findViewById(R.id.start_time_tv);
            travelHolder.endTime = view.findViewById(R.id.end_time_tv);
            travelHolder.day = view.findViewById(R.id.day_tv);

            if (mList.size() == 1) {
                travelHolder.delete.setVisibility(View.GONE);
            } else {
                travelHolder.delete.setVisibility(View.VISIBLE);
            }
            int a = i + 1;
            travelHolder.mingxi.setText("行程明细" + "(" + a + ")");
            //输入地点后，保存数据
            travelHolder.place.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    mList.get(i).setPlace("" + editable);
                }
            });
            //设置地点数据
            travelHolder.place.setText(mList.get(i).getPlace());
            travelHolder.startTime.setText(mList.get(i).getBeginDate());
            travelHolder.endTime.setText(mList.get(i).getEndDate());
            travelHolder.day.setText(mList.get(i).getDuration() + "");
            //删除
            travelHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList.size() != 1) {
                        deleteItem(i);
                    }

                }
            });
            // 开始时间
            travelHolder.startTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStartTimeAlertDialog.show();
                    mPosition = i;
                }
            });
            //结束时间
            travelHolder.endTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEndTimeAlertDialog.show();
                    mPosition = i;
                }
            });
            return view;
        }

        class TravelHolder {
            TextView mingxi, delete, startTime, endTime, day;
            EditText place;
        }
    }

    /**
     * 判断开始时间.结束时间.当前时间
     *
     * @return
     */
    private boolean checkTime(int pos) {
        if ("".equals(mList.get(pos).getBeginDate())) {
            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if ("".equals(mList.get(pos).getEndDate())) {
                Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    Date jieTimeDate = dateformat.parse(mList.get(pos).getBeginDate() + " 00:00:00");
                    Date returnTimeDate = dateformat.parse(mList.get(pos).getEndDate() + " 00:00:00");
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
                    // jieTimeDate.getTime() - currentDate.getTime() >= 0 && returnTimeDate.getTime() - currentDate.getTime() >= 0 && returnTimeDate.getTime() - jieTimeDate.getTime() >= 0
                    if (returnTimeDate.getTime() - jieTimeDate.getTime() >= 0) {//判断开始时间是否大于当前时间
                        long betweenDays = ((returnTimeDate.getTime() - jieTimeDate.getTime()) / (1000 * 60 * 60 * 24));
                        String d = String.valueOf(betweenDays + 1);
                        mList.get(pos).setDuration(Integer.valueOf(d));//设置天数
                        return true;
                    } else {
                        Toast.makeText(AbusinessTravelActivity.this, "请选择正确的时间", Toast.LENGTH_SHORT).show();
                        mList.get(pos).setDuration(0);//如果时间选择错误，天数归0
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
}
