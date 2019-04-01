package com.oa.wanyu.activity.goodsUseActivity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.oa.wanyu.bean.GoodsUseBean;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.TravelBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//物品领用
public class GoodsUseActivity extends AppCompatActivity {
    private ImageView mBack;
    private ListView mListview;
    private GoodsUseAda goodsUseAda;
    private List<GoodsUseBean> myList = new ArrayList<>();
    private TextView mSubmit_btn;
    private View footer;
    private TextView mAdd_goods_btn;//添加领用
    private EditText goods_use_mess_edit;//物品详情
    private RelativeLayout mAll;
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
                                Toast.makeText(GoodsUseActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(GoodsUseActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(GoodsUseActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("3".equals(successBean.getCode())) {
                                Toast.makeText(GoodsUseActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("4".equals(successBean.getCode())) {
                                Toast.makeText(GoodsUseActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(GoodsUseActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(GoodsUseActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(GoodsUseActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_use);
        mAll = (RelativeLayout) findViewById(R.id.activity_goods_use);
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_use;

        mSubmit_btn = (TextView) findViewById(R.id.goods_use_submit);
        mSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = gson.toJson(myList);
                Log.e("json字符串", s);
                try {
                    mSubmit_btn.setEnabled(false);
                    BallProgressUtils.showLoading(GoodsUseActivity.this, mAll);
                    String ss = URLEncoder.encode(s, "UTF-8");
                    Log.e("json字符串编码之后", ss);
                    Map<Object, Object> map = new HashMap<>();
                    map.put("items", ss);
                    map.put("detail", goods_use_mess_edit.getText().toString());
                    okHttpManager.postMethod(false, url, "物品领用", map, handler, 1);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(GoodsUseActivity.this, "编码错误", Toast.LENGTH_SHORT).show();
                    BallProgressUtils.dismisLoading();
                }
            }
        });
        footer = LayoutInflater.from(this).inflate(R.layout.goods_use_footer, null);
        goods_use_mess_edit = footer.findViewById(R.id.edit_goods_use_reason);
        mAdd_goods_btn = footer.findViewById(R.id.add_goods_use);
        mAdd_goods_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodsUseAda.addItem();
            }
        });
        mListview = (ListView) findViewById(R.id.goods_listview_id);
        myList.add(new GoodsUseBean("", 0));
        goodsUseAda = new GoodsUseAda();
        mListview.setAdapter(goodsUseAda);
        mListview.addFooterView(footer);
    }

    class GoodsUseAda extends BaseAdapter {
        public void addItem() {
            myList.add(new GoodsUseBean("", 0));
            this.notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            myList.remove(position);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return myList.size();
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
            GoodsUseHolder goodsUseHolder = new GoodsUseHolder();
            view = LayoutInflater.from(GoodsUseActivity.this).inflate(R.layout.goods_use_item, null);
            goodsUseHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
            goodsUseHolder.delete = view.findViewById(R.id.delete_btn);
            goodsUseHolder.name = view.findViewById(R.id.edit_goods_name);
            goodsUseHolder.num = view.findViewById(R.id.edit_goods_num);
            //goodsUseHolder.mess = view.findViewById(R.id.edit_goods_use_reason);

            goodsUseHolder.name.setText(myList.get(i).getNam() + "");
            goodsUseHolder.num.setText(myList.get(i).getNum()+"");
            //goodsUseHolder.mess.setText(myList.get(i).getMess());

            if (myList.size() == 1) {
                goodsUseHolder.delete.setVisibility(View.GONE);
            } else {
                goodsUseHolder.delete.setVisibility(View.VISIBLE);
            }
            int a = i + 1;
            goodsUseHolder.mingxi.setText("物品领用明细" + "(" + a + ")");
            //删除
            goodsUseHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myList.size() != 1) {
                        deleteItem(i);
                    }
                }
            });
            //物品名称
            goodsUseHolder.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    myList.get(i).setNam("" + editable);
                }
            });

            //物品数量
            goodsUseHolder.num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length()!=0){
                        myList.get(i).setNum(Integer.valueOf("" + editable));
                    }else {
                        myList.get(i).setNum(0);
                    }

                }
            });
//
//            //领用详情
//            goodsUseHolder.mess.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    myList.get(i).setMess("" + editable);
//                }
//            });
            return view;
        }

        class GoodsUseHolder {
            TextView mingxi, delete;
            EditText name, num, mess;
        }
    }
}
