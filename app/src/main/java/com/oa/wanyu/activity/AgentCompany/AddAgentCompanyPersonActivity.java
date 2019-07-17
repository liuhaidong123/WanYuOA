package com.oa.wanyu.activity.AgentCompany;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.Customer.CustomerAddActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CheckPhone;
import com.oa.wanyu.myutils.CircleImg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//添加代理公司人员
public class AddAgentCompanyPersonActivity extends AppCompatActivity {
    private ImageView mBack;
    private RelativeLayout mAll_rl;
    private TextView sure_btn, sex_tv;
    private CircleImg circleImg;
    private EditText name_edit, age_edit, phone1_edit, password_edit, live_edit;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private View alertView;
    private TextView mCamera_Btn, mPicture_Btn;
    private static final int PERMISSIONS_REQUESTCODE = 300;
    private int flag;//1代表跳转到相册，2代表是调起相机拍照
    public static final int INTENT_ALBUM = 100;//跳转相册
    public static final int INTENT_CAMERA = 200;//拍照
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int ScreenWith, ScreenHeight;
    private File file;//相册file
    private File mediaFile;
    private Uri fileUri;
    private List<File> list = new ArrayList<>();

    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url;
    private int position = 0;
    private long id = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            sure_btn.setEnabled(true);

            if (msg.what == 1) {//添加客户或者修改客户信息
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(AddAgentCompanyPersonActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, getIntent());
                                finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(AddAgentCompanyPersonActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddAgentCompanyPersonActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(AddAgentCompanyPersonActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(AddAgentCompanyPersonActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
//                no_data_rl.setVisibility(View.VISIBLE);
//                no_mess_tv.setText("服务器连接失败,请重新尝试");
//                show_img.setVisibility(View.GONE);
//                agree_disagree_ll.setVisibility(View.GONE);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent_company_person);
        initUI();
    }

    private void initUI() {
        mAll_rl = (RelativeLayout) findViewById(R.id.activity_add_agent_company_person);
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //提交
        sure_btn = (TextView) findViewById(R.id.sure_btn);
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(name_edit.getText().toString().trim())) {
                    if ("".equals(phone1_edit.getText().toString().trim())) {
                        Toast.makeText(AddAgentCompanyPersonActivity.this, "请填写电话", Toast.LENGTH_SHORT).show();
                    } else {
                        if (CheckPhone.checkPhone(phone1_edit.getText().toString().trim())) {

                            if (!"".equals(password_edit.getText().toString().trim())) {
                                if (password_edit.getText().toString().trim().length() == 6) {
                                    Map<Object, Object> map = new HashMap<Object, Object>();
                                    map.put("trueName", name_edit.getText().toString().trim());
                                    map.put("gender", String.valueOf(position + 1));
                                    map.put("age", age_edit.getText().toString().trim());
                                    map.put("mobile", phone1_edit.getText().toString().trim());
                                    map.put("address", live_edit.getText().toString().trim());
                                    map.put("companyId", String.valueOf(id));
                                    map.put("roleId", String.valueOf(2));
                                    map.put("password", password_edit.getText().toString().trim());

                                    BallProgressUtils.showLoading(AddAgentCompanyPersonActivity.this, mAll_rl);
                                    sure_btn.setEnabled(false);
                                    okHttpManager.postFileMethod(url, "添加销售人员接口", map, list, handler, 1);
                                } else {
                                    Toast.makeText(AddAgentCompanyPersonActivity.this, "密码长度不够6位数", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddAgentCompanyPersonActivity.this, "请填写6位数密码", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(AddAgentCompanyPersonActivity.this, "请填写正确的电话", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(AddAgentCompanyPersonActivity.this, "请填写姓名", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //选择头像
        circleImg = (CircleImg) findViewById(R.id.add_head_img);
        circleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        final String[] str = {"男", "女"};
        sex_tv = (TextView) findViewById(R.id.sex_edit);
        sex_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 0;
                new AlertDialog.Builder(AddAgentCompanyPersonActivity.this)
                        .setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                position = i;
                            }
                        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sex_tv.setText(str[position]);
                    }
                }).create().show();


            }
        });

        name_edit = (EditText) findViewById(R.id.name_edit);
        age_edit = (EditText) findViewById(R.id.age_edit);
        phone1_edit = (EditText) findViewById(R.id.phone1_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        live_edit = (EditText) findViewById(R.id.live_edit);

        //选择头像
        picture();

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.agent_company_add_person;//添加联系人
        id = getIntent().getLongExtra("id", -1);
    }

    private void picture() {
        builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertView = LayoutInflater.from(this).inflate(R.layout.camrea_picture, null);
        alertDialog.setView(alertView);
        mCamera_Btn = (TextView) alertView.findViewById(R.id.camrea_btn);
        mPicture_Btn = (TextView) alertView.findViewById(R.id.picture_btn);
        ScreenWith = getResources().getDisplayMetrics().widthPixels;
        ScreenHeight = getResources().getDisplayMetrics().heightPixels;
        //拍照
        mCamera_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 2;
                alertDialog.dismiss();
                checkMyPermission();
            }
        });
        //选择图片
        mPicture_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                alertDialog.dismiss();
                checkMyPermission();
            }
        });
    }

    //判断权限并跳转相册
    public void checkMyPermission() {
        //sdk版本>=23时，
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //如果没有授权
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUESTCODE);
                return;
                //如果已经授权，执行业务逻辑
            } else {
                if (flag == 1) {//相册
                    intentAlbum();
                } else {//相机
                    intentCamera();
                }
            }
            //版本小于23时，不需要判断敏感权限，执行业务逻辑
        } else {
            if (flag == 1) {//相册
                intentAlbum();
            } else {//相机
                intentCamera();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUESTCODE:
                //点击了允许
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 1) {//相册
                        intentAlbum();
                    } else {//相机
                        intentCamera();
                    }
                    //点击了拒绝
                } else {
                    Toast.makeText(this, "您已禁止访问相册", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_ALBUM) {//获取相册图片并显示
            showAlbumPicture(data);
        } else {//拍照以后显示图片
            if (data != null) {
                if (data.hasExtra("data")) {
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    circleImg.setImageBitmap(thumbnail);
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
                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
                    int imageWidth = factoryOptions.outWidth;
                    int imageHeight = factoryOptions.outHeight;
                    int scaleFactor = Math.min(imageWidth / ScreenWith, imageHeight / ScreenWith);
                    factoryOptions.inJustDecodeBounds = false;
                    factoryOptions.inSampleSize = scaleFactor;
                    factoryOptions.inPurgeable = true;
                    resizeBitmap = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);


                }

                if (resizeBitmap != null) {
                    circleImg.setImageBitmap(resizeBitmap);
                    //拍照上传头像
                    File file = new File(mediaFile.getAbsolutePath());//将要保存图片的路径
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                        bos.flush();
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    list.clear();
                    list.add(file);

                } else {
                    //如果没有拍照

                }


            }
        }
    }

    //===================调用相册==============================

    /**
     * 调用相册： 跳转相册
     */
    private void intentAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, INTENT_ALBUM);
    }

    /**
     * 调用相册： 获取相册图片路径
     *
     * @param uri
     * @return
     */
    private String getRealPathFromURI(Uri uri) {
        String strPath = null;
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            strPath = cursor.getString(column_index);
        }
        cursor.close();
        return strPath;
    }

    /**
     * 调用相册： 显示相册选取的图片
     */
    private void showAlbumPicture(Intent data) {
        //获取相册图片
        if (data != null && data.getData() != null) {
            String path = getRealPathFromURI(data.getData());
            file = new File(path);
            if (file == null) {
                return;
            }
            if (file.length() == 0) {
                file.delete();
                return;
            }

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
                circleImg.setImageBitmap(bitmap);//显示图片
                Log.e("缩放前bitmap宽", bitmap_width + "");
                Log.e("缩放前bitmap高", bitmap_height + "");
                Log.e("缩放后bitmap宽", bitmap.getWidth() + "");
                Log.e("缩放后bitmap高", bitmap.getHeight() + "");
                Log.e("scale缩放比例：", scale + "");
                Log.e("file路径：", path);
                Log.e("file名称：", file.getName());
                Log.e("file长度：", file.length() + "");
                Log.e("file大小：", file.length() / 1024 + "k");

                //相册上传头像

                list.clear();
                list.add(file);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("bitmap error Msg", e.toString());
            }
        } else {
            // Toast.makeText(getContext(), "图片错误", Toast.LENGTH_SHORT).show();
        }


    }


    //===================拍照：调出相机==============================

    //拍照：调出相机
    private void intentCamera() {
        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, INTENT_CAMERA);
    }

    //拍照： 由file文件获取的uri
    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //SDK大于等于24的时候，必须使用下面这种方法获取uri
            return FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", getOutputMediaFile(type));
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
