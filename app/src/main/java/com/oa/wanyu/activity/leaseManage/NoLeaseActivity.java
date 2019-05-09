package com.oa.wanyu.activity.leaseManage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivity;
import com.oa.wanyu.activity.reimbursementActivity.SelectPictureActivity;
import com.oa.wanyu.bean.ShopsMessageBean;
import com.oa.wanyu.bean.ShopsMessageRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CheckPhone;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.picasso.Picasso;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

//未租详情
public class NoLeaseActivity extends AppCompatActivity {
    private RelativeLayout mAll_Rl;
    private ImageView mback_img, end_time_img_btn, start_time_img_btn, mBig_img;
    private TextView title_name, location_mess, type_mess, floor_mess, area_mess, status_mess, sure_lease_btn;
    private EditText edit_name, edit_phone, edit_price, edit_money, edit_ya_money, start_time, end_time;
    private AlertDialog.Builder mStartTimeBuilder, mEndTimeBuilder, camrea_builder;//开始，截止时间builder
    private AlertDialog mStartTimeAlertDialog, mEndTimeAlertDialog, camera_alertdialog;//开始，截止时间alertdialog
    private DatePicker datePickerEnd, datePickerStart;
    private GridView gridView;
    private ImgAdapter imgAdapter;
    private ArrayList<String> imgList = new ArrayList<>();
    private List<File> fileList = new ArrayList<>();
    private int ScreenWith, ScreenHeight;
    private File mediaFile;
    private Uri fileUri;
    private int point = 2;
    private int flag = -1;//1是拍照，2是相册
    private OkHttpManager okHttpManager;
    private String url, sure_url;
    private long id;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sure_lease_btn.setEnabled(true);
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
                                        edit_name.setText(shopsMessageBean.getNam() + "");
                                    } else {
                                        edit_name.setText("");
                                    }
                                    if (!"".equals(shopsMessageBean.getTell())) {
                                        edit_phone.setText(shopsMessageBean.getTell() + "");
                                    } else {
                                        edit_phone.setText("");
                                    }

                                    if (!"".equals(shopsMessageBean.getPrice())) {
                                        edit_price.setText(shopsMessageBean.getPrice());
                                    } else {
                                        edit_price.setText("");
                                    }

                                    if (!"".equals(shopsMessageBean.getRental())) {
                                        edit_money.setText(shopsMessageBean.getRental());
                                    } else {
                                        edit_money.setText("");
                                    }

                                    if (!"".equals(shopsMessageBean.getDeposit())) {
                                        edit_ya_money.setText(shopsMessageBean.getDeposit());
                                    } else {
                                        edit_ya_money.setText("");
                                    }
                                    if (!"".equals(shopsMessageBean.getBeginDateString())) {
                                        start_time.setText(shopsMessageBean.getBeginDateString() + "");
                                    } else {
                                        start_time.setText("");
                                    }

                                    if (!"".equals(shopsMessageBean.getEndDateString())) {
                                        end_time.setText(shopsMessageBean.getEndDateString() + "");
                                    } else {
                                        end_time.setText("");
                                    }


                                } else {
                                    Toast.makeText(NoLeaseActivity.this, "商铺信息错误", Toast.LENGTH_SHORT).show();

                                }


                            } else if ("-1".equals(shopsMessageRoot.getCode())) {
                                Toast.makeText(NoLeaseActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(NoLeaseActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();

                }

            } else if (msg.what == 2) {//提交确认出租/变更
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(NoLeaseActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK,getIntent());
                                finish();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(NoLeaseActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(NoLeaseActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 1010) {
                //数据错误
                Toast.makeText(NoLeaseActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_lease);

        mAll_Rl = (RelativeLayout) findViewById(R.id.activity_no_lease);
        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBig_img = (ImageView) findViewById(R.id.big_img);//显示合同大图片
        mBig_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBig_img.setVisibility(View.GONE);
            }
        });


        ScreenWith = getResources().getDisplayMetrics().widthPixels;
        ScreenHeight = getResources().getDisplayMetrics().heightPixels;
        okHttpManager = OkHttpManager.getInstance();
        id = getIntent().getLongExtra("id", -1);
        sure_url = URLTools.urlBase + URLTools.sign_sell;
        url = URLTools.urlBase + URLTools.shops_message + "id=" + id;
        if (id != -1) {
            okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
        } else {
            Toast.makeText(this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
        }

        title_name = (TextView) findViewById(R.id.sq_title);
        location_mess = (TextView) findViewById(R.id.location_tv_mess);
        type_mess = (TextView) findViewById(R.id.floor_type_tv_mess);
        floor_mess = (TextView) findViewById(R.id.floor_num_tv_mess);
        area_mess = (TextView) findViewById(R.id.area_tv_mess);
        status_mess = (TextView) findViewById(R.id.status_tv_mess);
        //确定出租，更新数据
        sure_lease_btn = (TextView) findViewById(R.id.sure_lease_btn);
        sure_lease_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(edit_name.getText().toString().trim())) {
                    Toast.makeText(NoLeaseActivity.this, "请填写租房人姓名", Toast.LENGTH_SHORT).show();
                } else {
                    if ("".equals(edit_phone.getText().toString().trim())) {
                        Toast.makeText(NoLeaseActivity.this, "请填写租房人电话", Toast.LENGTH_SHORT).show();
                    } else {
                        if (CheckPhone.checkPhone(edit_phone.getText().toString().trim())) {
                            if ("".equals(edit_price.getText().toString().trim())) {
                                Toast.makeText(NoLeaseActivity.this, "请填写单价", Toast.LENGTH_SHORT).show();
                            } else {
                                if ("".equals(edit_money.getText().toString().trim())) {
                                    Toast.makeText(NoLeaseActivity.this, "请填写租金", Toast.LENGTH_SHORT).show();
                                } else {
                                    if ("".equals(edit_ya_money.getText().toString().trim())) {
                                        Toast.makeText(NoLeaseActivity.this, "请填写押金", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (checkTime()) {//判断起租日期和截止日期
                                            //
                                            if (id != -1) {
                                                sure_lease_btn.setEnabled(false);
                                                BallProgressUtils.showLoading(NoLeaseActivity.this, mAll_Rl);
                                                Map<Object, Object> map = new HashMap<>();
                                                map.put("state", "40");
                                                map.put("stateText", "已租");
                                                map.put("nam", edit_name.getText().toString().trim());
                                                map.put("tell", edit_phone.getText().toString().trim());
                                                map.put("price", edit_price.getText().toString().trim());
                                                map.put("rental", edit_money.getText().toString().trim());
                                                map.put("deposit", edit_ya_money.getText().toString().trim());
                                                map.put("beginDate", start_time.getText().toString().trim());
                                                map.put("endDate", end_time.getText().toString().trim());
                                                okHttpManager.postFileMethod(sure_url + "id=" + id+"&", "确定出租/变更", map, fileList, handler, 2);
                                            } else {
                                                Toast.makeText(NoLeaseActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(NoLeaseActivity.this, "电话错误，请重新填写", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });


        edit_name = (EditText) findViewById(R.id.name_edit);
        edit_phone = (EditText) findViewById(R.id.phone_edit);
//        单价
        edit_price = (EditText) findViewById(R.id.price_edit);
        edit_price.addTextChangedListener(new TextWatcher() {
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
                        edit_price.setText(s);
                        edit_price.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edit_price.setText(s);
                    edit_price.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edit_price.setText(s.subSequence(0, 1));
                        edit_price.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        租金
        edit_money = (EditText) findViewById(R.id.money_edit);
        edit_money.addTextChangedListener(new TextWatcher() {
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
                        edit_money.setText(s);
                        edit_money.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edit_money.setText(s);
                    edit_money.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edit_money.setText(s.subSequence(0, 1));
                        edit_money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        押金
        edit_ya_money = (EditText) findViewById(R.id.ya_money_edit);
        edit_ya_money.addTextChangedListener(new TextWatcher() {
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
                        edit_ya_money.setText(s);
                        edit_ya_money.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edit_ya_money.setText(s);
                    edit_ya_money.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edit_ya_money.setText(s.subSequence(0, 1));
                        edit_ya_money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        start_time = (EditText) findViewById(R.id.start_time_mess);
        end_time = (EditText) findViewById(R.id.end_time_mess);
        end_time_img_btn = (ImageView) findViewById(R.id.end_time_img);
        end_time_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndTimeAlertDialog.show();
            }
        });
        start_time_img_btn = (ImageView) findViewById(R.id.start_time_img);
        start_time_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimeAlertDialog.show();
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
                start_time.setText(year + "-" + month2 + "-" + day2);
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
        //确定时间
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
                end_time.setText(year + "-" + month2 + "-" + day2);
                mEndTimeAlertDialog.dismiss();

                checkTime();
            }
        });

        gridView = (GridView) findViewById(R.id.lease_gridview_id);
        imgAdapter = new ImgAdapter();
        gridView.setAdapter(imgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (imgList.size() == 0 || i == imgList.size()) {//跳转相册
                    // checkPermission();
                    camera_alertdialog.show();
                } else {//显示图片大图
                    mBig_img.setVisibility(View.VISIBLE);
                    Picasso.with(NoLeaseActivity.this).load(imgList.get(i)).into(mBig_img);
                }
            }
        });


        camrea_builder = new AlertDialog.Builder(this);
        camera_alertdialog = camrea_builder.create();
        Window window = camera_alertdialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        View camrea_view = LayoutInflater.from(this).inflate(R.layout.camera_view, null);
        camera_alertdialog.setView(camrea_view);
        TextView camera_btn = camrea_view.findViewById(R.id.camrea_btn);
        TextView photo_btn = camrea_view.findViewById(R.id.photo_btn);
        //跳转拍照
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                if (imgList.size() >= 3) {
                    Toast.makeText(NoLeaseActivity.this, "图片已达到3张，若想上传其他照片，请删除原有照片", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermission();
                    camera_alertdialog.dismiss();
                }

            }
        });
        //跳转相册
        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 2;
                checkPermission();
                camera_alertdialog.dismiss();
            }
        });


    }

    /**
     * 判断开始时间.结束时间.当前时间
     *
     * @return
     */
    private boolean checkTime() {
        if ("".equals(start_time.getText().toString())) {
            Toast.makeText(this, "请选择起租日期", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if ("".equals(end_time.getText().toString())) {
                Toast.makeText(this, "请选择截止日期", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    Date jieTimeDate = dateformat.parse(start_time.getText().toString() + " 00:00:00");
                    Date returnTimeDate = dateformat.parse(end_time.getText().toString() + " 00:00:00");
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
                        return true;
                    } else {
                        Toast.makeText(NoLeaseActivity.this, "出租截止日期小于起租日期", Toast.LENGTH_SHORT).show();
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


    //图片适配器
    class ImgAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgList.size() + 1;
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

            PHolder pHolder = new PHolder();
            view = LayoutInflater.from(NoLeaseActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
            pHolder.imageView = view.findViewById(R.id.picture);
            pHolder.cha_img = view.findViewById(R.id.cha_img);

            //没没有图片时，第一个图片为加号//有图片时，最后一个图片为加号
            if (imgList.size() == 0 || imgList.size() == i) {
                pHolder.imageView.setImageResource(R.mipmap.add_picture);
                pHolder.cha_img.setVisibility(View.GONE);
            } else {
                pHolder.cha_img.setVisibility(View.VISIBLE);
                Picasso.with(NoLeaseActivity.this).load(imgList.get(i)).error(R.mipmap.errorpicture).into(pHolder.imageView);
            }
            pHolder.cha_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgList.remove(i);
                    notifyDataSetChanged();
                }
            });

            return view;
        }

        class PHolder {
            ImageView imageView, cha_img;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int p = ContextCompat.checkSelfPermission(NoLeaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (p != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NoLeaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                return;
            } else {
                if (flag == 1) {
                    intentCamera();
                } else {
                    intentAlbum();
                }

            }

        } else {
            if (flag == 1) {
                intentCamera();
            } else {
                intentAlbum();
            }
        }
    }

    //调出相册
    private void intentAlbum() {

        Intent intent = new Intent(NoLeaseActivity.this, SelectPictureActivity.class);
        intent.putStringArrayListExtra("img_list", imgList);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {//相册
            if (data != null) {
                ArrayList<String> list = data.getStringArrayListExtra("imgList");

                if (list != null) {
                    imgList = list;
                    Log.e("返回图片多少", list.size() + "");
                    imgAdapter.notifyDataSetChanged();
                    fileList.clear();
                    getFiles();
                } else {
                    Toast.makeText(NoLeaseActivity.this, "图片无效", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(NoLeaseActivity.this, "数据无效", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 1) {//拍照
            if (data != null) {
                if (data.hasExtra("data")) {
                    //Bitmap thumbnail = data.getParcelableExtra("data");
                    // mHead_img.setImageBitmap(thumbnail);
                    if (mediaFile != null) {
                        Log.e("图片名:", mediaFile.getAbsolutePath());
                        imgList.add(mediaFile.getAbsolutePath());
                        imgAdapter.notifyDataSetChanged();
                        fileList.clear();
                        getFiles();
                    } else {
                        Toast.makeText(NoLeaseActivity.this, "没有拍照", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                Bitmap resizeBitmap = null;

                //拍照完毕后，获取返回数据显示照片，api24以后，需要转换输入流来获取Bitmap，
                // 而输入流的获取需要通过getContentResolvrer.openInputStream()来获取，
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(fileUri));
                        int bitmapWidth = bitmap.getWidth();
                        int bitmapHeight = bitmap.getHeight();
                        // 缩放图片的尺寸
                        float scaleWidth = (float) ScreenWith / bitmapWidth - 0.1f;
                        float scaleHeight = (float) ScreenHeight / bitmapHeight - 0.1f;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);
                        // 产生缩放后的Bitmap对象
                        resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }

                } else {

                    BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                    factoryOptions.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
//                    int imageWidth = factoryOptions.outWidth;
//                    int imageHeight = factoryOptions.outHeight;
//                    int scaleFactor = Math.min(imageWidth / ScreenWith, imageHeight / ScreenWith);
//                    factoryOptions.inJustDecodeBounds = false;
//                    factoryOptions.inSampleSize = scaleFactor;
//                    factoryOptions.inPurgeable = true;
                    resizeBitmap = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

                }

                if (resizeBitmap != null) {
                    //mHead_img.setImageBitmap(resizeBitmap);
                    //拍照上传头像
                    File file = new File(mediaFile.getAbsolutePath());//将要保存图片的路径
//                    try {
//                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
//                        bos.flush();
//                        bos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    imgList.add(file.getAbsolutePath());
                    imgAdapter.notifyDataSetChanged();
                    fileList.clear();
                    getFiles();
                    //  Map<Object, Object> map = new HashMap<>();
                    // File[] files = {file};
                    //String url = URLTools.urlBase + URLTools.post_head_url;
                    //  okHttpManager.postFileMethod(url, "拍照上传头像", map, files, handler, 1);
                } else {
                    //如果没有拍照

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                //点击了允许
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 1) {
                        intentCamera();
                    } else {
                        intentAlbum();
                    }
                } else {
                    Toast.makeText(NoLeaseActivity.this, "您已禁止访问相册", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //处理上传的图片
    private void getFiles() {
        for (int i = 0; i < imgList.size(); i++) {
            File file = new File(imgList.get(i));
            if (file != null && file.length() != 0) {
                try {
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;//如果该 值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。但是允许我们查询图片的信息这其中就包括图片大小信息
                    FileInputStream fileInputStream = new FileInputStream(file);//获取文件输入流
                    BitmapFactory.decodeStream(fileInputStream, null, o);//

                    int bitmap_width = o.outWidth;
                    int bitmap_height = o.outHeight;
                    int scale = 2;
                    while (true) {
                        //如果返回bitmap的宽度除以缩放的比例后，仍然比屏幕的的宽度大，那么继续缩放
                        if (bitmap_width / scale < ScreenWith) {
                            break;
                        }
                        scale *= 2;
                    }

                    scale /= 2;//再次缩小2
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    FileInputStream fileInpustream2 = new FileInputStream(file);
                    o2.inPreferredConfig = Bitmap.Config.RGB_565;//默认是ARGB_8888 一个像素占用4个字节，RGB_565一个像素2个字节
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInpustream2, null, o2);
                    // mHead_img.setImageBitmap(bitmap);//显示图片
                    Log.e("缩放前bitmap宽", bitmap_width + "");
                    Log.e("缩放前bitmap高", bitmap_height + "");
                    Log.e("缩放后bitmap宽", bitmap.getWidth() + "");
                    Log.e("缩放后bitmap高", bitmap.getHeight() + "");
                    Log.e("scale缩放比例：", scale + "");
                    Log.e("file名称：", file.getName());
                    Log.e("file长度：", file.length() + "");
                    Log.e("file大小：", file.length() / 1024 + "k");
                    fileList.add(file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("bitmap error Msg", e.toString());
                }
            }
        }

    }

    //拍照：调出相机
    private void intentCamera() {
        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, 1);
    }

    //拍照： 由file文件获取的uri
    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //SDK大于等于24的时候，必须使用下面这种方法获取uri
            return FileProvider.getUriForFile(this, this.getPackageName() + ".fileProvider", getOutputMediaFile(type));
        }
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //拍照： 创建一个文件
    private File getOutputMediaFile(int type) {
        File mediaStorageDir = null;
        try {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        Log.e("mediaFile", mediaFile.getAbsolutePath());
        return mediaFile;
    }
}
