package com.oa.wanyu.activity.floorManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.oa.wanyu.activity.leaseManage.AlreadyLeaseActivity;
import com.oa.wanyu.bean.FloorAddressNumUnitRoot;
import com.oa.wanyu.bean.FloorAddressNumUnitRows;
import com.oa.wanyu.bean.FloorAddressRoot;
import com.oa.wanyu.myutils.BallProgressUtils;
import com.scwang.smartrefresh.header.CircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//楼盘详情
public class FloorMessageActivity extends AppCompatActivity {
    private RelativeLayout mAll;
    private ImageView mback_img;
    private TextView mTitle_tv;
    private GridView mGridView;
    private MyAdapter myAdapter;
    private List<FloorAddressNumUnitRows> mList = new ArrayList<>();

    private long buildingID = -1;//小区ID
    private String address = "";
    private String houseNum = "";//楼号
    private String unit = "";//单元
    private String url;
    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {//小区接口
                try {
                    String str = (String) msg.obj;
                    Object o = gson.fromJson(str, FloorAddressNumUnitRoot.class);
                    if (o != null && o instanceof FloorAddressNumUnitRoot) {
                        FloorAddressNumUnitRoot floorAddressNumUnitRoot = (FloorAddressNumUnitRoot) o;
                        if (floorAddressNumUnitRoot != null) {
                            if ("0".equals(floorAddressNumUnitRoot.getCode())) {

                                if (floorAddressNumUnitRoot.getRows() != null) {

                                    if (floorAddressNumUnitRoot.getRows().size() == 0) {
                                        Toast.makeText(FloorMessageActivity.this, "暂未获取所有楼房信息", Toast.LENGTH_SHORT).show();
                                    }
                                    mList = floorAddressNumUnitRoot.getRows();
                                    Collections.reverse(mList);
                                    myAdapter.notifyDataSetChanged();

                                }


                            } else if ("-1".equals(floorAddressNumUnitRoot.getCode())) {
                                Toast.makeText(FloorMessageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(FloorMessageActivity.this, "错误信息:"+floorAddressNumUnitRoot.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }


                    } else {
                        Toast.makeText(FloorMessageActivity.this, "获取楼房信息失败", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(FloorMessageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(FloorMessageActivity.this, "连接服务器失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_message);
        mAll = (RelativeLayout) findViewById(R.id.activity_floor_message);
        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle_tv = (TextView) findViewById(R.id.sq_title);

        mGridView = (GridView) findViewById(R.id.floor_gridview_id);
        myAdapter = new MyAdapter();
        mGridView.setAdapter(myAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mList.get(i).getState()!=0){
                    Intent intent = new Intent(FloorMessageActivity.this, FloorHouseInformationActivity.class);
                    intent.putExtra("id",mList.get(i).getId());
                    intent.putExtra("state",mList.get(i).getState());
//                intent.putExtra("address",address);
//                intent.putExtra("house",houseNum);
//                intent.putExtra("unit",unit);
//                intent.putExtra("roomNum",mList.get(i).getRoomNum());
                    startActivityForResult(intent,1);
                }else {
                    Toast.makeText(FloorMessageActivity.this, "此房屋不存在,请选择其他房屋", Toast.LENGTH_SHORT).show();

                }
            }
        });


        url = URLTools.urlBase + URLTools.floor_all_house;
        okHttpManager = OkHttpManager.getInstance();
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        buildingID = intent.getLongExtra("building", -1);
        houseNum = intent.getStringExtra("house");
        unit = intent.getStringExtra("unit");

        if (buildingID != -1 && !"".equals(houseNum) && !"".equals(unit)) {
            mTitle_tv.setText(address + "-" + houseNum + "号楼-" + unit + "单元");
            BallProgressUtils.showLoading(this, mAll);
            okHttpManager.getMethod(false, url + "buildingId=" + buildingID + "&houseNum=" + houseNum + "&uint=" + unit, "所有单元接口", handler, 1);
        } else {
            Toast.makeText(FloorMessageActivity.this, "获取房屋楼号信息失败，请重新尝试", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK){
            BallProgressUtils.showLoading(this, mAll);
            okHttpManager.getMethod(false, url + "buildingId=" + buildingID + "&houseNum=" + houseNum + "&uint=" + unit, "所有单元接口", handler, 1);
        }
    }

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
                view = LayoutInflater.from(FloorMessageActivity.this).inflate(R.layout.floor_gridview_item, null);
                myHolder.tv = view.findViewById(R.id.floor_num);
                view.setTag(myHolder);
            } else {
                myHolder = (MyHolder) view.getTag();
            }

            if (mList.get(i).getState() == 0) {
                myHolder.tv.setText("");//假数据
            } else {
                myHolder.tv.setText(mList.get(i).getRoomNum() + "");
            }

            if (mList.get(i).getState() == 30) {//可售
                myHolder.tv.setBackgroundResource(R.color.color_b9b9b9);
            } else if (mList.get(i).getState() == 50) {//已售
                myHolder.tv.setBackgroundResource(R.color.color_8bc34a);
            } else if (mList.get(i).getState() == 45) {//预定
                myHolder.tv.setBackgroundResource(R.color.color_fda570);
            }

            return view;
        }

        class MyHolder {
            TextView tv;
        }
    }

}
