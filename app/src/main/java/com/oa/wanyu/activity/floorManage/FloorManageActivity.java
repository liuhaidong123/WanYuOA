package com.oa.wanyu.activity.floorManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import com.oa.wanyu.activity.leaseManage.AlreadyLeaseActivity;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivity;
import com.oa.wanyu.bean.FloorAddressRoot;
import com.oa.wanyu.bean.FloorAddressRows;
import com.oa.wanyu.bean.FloorNumRoot;
import com.oa.wanyu.bean.FloorNumRows;
import com.oa.wanyu.bean.FloorUnitRoot;
import com.oa.wanyu.bean.FloorUnitRows;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.util.ArrayList;
import java.util.List;

//楼盘管理--》查询页面
public class FloorManageActivity extends AppCompatActivity {
    private RelativeLayout mAll;
    private ImageView mBack_img;
    private TextView mAddress_tv, mFloor_tv, mUnit_tv, mSelect_btn;

    private AlertDialog.Builder address_builder, floor_builder, unit_builder;
    private AlertDialog address_alertDialog, floor_alertDialog, unit_alertDialog;
    private ListView mAddressListView, mFloorListView, mUnitListView;
    private AddressAdapter addressAdapter;
    private FloorAdapter floorAdapter;
    private UnitAdapter unitAdapter;
    private List<FloorAddressRows> addressList = new ArrayList();
    private List<FloorNumRows> floorList = new ArrayList();
    private List<FloorUnitRows> unitList = new ArrayList();
    private long buildingID = -1;//小区ID
    private String houseNum = "";//楼号
    private String unit = "";//单元

