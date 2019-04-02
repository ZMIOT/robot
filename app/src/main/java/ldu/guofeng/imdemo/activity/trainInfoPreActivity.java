package ldu.guofeng.imdemo.activity;

import android.content.Intent;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.BookInfoAdapter;
import ldu.guofeng.imdemo.adapter.TrainInfoAdapter;
import ldu.guofeng.imdemo.bean.Book;
import ldu.guofeng.imdemo.bean.TrainInfo;
import ldu.guofeng.imdemo.bean.TrainTicket;

import static android.support.constraint.R.id.parent;
import static com.baidu.mapapi.BMapManager.getContext;


public class trainInfoPreActivity extends AppCompatActivity {

    private static String Base_url="http://api.12306.com/v1/train/trainInfos?";
    private String URL;
   // private static String URL="http://api.12306.com/v1/train/trainInfos?arrStationCode=CSQ&deptDate=2019-04-02&deptStationCode=GLZ&findGD=false";
    private  int PAGE_NUM=1;
    private String deptStationCode;
    private String arrStationCode;
    private String deptDate;
    private CardView cardView;

    List<TrainInfo> trainInfoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_info_pre);
        initView();
        getTrainIntent();
        fetchData();


    }
    private Handler trainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    TrainInfoAdapter trainInfoAdapter=new TrainInfoAdapter(trainInfoPreActivity.this,R.layout.item_train_info,trainInfoList);
                    ListView listView=(ListView)findViewById(R.id.trainInfoPre);
                    listView.setAdapter(trainInfoAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TrainInfo trainInfo=trainInfoList.get(position);
                        }
                    });
                    break;
            }
        }
    };

    private void initView(){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.item_train_info,null);
        cardView=(CardView)view.findViewById(R.id.cardView);
        cardView.setRadius(8);//设置图片圆角的半径大小

        cardView.setCardElevation(8);//设置阴影部分大小

        cardView.setContentPadding(5,5,5,5);//设置图片距离阴影大小
    }

    private void getTrainIntent(){
        Intent intent=getIntent();
        deptStationCode=intent.getStringExtra("deptStationCode");
        arrStationCode=intent.getStringExtra("arrStationCode");
        deptDate=intent.getStringExtra("deptTime");
        URL=Base_url+"arrStationCode="+arrStationCode+"&deptDate="+deptDate+"&deptStationCode="+deptStationCode+"&findGD=false";
    }

    private void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    String[] date=getDate(PAGE_NUM);
                    //拼接URL
                    // URL = URL + "&start=" + date[0] + "&end=" + date[1];
                    Connection.Response response = Jsoup
                            .connect(URL)
                            .header("Accept","*/*")
                            .header("Accept-Encoding","gzip, deflate")
                            .header("Accept-Language","en-US,en;q=0.9,zh;q=0.8")
                            .header("Content-Type","application/json")
                            .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                            .header("Referer","http://www.12306.com/")
                            .timeout(10000).ignoreContentType(true).execute();
                    String body=response.body();
                    JSONObject jsonObject=new JSONObject(body);
                    JSONArray TrainArray=jsonObject.getJSONObject("data").getJSONArray("trainInfos");
                    for(int i=0;i<TrainArray.length();i++){
                        JSONObject jobj=TrainArray.getJSONObject(i);
                        if(jobj!=null && TrainArray.length()>0){
                            String trainCode=jobj.getString("trainCode");
                            String deptStationName=jobj.getString("deptStationName");
                            String arrStation=jobj.getString("arrStationName");
                            String deptTime=jobj.getString("deptTime");
                            String arrTime=jobj.getString("arrTime");
                            String runTime=jobj.getString("runTime");
                            String arrDate=jobj.getString("arrDate");
                            List<TrainTicket> tickets=new ArrayList<TrainTicket>();
                            JSONArray seatInfos=jobj.getJSONArray("seatList");
                            for(int j=0;j<seatInfos.length();j++){
                                JSONObject seatInfo=seatInfos.getJSONObject(j);
                                TrainTicket trainTicket=new TrainTicket();
                                trainTicket.setSeatName(seatInfo.getString("seatName"));
                                trainTicket.setPrice(seatInfo.getString("seatPrice"));
                                trainTicket.setTicketNum(seatInfo.getString("seatNum"));
                                tickets.add(trainTicket);
                            }

                            TrainInfo trainInfo=new TrainInfo(deptDate,trainCode,deptTime,arrTime,deptStationName,arrStation,arrDate,runTime,tickets);
                            trainInfoList.add(trainInfo);
                        }
                    }
                    final List<TrainInfo> datas = parseHtml(doc);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //更新列表
                            parseDataFinish(datas);
                        }
                    });
                }catch (Exception e){
                    e.getMessage();
                }
                Message message=new Message();
                message.what=1;
                trainHandler.sendMessage(message);
            }


        }).start();
    }

    /***
     * 更新数据
     */
    private void parseDataFinish(List<TrainInfo> data){

    }


    /***
     * 解析网页
     */
    private List<TrainInfo> parseHtml(Document doc){
        List<TrainInfo> trainInfos=new ArrayList<>();

        return trainInfos;
    }

    /**
     * 计算每一页的开始、结束日期（默认相差30天），例如2017-05-18到2017-06-17
     *
     * @param num
     * @return
     */
    private String[] getDate(int num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, (-30 * (num - 1) - (num - 1)));
        String end = sdf.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, -30);
        String start = sdf.format(calendar.getTime());

        return new String[]{start, end};
    }
}
