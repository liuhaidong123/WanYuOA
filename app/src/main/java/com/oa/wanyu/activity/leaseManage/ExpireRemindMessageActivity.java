package com.oa.wanyu.activity.leaseManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.aBusinessTravelActivity.AbusinessTravelActivity;
import com.oa.wanyu.bean.ShopsMessageBean;
import com.oa.wanyu.bean.ShopsMessageRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

//到期提醒详情
public class ExpireRemindMessageActivity extends AppCompatActivity {
    private TextView title_name, location_mess, type_mess, floor_mess, area_mess, status_mess, person_name_mess, person_phone_mess, price_mess, money_mess, ya_money_mess, start_time_mess, end_time_mess, select_history_btn;
    private TextView sure_btn;
    private ImageView mback_img;
    private RelativeLayout mAll, no_data_rl;
    private TextView no_mess_tv;
    private GridView mGridView;
    private PictureAdapter pictureAdapter;
    private List<String> imgList = new ArrayList<>();

    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url, sure_url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //sure_btn.setEnabled(true);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ShopsMessageRoot.class);
                    if (o != null && o instanceof ShopsMessageRoot) {
                        ShopsMessageRoot shopsMessageRoot = (ShopsMessageRoot) o;
                        if (shopsMessageRoot != null) {
                            if ("0".equals(shopsMessageRoot.getCode())) {
                                ShopsMessageBean shopsMessageBean = shopsMessageRoot.getShop();
                                if (shopsMessageBean != null) {
                                    no_data_rl.setVisibility(View.GONE);
                                    no_mess_tv.setText("");
                                    if (!"".equals(shopsMessageBean.getBuildingName())) {
                                        title_name.setText(shopsMessageBean.getBuildingName() + shopsMessageBean.getRoomNum());
                                    } else {
                                        title_name.setText("未获取商铺名称");
                                    }

                                    if (!"".equals(shopsMessageBean.getAddress().trim())) {
                                        location_mess.setText(shopsMessageBean.getAddress() + "");
                                    } else {
                                        location_mess.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getHouseType().trim())) {
                                        type_mess.setText(shopsMessageBean.getHouseType() + "");
                                    } else {
                                        type_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getFloor())) {
                                        floor_mess.setText(shopsMessageBean.getFloor() + "");
                                    } else {
                                        floor_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getArea())) {
                                        area_mess.setText(shopsMessageBean.getArea() + "平米");
                                    } else {
                                        area_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getStateText())) {
                                        status_mess.setText(shopsMessageBean.getStateText() + "");
                                    } else {
                                        status_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getNam())) {
                                        person_name_mess.setText(shopsMessageBean.getNam() + "");
                                    } else {
                                        person_name_mess.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getTell())) {
                                        person_phone_mess.setText(shopsMessageBean.getTell() + "");
                                    } else {
                                        person_phone_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getPrice())) {
                                        price_mess.setText(shopsMessageBean.getPrice() + "元/天/平米");
                                    } else {
                                        price_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getRental())) {
                                        money_mess.setText(shopsMessageBean.getRental() + "元");
                                    } else {
                                        money_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getDeposit())) {
                                        ya_money_mess.setText(shopsMessageBean.getDeposit() + "元");
                                    } else {
                                        ya_money_mess.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getBeginDateString())) {
                                        start_time_mess.setText(shopsMessageBean.getBeginDateString() + "");
                                    } else {
                                        start_time_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getEndDateString())) {
                                        end_time_mess.setText(shopsMessageBean.getEndDateString() + "");
                                    } else {
                                        end_time_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getContract())) {
                                        String[] strings = shopsMessageBean.getContract().split(";");
                                        for (int i = 0; i < strings.length; i++) {
                                            imgList.add(strings[i]);
                                        }
                                        Log.e("图片几张", imgList.size() + "");
                                        pictureAdapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Toast.makeText(ExpireRemindMessageActivity.this, "商铺信息错误", Toast.LENGTH_SHORT).show();
                                    no_data_rl.setVisibility(View.VISIBLE);
                                    no_mess_tv.setText("商铺信息错误，请刷新");
                                }


                            } else if ("-1".equals(shopsMessageRoot.getCode())) {
                                Toast.makeText(ExpireRemindMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(ExpireRemindMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else if (msg.what == 2) {//确认交租
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(ExpireRemindMessageActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, getIntent());
                                finish();

                            } else if ("-1".equals(successBean.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(ExpireRemindMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(ExpireRemindMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(ExpireRemindMessageActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    private long id;
    private ImageView show_img;
    private AlertDialog.Builder mStartTimeBuilder, mEndTimeBuilder, myBuilder;//开始，结束时间builder
    private AlertDialog mStartTimeAlertDialog, mEndTimeAlertDialog, myAlertDialog;//开始，结束时间alertdialog
    private DatePicker datePickerEnd, datePickerStart;
    private EditText sure_money;
    private TextView sure_start_time, sure_end_time;
    private int point = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire_remind_message);
        title_name = (TextView) findViewById(R.id.sq_title);
        location_mess = (TextView) findViewById(R.id.location_tv_mess);
        type_mess = (TextView) findViewById(R.id.floor_type_tv_mess);
        floor_mess = (TextView) findViewById(R.id.floor_num_tv_mess);
        area_mess = (TextView) findViewById(R.id.area_tv_mess);
        status_mess = (TextView) findViewById(R.id.status_tv_mess);
        person_name_mess = (TextView) findViewById(R.id.name_tv_mess);
        person_phone_mess = (TextView) findViewById(R.id.phone_tv_mess);
        price_mess = (TextView) findViewById(R.id.price_tv_mess);
        money_mess = (TextView) findViewById(R.id.price_all_tv_mess);
        ya_money_mess = (TextView) findViewById(R.id.money_ya_tv_mess);
        start_time_mess = (TextView) findViewById(R.id.start_time_tv_mess);
        end_time_mess = (TextView) findViewById(R.id.end_time_tv_mess);

        select_history_btn = (TextView) findViewById(R.id.select_history_btn);
        select_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != -1) {
                    Intent intent = new Intent(ExpireRemindMessageActivity.this, LeaseHistoryActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    Toast.makeText(ExpireRemindMessageActivity.this, "暂无商铺详情", Toast.LENGTH_SHORT).show();
                }

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
        mGridView = (GridView) findViewById(R.id.lease_gridview_id);
        pictureAdapter = new PictureAdapter();
        mGridView.setAdapter(pictureAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                show_img.setVisibility(View.VISIBLE);
                com.squareup.picasso.Picasso.with(ExpireRemindMessageActivity.this).load(URLTools.urlBase + imgList.get(i)).into(show_img);
            }
        });
        okHttpManager = OkHttpManager.getInstance();
        sure_url=URLTools.urlBase + URLTools.sure_money;//确认交租接口
        id = getIntent().getLongExtra("id", -1);
        url = URLTools.urlBase + URLTools.shops_message + "id=" + id;//详情接口
        if (id != -1) {
            okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
        } else {
            Toast.makeText(this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
        }

        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id != -1) {
                    BallProgressUtils.showLoading(ExpireRemindMessageActivity.this, mAll);
                    okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
                } else {
                    Toast.makeText(ExpireRemindMessageActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll = (RelativeLayout) findViewById(R.id.activity_expire_remind_message);

        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        //确认交租
        sure_btn = (TextView) findViewById(R.id.remind_submit);
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAlertDialog.show();
            }
        });

        myBuilder = new AlertDialog.Builder(this);
        myBuilder.setCancelable(false);
        myAlertDialog = myBuilder.create();
        View sureView = LayoutInflater.from(this).inflate(R.layout.remind_sure_alert, null);
        myAlertDialog.setView(sureView);
        sure_money = sureView.findViewById(R.id.edit_money);
        sure_start_time = sureView.findViewById(R.id.start_time_tv);
        sure_end_time = sureView.findViewById(R.id.end_time_tv);
        TextView  cancle= sureView.findViewById(R.id.cancle_btn);
        TextView sure = sureView.findViewById(R.id.sure_btn);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交确认交租接口
                if (checkTime()){
                    BallProgressUtils.showLoading(ExpireRemindMessageActivity.this,mAll);
                    okHttpManager.getMethod(false, sure_url+"id="+id+"&lastBeginDate="+sure_start_time.getText().toString().trim()+"&lastEndDate="+sure_end_time.getText().toString().trim()+"&lastPaidIn="+sure_money.getText().toString().trim(), "提交确认交租接口", handler, 2);
                    myAlertDialog.dismiss();
                }

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAlertDialog.dismiss();
            }
        });

        //        租金
        sure_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > point) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + point + 1);
                        sure_money.setText(s);
                        sure_money.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    sure_money.setText(s);
                    sure_money.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        sure_money.setText(s.subSequence(0, 1));
                        sure_money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                sure_start_time.setText(year + "-" + month2 + "-" + day2);
                //checkTime();//判断时间
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
                sure_end_time.setText(year + "-" + month2 + "-" + day2);
               // checkTime();//判断开始时间与结束时间
                mEndTimeAlertDialog.dismiss();
            }
        });

        sure_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimeAlertDialog.show();
            }
        });

        sure_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndTimeAlertDialog.show();
            }
        });
    }

    /**
     * 判断开始时间.结束时间.当前时间
     *
     * @return
     */
    private boolean checkTime() {

        if ("".equals(sure_money.getText().toString().trim())){
            Toast.makeText(this, "请输入租金", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if ("".equals(sure_start_time.getText().toString().trim())) {
                Toast.makeText(this, "请选择开始日期", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if ("".equals(sure_end_time.getText().toString().trim())) {
                    Toast.makeText(this, "请选择结束日期", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {

                        Date jieTimeDate = dateformat.parse(sure_start_time.getText().toString().trim() + " 00:00:00");
                        Date returnTimeDate = dateformat.parse(sure_end_time.getText().toString().trim() + " 00:00:00");

                        if (returnTimeDate.getTime() - jieTimeDate.getTime() > 0) {//判断开始日期是否大于结束日期

                            return true;
                        } else {

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

    class PictureAdapter extends BaseAdapter {

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
            PHMyolder pHolder = null;
            if (view == null) {
                pHolder = new PHMyolder();
                view = LayoutInflater.from(ExpireRemindMessageActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
                pHolder.imageView = view.findViewById(R.id.picture);
                view.setTag(pHolder);
            } else {
                pHolder = (PHMyolder) view.getTag();
            }
            Picasso.with(ExpireRemindMessageActivity.this).load(URLTools.urlBase + imgList.get(i)).error(R.mipmap.errorpicture).into(pHolder.imageView);

            return view;
        }

        class PHMyolder {
            ImageView imageView;
        }
    }

}
