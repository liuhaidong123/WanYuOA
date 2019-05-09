package com.oa.wanyu.activity.reimbursementActivity;

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
import android.util.PrintStreamPrinter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.oa.wanyu.activity.leaseManage.NoLeaseActivity;
import com.oa.wanyu.activity.leave.LeaveActivity;
import com.oa.wanyu.bean.ReimbursementBean;
import com.oa.wanyu.bean.ReimbursementTypeBean;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.picasso.Picasso;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ReimbursementActivity extends AppCompatActivity {

    //报销

    private ImageView mBack, mBig_img;
    private ListView mListview;
    private TextView mSubmit_btn;
    private View footer;
    private TextView mAdd_Travel_btn, all_money;//添加报销,总额
    private MyGridView myGridView;
    private PictureAda pictureAda;
    private int ScreenWith, ScreenHeight;
    private File mediaFile;
    private Uri fileUri;
    private AlertDialog.Builder camrea_builder;
    private AlertDialog camera_alertdialog;
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
    private int flag = -1;//1是拍照，2是相册
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
                Toast.makeText(ReimbursementActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {//报销类型
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ReimbursementTypeBean.class);
                    if (o != null && o instanceof ReimbursementTypeBean) {
                        ReimbursementTypeBean reimbursementTypeBean = (ReimbursementTypeBean) o;
                        if ("0".equals(reimbursementTypeBean.getCode()) && reimbursementTypeBean != null) {
                            if (reimbursementTypeBean.getReimburseType() != null) {
                                typeList = reimbursementTypeBean.getReimburseType();
                                rTypeAda.notifyDataSetChanged();
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
    private int point = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement);

        mAll = (RelativeLayout) findViewById(R.id.activity_reimbursement);
        okHttpManager = OkHttpManager.getInstance();
        urlSubmit = URLTools.urlBase + URLTools.apply_reimbursement;
        urlType = URLTools.urlBase + URLTools.apply_reimbursement_type;//报销类型
        okHttpManager.getMethod(false, urlType, "报销类型", handler, 2);
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
        mBig_img = (ImageView) findViewById(R.id.big_img);//显示报销大图片
        mBig_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBig_img.setVisibility(View.GONE);
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

                if (imgList.size() == 0 || i == imgList.size()) {//跳转相册
                   // checkPermission();
                    camera_alertdialog.show();
                } else {//显示图片大图
                    mBig_img.setVisibility(View.VISIBLE);
                    Picasso.with(ReimbursementActivity.this).load(imgList.get(i)).into(mBig_img);
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
                if (imgList.size()>=3){
                    Toast.makeText(ReimbursementActivity.this,"图片已达到3张，若想上传其他照片，请删除原有照片",Toast.LENGTH_SHORT).show();
                }else {
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
                mList.get(mPosition).setTyp(typeList.get(i));
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

                    okHttpManager.postFileMethod(urlSubmit, "物品报销", map, fileList, handler, 1);

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
                if (!"".equals(mList.get(k).getAmount())) {
                    price += Double.valueOf(mList.get(k).getAmount());
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
            final ReimbursementHolder reimbursementHolder = new ReimbursementHolder();
            view = LayoutInflater.from(ReimbursementActivity.this).inflate(R.layout.reimbursement_item, null);
            reimbursementHolder.money = view.findViewById(R.id.edit_money);
            reimbursementHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
            reimbursementHolder.delete = view.findViewById(R.id.delete_btn);
            reimbursementHolder.type = view.findViewById(R.id.type_mess_tv);
            int a = i + 1;
            reimbursementHolder.money.setText(mList.get(i).getAmount() + "");
            reimbursementHolder.mingxi.setText("报销明细" + "(" + a + ")");
            reimbursementHolder.type.setText(mList.get(i).getTyp());
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
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    //删除“.”后面超过2位后的数据
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > point) {
                            s = s.toString().subSequence(0, s.toString().indexOf(".") + point + 1);
                            reimbursementHolder.money.setText(s);
                            reimbursementHolder.money.setSelection(s.length()); //光标移到最后
                        }
                    }
                    //如果"."在起始位置,则起始位置自动补0
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        reimbursementHolder.money.setText(s);
                        reimbursementHolder.money.setSelection(2);
                    }

                    //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            reimbursementHolder.money.setText(s.subSequence(0, 1));
                            reimbursementHolder.money.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (DoubleUtils.myDoubleC(editable + "")) {

                        if (!"".equals(editable)) {

                            if (editable.toString().contains(".")) {
                                if (editable.length() - 1 - editable.toString().indexOf(".") > point) {
                                    CharSequence sequence1 = editable.toString().subSequence(0, editable.toString().indexOf(".") + point + 1);
                                    mList.get(i).setAmount(sequence1 + "");//保存数据
                                } else {
                                    mList.get(i).setAmount(editable + "");//保存数据
                                }
                            } else {
                                mList.get(i).setAmount(editable + "");//保存数据
                            }


                        } else {
                            mList.get(i).setAmount("");//保存数据
                        }


                        //计算最终的总价
                        double price = 0.0;
                        for (int k = 0; k < mList.size(); k++) {
                            if (!"".equals(mList.get(k).getAmount())) {
                                price += Double.valueOf(mList.get(k).getAmount());
                            } else {
                                price += 0;
                            }

                        }
                        String allPrice = String.format("%.2f", price);
                        all_money.setText(allPrice + "");
                    } else {
                        all_money.setText("");
                        mList.get(i).setAmount("");//保存数据
                        // Toast.makeText(ReimbursementActivity.this, "请输入正确金额", Toast.LENGTH_SHORT).show();
                        //计算最终的总价
                        double price = 0;
                        for (int k = 0; k < mList.size(); k++) {
                            if (!"".equals(mList.get(k).getAmount())) {
                                price += Double.valueOf(mList.get(k).getAmount());
                            } else {
                                price += 0;
                            }

                        }
                        String allPrice = String.format("%.2f", price);
                        all_money.setText(allPrice + "");
                    }

                }
            });

            //选择报销类型
            reimbursementHolder.type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (typeList.size() != 0) {
                        alertDialog.show();
                        mPosition = i;
                    } else {
                        Toast.makeText(ReimbursementActivity.this, "正在请求数据,请稍后。。。", Toast.LENGTH_SHORT).show();
                        okHttpManager.getMethod(false, urlType, "报销类型", handler, 2);
                    }

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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            PHolder pHolder = new PHolder();
            view = LayoutInflater.from(ReimbursementActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
            pHolder.imageView = view.findViewById(R.id.picture);
            pHolder.cha_img = view.findViewById(R.id.cha_img);

            //没没有图片时，第一个图片为加号//有图片时，最后一个图片为加号
            if (imgList.size() == 0 || imgList.size() == i) {
                pHolder.imageView.setImageResource(R.mipmap.add_picture);
                pHolder.cha_img.setVisibility(View.GONE);
            } else {
                pHolder.cha_img.setVisibility(View.VISIBLE);
                Picasso.with(ReimbursementActivity.this).load(imgList.get(i)).error(R.mipmap.errorpicture).into(pHolder.imageView);
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

    private void intentAlbum() {

        Intent intent = new Intent(ReimbursementActivity.this, SelectPictureActivity.class);
        intent.putStringArrayListExtra("img_list", imgList);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> list = data.getStringArrayListExtra("imgList");

                if (list != null) {
                    imgList = list;
                    Log.e("返回图片多少", list.size() + "");
                    pictureAda.notifyDataSetChanged();
                    fileList.clear();
                    getFiles();
                } else {
                    Toast.makeText(ReimbursementActivity.this, "图片无效", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ReimbursementActivity.this, "数据无效", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == 1) {//拍照
            if (data != null) {
                if (data.hasExtra("data")) {
                    //Bitmap thumbnail = data.getParcelableExtra("data");
                    // mHead_img.setImageBitmap(thumbnail);
                    if (mediaFile != null) {
                        Log.e("图片名:", mediaFile.getAbsolutePath());
                        imgList.add(mediaFile.getAbsolutePath());
                        pictureAda.notifyDataSetChanged();
                        fileList.clear();
                        getFiles();
                    } else {
                        Toast.makeText(ReimbursementActivity.this, "没有拍照", Toast.LENGTH_LONG).show();
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
                    pictureAda.notifyDataSetChanged();
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
