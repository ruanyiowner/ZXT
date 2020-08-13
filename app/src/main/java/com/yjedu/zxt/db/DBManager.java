package com.yjedu.zxt.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yjedu.zxt.db.model.Person;
import com.yjedu.zxt.db.model.mdllogdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();

        android.util.Log.v("MyDebug","111DB db.getVersion："+db.getVersion());

    }

    public void TestAdd()
    {

        db.beginTransaction();  //开始事务
        try {

            db.execSQL("INSERT INTO logdata (logtitle,logcontent,appeartime,createtime,edittime) VALUES( ?, ?,?,?,?)", new Object[]{"111","222","2019-12-11","2019-12-11","2019-12-11"});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    /**
     * add persons
     * @param persons
     */
    public void add(List<Person> persons) {
        db.beginTransaction();  //开始事务
        try {
            for (Person person : persons) {
                db.execSQL("INSERT INTO person VALUES(null, ?, ?, ?)", new Object[]{person.name, person.age, person.info});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update person's age
     * @param person
     */
    public void updateAge(Person person) {
        ContentValues cv = new ContentValues();
        cv.put("age", person.age);

        db.update("person", cv, "name = ?", new String[]{person.name});
    }

    /**
     * delete old person
     * @param person
     */
    public void deleteOldPerson(Person person) {
        db.delete("person", "age >= ?", new String[]{String.valueOf(person.age)});
    }

    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<Person> query() {
        ArrayList<Person> persons = new ArrayList<Person>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Person person = new Person();
            person.ID = c.getInt(c.getColumnIndex("ID"));
            person.name = c.getString(c.getColumnIndex("name"));
            person.age = c.getInt(c.getColumnIndex("age"));
            person.info = c.getString(c.getColumnIndex("info"));
            persons.add(person);
        }
        c.close();
        return persons;
    }



    // logdata
    public void logdata_add(List<mdllogdata> logdatas) {
        db.beginTransaction();  //开始事务
        try {
            for (mdllogdata logdata : logdatas) {

                db.execSQL("INSERT INTO logdata VALUES(null, ?,?,?,?,?,?)", new Object[]{logdata.Get_logtype(),logdata.Get_logtitle(),logdata.Get_logcontent(),logdata.Get_appeartime(),logdata.Get_createtime(),logdata.Get_edittime()});

            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }






    // logdata
    public void logdata_add(mdllogdata model)
    {
        List<mdllogdata> logdatas = new ArrayList<mdllogdata>();
        logdatas.add(model);
        logdata_add(logdatas);
    }

    // 编辑日志数据
    public void logdata_edit(mdllogdata model)
    {
        ContentValues cv = new ContentValues();
        cv.put("logtype", model.Get_logtype());
        cv.put("logtitle", model.Get_logtitle());
        cv.put("logcontent", model.Get_logcontent());
        cv.put("appeartime", model.Get_appeartime());
        cv.put("edittime", model.Get_edittime().toString());
        db.update("logdata", cv, "_id = ?", new String[]{ String.valueOf(model.Get__id())});
    }

    public List<mdllogdata> logdata_getModelList(String condition,String orderBy)
    {
        List<mdllogdata> list = new ArrayList<mdllogdata>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor c = queryTheCursor("select * from logdata " + condition + " " + orderBy);

        while (c.moveToNext()) {
            mdllogdata model =  new mdllogdata();

            model.Set__id(c.getLong(c.getColumnIndex("_id")));
            model.Set_logtype(c.getString(c.getColumnIndex("logtype")));
            model.Set_logtitle(c.getString(c.getColumnIndex("logtitle")));
            model.Set_logcontent(c.getString(c.getColumnIndex("logcontent")));
            model.Set_appeartime(c.getLong(c.getColumnIndex("appeartime")));
            try {
                model.Set_createtime(sdf.parse(c.getString(c.getColumnIndex("createtime"))));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                model.Set_edittime(sdf.parse(c.getString(c.getColumnIndex("edittime"))));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            list.add(model);
        }
        c.close();
        return list;
    }

    public mdllogdata logdata_getFirstModel(String where,String orderBy)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor c = queryTheCursor("select  * from logdata " + where + " " + orderBy+"  limit 0,1");
        mdllogdata model = null;
        if (c.moveToNext()) {
            model = new mdllogdata();

            model.Set__id(c.getLong(c.getColumnIndex("_id")));
            model.Set_logtype(c.getString(c.getColumnIndex("logtype")));
            model.Set_logtitle(c.getString(c.getColumnIndex("logtitle")));
            model.Set_logcontent(c.getString(c.getColumnIndex("logcontent")));
            model.Set_appeartime(c.getLong(c.getColumnIndex("appeartime")));
            try {
                model.Set_createtime(sdf.parse(c.getString(c.getColumnIndex("createtime"))));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                model.Set_edittime(sdf.parse(c.getString(c.getColumnIndex("edittime"))));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        c.close();
        return model;
    }

    public Cursor queryTheCursor(String sqlString)
    {
        Cursor c = db.rawQuery(sqlString, null);
        return c;

    }


    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM person", null);
        return c;
    }


    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
