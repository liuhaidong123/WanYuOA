package com.oa.wanyu.activity.goodsBuyActivity;

import android.net.Uri;
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
import com.oa.wanyu.activity.goodsUseActivity.GoodsUseActivity;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivity;
import com.oa.wanyu.bean.GoodsBuyBean;
import com.oa.wanyu.bean.GoodsUseBean;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.DoubleUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//物品申购
public class GoodsBuyActivity extends AppCompatActivity {

    private ImageView mBack;
    private ListView mListview;
    private GoodsBuyAda goodsUseAda;
    private List<GoodsBuyBean> myList = new ArrayList<>();
    private TextView mSubmit_btn;
    private View footer;
    private TextView mAdd_goods_btn, all_price;//添加物品申购，总价
    //DecimalFormat df = new DecimalFormat("#.00");

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
                                Toast.makeText(GoodsBuyActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if ("1".equals(successBean.getCode())) {
                                Toast.makeText(GoodsBuyActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(GoodsBuyActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("3".equals(successBean.getCode())) {
                                Toast.makeText(GoodsBuyActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("4".equals(successBean.getCode())) {
                                Toast.makeText(GoodsBuyActivity.this, successBean.getMessage() + "", Toast.LENGTH_SHORT).show();

                            } else if ("-1".equals(successBean.getCode())) {
                                Toast.makeText(GoodsBuyActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(GoodsBuyActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(GoodsBuyActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_buy);
        mAll = (RelativeLayout) findViewById(R.id.activity_goods_buy);
        mBack = (ImageView) findViewById(R.id.back_img);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.apply_buy;


        mSubmit_btn = (TextView) findViewById(R.id.goods_buy_submit);
        mSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = gson.toJson(myList);
                Log.e("json字符串", s);
                try {
                    mSubmit_btn.setEnabled(false);
                    BallProgressUtils.showLoading(GoodsBuyActivity.this, mAll);
                    String ss = URLEncoder.encode(s, "UTF-8");
                    Log.e("json字符串编码之后", ss);
                    Map<Object, Object> map = new HashMap<>();
                    map.put("items", ss);

                    okHttpManager.postMethod(false, url, "物品申购", map, handler, 1);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(GoodsBuyActivity.this, "编码错误", Toast.LENGTH_SHORT).show();
                    BallProgressUtils.dismisLoading();
                }
            }
        });

        myList.add(new GoodsBuyBean("", "", "", "", "", ""));
        footer = LayoutInflater.from(this).inflate(R.layout.goods_buy_footer, null);
        mListview = (ListView) findViewById(R.id.goods_buy_listview_id);
        goodsUseAda = new GoodsBuyAda();
        mListview.addFooterView(footer);
        mListview.setAdapter(goodsUseAda);

        mAdd_goods_btn = footer.findViewById(R.id.add_goods_buy);
        mAdd_goods_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodsUseAda.addItem();
            }
        });

        //总价
        all_price = footer.findViewById(R.id.goods_buy_all_price_tv);
    }

    private int point=2;
    class GoodsBuyAda extends BaseAdapter {
        public void addItem() {
            myList.add(new GoodsBuyBean("", "", "", "", "", ""));
            this.notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            myList.remove(position);
            this.notifyDataSetChanged();
            //计算最终的总价
            double price = 0;
            for (int k = 0; k < myList.size(); k++) {
                if (!"".equals(myList.get(k).getAmount())) {
                    price += Double.valueOf(myList.get(k).getAmount());
                } else {
                    price += 0;
                }
            }
            all_price.setText(price + "");
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
            final GoodsBuyHolder goodsBuyHolder = new GoodsBuyHolder();
            view = LayoutInflater.from(GoodsBuyActivity.this).inflate(R.layout.goods_buy_item, null);
            goodsBuyHolder.mingxi = view.findViewById(R.id.mingxi_num_tv);
            goodsBuyHolder.delete = view.findViewById(R.id.delete_btn);

            goodsBuyHolder.name = view.findViewById(R.id.edit_goods_buy_name);
            goodsBuyHolder.model = view.findViewById(R.id.edit_goods_buy_model);
            goodsBuyHolder.unitprice = view.findViewById(R.id.edit_goods_buy_price);
            goodsBuyHolder.num = view.findViewById(R.id.edit_goods_buy_num);
            goodsBuyHolder.allprice = view.findViewById(R.id.goods_all_price);
            goodsBuyHolder.mess = view.findViewById(R.id.edit_goods_use_reason);

            goodsBuyHolder.name.setText(myList.get(i).getNam() + "");
            goodsBuyHolder.model.setText(myList.get(i).getTyp() + "");
            goodsBuyHolder.unitprice.setText(myList.get(i).getPrice() + "");
            goodsBuyHolder.num.setText(myList.get(i).getNum() + "");
            goodsBuyHolder.allprice.setText(myList.get(i).getAmount() + "");
            goodsBuyHolder.mess.setText(myList.get(i).getPurpose() + "");


            if (myList.size() == 1) {
                goodsBuyHolder.delete.setVisibility(View.GONE);
            } else {
                goodsBuyHolder.delete.setVisibility(View.VISIBLE);
            }

            int a = i + 1;
            goodsBuyHolder.mingxi.setText("物品领用明细" + "(" + a + ")");
            //删除
            goodsBuyHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myList.size() != 1) {
                        deleteItem(i);
                    }
                }
            });

