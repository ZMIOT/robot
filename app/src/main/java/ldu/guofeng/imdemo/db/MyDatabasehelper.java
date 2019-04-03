package ldu.guofeng.imdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/4/2.
 */

public class MyDatabasehelper extends SQLiteOpenHelper{
    public static final String  CREATE_TALK="create table ChatLog("
            +"id integer primary key autoincrement,"
            +"speaker text,"
            +"msgType text,"
            +"txtContent text)";

    private Context mContext;


    public MyDatabasehelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TALK);
        Log.i("oncreate:","here");
        Toast.makeText(mContext,"create successed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists ChatLog");
        onCreate(db);

    }
}
