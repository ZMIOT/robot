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
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.logging.LogRecord;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.BookInfoAdapter;
import ldu.guofeng.imdemo.bean.Book;
import ldu.guofeng.imdemo.util.HttpUtil;

public class BookInfoActivity extends AppCompatActivity {
    private static final String KGURL = "http://134.175.105.227:8080/HelloWeb/BookInfolet";
    private String BookMark;
    private List<Book> Books=new ArrayList<Book>();
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        new Thread(new BookInfoThread()).start();
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BookInfoAdapter bookInfoAdapter=new BookInfoAdapter(BookInfoActivity.this,R.layout.item_bookinfo,Books);
                    ListView listView=(ListView)findViewById(R.id.bookInfoList);
                    listView.setAdapter(bookInfoAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Book book=Books.get(position);
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
        BookMark=intent.getStringExtra("bookmark");

    }

    public class BookInfoThread implements Runnable{
        final HashMap<String,String> params=new HashMap<>();
        public HashMap<String,String> getParams(){
            params.put("BookInfoStr","BookInfo");
            params.put("BookMark",BookMark);
            return params;
        }

        @Override
        public void run() {

            String response= HttpUtil.post(KGURL,getParams());
            try{
                JSONArray jsonArray =new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject!=null && jsonArray.length()>0) {
                        editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                        String imgurl=URLDecoder.decode(jsonObject.getString("imgurl"),"UTF-8");
                        String bookname=URLDecoder.decode(jsonObject.getString("bookname"),"UTF-8");
                        String author=URLDecoder.decode(jsonObject.getString("author"),"UTF-8");
                        String mark=URLDecoder.decode(jsonObject.getString("mark"),"UTF-8");
                        editor.putString("bookname",bookname);
                        editor.putString("score",jsonObject.getString("score"));
                        editor.putString("author",author);
                        editor.putString("imgurl",imgurl);
                        editor.putString("mark",mark);
                        Book book=new Book(bookname,author,jsonObject.getString("score"),imgurl,mark);
                        Books.add(book);
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