            //物品名称
            goodsBuyHolder.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    myList.get(i).setNam(editable + "");
                }
            });

            //规格型号
            goodsBuyHolder.model.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    myList.get(i).setTyp(editable + "");
                }
            });
            //单价
            goodsBuyHolder.unitprice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    //删除“.”后面超过2位后的数据
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > point) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + point+1);
                            goodsBuyHolder.unitprice.setText(s);
                            goodsBuyHolder.unitprice.setSelection(s.length()); //光标移到最后
                        }
                    }
                    //如果"."在起始位置,则起始位置自动补0
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        goodsBuyHolder.unitprice.setText(s);
                        goodsBuyHolder.unitprice.setSelection(2);
                    }

                    //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            goodsBuyHolder.unitprice.setText(s.subSequence(0, 1));
                            goodsBuyHolder.unitprice.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //判断输入单价是不是正确的类型
                    if (DoubleUtils.myDoubleC(editable + "")) {
                        if (editable.toString().contains(".")) {
                            if (editable.length() - 1 - editable.toString().indexOf(".") > point) {
                                CharSequence sequence1 = editable.toString().subSequence(0, editable.toString().indexOf(".") + point + 1);
                                myList.get(i).setPrice(sequence1 + "");//保存数据
                            }else {
                                myList.get(i).setPrice(editable + "");//保存数据
                            }
                        }else {
                            myList.get(i).setPrice(editable + "");//保存数据
                        }

//
                    } else {
                        myList.get(i).setPrice("0");//保存单价数据
                        Toast.makeText(GoodsBuyActivity.this, "请输入正确单价", Toast.LENGTH_SHORT).show();
                    }

                    if (!"".equals(goodsBuyHolder.num.getText().toString())) {
                        myList.get(i).setNum(goodsBuyHolder.num.getText().toString());
                    } else {
                        myList.get(i).setNum("0");
                    }

                    //计算单价乘数量
                    double unitPrice = Double.valueOf(myList.get(i).getPrice());
                    double num = Double.valueOf(myList.get(i).getNum());
                    String allPrice = String.format("%.1f", unitPrice * num);
                    ;//保留1位小数点

                    myList.get(i).setAmount(allPrice + "");
                    goodsBuyHolder.allprice.setText(allPrice);


                    //计算最终的总价
                    double price = 0;
                    for (int k = 0; k < myList.size(); k++) {
                        if (!"".equals(myList.get(k).getAmount())) {
                            price += Double.valueOf(myList.get(k).getAmount());
                        } else {
                            price += 0;
                        }
                    }

                    all_price.setText(price + "");
                }
            });
            //物品数量
            goodsBuyHolder.num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text=editable.toString();
                    int len=editable.toString().length();
                    if (len>1&&text.startsWith("0")){
                        editable.replace(0,1,"");
                    }
                    //判断输入单价是不是正确的类型
                    if (DoubleUtils.myDoubleC(goodsBuyHolder.unitprice.getText().toString())) {
                        myList.get(i).setPrice(goodsBuyHolder.unitprice.getText().toString() + "");//保存单价数据
                    } else {
                        myList.get(i).setPrice("0");//保存单价数据

                    }

                    if (!"".equals(goodsBuyHolder.num.getText().toString())) {
                        myList.get(i).setNum(goodsBuyHolder.num.getText().toString());
                    } else {
                        myList.get(i).setNum("0");
                    }

                    //计算单价乘数量
                    double unitPrice = Double.valueOf(myList.get(i).getPrice());
                    double num = Double.valueOf(myList.get(i).getNum());
                    String allPrice = String.format("%.1f", unitPrice * num);
                    ;//保留两位小数点
                    //double d=unitPrice*num;
                    //String allPrice=String.valueOf(d);
                    myList.get(i).setAmount(allPrice + "");
                    goodsBuyHolder.allprice.setText(allPrice);


                    //计算最终的总价
                    double price = 0;
                    for (int k = 0; k < myList.size(); k++) {
                        if (!"".equals(myList.get(k).getAmount())) {
                            price += Double.valueOf(myList.get(k).getAmount());
                        } else {
                            price += 0;
                        }
                    }

                    all_price.setText(price + "");
                }
            });

            //物品用途
            goodsBuyHolder.mess.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    myList.get(i).setPurpose(editable + "");
                }
            });
            return view;
        }

        class GoodsBuyHolder {
            TextView mingxi, delete, allprice;
            EditText name, model, unitprice, num, mess;
        }
    }
}
