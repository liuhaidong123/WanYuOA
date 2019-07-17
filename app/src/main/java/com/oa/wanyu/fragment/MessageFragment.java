package com.oa.wanyu.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.SP_Activity.SPActivity;
import com.oa.wanyu.activity.aBusinessTravelActivity.AbusinessTravelMessageActivity;
import com.oa.wanyu.activity.completeActivity.CompleteActivity;
import com.oa.wanyu.activity.currencyApply.CurrencyApplyActivityMessageActivity;
import com.oa.wanyu.activity.goodsBuyActivity.GoodsBuyActivityMessageActivity;
import com.oa.wanyu.activity.goodsUseActivity.GoodsUseActivityMessageActivity;
import com.oa.wanyu.activity.leave.LeaveActivityMessageActivity;
import com.oa.wanyu.activity.notice_activity.NoticeMessageActivity;
import com.oa.wanyu.activity.outActivity.OutActivityMessageActivity;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivityMessageActivity;
import com.oa.wanyu.activity.shopsManage.ShopsManageMessageActivity;
import com.oa.wanyu.bean.NoticeBean;
import com.oa.wanyu.bean.NoticeRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.bean.approvastatus.ApprovalRoot;
import com.oa.wanyu.bean.viewpager_img.ImgBean;
import com.oa.wanyu.bean.viewpager_img.ImgRoot;
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

/**
 * 消息
 */
public class MessageFragment extends Fragment {

