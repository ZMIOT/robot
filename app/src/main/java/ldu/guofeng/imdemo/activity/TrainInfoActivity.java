package ldu.guofeng.imdemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.BookInfoAdapter;
import ldu.guofeng.imdemo.bean.Book;
import ldu.guofeng.imdemo.bean.TrainInfo;
import ldu.guofeng.imdemo.util.HttpUtil;

import static android.provider.Contacts.SettingsColumns.KEY;

public class TrainInfoActivity extends AppCompatActivity  implements View.OnClickListener, DatePicker.OnDatePickedListener{

    private StringBuffer date, time;
    private int year, month, day, hour, minute;
    private LinearLayout ll_date;
    private TextView tvDate;
    private String deptTime;
    private EditText deptStation;
    private EditText arrStation;
    private Button searchCode;
    private String stationStr;
    private String deptStationCode;
    private String arrStationCode;
    private int isDept=0;
    private int isArr=0;
    List<HotCity> hotCities = new ArrayList<>();
    private int anim;
    private int theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            theme = savedInstanceState.getInt(KEY);
            setTheme(theme > 0 ? theme : R.style.DefaultCityPickerTheme);
        }
        setContentView(R.layout.activity_train_info);
        date = new StringBuffer();
        time = new StringBuffer();
        initDateTime();
        initView();

        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrainInfoActivity.this,R.style.AlertDialogCustom);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvDate.setText(date);
                        deptTime=date.toString();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();

                View dialogView = View.inflate(getApplicationContext(), R.layout.dialog_date, null);
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                datePicker.setDate(year,month);
                datePicker.setMode(DPMode.SINGLE);
                datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String da) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        date.append(da);
                    }
                });
                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
//                //初始化日期监听事件

            }
        });
        searchCode.setOnClickListener(this);
        deptStation.setOnClickListener(this);
        arrStation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seachTrain:
                Intent intent=new Intent(TrainInfoActivity.this, trainInfoPreActivity.class);
                intent.putExtra("deptTime",deptTime);
                intent.putExtra("deptStationCode",deptStationCode);
                intent.putExtra("arrStationCode",arrStationCode);
                startActivity(intent);
                break;
            case R.id.deptStation:
                stationStr="";
                isDept=0;
                isArr=0;
                CityPicker.from(TrainInfoActivity.this) //activity或者fragment
                        .enableAnimation(true)	//启用动画效果，默认无
                        .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101"))  //APP自身已定位的城市，传null会自动定位（默认）
                        .setHotCities(hotCities)	//指定热门城市
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, City data) {
                                /*Toast.makeText(getApplicationContext(), data.getName(), Toast.LENGTH_SHORT).show();*/
                                deptStation.setText(data.getName());
                                stationStr=deptStation.getText().toString();
                                isDept=1;
                                new Thread(new StationsThread()).start();
                            }

                            @Override
                            public void onCancel(){
                                Toast.makeText(getApplicationContext(), "取消选择", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLocate() {
                                //定位接口，需要APP自身实现，这里模拟一下定位
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //定位完成之后更新数据
                                        //CityPicker.getInstance().locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                                    }
                                }, 3000);
                            }
                        })
                        .show();
                break;
            case R.id.arrStation:
                stationStr="";
                isArr=0;
                isDept=0;
                CityPicker.from(TrainInfoActivity.this) //activity或者fragment
                        .enableAnimation(true)	//启用动画效果，默认无
                        .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101"))  //APP自身已定位的城市，传null会自动定位（默认）
                        .setHotCities(hotCities)	//指定热门城市
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, City data) {
                               /* Toast.makeText(getApplicationContext(), data.getName(), Toast.LENGTH_SHORT).show();*/
                                arrStation.setText(data.getName());
                                stationStr=arrStation.getText().toString();
                                isArr=1;
                                new Thread(new StationsThread()).start();
                            }

                            @Override
                            public void onCancel(){
                                Toast.makeText(getApplicationContext(), "取消选择", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLocate() {
                                //定位接口，需要APP自身实现，这里模拟一下定位
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //定位完成之后更新数据
                                        //CityPicker.getInstance().locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                                    }
                                }, 3000);
                            }
                        })
                        .show();
                break;
        }
    }

    private class StationsThread implements Runnable{

        private HashMap<String,String> getParams(){
            HashMap<String,String> params=new HashMap<>();
            params.put("StationStr",stationStr);
            return params;
        }
            @Override
            public void run() {
                String res= HttpUtil.post("http://134.175.105.227:8080/HelloWeb/CityCodeLet",getParams());
                try{
                    JSONArray jsonArray =new JSONArray(res);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String stationName=URLDecoder.decode(jsonObject.getString("stationName"),"UTF-8");
                        String stationCode=jsonObject.getString("stationCode");
                        if(isDept==1){
                            deptStationCode=stationCode;
                        }
                        if(isArr==1){
                            arrStationCode=stationCode;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

    }

    private void initView(){
        ll_date=(LinearLayout)findViewById(R.id.ll_date);
        tvDate=(TextView)findViewById(R.id.tvDate);
        deptStation=(EditText)findViewById(R.id.deptStation);
        arrStation=(EditText)findViewById(R.id.arrStation);
        searchCode=(Button)findViewById(R.id.seachTrain);
    }

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onDatePicked(String date) {
        tvDate.setText(date);
    }
}
