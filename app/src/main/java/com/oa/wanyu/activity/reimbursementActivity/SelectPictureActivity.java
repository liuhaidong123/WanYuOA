package com.oa.wanyu.activity.reimbursementActivity;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oa.wanyu.R;
import com.oa.wanyu.myutils.ImgUitls;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

//选择报销的图片
public class SelectPictureActivity extends AppCompatActivity {
    private ImageView mBack;
    private TextView mText;
    private GridView mGridView;
    private ImgAdapter mAdapter;
    private int num = 0;
    private ArrayList<String> mImgList = new ArrayList<>();//选择后的图片集合或者从上一页跳转过来的图片集合
    //private ArrayList<String> myList = new ArrayList<>();//从上一页跳转过来的图片集合
    private List<Boolean> checkList = new ArrayList<>();
    private Cursor cursor;
    private ArrayList<String> mList = new ArrayList<>();//所有相册图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        mImgList = getIntent().getStringArrayListExtra("img_list");
        Log.e("传过来的集合大小=",mImgList.size()+"");
        num = mImgList.size();
        //不论点击返回还是完成都需要将文件传到上个活动
        mBack = (ImageView) findViewById(R.id.select_img_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putStringArrayListExtra("imgList", mImgList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //完成
        mText = (TextView) findViewById(R.id.title_tv);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putStringArrayListExtra("imgList", mImgList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        selectImg();
        mGridView = (GridView) findViewById(R.id.select_img_gridview);
        mAdapter = new ImgAdapter();
        mGridView.setAdapter(mAdapter);

        if (mImgList.size() != 0) {
            mText.setText("完成(" + mImgList.size() + "/6)");
        } else {
            mText.setText("完成");
        }
        //点击选择相片
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view.findViewById(R.id.select_small_img);
                Log.e("文件", mAdapter.getItem(position).toString());
                if (num != 6) {
                    if (mImgList.contains(mAdapter.getItem(position))) {
                        mImgList.remove(mAdapter.getItem(position));
                        checkList.set(position, false);
                        num--;
                        mText.setText("完成" + num + "/" + 6);
                        imageView.setImageResource(R.mipmap.selectfa);
                    } else {
                        num++;
                        mText.setText("完成" + num + "/" + 6);
                        imageView.setImageResource(R.mipmap.selecttr);
                        mImgList.add(mAdapter.getItem(position).toString());
                        checkList.set(position, true);
                    }
                } else {
                    if (mImgList.contains(mAdapter.getItem(position))) {
                        mImgList.remove(mAdapter.getItem(position));
                        num--;
                        mText.setText("完成" + num + "/" + 6);
                        imageView.setImageResource(R.mipmap.selectfa);
                        checkList.set(position, false);
                    } else {
                        Toast.makeText(SelectPictureActivity.this, "亲,最多选择" + 6 + "张图片哦", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    boolean isFlag = false;

    //查询所有图片
    public void selectImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String imgMsg = cursor.getString(index);
                    mList.add(imgMsg);
                    for (int i = 0; i < mImgList.size(); i++) {
                        if (mImgList.get(i).equals(imgMsg)) {
                            isFlag = true;
                            break;
                        } else {
                            isFlag = false;
                        }

                    }
                    checkList.add(isFlag);
                    Log.e("路径", imgMsg);

                }
            }
        }).start();
    }

    class ImgAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            SelectImgHolder holder = null;
            if (view == null) {
                holder = new SelectImgHolder();
                view = LayoutInflater.from(SelectPictureActivity.this).inflate(R.layout.repair_select_img_gridview_item, null);
                holder.img = (ImageView) view.findViewById(R.id.select_img);
                holder.smallImg = (ImageView) view.findViewById(R.id.select_small_img);
                view.setTag(holder);
            } else {
                holder = (SelectImgHolder) view.getTag();
            }
            Picasso.with(SelectPictureActivity.this).load(mList.get(i)).centerCrop().resize(ImgUitls.getWith(SelectPictureActivity.this) / 3,
                    ImgUitls.getWith(SelectPictureActivity.this) / 3).error(R.mipmap.errorpicture).into(holder.img);
            if (checkList.get(i)) {
                holder.smallImg.setImageResource(R.mipmap.selecttr);
            } else {
                holder.smallImg.setImageResource(R.mipmap.selectfa);
            }


            return view;
        }

        class SelectImgHolder {
            ImageView smallImg;
            ImageView img;
        }
    }
}
