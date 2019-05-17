package com.oa.wanyu.activity.leaseManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oa.wanyu.OkHttpUtils.OkHttpManager;
import com.oa.wanyu.OkHttpUtils.URLTools;
import com.oa.wanyu.R;
import com.oa.wanyu.activity.reimbursementActivity.ReimbursementActivityMessageActivity;
import com.oa.wanyu.activity.shopsManage.ShopsManageMessageActivity;
import com.oa.wanyu.bean.ShopsMessageBean;
import com.oa.wanyu.bean.ShopsMessageRoot;
import com.oa.wanyu.bean.SuccessBean;
import com.oa.wanyu.myutils.BallProgressUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.picasso.Picasso;

//已租详情
public class AlreadyLeaseActivity extends AppCompatActivity {
    private TextView title_name, location_mess, type_mess, floor_mess, area_mess, status_mess, person_name_mess, person_phone_mess, price_mess, money_mess, ya_money_mess, start_time_mess, end_time_mess, select_history_btn;
    private ImageView mback_img;
    private RelativeLayout mAll, no_data_rl;
    private TextView no_mess_tv;
    private GridView mGridView;
    private PictureAdapter pictureAdapter;
    private List<String> imgList = new ArrayList<>();

    private OkHttpManager okHttpManager;
    private Gson gson = new Gson();
    private String url, sure_url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //sign_btn.setEnabled(true);
            BallProgressUtils.dismisLoading();
            if (msg.what == 1) {
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, ShopsMessageRoot.class);
                    if (o != null && o instanceof ShopsMessageRoot) {
                        ShopsMessageRoot shopsMessageRoot = (ShopsMessageRoot) o;
                        if (shopsMessageRoot != null) {
                            if ("0".equals(shopsMessageRoot.getCode())) {
                                ShopsMessageBean shopsMessageBean = shopsMessageRoot.getShop();
                                if (shopsMessageBean != null) {
                                    no_data_rl.setVisibility(View.GONE);
                                    no_mess_tv.setText("");
                                    if (!"".equals(shopsMessageBean.getBuildingName())) {
                                        title_name.setText(shopsMessageBean.getBuildingName() + shopsMessageBean.getRoomNum());
                                    } else {
                                        title_name.setText("未获取商铺名称");
                                    }

                                    if (!"".equals(shopsMessageBean.getAddress().trim())) {
                                        location_mess.setText(shopsMessageBean.getAddress() + "");
                                    } else {
                                        location_mess.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getHouseType().trim())) {
                                        type_mess.setText(shopsMessageBean.getHouseType() + "");
                                    } else {
                                        type_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getFloor())) {
                                        floor_mess.setText(shopsMessageBean.getFloor() + "");
                                    } else {
                                        floor_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getArea())) {
                                        area_mess.setText(shopsMessageBean.getArea() + "平米");
                                    } else {
                                        area_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getStateText())) {
                                        status_mess.setText(shopsMessageBean.getStateText() + "");
                                    } else {
                                        status_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getNam())) {
                                        person_name_mess.setText(shopsMessageBean.getNam() + "");
                                    } else {
                                        person_name_mess.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getTell())) {
                                        person_phone_mess.setText(shopsMessageBean.getTell() + "");
                                    } else {
                                        person_phone_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getPrice())) {
                                        price_mess.setText(shopsMessageBean.getPrice() + "元/天/平米");
                                    } else {
                                        price_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getRental())) {
                                        money_mess.setText(shopsMessageBean.getRental() + "元");
                                    } else {
                                        money_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getDeposit())) {
                                        ya_money_mess.setText(shopsMessageBean.getDeposit() + "元");
                                    } else {
                                        ya_money_mess.setText("--------");
                                    }
                                    if (!"".equals(shopsMessageBean.getBeginDateString())) {
                                        start_time_mess.setText(shopsMessageBean.getBeginDateString() + "");
                                    } else {
                                        start_time_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getEndDateString())) {
                                        end_time_mess.setText(shopsMessageBean.getEndDateString() + "");
                                    } else {
                                        end_time_mess.setText("--------");
                                    }

                                    if (!"".equals(shopsMessageBean.getContract())) {
                                        String[] strings = shopsMessageBean.getContract().split(";");
                                        for (int i = 0; i < strings.length; i++) {
                                            imgList.add(strings[i]);
                                        }
                                        Log.e("图片几张", imgList.size() + "");
                                        pictureAdapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Toast.makeText(AlreadyLeaseActivity.this, "商铺信息错误", Toast.LENGTH_SHORT).show();
                                    no_data_rl.setVisibility(View.VISIBLE);
                                    no_mess_tv.setText("商铺信息错误，请刷新");
                                }


                            } else if ("-1".equals(shopsMessageRoot.getCode())) {
                                Toast.makeText(AlreadyLeaseActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(AlreadyLeaseActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                    no_data_rl.setVisibility(View.VISIBLE);
                    no_mess_tv.setText("数据解析错误,请重新尝试");
                }

            } else if (msg.what == 2) {//闲置
                try {
                    String mes = (String) msg.obj;
                    Object o = gson.fromJson(mes, SuccessBean.class);
                    if (o != null && o instanceof SuccessBean) {
                        SuccessBean successBean = (SuccessBean) o;
                        if (successBean != null) {
                            if ("0".equals(successBean.getCode())) {
                                Toast.makeText(AlreadyLeaseActivity.this, "闲置成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, getIntent());
                                finish();

                            } else if ("-1".equals(successBean.getCode())) {
                                no_data_rl.setVisibility(View.VISIBLE);
                                no_mess_tv.setText("登录过期，请重新登录");
                                Toast.makeText(AlreadyLeaseActivity.this, "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(AlreadyLeaseActivity.this, "数据解析错误,请重新尝试", Toast.LENGTH_SHORT).show();
                }
            } else {
                //数据错误
                Toast.makeText(AlreadyLeaseActivity.this, "服务器连接失败，请重新尝试", Toast.LENGTH_SHORT).show();
                no_data_rl.setVisibility(View.VISIBLE);
                no_mess_tv.setText("服务器连接失败,请重新尝试");
            }
        }
    };

    private long id;
    private Toolbar toolbar;
    private ImageView show_img;

    private AlertDialog.Builder oneBuilder,tooBuilder,threeBuilder;
    private AlertDialog oneAlertDialog,twoAlertDialog,threeAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_lease);
        toolbar = (Toolbar) findViewById(R.id.bx_rl);
        setSupportActionBar(toolbar);
        title_name = (TextView) findViewById(R.id.sq_title);
        location_mess = (TextView) findViewById(R.id.location_tv_mess);
        type_mess = (TextView) findViewById(R.id.floor_type_tv_mess);
        floor_mess = (TextView) findViewById(R.id.floor_num_tv_mess);
        area_mess = (TextView) findViewById(R.id.area_tv_mess);
        status_mess = (TextView) findViewById(R.id.status_tv_mess);
        person_name_mess = (TextView) findViewById(R.id.name_tv_mess);
        person_phone_mess = (TextView) findViewById(R.id.phone_tv_mess);
        price_mess = (TextView) findViewById(R.id.price_tv_mess);
        money_mess = (TextView) findViewById(R.id.price_all_tv_mess);
        ya_money_mess = (TextView) findViewById(R.id.money_ya_tv_mess);
        start_time_mess = (TextView) findViewById(R.id.start_time_tv_mess);
        end_time_mess = (TextView) findViewById(R.id.end_time_tv_mess);

        select_history_btn = (TextView) findViewById(R.id.select_history_btn);
        select_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id!=-1){
                    Intent intent=new Intent(AlreadyLeaseActivity.this,LeaseHistoryActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    Toast.makeText(AlreadyLeaseActivity.this, "暂无商铺详情", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //显示发票大图
        show_img = (ImageView) findViewById(R.id.show_img);
        show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_img.setVisibility(View.GONE);
            }
        });
        mGridView = (GridView) findViewById(R.id.lease_gridview_id);
        pictureAdapter = new PictureAdapter();
        mGridView.setAdapter(pictureAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                show_img.setVisibility(View.VISIBLE);
                com.squareup.picasso.Picasso.with(AlreadyLeaseActivity.this).load(URLTools.urlBase + imgList.get(i)).into(show_img);
            }
        });
        okHttpManager = OkHttpManager.getInstance();
        id = getIntent().getLongExtra("id", -1);
        sure_url = URLTools.urlBase + URLTools.sign_sell;//闲置
        url = URLTools.urlBase + URLTools.shops_message + "id=" + id;
        if (id != -1) {
            okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
        } else {
            Toast.makeText(this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
        }

        no_data_rl = (RelativeLayout) findViewById(R.id.no_data_rl);
        no_data_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id != -1) {
                    BallProgressUtils.showLoading(AlreadyLeaseActivity.this, mAll);
                    okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
                } else {
                    Toast.makeText(AlreadyLeaseActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no_mess_tv = (TextView) findViewById(R.id.no_mess_tv);
        mAll = (RelativeLayout) findViewById(R.id.activity_already_lease);

        mback_img = (ImageView) findViewById(R.id.back_img);
        mback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK,getIntent());
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_1://闲置
                //Toast.makeText(this, "闲置", Toast.LENGTH_SHORT).show();
                oneBuilder=new AlertDialog.Builder(AlreadyLeaseActivity.this);
                oneBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        oneAlertDialog.dismiss();
                    }
                });

                oneBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (id != -1) {
                            BallProgressUtils.showLoading(AlreadyLeaseActivity.this, mAll);
                            Map<Object, Object> map = new HashMap<>();
                            map.put("state", "10");
                            map.put("stateText", "未租");
                            map.put("nam", "");
                            map.put("tell", "");
                            map.put("price", "");
                            map.put("rental", "");
                            map.put("deposit", "");
                            map.put("beginDate", "");
                            map.put("endDate","");
                            map.put("paidIn","");
                            map.put("comment","");
                            map.put("remind","");
                            map.put("contract","");
                            okHttpManager.postMethod(false,sure_url + "id=" + id+"&", "闲置",  map, handler, 2);

                            oneAlertDialog.dismiss();
                        } else {
                            Toast.makeText(AlreadyLeaseActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                oneBuilder.setTitle("确认闲置此商铺？请谨慎操作");
                oneAlertDialog=oneBuilder.create();
                oneAlertDialog.show();

                break;
            case R.id.menu_2://变更
               // Toast.makeText(this, "变更", Toast.LENGTH_SHORT).show();

                tooBuilder=new AlertDialog.Builder(AlreadyLeaseActivity.this);

                tooBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        twoAlertDialog.dismiss();
                    }
                });

                tooBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(AlreadyLeaseActivity.this,NoLeaseActivity.class);
                        intent.putExtra("id",id);
                        startActivityForResult(intent,11);
                    }
                });
                tooBuilder.setTitle("确认变更此商铺信息？请谨慎操作");
                twoAlertDialog=tooBuilder.create();
                twoAlertDialog.show();
                break;
            case R.id.menu_3://续约
                //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

                threeBuilder=new AlertDialog.Builder(AlreadyLeaseActivity.this);

                threeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        threeAlertDialog.dismiss();
                    }
                });

                threeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(AlreadyLeaseActivity.this,NoLeaseActivity.class);
                        intent.putExtra("id",id);
                        startActivityForResult(intent,11);
                    }
                });
                threeBuilder.setTitle("确认续约此商铺？请谨慎操作");
                threeAlertDialog=threeBuilder.create();
                threeAlertDialog.show();
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11&&resultCode==RESULT_OK){
            if (id != -1) {
                BallProgressUtils.showLoading(AlreadyLeaseActivity.this, mAll);
                imgList.clear();//
                okHttpManager.getMethod(false, url, "商铺详情", handler, 1);
            } else {
                Toast.makeText(AlreadyLeaseActivity.this, "获取商铺id错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class PictureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgList.size();
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
            PHolder pHolder = null;
            if (view == null) {
                pHolder = new PHolder();
                view = LayoutInflater.from(AlreadyLeaseActivity.this).inflate(R.layout.reimbursement_gridview_item, null);
                pHolder.imageView = view.findViewById(R.id.picture);
                view.setTag(pHolder);
            } else {
                pHolder = (PHolder) view.getTag();
            }
            Picasso.with(AlreadyLeaseActivity.this).load(URLTools.urlBase + imgList.get(i)).error(R.mipmap.errorpicture).into(pHolder.imageView);

            return view;
        }

        class PHolder {
            ImageView imageView;
        }
    }
}