    private RollPagerView rollPagerView;
    private List<ImgBean> imgList=new ArrayList<>();
    private String img_url;
    private PagerAdapter pagerAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private ListView mListView;
    private int mPosition, mSignPosition;//长按删除时，标记下标,标记为已读
    private List<NoticeBean> mList = new ArrayList<>();
    private int refresh = 0;//0默认刷新，1为加载数据
    private RelativeLayout no_data_rl;
    private TextView no_mess_tv;
    private MyAdapter myAdapter;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private int start = 0, limit = 30;
    private String url, url_delete, url_sign;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            no_data_rl.setEnabled(true);
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, NoticeRoot.class);
                    if (o != null && o instanceof NoticeRoot) {
                        NoticeRoot noticeRoot = (NoticeRoot) o;
                        if (noticeRoot != null) {
                            if ("0".equals(noticeRoot.getCode())) {
                                if (noticeRoot.getRows() != null) {

                                    if (refresh == 0) {//刷新
                                        if (noticeRoot.getRows().size() == 0) {
                                            Toast.makeText(getActivity(), "暂无消息", Toast.LENGTH_SHORT).show();
                                            no_data_rl.setVisibility(View.VISIBLE);
                                            no_mess_tv.setText("空空如也");
                                        } else {
                                            no_data_rl.setVisibility(View.GONE);
                                            no_mess_tv.setText("");
                                        }
                                        mList = noticeRoot.getRows();

                                    } else {//加载
                                        for (int i = 0; i < noticeRoot.getRows().size(); i++) {
                                            mList.add(noticeRoot.getRows().get(i));
                                        }

                                        Toast.makeText(getActivity(), "加载完毕", Toast.LENGTH_SHORT).show();
                                    }

                                    myAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(noticeRoot.getCode())) {
                                Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else if (msg.what == 2) {//提交删除
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                mList.remove(mPosition);
                                myAdapter.notifyDataSetChanged();

                            } else if ("-1".equals(successBean.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            } else if ("2".equals(successBean.getCode())) {
                                Toast.makeText(getActivity(), successBean.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 3) {//标记为已读
                mList.get(mSignPosition).setRead(true);
                myAdapter.notifyDataSetChanged();
            }else if (msg.what == 4){//轮播图
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ImgRoot.class);
                    if (o != null && o instanceof ImgRoot) {
                        ImgRoot imgRoot = (ImgRoot) o;
                        if (imgRoot != null) {
                            if ("0".equals(imgRoot.getCode())) {
                                if (imgRoot.getRows() != null) {

                                    if (imgRoot.getRows().size()==0){
                                        Toast.makeText(getActivity(), "暂无图片", Toast.LENGTH_SHORT).show();
                                    }else {
                                        imgList=imgRoot.getRows();
                                    }

                                    pagerAdapter.notifyDataSetChanged();
                                }


                            } else if ("-1".equals(imgRoot.getCode())) {
                                Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }else {
                                Toast.makeText(getActivity(), "错误信息"+imgRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText( "错误信息"+imgRoot.getMessage());
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }
            }

            else {
                //数据错误
                Toast.makeText(getActivity(), "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }


        }
    };

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {

        okHttpManager = OkHttpManager.getInstance();
        url = URLTools.urlBase + URLTools.notice_list + "start=" + start + "&limit=" + limit;
        url_delete = URLTools.urlBase + URLTools.notice_delete;//删除
        url_sign = URLTools.urlBase + URLTools.notice_sign;//标记为已读
        okHttpManager.getMethod(false, url, "消息列表", handler, 1);
//轮播图
        rollPagerView = view.findViewById(R.id.my_rollpagerview);
        rollPagerView.setPlayDelay(3000);
        rollPagerView.setAnimationDurtion(500);
        img_url=URLTools.urlBase + URLTools.viewpager_img;//轮播图
        okHttpManager.getMethod(false, img_url, "轮播图片", handler, 4);
        pagerAdapter=new PagerAdapter(rollPagerView);
        rollPagerView.setAdapter(pagerAdapter);
        rollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.BLUE, Color.WHITE));


        no_data_rl = (RelativeLayout) view.findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_data_rl.setEnabled(false);
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.notice_list + "start=" + start + "&limit=" + limit;
                BallProgressUtils.showLoading(getActivity(), no_data_rl);
                okHttpManager.getMethod(false, url, "消息列表", handler, 1);
            }
        });
        no_mess_tv = (TextView) view.findViewById(R.id.no_mess_tv);

        //同意
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除此通知?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                okHttpManager.getMethod(false, url_delete + "id=" + mList.get(mPosition).getId(), "删除通知", handler, 2);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();


        mListView = view.findViewById(R.id.listview_notify);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
//        长按删除
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPosition = i;
                alertDialog.show();
                return true;
            }
        });
//        查看详情
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSignPosition = i;
                if (!mList.get(i).isRead()) {
                    okHttpManager.getMethod(false, url_sign + "id=" + mList.get(i).getId(), "标记", handler, 3);
                }


                if (mList.get(i).getMsgType() == 35) {//出差详情页面
                    Intent intent = new Intent(getActivity(), AbusinessTravelMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);//10表示从消息、被驳回、已完成、已失效、待审批、已审批中跳过去的，不显示撤回按钮；20表示从自己提交申请的审批中跳转过去的，显示撤回按钮。
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 15) {//请假
                    Intent intent = new Intent(getActivity(), LeaveActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 20) {//外出
                    Intent intent = new Intent(getActivity(), OutActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 30) {//报销
                    Intent intent = new Intent(getActivity(), ReimbursementActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 25) {//物品领用
                    Intent intent = new Intent(getActivity(), GoodsUseActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 10) {//物品申购
                    Intent intent = new Intent(getActivity(), GoodsBuyActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 40) {//通用申请
                    Intent intent = new Intent(getActivity(), CurrencyApplyActivityMessageActivity.class);
                    intent.putExtra("withdraw_flag",10);
                    intent.putExtra("id", mList.get(i).getReferId());
                    startActivity(intent);
                } else if (mList.get(i).getMsgType() == 0) {//公告详情
                    Intent intent = new Intent(getActivity(), NoticeMessageActivity.class);
                    intent.putExtra("title", mList.get(i).getTitle() + "");
                    intent.putExtra("content", mList.get(i).getContent() + "");
                    startActivity(intent);
                }
            }
        });

        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        smartRefreshLayout.setRefreshHeader(new CircleHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(ContextCompat.getColor(getContext(), R.color.color_1c82d4)));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refresh = 0;
                start = 0;
                url = URLTools.urlBase + URLTools.notice_list + "start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "消息列表", handler, 1);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                refresh = 1;
                start += 20;
                url = URLTools.urlBase + URLTools.notice_list + "start=" + start + "&limit=" + limit;
                okHttpManager.getMethod(false, url, "消息列表", handler, 1);
            }
        });
    }

    /**
     * 消息适配器
     */

    class MyAdapter extends BaseAdapter {

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
            MyHolder myHolder = null;
            if (view == null) {
                myHolder = new MyHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.mess_item, null);
                myHolder.red_circle = view.findViewById(R.id.red_circle_img);
                myHolder.img = view.findViewById(R.id.img);
                myHolder.title = view.findViewById(R.id.title);
                myHolder.mess = view.findViewById(R.id.message);
                myHolder.date = view.findViewById(R.id.date);
                view.setTag(myHolder);
            } else {
                myHolder = (MyHolder) view.getTag();
            }

            if (mList.get(i).isRead()) {//已读
                myHolder.red_circle.setVisibility(View.GONE);
            } else {
                myHolder.red_circle.setVisibility(View.VISIBLE);
            }
            if (mList.get(i).getMsgType() == 0) {
                myHolder.img.setImageResource(R.mipmap.job_gonggao_icon);
            } else if (mList.get(i).getMsgType() == 35) {//出差申请图片
                myHolder.img.setImageResource(R.mipmap.chucha);
            } else if (mList.get(i).getMsgType() == 15) {//请假图片
                myHolder.img.setImageResource(R.mipmap.qingjia);
            } else if (mList.get(i).getMsgType() == 20) {//外出图片
                myHolder.img.setImageResource(R.mipmap.waichu);
            } else if (mList.get(i).getMsgType() == 30) {//报销
                myHolder.img.setImageResource(R.mipmap.baoxiao);
            } else if (mList.get(i).getMsgType() == 25) {//物品领用图片
                myHolder.img.setImageResource(R.mipmap.wu_lingyong);
            } else if (mList.get(i).getMsgType() == 10) {//物品申购图片
                myHolder.img.setImageResource(R.mipmap.wu_shenggou);
            } else if (mList.get(i).getMsgType() == 40) {//通用申请图片
                myHolder.img.setImageResource(R.mipmap.tongyong);
            }
            myHolder.title.setText(mList.get(i).getTitle() + "");
            myHolder.mess.setText(mList.get(i).getContent() + "");
            myHolder.date.setText(mList.get(i).getCreateTimeString() + "");
            return view;
        }

        class MyHolder {
            ImageView red_circle;
            CircleImg img;
            TextView title, mess, date;

        }
    }

    /**
     * 轮播图适配器
     */

    class PagerAdapter extends LoopPagerAdapter{


        public PagerAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Picasso.with(getActivity()).load(URLTools.urlBase+imgList.get(position).getPicture()).error(R.mipmap.errorpicture).into(view);
            //view.setImageResource();
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


            return view;
        }

        @Override
        public int getRealCount() {
            return imgList.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rollPagerView.pause();
        rollPagerView.resume();
        rollPagerView.isPlaying();
    }
}
