package ldu.guofeng.imdemo.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.AboutActivity;
import ldu.guofeng.imdemo.activity.ChatActivity;
import ldu.guofeng.imdemo.activity.LoginActivity;
import ldu.guofeng.imdemo.db.MyDatabasehelper;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.view.CustomPopWindow;

/**
 * 设置页
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_clear_msg;
    private Context mContext;//上下文
    private View view;//设置view
    private RelativeLayout about;//作者
    private RelativeLayout exit;//退出
    private TextView name;//昵称
    private MyDatabasehelper dbhelper;
    private ImageView cleanChat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_setting, container);
        findView();
        init();
        initName();
        return view;
    }


    private void findView() {
        about = (RelativeLayout) view.findViewById(R.id.rl_about);
        exit = (RelativeLayout) view.findViewById(R.id.rl_app_exit);
        name = (TextView) view.findViewById(R.id.name);
        cleanChat=(ImageView)view.findViewById(R.id.cleanChat);
        rl_clear_msg=(RelativeLayout)view.findViewById(R.id.rl_msg_clear);
    }

    private void init() {
        //about.setOnClickListener(this);
        exit.setOnClickListener(this);
        cleanChat.setOnClickListener(this);
        rl_clear_msg.setOnClickListener(this);

    }

    private void initName() {
        //设置昵称
        name.setText(PreferencesUtils.getInstance().getString("username"));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_about:
                intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_app_exit:
                showPopView(exit);
                break;
            case R.id.rl_msg_clear:
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("确认要删除聊天记录吗？");
                alertDialogBuilder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbhelper=new MyDatabasehelper(getActivity(),"ChatLog.db",null,1);
                        SQLiteDatabase db=dbhelper.getWritableDatabase();
                        String sql="delete from ChatLog";
                        db.execSQL(sql);
                    }

                });

                alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.dismiss();
                alertDialog.show();
                break;
        }
    }

    private void showPopView(RelativeLayout exit) {
        View popview = LayoutInflater.from(mContext).inflate(R.layout.pop_exit, null);
        //创建并显示popWindow
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                .setView(popview)
                .setAnimationStyle(android.R.style.Animation_InputMethod)
                .create()
                .showAtLocation(exit, Gravity.BOTTOM, 0, 0);
        initPop(popWindow, popview);

    }

    public void initPop(final CustomPopWindow popWindow, View popview) {
        TextView exit = (TextView) popview.findViewById(R.id.exit);//退出
        TextView cancel = (TextView) popview.findViewById(R.id.cancel);//取消
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /**
                 * 断开连接逻辑
                 */
                SmackUtils.getInstance().exitConnect();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                getActivity().finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dissmiss();
            }
        });
    }
}
