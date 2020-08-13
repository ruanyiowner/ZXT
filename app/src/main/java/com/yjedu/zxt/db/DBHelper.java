package com.yjedu.zxt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 1;  // 注意要填写正整数。


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
        android.util.Log.v("MyDebug"," Create db.getVersion："+db.getVersion());
    }

    //如果DATABASE_VERSION值被改为新版本，比如原来是1，现改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
        android.util.Log.v("MyDebug","旧版本："+oldVersion+" 目前新版本："+newVersion+"  db.getVersion："+db.getVersion());

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
                // 创建人员表
                db.execSQL("CREATE TABLE IF NOT EXISTS person" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT)");
                // 创建日志表
                db.execSQL("CREATE TABLE IF NOT EXISTS logdata ( ID INTEGER   PRIMARY KEY   AUTOINCREMENT , logtype VARCHAR , logtitle VARCHAR , logcontent VARCHAR , appeartime INTEGER , createtime DATETIME , edittime DATETIME )");

                // 创建储物柜格子表
                db.execSQL("CREATE TABLE IF NOT EXISTS Lattice (ID INTEGER PRIMARY KEY AUTOINCREMENT,LatticeNo VARCHAR,Remark VARCHAR)");

                // 创建物品表
                db.execSQL("CREATE TABLE IF NOT EXISTS Goods (ID INTEGER PRIMARY KEY AUTOINCREMENT,IDCode VARCHAR,LatticeNo VARCHAR,PhoneNumber VARCHAR,PutInTime DATETIME) ");
                break;
            case 2:


                break;
        }


    }

}
