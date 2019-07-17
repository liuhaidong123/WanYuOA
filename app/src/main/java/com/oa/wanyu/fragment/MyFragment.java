package com.oa.wanyu.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.AgentCompany.AddAgentCompanyPersonActivity;
import com.oa.wanyu.activity.AgentCompany.AgentCompanyInformationActivity;
import com.oa.wanyu.activity.login.LoginActivity;
import com.oa.wanyu.activity.setpassword.SetPasswordActivity;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.UserMessageRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CheckPhone;
import com.oa.wanyu.myutils.CircleImg;
import com.oa.wanyu.myutils.SharedPrefrenceTools;
import com.squareup.picasso.Picasso;

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

/**
 * 我的
 */
public class MyFragment extends Fragment {
    private TextView m_name, m_company, m_department, m_job, m_phone;
    private CircleImg head_img;
    private ImageView m_edit_img;
    private long id = -1;
    private OkHttpManager okhttpmanager;
    private SharedPrefrenceTools sharedPrefrenceTools;
    private Gson gson = new Gson();
    private String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, UserMessageRoot.class);
                    if (o != null && o instanceof UserMessageRoot) {
                        UserMessageRoot userMessageRoot = (UserMessageRoot) o;
                        if (userMessageRoot != null) {
                            id = userMessageRoot.getId();

                            if ("".equals(userMessageRoot.getTrueName())) {
                                m_name.setText("未知姓名");
                            } else {
                                if (userMessageRoot.getTrueName()==null){
                                    m_name.setText("姓名：****");
                                }else {
                                    m_name.setText(userMessageRoot.getTrueName() + "");
                                }

                            }

                            if ("".equals(userMessageRoot.getDepartmentName())) {
                                m_department.setText("部门： -----");
                            } else {
                                if (userMessageRoot.getDepartmentName()==null){
                                    m_department.setText("部门：***");
                                }else {
                                    m_department.setText("部门：" + userMessageRoot.getDepartmentName() + "");
                                }

                            }

                            if ("".equals(userMessageRoot.getPosition())) {
                                m_job.setText("职位： -----");
                            } else {
                                if (userMessageRoot.getPosition()==null){
                                    m_job.setText("职位： ***");
                                }else {
                                    m_job.setText("职位： " + userMessageRoot.getPosition() + "");
                                }

                            }

                            if ("".equals(userMessageRoot.getMobile())) {
                                m_phone.setText("电话： -----");
                            } else {

                                if (userMessageRoot.getMobile()==null){
                                    m_phone.setText("电话： ***");
                                }else {
                                    m_phone.setText("电话： " + userMessageRoot.getMobile() + "");
                                }

                            }

                            if ("".equals(userMessageRoot.getCompanyName())){
                                m_company.setText("公司名称：-----");
                            }else {
                                if (userMessageRoot.getCompanyName()==null){
                                    m_company.setText("公司名称：***");
                                }else {
                                    m_company.setText(userMessageRoot.getCompanyName()+"");
                                }
                            }



                            Picasso.with(getActivity()).load(URLTools.urlBase + userMessageRoot.getAvatar()).error(R.mipmap.head_img_icon).into(head_img);

                        } else {
                            Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }


            } else if (msg.what == 2) {//上传头像
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(getActivity(), "上传头像成功", Toast.LENGTH_SHORT).show();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(getActivity(), successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 3) {//退出
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                                sharedPrefrenceTools.clear();
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(getActivity(), successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                                sharedPrefrenceTools.clear();
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                                sharedPrefrenceTools.clear();
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            }


            else {
                Toast.makeText(getActivity(), "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }

        }
    };


    private AlertDialog.Builder builder,builderExit;
    private AlertDialog alertDialog,alertDialogExit;
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
    private String add_url,exit_app_url;//跟换头像接口

    private TextView mVerson_tv,exit_btn;
    private RelativeLayout mPassword_rl, mCall_us_rl;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        sharedPrefrenceTools=SharedPrefrenceTools.getSharedPrefrenceToolsInstance(getActivity());
        m_name = view.findViewById(R.id.my_name);
        m_company = view.findViewById(R.id.my_company);
        m_department = view.findViewById(R.id.my_bumen);
        m_job = view.findViewById(R.id.my_job);
        m_phone = view.findViewById(R.id.my_phone);
        head_img = view.findViewById(R.id.my_head_img);
        m_edit_img = view.findViewById(R.id.my_edit);

        okhttpmanager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.user_message;
        exit_app_url=URLTools.urlBase+URLTools.exit_app;
        okhttpmanager.getMethod(false, url, "用户信息", handler, 1);

        add_url = URLTools.urlBase + URLTools.agent_company_add_person;//跟换头像
        picture();
        //跟换头像
        head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        exit_btn=view.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogExit.show();
            }
        });

        //确定退出程序
        builderExit = new AlertDialog.Builder(getActivity());
        builderExit.setTitle("确定退出程序？");
        builderExit.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialogExit.dismiss();
                okhttpmanager.getMethod(false, exit_app_url, "退出登录", handler, 3);
            }
        });
        builderExit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialogExit.dismiss();
            }
        });

        alertDialogExit = builderExit.create();
        mVerson_tv = view.findViewById(R.id.user_verson_tv);
        mPassword_rl = view.findViewById(R.id.user_password_rl);
        mPassword_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetPasswordActivity.class);
                startActivity(intent);
            }
        });
        mCall_us_rl = view.findViewById(R.id.user_callus_rl);
        mCall_us_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return;
                    } else {
                        intentPhone();
                    }
                } else {
                    intentPhone();
                }
            }
        });


        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
            String version = packInfo.versionName;
            mVerson_tv.setText("当前版本："+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void intentPhone() {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + "03123850331"));
            startActivity(intent);

    }


    //选头像弹框
    private void picture() {
        builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.create();
        alertView = LayoutInflater.from(getActivity()).inflate(R.layout.camrea_picture, null);
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
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //如果没有授权
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUESTCODE);
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
                    Toast.makeText(getActivity(), "您已禁止访问相册", Toast.LENGTH_SHORT).show();
                }
                break;

            //打电话
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "电话授权成功", Toast.LENGTH_SHORT).show();
                    //注意这里电话需要传过来
                    intentPhone();
                } else {
                    Toast.makeText(getActivity(), "电话授权失败", Toast.LENGTH_SHORT).show();
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
                    head_img.setImageBitmap(thumbnail);
                }

            } else {
                Bitmap resizeBitmap = null;

                //拍照完毕后，获取返回数据显示照片，api24以后，需要转换输入流来获取Bitmap，
                // 而输入流的获取需要通过getContentResolvrer.openInputStream()来获取，
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(fileUri));
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
                    head_img.setImageBitmap(resizeBitmap);
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
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    map.put("id", String.valueOf(id));
                    okhttpmanager.postFileMethod(add_url, "更换头像接口", map, list, handler, 2);
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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
                head_img.setImageBitmap(bitmap);//显示图片
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
                Map<Object, Object> map = new HashMap<Object, Object>();
                map.put("id", String.valueOf(id));
                okhttpmanager.postFileMethod(add_url, "更换头像接口", map, list, handler, 2);

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
            return FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileProvider", getOutputMediaFile(type));
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