    private String url, numUrl, unitUrl;//小区，楼号，单元接口
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
                    Object o = gson.fromJson(str, FloorAddressRoot.class);
                    if (o != null && o instanceof FloorAddressRoot) {
                        FloorAddressRoot floorAddressRoot = (FloorAddressRoot) o;
                        if (floorAddressRoot != null) {
                            if ("0".equals(floorAddressRoot.getCode())) {

                                if (floorAddressRoot.getRows() != null) {

                                    if (floorAddressRoot.getRows().size() == 0) {
                                        Toast.makeText(FloorManageActivity.this, "暂未获取小区列表", Toast.LENGTH_SHORT).show();
                                    }
                                    addressList = floorAddressRoot.getRows();
                                    addressAdapter.notifyDataSetChanged();

                                }


                            } else if ("-1".equals(floorAddressRoot.getCode())) {
                                Toast.makeText(FloorManageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        Toast.makeText(FloorManageActivity.this, "获取小区列表失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FloorManageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 2) {//楼号
                try {
                    String str = (String) msg.obj;
                    Object o = gson.fromJson(str, FloorNumRoot.class);
                    if (o != null && o instanceof FloorNumRoot) {
                        FloorNumRoot floorNumRoot = (FloorNumRoot) o;
                        if (floorNumRoot != null) {
                            if ("0".equals(floorNumRoot.getCode())) {

                                if (floorNumRoot.getRows() != null) {

                                    if (floorNumRoot.getRows().size() == 0) {
                                        Toast.makeText(FloorManageActivity.this, "暂未获取楼号信息", Toast.LENGTH_SHORT).show();
                                    }
                                    floorList = floorNumRoot.getRows();
                                    floorAdapter.notifyDataSetChanged();

                                }


                            } else if ("-1".equals(floorNumRoot.getCode())) {
                                Toast.makeText(FloorManageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        Toast.makeText(FloorManageActivity.this, "获取楼号信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FloorManageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }

            } else if (msg.what == 3) {
                try {
                    String str = (String) msg.obj;
                    Object o = gson.fromJson(str, FloorUnitRoot.class);
                    if (o != null && o instanceof FloorUnitRoot) {
                        FloorUnitRoot floorUnitRoot = (FloorUnitRoot) o;
                        if (floorUnitRoot != null) {
                            if ("0".equals(floorUnitRoot.getCode())) {

                                if (floorUnitRoot.getRows() != null) {

                                    if (floorUnitRoot.getRows().size() == 0) {
                                        Toast.makeText(FloorManageActivity.this, "暂未获取单元信息", Toast.LENGTH_SHORT).show();
                                    }
                                    unitList = floorUnitRoot.getRows();
                                    unitAdapter.notifyDataSetChanged();
                                }

                            } else if ("-1".equals(floorUnitRoot.getCode())) {
                                Toast.makeText(FloorManageActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        Toast.makeText(FloorManageActivity.this, "获取单元信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FloorManageActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_manage);
        mAll = (RelativeLayout) findViewById(R.id.activity_floor_manage);
        initUI();

        url = URLTools.urlBase + URLTools.floor_address_list;
        okHttpManager = OkHttpManager.getInstance();
        okHttpManager.getMethod(false, url, "地址接口", handler, 1);

        numUrl = URLTools.urlBase + URLTools.floor_num_list;  //楼号接口
        unitUrl = URLTools.urlBase + URLTools.floor_unit_list;//单元列表接口
    }

    private void initUI() {
        mBack_img = (ImageView) findViewById(R.id.back_img);
        mBack_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAddress_tv = (TextView) findViewById(R.id.address_mess_tv);
        mAddress_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressList.size() == 0) {
                    BallProgressUtils.showLoading(FloorManageActivity.this, mAll);
                    okHttpManager.getMethod(false, url, "小区接口", handler, 1);
                    Toast.makeText(FloorManageActivity.this, "正在查询数据，请稍等。。。", Toast.LENGTH_SHORT).show();
                } else {
                    address_alertDialog.show();
                }

            }
        });
        mFloor_tv = (TextView) findViewById(R.id.floor_mess_tv);
        mFloor_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (buildingID != -1) {
                    if (floorList.size() == 0) {
                        BallProgressUtils.showLoading(FloorManageActivity.this, mAll);
                        okHttpManager.getMethod(false, numUrl + "buildingId=" + buildingID, "楼号接口", handler, 2);
                        Toast.makeText(FloorManageActivity.this, "正在查询数据，请稍等。。。", Toast.LENGTH_SHORT).show();
                    } else {
                        floor_alertDialog.show();
                    }
                } else {
                    Toast.makeText(FloorManageActivity.this, "请先选择小区", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mUnit_tv = (TextView) findViewById(R.id.unit_mess_tv);
        mUnit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!"".equals(houseNum)) {
                    if (unitList.size() == 0) {
                        BallProgressUtils.showLoading(FloorManageActivity.this, mAll);
                        okHttpManager.getMethod(false, unitUrl + "buildingId=" + buildingID + "&houseNum=" + houseNum, "单元列表接口", handler, 3);
                        Toast.makeText(FloorManageActivity.this, "正在查询数据，请稍等。。。", Toast.LENGTH_SHORT).show();
                    } else {
                        unit_alertDialog.show();
                    }
                } else {
                    Toast.makeText(FloorManageActivity.this, "请先选择楼号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSelect_btn = (TextView) findViewById(R.id.select_submit);
        mSelect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (buildingID != -1 && !"".equals(houseNum) && !"".equals(unit)) {
                    Intent intent = new Intent(FloorManageActivity.this, FloorMessageActivity.class);
                    intent.putExtra("building", buildingID);
                    intent.putExtra("address", mAddress_tv.getText().toString());
                    intent.putExtra("house", houseNum);
                    intent.putExtra("unit", unit);
                    startActivity(intent);
                } else {
                    Toast.makeText(FloorManageActivity.this, "请先选择楼号", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //==楼盘地址
        address_builder = new AlertDialog.Builder(this);
        address_alertDialog = address_builder.create();
        View addressview = LayoutInflater.from(this).inflate(R.layout.address_alert, null);
        address_alertDialog.setView(addressview);
        mAddressListView = addressview.findViewById(R.id.address_listview_id);
        addressAdapter = new AddressAdapter();
        mAddressListView.setAdapter(addressAdapter);
        mAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAddress_tv.setText(addressList.get(i).getName());
                buildingID = addressList.get(i).getId();
                okHttpManager.getMethod(false, numUrl + "buildingId=" + buildingID, "楼号接口", handler, 2);
                address_alertDialog.dismiss();
            }
        });


        //====楼号
        floor_builder = new AlertDialog.Builder(this);
        floor_alertDialog = floor_builder.create();
        View floorview = LayoutInflater.from(this).inflate(R.layout.floor_alert, null);
        floor_alertDialog.setView(floorview);
        mFloorListView = floorview.findViewById(R.id.floor_listview_id);
        floorAdapter = new FloorAdapter();
        mFloorListView.setAdapter(floorAdapter);
        mFloorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mFloor_tv.setText(floorList.get(i).getHouseNum() + "号楼");
                houseNum = floorList.get(i).getHouseNum();
                okHttpManager.getMethod(false, unitUrl + "buildingId=" + buildingID + "&houseNum=" + houseNum, "单元列表接口", handler, 3);
                floor_alertDialog.dismiss();
            }
        });
        //====单元
        unit_builder = new AlertDialog.Builder(this);
        unit_alertDialog = unit_builder.create();
        View unitview = LayoutInflater.from(this).inflate(R.layout.unit_alert, null);
        unit_alertDialog.setView(unitview);
        mUnitListView = unitview.findViewById(R.id.unit_listview_id);
        unitAdapter = new UnitAdapter();
        mUnitListView.setAdapter(unitAdapter);
        mUnitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mUnit_tv.setText(unitList.get(i).getUint() + "单元");
                unit = unitList.get(i).getUint();
                unit_alertDialog.dismiss();
            }
        });
    }

    class AddressAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return addressList.size();
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
            AddressHolder addressHolder = null;
            if (view == null) {
                addressHolder = new AddressHolder();
                view = LayoutInflater.from(FloorManageActivity.this).inflate(R.layout.leave_item, null);
                addressHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(addressHolder);
            } else {
                addressHolder = (AddressHolder) view.getTag();
            }
            addressHolder.tv.setText(addressList.get(i).getName() + "");

            return view;
        }

        class AddressHolder {
            TextView tv;
        }
    }

    class FloorAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return floorList.size();
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
            FloorHolder floorHolder = null;
            if (view == null) {
                floorHolder = new FloorHolder();
                view = LayoutInflater.from(FloorManageActivity.this).inflate(R.layout.leave_item, null);
                floorHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(floorHolder);
            } else {
                floorHolder = (FloorHolder) view.getTag();
            }

            floorHolder.tv.setText(floorList.get(i).getHouseNum() + "号楼");
            return view;
        }

        class FloorHolder {
            TextView tv;
        }
    }

    class UnitAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return unitList.size();
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
            UnitHolder unitHolder = null;
            if (view == null) {
                unitHolder = new UnitHolder();
                view = LayoutInflater.from(FloorManageActivity.this).inflate(R.layout.leave_item, null);
                unitHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(unitHolder);
            } else {
                unitHolder = (UnitHolder) view.getTag();
            }
            unitHolder.tv.setText(unitList.get(i).getUint() + "单元");

            return view;
        }

        class UnitHolder {
            TextView tv;
        }
    }
}
