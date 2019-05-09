package com.oa.wanyu.activity.floorManage;

import android.content.Intent;
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
import android.widget.TextView;

import com.oa.wanyu.R;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivity;

import java.util.ArrayList;
import java.util.List;

//楼盘管理--》查询页面
public class FloorManageActivity extends AppCompatActivity {

    private ImageView mBack_img;
    private TextView mAddress_tv,mFloor_tv,mUnit_tv,mSelect_btn;

    private AlertDialog.Builder address_builder,floor_builder,unit_builder;
    private AlertDialog address_alertDialog,floor_alertDialog,unit_alertDialog;
    private ListView mAddressListView,mFloorListView,mUnitListView;
    private AddressAdapter addressAdapter;
    private FloorAdapter floorAdapter;
    private UnitAdapter unitAdapter;
    private List<String> addressList=new ArrayList();
    private List<String> floorList=new ArrayList();
    private List<String> unitList=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_manage);
        initUI();
    }

    private void initUI(){
        mBack_img= (ImageView) findViewById(R.id.back_img);
        mBack_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAddress_tv= (TextView) findViewById(R.id.address_mess_tv);
        mFloor_tv= (TextView) findViewById(R.id.floor_mess_tv);
        mUnit_tv= (TextView) findViewById(R.id.unit_mess_tv);

        mSelect_btn= (TextView) findViewById(R.id.select_submit);
        mSelect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FloorManageActivity.this,FloorMessageActivity.class);
                startActivity(intent);
            }
        });

        //==楼盘地址
        address_builder=new AlertDialog.Builder(this);
        address_alertDialog=address_builder.create();
        View addressview = LayoutInflater.from(this).inflate(R.layout.address_alert, null);
        address_alertDialog.setView(addressview);
        mAddressListView=addressview.findViewById(R.id.address_listview_id);
        addressAdapter=new AddressAdapter();
        mAddressListView.setAdapter(addressAdapter);
        mAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

//        mAddressListViewess = addressview.findViewById(R.id.reimbursment_listview_id);
//        rTypeAda = new RTypeAda();
//        alertListView.setAdapter(rTypeAda);
//        alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //选择类型以后，需要重新设置数据
//                mList.get(mPosition).setTyp(typeList.get(i));
//                reimbursementAda.notifyDataSetChanged();
//                alertDialog.dismiss();
//            }
//        });


            //====楼号
        floor_builder=new AlertDialog.Builder(this);
        floor_alertDialog=floor_builder.create();
        View floorview = LayoutInflater.from(this).inflate(R.layout.floor_alert, null);
        floor_alertDialog.setView(floorview);
        mFloorListView=floorview.findViewById(R.id.floor_listview_id);
        floorAdapter=new FloorAdapter();
        mFloorListView.setAdapter(floorAdapter);
        mFloorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        //====单元
        unit_builder=new AlertDialog.Builder(this);
        unit_alertDialog=   unit_builder.create();
        View unitview = LayoutInflater.from(this).inflate(R.layout.unit_alert, null);
        unit_alertDialog.setView(unitview);
        mUnitListView=floorview.findViewById(R.id.unit_listview_id);
        unitAdapter=new UnitAdapter();
        mUnitListView.setAdapter(unitAdapter);
        mUnitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    class  AddressAdapter extends BaseAdapter{
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
            AddressHolder addressHolder=null;
            if (view==null){
                addressHolder=new AddressHolder();
                view = LayoutInflater.from(FloorManageActivity.this).inflate(R.layout.leave_item, null);
                addressHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(addressHolder);
            }else {
                addressHolder= (AddressHolder) view.getTag();
            }


            return view;
        }
        class AddressHolder{
            TextView tv;
        }
    }

    class  FloorAdapter extends BaseAdapter{
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
            FloorHolder floorHolder=null;
            if (view==null){
                floorHolder=new FloorHolder();
                view = LayoutInflater.from(FloorManageActivity.this).inflate(R.layout.leave_item, null);
                floorHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(floorHolder);
            }else {
                floorHolder= (FloorHolder) view.getTag();
            }


            return view;
        }
        class FloorHolder{
            TextView tv;
        }
    }

    class  UnitAdapter extends BaseAdapter{
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
            UnitHolder unitHolder=null;
            if (view==null){
                unitHolder=new UnitHolder();
                view = LayoutInflater.from(FloorManageActivity.this).inflate(R.layout.leave_item, null);
                unitHolder.tv = view.findViewById(R.id.leave_);
                view.setTag(unitHolder);
            }else {
                unitHolder= (UnitHolder) view.getTag();
            }


            return view;
        }
        class UnitHolder{
            TextView tv;
        }
    }
}
