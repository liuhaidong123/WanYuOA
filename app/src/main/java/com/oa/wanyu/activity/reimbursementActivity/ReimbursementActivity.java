package com.oa.wanyu.activity.reimbursementActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.PrintStreamPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.goodsBuyActivity.GoodsBuyActivity;
import com.oa.wanyu.activity.leave.LeaveActivity;
import com.oa.wanyu.bean.ReimbursementBean;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.TravelBean;
import com.oa.wanyu.bean.leaveType.LeaveRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.DoubleUtils;
import com.oa.wanyu.myutils.ImgUitls;
import com.oa.wanyu.myutils.MyGridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.picasso.Picasso;

public class ReimbursementActivity extends AppCompatActivity {

    //报销

    private ImageView mBack;
    private ListView mListview;
    private TextView mSubmit_btn;
    private View footer;
    private TextView mAdd_Travel_btn, all_money;//添加报销,总额
    private MyGridView myGridView;
    private PictureAda pictureAda;
    private int ScreenWith;//屏幕宽度
    private ArrayList<String> imgList = new ArrayList<>();
    private List<ReimbursementBean> mList = new ArrayList<>();
    private ReimbursementAda reimbursementAda;
    //报销类型弹框
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ListView alertListView;
    private List<String> typeList = new ArrayList<>();
    private RTypeAda rTypeAda;
    private int mPosition;

