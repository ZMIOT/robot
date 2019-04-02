package ldu.guofeng.imdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.BookInfoAdapter;
import ldu.guofeng.imdemo.adapter.HotMovieAdapter;
import ldu.guofeng.imdemo.bean.Book;
import ldu.guofeng.imdemo.bean.HotMovie;
import ldu.guofeng.imdemo.util.HttpUtil;

public class HotMovieActivity extends AppCompatActivity {
    private static final String KGURL = "http://134.175.105.227:8080/HelloWeb/HotMovie";
    private String HotMovieMark;
    private List<HotMovie> HotMovies=new ArrayList<HotMovie>();
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_movie);
        new Thread(new HotMovieThread()).start();
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    HotMovieAdapter HotMovieAdapter=new HotMovieAdapter(HotMovieActivity.this,R.layout.item_hotmovie,HotMovies);
                    ListView listView=(ListView)findViewById(R.id.hotMovielist);
                    listView.setAdapter(HotMovieAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HotMovie hotMovie= HotMovies.get(position);
                            pref=getSharedPreferences("data",MODE_PRIVATE);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    private void getIntentInfo(){
        Intent intent=getIntent();
        HotMovieMark=intent.getStringExtra("HotMovieMark");

    }

    public class HotMovieThread implements Runnable{
        final HashMap<String,String> params=new HashMap<>();
        public HashMap<String,String> getParams(){
            params.put("HotMovieStr","HotMovie");
            params.put("HotMovieMark",HotMovieMark);
            return params;
        }

        @Override
        public void run() {

            String response= HttpUtil.get(KGURL);
            try{
                JSONArray jsonArray =new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject!=null && jsonArray.length()>0) {
                        String imgurl= URLDecoder.decode(jsonObject.getString("imgurl"),"UTF-8");
                        String title=URLDecoder.decode(jsonObject.getString("title"),"UTF-8");
                        HotMovie HotMovie=new HotMovie(title,jsonObject.getString("score"),imgurl);
                        HotMovies.add(HotMovie);
                    }
                    else
                    {
                        Log.i("jsonArray","error");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Message message =new Message();
            message.what=1;
            handler.sendMessage(message);
        }
    }
}
