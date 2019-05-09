package com.oa.wanyu.activity.invalidActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.aBusinessTravelActivity.AbusinessTravelMessageActivity;
import com.oa.wanyu.activity.approvalActivity.ApprovalActivity;
import com.oa.wanyu.activity.currencyApply.CurrencyApplyActivityMessageActivity;
import com.oa.wanyu.activity.goodsBuyActivity.GoodsBuyActivityMessageActivity;
import com.oa.wanyu.activity.goodsUseActivity.GoodsUseActivityMessageActivity;
import com.oa.wanyu.activity.leave.LeaveActivityMessageActivity;
import com.oa.wanyu.activity.outActivity.OutActivityMessageActivity;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivityMessageActivity;
import com.oa.wanyu.bean.applystatus.ApplyRoot;
import com.oa.wanyu.bean.applystatus.ApplyRows;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.oa.wanyu.myutils.CircleImg;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//已失效
public class InvalidActivity extends AppCompatActivity {
    private ImageView mbcak;
    private ListView mListView;
    private SmartRefreshLayout smartRefreshLayout;
    private InvalidAda invalidAda;
    private int flag=1;
    private RelativeLayout mAll_RL,no_data_rl;
    private TextView no_mess_tv;
    private List<ApplyRows> mList=new ArrayList<>();
    private int refresh=0;//0默认刷新，1为加载数据
    private int start=0,limit=20;
    private OkHttpManager okHttpManager;
    private Gson gson=new Gson();
    private String url;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what==1){
                try{
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ApplyRoot.class);
                    if (o!=null&&o instanceof ApplyRoot){
                        ApplyRoot applyRoot= (ApplyRoot) o;
                        if (applyRoot!=null){
                            if ("0".equals(applyRoot.getCode())){
                                if (applyRoot.getRows()!=null){

                                    if (refresh==0){//刷新
                                        if (applyRoot.getRows().size()==0){
                                            Toast.makeText(InvalidActivity.this, "暂无失效申请", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("空空如也");
                                        }else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList=applyRoot.getRows();

                                    }else {//加载
                                        for (int i = 0; i < applyRoot.getRows().size(); i++) {
                                            mList.add(applyRoot.getRows().get(i));
                                        }

                                        if (applyRoot.getRows().size()==0){
                                            Toast.makeText(InvalidActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    invalidAda.notifyDataSetChanged();
                                }


                            }else if ("-1".equals(applyRoot.getCode())){
                                Toast.makeText(InvalidActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(InvalidActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            }else {
                //数据错误
                Toast.makeText(InvalidActivity.this, "网络错误，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid);

        mbcak = (ImageView) findViewById(R.id.back_img);
        mbcak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        no_data_rl=(RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh=0;
                start=0;
                url= URLTools.urlBase+URLTools.apply_all_status+"msgStatus="+3+"&start="+start+"&limit="+limit;
                BallProgressUtils.showLoading(InvalidActivity.this,mAll_RL);
                okHttpManager.getMethod(false,url,"已失效列表",handler,1);
            }
        });
        no_mess_tv= (TextView) findViewById(R.id.no_mess_tv);
        mAll_RL = (RelativeLayout) findViewById(R.id.activity_invalid);

        url= URLTools.urlBase+URLTools.apply_all_status+"msgStatus="+3+"&start="+start+"&limit="+limit;
        okHttpManager=OkHttpManager.getInstance();
        okHttpManager.getMethod(false,url,"已失效列表",handler,1);

        mListView = (ListView) findViewById(R.id.invalid_listview_id);
        invalidAda = new InvalidAda();
        mListView.setAdapter(invalidAda);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mList.get(i).getMsgType() == 35) {//出差详情页面
                    Intent intent = new Intent(InvalidActivity.this, AbusinessTravelMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 15) {//请假
                    Intent intent = new Intent(InvalidActivity.this, LeaveActivityMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 20) {//外出
                    Intent intent = new Intent(InvalidActivity.this, OutActivityMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 30) {//报销
                    Intent intent = new Intent(InvalidActivity.this, ReimbursementActivityMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 25) {//物品领用
                    Intent intent = new Intent(InvalidActivity.this, GoodsUseActivityMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 10) {//物品申购
                    Intent intent = new Intent(InvalidActivity.this, GoodsBuyActivityMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 40) {//通用申请
                    Intent intent = new Intent(InvalidActivity.this, CurrencyApplyActivityMessageActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("id",mList.get(i).getReferId());
                    startActivity(intent);
                }
            }
        });
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.invalid_smartRefresh);
        smartRefreshLayout.setRefreshHeader(new CircleHeader(this));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(this, R.color.color_1c82d4)));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refresh=0;
                start=0;
                url= URLTools.urlBase+URLTools.apply_all_status+"msgStatus="+3+"&start="+start+"&limit="+limit;
                okHttpManager.getMethod(false,url,"失效列表",handler,1);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh=1;
                start+=20;
                url= URLTools.urlBase+URLTools.apply_all_status+"msgStatus="+3+"&start="+start+"&limit="+limit;
                okHttpManager.getMethod(false,url,"失效列表",handler,1);
            }
        });

        //延迟5，加载动画消失
        BallProgressUtils.showLoading(InvalidActivity.this, mAll_RL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BallProgressUtils.dismisLoading();
            }
        }, 1000);
    }

    class InvalidAda extends BaseAdapter{

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            InvalidHolder invalidHolder = null;
            if (view == null) {
                invalidHolder = new InvalidHolder();
                view = LayoutInflater.from(InvalidActivity.this).inflate(R.layout.approval_listview_item, null);
                invalidHolder.type_img = view.findViewById(R.id.type_img);
                invalidHolder.status = view.findViewById(R.id.status_tv);
                invalidHolder.name = view.findViewById(R.id.persson_name);
                invalidHolder.bumen = view.findViewById(R.id.bumen_name);
                invalidHolder.job = view.findViewById(R.id.job_name);
                invalidHolder.time = view.findViewById(R.id.time_name);
                invalidHolder.circleImg = view.findViewById(R.id.person_head_img);
                invalidHolder.title_rl=view.findViewById(R.id.title_rl);
                view.setTag(invalidHolder);
            } else {
                invalidHolder = (InvalidHolder) view.getTag();
            }
            invalidHolder.status.setText("已失效");
            invalidHolder.title_rl.setBackgroundResource(R.drawable.ysx_bg);

            if (mList.get(i).getMsgType()==35){//出差申请图片
                invalidHolder.type_img.setImageResource(R.mipmap.chucha_item);
            }else if (mList.get(i).getMsgType()==15){//请假图片
                invalidHolder.type_img.setImageResource(R.mipmap.jia_item);
            }else if (mList.get(i).getMsgType()==20){//外出图片
                invalidHolder.type_img.setImageResource(R.mipmap.waichu_item);
            }else if (mList.get(i).getMsgType()==30){//报销
                invalidHolder.type_img.setImageResource(R.mipmap.baoxiao_item);
            }else if (mList.get(i).getMsgType()==25){//物品领用图片
                invalidHolder.type_img.setImageResource(R.mipmap.lingyong_item);
            }else if (mList.get(i).getMsgType()==10){//物品申购图片
                invalidHolder.type_img.setImageResource(R.mipmap.shengou_item);
            }else if (mList.get(i).getMsgType()==40){//通用申请图片
                invalidHolder.type_img.setImageResource(R.mipmap.tongyong_item);
            }


            invalidHolder.name.setText(mList.get(i).getTrueName()+"");
            if ("".equals(mList.get(i).getDepartmentName())){
                invalidHolder.bumen.setText("暂无");
            }else {
                invalidHolder.bumen.setText(mList.get(i).getDepartmentName()+"");
            }

            if ("".equals(mList.get(i).getPosition())){
                invalidHolder.job.setText("暂无");
            }else {
                invalidHolder.job.setText(mList.get(i).getPosition()+"");
            }

            invalidHolder.time.setText(mList.get(i).getCreateTimeString()+"");
            Picasso.with(InvalidActivity.this).load(URLTools.urlBase+mList.get(i).getAvatar()).error(R.mipmap.head_img_icon).into(invalidHolder.circleImg);
            return view;
        }

        class InvalidHolder {
            ImageView type_img;
            TextView status, name, bumen, job, time;
            CircleImg circleImg;
            RelativeLayout title_rl;
        }
    }
}
