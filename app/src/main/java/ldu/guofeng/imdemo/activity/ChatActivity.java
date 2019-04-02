package ldu.guofeng.imdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.ChatAdapter;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.bean.Book;
import ldu.guofeng.imdemo.bean.ItemModel;
import ldu.guofeng.imdemo.bean.MsgModel;
import ldu.guofeng.imdemo.bean.SessionModel;
import ldu.guofeng.imdemo.bean.kdJSON;
import ldu.guofeng.imdemo.bean.kdString;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.HttpUtil;
import ldu.guofeng.imdemo.util.HttpUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.util.ToastUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 聊天
 */
public class ChatActivity extends CustomReturnToolbar implements View.OnClickListener {

    private Context mContext;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private String txtContent;
    private String resultText;
    private String form, to;
    private String kdResult;

    private EditText et_message;//文本输入框
    private TextView tv_send;//发送标签
    private ImageView chat_more;//用于切换键盘与功能面板
    private LinearLayout chat_more_container;//功能面板布局
    private TextView send_loc;//发送位置标签
    private MsgModel msgModelAll;
    private String kdtext;
    private int kdExsit=0;
    int count=0;

    private static final String JOKESTR = "/jokeLet";

    /**
     * 订阅接收消息
     * Subscribe，其含义为订阅者。
     * 在其内传入了threadMode，我们定义为ThreadMode.MAIN，其含义是该方法在UI线程完成。
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recMsgEventBus(MsgModel msg) {
        if (msg.getFromUser().equals(to)) {
            if (msg.getType() == Constant.MSG_TYPE_TEXT) {
                //在最后插入一条item，包括布局，聊天信息
                adapter.insertLastItem(new ItemModel(ItemModel.LEFT_TEXT, msg));
            } else if (msg.getType() == Constant.MSG_TYPE_LOC) {
                adapter.insertLastItem(new ItemModel(ItemModel.LEFT_LOCATION, msg));
            }
            //滑动到最后
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void postDataWithParame() {
        if(msgModelAll==null||msgModelAll.getType()==1){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("username","zhangsan");//传递键值对参数
        JSONObject jsonObj=new JSONObject();
        try {
            jsonObj.put("reqType", 0);
            JSONObject perception=new JSONObject();
            JSONObject inputText=new JSONObject();
            JSONObject inputImage=new JSONObject();
            JSONObject selfInfo=new JSONObject();
            inputImage.put("","");
            inputText.put("text",txtContent);
            JSONObject location=new JSONObject();
            location.put("city","北京");
            location.put("province","北京");
            location.put("street","信息路");
            selfInfo.put("location",location);
            perception.put("inputText",inputText);
            perception.put("inputImage",inputImage);
            jsonObj.put("perception",perception);
            JSONObject userinfo=new JSONObject();
            userinfo.put("apiKey","1652f506031a497d8fe5eb211ba61b19");
            userinfo.put("userId","0441e9de22cadebe");
            jsonObj.put("userInfo",userinfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");

        RequestBody body=RequestBody.create(JSON,jsonObj.toString());
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://openapi.tuling123.com/openapi/api/v2")
                .post(body)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(true){//回调的方法执行在子线程。
                    Log.d("kwwl","获取数据成功了");
                    //Log.d("kwwl","response.code()=="+response.code());
                    //Log.d("kwwl","response.body().string()=="+response.body().string());
                    try {
                            String str=response.body().string();
                            JSONObject jsonstr=new JSONObject(str);
                            JSONArray jsonarray=jsonstr.getJSONArray("results");
                            JSONObject jobj=(JSONObject) jsonarray.get(0);
                            String values=jobj.getString("values");
                            JSONObject text=new JSONObject(values);
                            resultText=text.getString("text");
                            sendRobotMessage();
                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            }

        });//回调方法的使用与get异步请求相同，此时略。
        }
    }
    /**
     * 发送文本消息
     */
    private void sendRobotMessage() {
        if (resultText.equals("")) {
            ToastUtils.showShortToast("您输入的是空消息");
            return;
        }
        //postDataWithParame();
        final String message = form + Constant.SPLIT + to + Constant.SPLIT
                + Constant.MSG_TYPE_TEXT + Constant.SPLIT
                + resultText;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackUtils.getInstance().sendMessage(message, to);
            }
        }).start();

        //在聊天列表插入一条文本消息
        MsgModel msgModel = new MsgModel();
        msgModel.setToUser(to);
        msgModel.setType(Constant.MSG_TYPE_SERVICER);
        msgModel.setContent(resultText);
        adapter.insertLastItem(new ItemModel(ItemModel.LEFT_TEXT, msgModel));
        insertSession(msgModel);
        msgModelAll=msgModel;
        //滑动到最后,清空输入框
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        et_message.setText("");
    }



    /**
     * 发送文本消息
     */
    private void sendTextMessage() {
        if (txtContent.equals("")) {
            ToastUtils.showShortToast("您输入的是空消息");
            return;
        }

        final String message = form + Constant.SPLIT + to + Constant.SPLIT
                + Constant.MSG_TYPE_TEXT + Constant.SPLIT
                + txtContent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackUtils.getInstance().sendMessage(message, to);
            }
        }).start();

        //在聊天列表插入一条文本消息
        MsgModel msgModel = new MsgModel();
        msgModel.setToUser(to);
        msgModel.setType(Constant.MSG_TYPE_TEXT);
        msgModel.setContent(txtContent);
        adapter.insertLastItem(new ItemModel(ItemModel.RIGHT_TEXT, msgModel));
        insertSession(msgModel);
        msgModelAll=msgModel;
        if(existOrNot(txtContent)==1){
            MsgModel msgGame2048Model=new MsgModel();
            msgGame2048Model.setToUser(to);
            msgGame2048Model.setType(Constant.MSG_TYPE_MySERVICER);
            msgGame2048Model.setContent(txtContent);
            adapter.insertLastItem(new ItemModel(ItemModel.LEFT_TEXT, msgGame2048Model));
            insertSession(msgModel);
            Intent intent =new Intent(this,Game2048.class);
            startActivity(intent);
        }else if(existOrNot(txtContent)==2){
            Intent intent =new Intent(this,BookInfoActivity.class);
            intent.putExtra("bookmark","小说");
            startActivity(intent);
        }else if(existOrNot(txtContent)==3){
            new Thread(new jokeThread()).start();
        }else if(existOrNot(txtContent)==4){
            Intent intent =new Intent(this,HotMovieActivity.class);
            intent.putExtra("HotMovieMark","热门电影");
            startActivity(intent);
        }else if(existOrNot(txtContent)==5){
            kdtext=txtContent;
            new Thread(new kdThread()).start();
        }else if(existOrNot(txtContent)==6){
            Intent intent =new Intent(this,TrainInfoActivity.class);
            intent.putExtra("HotMovieMark","热门电影");
            startActivity(intent);
        }
        else{
            postDataWithParame();//调用图灵机器人的接口
        }

        //滑动到最后,清空输入框
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        et_message.setText("");
    }

    public class kdThread implements Runnable{
        @Override
        public void run() {
            String host = "https://wuliu.market.alicloudapi.com";
            String path = "/kdi";
            String method = "GET";
            Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
            Matcher m = p.matcher(kdtext);

            String kdNumber="";
            String kdName="";
            while (m.find()){
                 kdNumber=m.group();
            }
            kdName=kdtext.replace(kdNumber,"");
            /*String kdNumber=kdtext.replaceAll("[a-zA-Z]","");
            String kdName=kdtext.replaceAll("[0-9]]","");*/
            String code=nameToNic(kdName);
            System.out.println("请先替换成自己的AppCode");
            String appcode = "767986babc7345e69e318af23c739817";  // !!!替换填写自己的AppCode 在买家中心查看
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + appcode); //格式为:Authorization:APPCODE 83359fd73fe11248385f570e3c139xxx
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("no",kdNumber);// !!! 请求参数
            querys.put("type",code);// !!! 请求参数
            //JDK 1.8示例代码请在这里下载：  http://code.fegine.com/Tools.zip
            try {
                /**
                    * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 或者直接下载：
                 * http://code.fegine.com/HttpUtils.zip
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 * 相关jar包（非pom）直接下载：
                 * http://code.fegine.com/aliyun-jar.zip
                 */
                HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
                Log.d("response",response.getStatusLine()+"");
                //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
                //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
                //获取response的body
                HttpEntity entity=response.getEntity();
                if(entity==null){
                    kdResult="";
                }else {
                    kdResult=inputStreamToString(entity.getContent());
                }
                resultText="";
                JSONObject jsonObject=new JSONObject(kdResult);
                JSONObject jsonObject1=jsonObject.getJSONObject("result");
                JSONArray jsonArray=jsonObject1.getJSONArray("list");
                for(int i=0;i<jsonArray.length();i++){
                    String kdtext=" ";
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    kdString kdstring=new kdString();
                    kdstring.setTime(jsonObj.getString("time"));
                    kdstring.setStatus(jsonObj.getString("status"));
                   /* List<kdString> kdlist=new ArrayList<>();
                    kdlist.add(kdstring);*/
                    kdtext=kdtext+kdstring.getTime();
                    kdtext=kdtext+"\n";
                    kdtext=kdtext+kdstring.getStatus();
                    resultText=resultText+kdtext;
                    resultText=resultText+"\n\n";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Message message=new Message();
            message.what=1;
            jokeHandler.sendMessage(message);
        }

    }
    private String nameToNic(String name){
        if(name.indexOf("圆通")==0){
            return "YTO";
        }else if(name.indexOf("圆通")==0){
            return "EMS";
        }else if(name.indexOf("中通")==0){
            return "zto";
        }else if(name.indexOf("顺丰")==0){
            return "SF";
        }else if(name.indexOf("天天")==0){
            return "HHTT";
        }else if(name.indexOf("百事汇通")==0){
            return "HTKY";
        }else if(name.indexOf("德邦")==0){
            return "DBL";
        }else if(name.indexOf("EMS")==0){
            return "EMS";
        }else if(name.indexOf("国通")==0){
            return "GTO";
        }else if(name.indexOf("韵达")==0){
            return "yd";
        }else if(name.indexOf("申通")!=-1){
            return "sto";
        }
        else{
            return "";
        }
    }
    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e("dd", e.getLocalizedMessage(), e);
        }

        // Return full string
        return total.toString();
    }


    private Handler jokeHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sendRobotMessage();
                    break;
            }
        }
    };


    private class jokeThread implements Runnable{
        private HashMap<String,String> getParams(){
            HashMap<String,String> params=new HashMap<>();
            params.put("jokeStr","joke");
            return params;
        }
        @Override
        public void run() {
            String jokeStr;
            String res=HttpUtil.get("http://134.175.105.227:8080/HelloWeb/jokeLet");

            try{
                JSONArray jsonArray =new JSONArray(res);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject!=null && jsonArray.length()>0) {
                        String joke= URLDecoder.decode(jsonObject.getString("text"),"gbk");
                        resultText=joke;
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
            jokeHandler.sendMessage(message);
        }
    }



    /**
     * 判断字符串中是否存在某个字段
     */
    private int existOrNot(String clientStr){
        if((clientStr.indexOf("2048")!=-1)){
            return 1;
        }else if(clientStr.indexOf("书")!=-1){
            return 2;
        }
        else if(clientStr.indexOf("笑话")!=-1){
            return 3;
        }else if(clientStr.indexOf("电影")!=-1){
            return 4;
        }
        else if(clientStr.indexOf("快递")!=-1){
            return 5;
        }
        else if(clientStr.indexOf("火车票")!=-1){
            return 6;
        }
        else{
            return 0;
        }

    }

    /**
     * 在消息列表插入一条位置消息
     *
     * @param locInfo
     */
    private void insertLocMessage(String locInfo) {
        //在聊天列表插入一条位置消息
        MsgModel msgModel = new MsgModel();
        msgModel.setToUser(to);
        msgModel.setType(Constant.MSG_TYPE_LOC);
        msgModel.setContent(locInfo);
        adapter.insertLastItem(new ItemModel(ItemModel.RIGHT_LOCTION, msgModel));
        insertSession(msgModel);
    }

    /**
     * 插入会话列表一条会话
     *
     * @param msg
     */
    public void insertSession(MsgModel msg) {
        SessionModel sessionModel = new SessionModel();
        sessionModel.setType(msg.getType());
        sessionModel.setForm(msg.getToUser());
        sessionModel.setContent(msg.getContent());
        EventBus.getDefault().post(sessionModel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_message:
                hidePanelView();//隐藏面板
                break;
            case R.id.tv_send:
                hidePanelView();//隐藏面板
                sendTextMessage();//发送文本消息
                break;
            case R.id.chat_more:
                hideSoftInputView();//隐藏软键盘
                hidePanelHandler.postDelayed(hidePanelTask, 200);//显示面板
                break;
            case R.id.tv_loc:
                sendLocMessage();//发送位置
                break;
            default:
                break;
        }
    }

    /**
     * 发送位置消息
     */
    private void sendLocMessage() {
        Intent intent_loc = new Intent(this, ShareLocActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("to_user", to);
        intent_loc.putExtras(bundle);
        startActivityForResult(intent_loc, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == 200) {
                String locInfo = data.getStringExtra("my_location");
                insertLocMessage(locInfo);
            }
        }
    }

    //------------------------------------------------------
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!to.equals("") || to != null)
            getToolbar().setTitle(to);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        findView();
        init();
        initEditText();
    }

    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_send = (TextView) findViewById(R.id.tv_send);
        chat_more = (ImageView) findViewById(R.id.chat_more);
        chat_more_container = (LinearLayout) findViewById(R.id.chat_more_container);
        send_loc = (TextView) findViewById(R.id.tv_loc);
    }

    private void init() {
        //注册EventBus
        EventBus.getDefault().register(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter = new ChatAdapter(mContext));
        et_message.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        chat_more.setOnClickListener(this);
        send_loc.setOnClickListener(this);
        form = PreferencesUtils.getInstance().getString("username");
        //接收数据
        Bundle bundle = this.getIntent().getExtras();
        to = bundle.getString("to_user");
        txtContent="";
    }

    private void initEditText() {
        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //去掉空格的消息
                txtContent = s.toString().trim();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
    }

    //------------------------------------------------

    Handler hidePanelHandler = new Handler();
    Runnable hidePanelTask = new Runnable() {
        @Override
        public void run() {
            showPanelView();//隐藏面板
        }
    };

    /**
     * 隐藏面板
     */
    private void hidePanelView() {
        if (chat_more_container.getVisibility() == View.VISIBLE) {
            chat_more_container.setVisibility(View.GONE);
        }
    }

    /**
     * 显示面板
     */
    private void showPanelView() {
        if (chat_more_container.getVisibility() == View.GONE) {
            chat_more_container.setVisibility(View.VISIBLE);
        } else {
            chat_more_container.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