    private List<File> fileList = new ArrayList<>();
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
                                Toast.makeText(ReimbursementActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("3".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("4".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("5".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(ReimbursementActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(ReimbursementActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 1010) {
                //数据错误
                Toast.makeText(ReimbursementActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {//报销类型
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, LeaveRoot.class);
                    if (o != null && o instanceof LeaveRoot) {
                        LeaveRoot leaveRoot = (LeaveRoot) o;
                        if ("0".equals(leaveRoot.getCode()) && leaveRoot != null) {
                            if (leaveRoot.getLeaveType() != null) {
                                // mLeaveList = leaveRoot.getLeaveType();
                                // leaveAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(ReimbursementActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ReimbursementActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement);

        mAll = (RelativeLayout) findViewById(R.id.activity_reimbursement);
        okHttpManager = OkHttpManager.getInstance();
        urlSubmit = URLTools.urlBase + URLTools.apply_reimbursement;
        ScreenWith = getResources().getDisplayMetrics().widthPixels;
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

        mList.add(new ReimbursementBean("", ""));
        mListview = (ListView) findViewById(R.id.bx_listview_id);
        reimbursementAda = new ReimbursementAda();
        mListview.setAdapter(reimbursementAda);
        footer = LayoutInflater.from(this).inflate(R.layout.baoxiao_footer, null);
        mListview.addFooterView(footer);

        //添加报销明细
        mAdd_Travel_btn = footer.findViewById(R.id.add_bx);
        mAdd_Travel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reimbursementAda.addItem();

            }
        });
        //报销总金额
        all_money = footer.findViewById(R.id.bx_tv);
        myGridView = footer.findViewById(R.id.img_gridview);
        pictureAda = new PictureAda();
        myGridView.setAdapter(pictureAda);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkPermission();
            }
        });

        //报销类型弹框
        typeList.add("住宿费");
        typeList.add("吃饭费");
        typeList.add("车费费");
        typeList.add("接待费");
        typeList.add("住宿费");
        builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.reimbursement_alert_listview, null);
        alertDialog.setView(view);
        alertListView = view.findViewById(R.id.reimbursment_listview_id);
        rTypeAda = new RTypeAda();
        alertListView.setAdapter(rTypeAda);
        alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //选择类型以后，需要重新设置数据
                mList.get(mPosition).setType(typeList.get(i));
                reimbursementAda.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

        //提交
        mSubmit_btn = (TextView) findViewById(R.id.bx_submit);
        mSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = gson.toJson(mList);
                Log.e("json字符串", s);
                try {
                    mSubmit_btn.setEnabled(false);
                    BallProgressUtils.showLoading(ReimbursementActivity.this, mAll);
                    String ss = URLEncoder.encode(s, "UTF-8");
                    Log.e("json字符串编码之后", ss);
                    Map<Object, Object> map = new HashMap<>();
                    map.put("items", ss);

                    okHttpManager.postFileMethod(urlSubmit, "物品报销", map,fileList , handler, 1);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(ReimbursementActivity.this, "编码错误", Toast.LENGTH_SHORT).show();
                    BallProgressUtils.dismisLoading();
                }
            }
        });

    }

    //报销适配器
    class ReimbursementAda extends BaseAdapter {

        public void addItem() {
            mList.add(new ReimbursementBean("", ""));
            this.notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            mList.remove(position);
            this.notifyDataSetChanged();
            //计算最终的总价
            //计算最终的总价
            double price = 0;
            for (int k = 0; k < mList.size(); k++) {
                if (!"".equals(mList.get(k).getMoney())) {
                    price += Double.valueOf(mList.get(k).getMoney());
                } else {
                    price += 0;
                }

            }

            all_money.setText(price + "");
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
            ReimbursementHolder reimbursementHolder = new ReimbursementHolder();
            view = LayoutInflater.from(ReimbursementActivity.this).inflate(R.layout.reimbursement_item, null);
            reimbursementHolder.money = view.findViewById(R.id.edit_money);
            reimbursementHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
            reimbursementHolder.delete = view.findViewById(R.id.delete_btn);
            reimbursementHolder.type = view.findViewById(R.id.type_mess_tv);
            int a = i + 1;
            reimbursementHolder.money.setText(mList.get(i).getMoney() + "");
            reimbursementHolder.mingxi.setText("报销明细" + "(" + a + ")");
            reimbursementHolder.type.setText(mList.get(i).getType());
            //删除报销明细
            reimbursementHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList.size() != 1) {
                        deleteItem(i);
                    }

                }
            });
            //输入报销金额
            reimbursementHolder.money.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (DoubleUtils.myDoubleC(editable + "")) {
                        if (!"".equals(editable)) {
                            mList.get(i).setMoney(editable + "");//保存数据
                        } else {
                            mList.get(i).setMoney("");//保存数据
                        }


                        //计算最终的总价
                        double price = 0;
                        for (int k = 0; k < mList.size(); k++) {
                            if (!"".equals(mList.get(k).getMoney())) {
                                price += Double.valueOf(mList.get(k).getMoney());
                            } else {
                                price += 0;
                            }

                        }

                        all_money.setText(price + "");
                    } else {
                        all_money.setText("");
                        mList.get(i).setMoney("");//保存数据
                        Toast.makeText(ReimbursementActivity.this, "请输入正确金额", Toast.LENGTH_SHORT).show();
                        //计算最终的总价
                        double price = 0;
                        for (int k = 0; k < mList.size(); k++) {
                            if (!"".equals(mList.get(k).getMoney())) {
                                price += Double.valueOf(mList.get(k).getMoney());
                            } else {
                                price += 0;
                            }

                        }

                        all_money.setText(price + "");
                    }

                }
            });

            //选择报销类型
            reimbursementHolder.type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.show();
                    mPosition = i;
                }
            });

            return view;
        }

        class ReimbursementHolder {
            EditText money;
            TextView type, mingxi, delete;
        }
    }

    //图片适配器
    class PictureAda extends BaseAdapter {

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
        public View getView(int i, View view, ViewGroup viewGroup) {

            PHolder pHolder = new PHolder();
            view = LayoutInflater.from(ReimbursementActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
            pHolder.imageView = view.findViewById(R.id.picture);

            //没没有图片时，第一个图片为加号//有图片时，最后一个图片为加号
            if (imgList.size() == 0 || imgList.size() == i) {
                pHolder.imageView.setImageResource(R.mipmap.add_picture);
            } else {
                Picasso.with(ReimbursementActivity.this).load(imgList.get(i)).centerCrop().resize(ImgUitls.getWith(ReimbursementActivity.this) / 3,
                        ImgUitls.getWith(ReimbursementActivity.this) / 3).error(R.mipmap.errorpicture).into(pHolder.imageView);
            }


            return view;
        }

        class PHolder {
            ImageView imageView;
        }
    }

    // 报销类型适配器
    class RTypeAda extends BaseAdapter {


        @Override
        public int getCount() {
            return typeList.size();
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
            TypeHolder typeHolder = new TypeHolder();
            view = LayoutInflater.from(ReimbursementActivity.this).inflate(R.layout.leave_item, null);
            typeHolder.type = view.findViewById(R.id.leave_);
            typeHolder.type.setText(typeList.get(i));

            return view;
        }

        class TypeHolder {
            TextView type;
        }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int p = ContextCompat.checkSelfPermission(ReimbursementActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (p != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ReimbursementActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                return;
            } else {
                intentAlbum();
            }

        } else {
            intentAlbum();
        }
    }

    private void intentAlbum() {
        Intent intent = new Intent(ReimbursementActivity.this, SelectPictureActivity.class);
        intent.putStringArrayListExtra("img_list", imgList);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> list = data.getStringArrayListExtra("imgList");

                if (list != null) {
                    imgList = list;
                    pictureAda.notifyDataSetChanged();
                    getFiles();
                } else {
                    Toast.makeText(ReimbursementActivity.this, "图片无效", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ReimbursementActivity.this, "数据无效", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                //点击了允许
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentAlbum();
                } else {
                    Toast.makeText(ReimbursementActivity.this, "您已禁止访问相册", Toast.LENGTH_SHORT).show();
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
}
