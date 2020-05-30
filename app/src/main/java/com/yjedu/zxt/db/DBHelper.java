package com.yjedu.zxt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jtxx_jt.db";
    private static final int DATABASE_VERSION = 2;  // 注意要填写正整数。

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i=1;i<=DATABASE_VERSION;i++)
        {
            ExeSqlByVersion(db,i);
        }

    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
        for(int i = oldVersion+1;i<=newVersion;i++)
        {
            ExeSqlByVersion(db,i);
        }
    }

    // 根据版本号执行Sql语句
    public void ExeSqlByVersion(SQLiteDatabase db,int ver)
    {
        switch(ver)
        {
            case 1:
                db.execSQL("CREATE TABLE IF NOT EXISTS person" +
                        "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT)");
                db.execSQL("CREATE TABLE IF NOT EXISTS logdata ( _id INTEGER   PRIMARY KEY   AUTOINCREMENT , logtype VARCHAR , logtitle VARCHAR , logcontent VARCHAR , appeartime INTEGER , createtime DATETIME , edittime DATETIME )");

                break;
            case 2:


                break;
        }


    }

}
