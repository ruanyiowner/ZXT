/*
 * 设计者：LQJ
 * 邮箱：869067911@qq.com
 */
package com.yjedu.zxt.db.base;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yjedu.zxt.db.DBHelper;
import com.yjedu.zxt.db.model.Person;
import com.yjedu.zxt.db.model.base.ModelBase;
import com.yjedu.zxt.db.model.base.ModelSetup;
public class DALFactory<T extends ModelBase> extends DALBase<T>  {
    private DBHelper helper;
    private SQLiteDatabase db;

    /*
    public String TableName = "";
    public String PKey="";
    public Type PKeyDataType;
    */
    //public ModelSetup Setup=null;
    Class<T> classT;

    public T model= null;

    public DALFactory(Class<T> t,Context context){
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();

        try {
            classT=t;
            model = classT.newInstance();
            //Setup=classT.newInstance().Setup;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 查询记录条数
     * @param where  要带where
     */
    public long Count(String where)
    {
        long c=0;
        String cmdText=String.format("select count(1) as cnt from [%s] %s", model.Setup.TableName, where);
        Cursor cursor = db.rawQuery(cmdText,null);
        cursor.moveToFirst();
        c = cursor.getLong(0);
        return c;
    }

    /**
     * 删除记录
     * @param
     */
    public boolean Delete(String pKeyValue)
    {
        boolean isOK = false;
        String whereClause = model.Setup.PKey+"=?";
        isOK=db.delete(model.Setup.TableName, whereClause, new String[]{pKeyValue})>0;
        return isOK;
    }
    /**
     * 添加记录
     * @param
     */
    public boolean AddModel(T mdl)
    {
        boolean isOK = false;
        isOK = db.insert(mdl.Setup.TableName, null, mdl.ColumnsAffectedCV)>0;
        return isOK;
    }


    public boolean UpdateModel(T mdl)
    {
        boolean isOK = false;
        isOK = db.update(mdl.Setup.TableName, mdl.ColumnsAffectedCV, mdl.Setup.PKey+"=?", new String[]{mdl.PKeyValue.toString()})>0;
        return isOK;
    }

    public T GetModel(Object pKeyValue)
    {
        T m = null;

        try {
            m = classT.newInstance();
            m.Setup = model.Setup;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String cmdText = "select %s from [%s] where [%s]=%s";
        if(model.Setup.PKeyDataType.equals(String.class))
        {
            cmdText = "select %s from [%s] where [%s]='%s'";
        }



        cmdText = String.format(cmdText, GetColumnString(),model.Setup.TableName,model.Setup.PKey,pKeyValue.toString());
        Cursor c = db.rawQuery(cmdText, null);
        if(c.moveToNext())
        {
            m.SetValueByCursor(c);
        }
        c.close();

        return m;
    }

    public List<T> GetModels(List<Object> pKeyValueList){
        StringBuilder ValueSB = new StringBuilder();
        for(int i=0;i<pKeyValueList.size();i++){
            if(i>0) ValueSB.append(",");
            if(model.Setup.PKeyDataType.equals(String.class)){
                ValueSB.append("'"+pKeyValueList.get(i)+"'");
            }else{
                ValueSB.append(pKeyValueList.get(i).toString());
            }
        }
        return GetModels(ValueSB.toString());
    }

    public List<T> GetModels(Object... pKeyValues){
        StringBuilder ValueSB = new StringBuilder();
        for(int i=0;i<pKeyValues.length;i++){
            if(i>0) ValueSB.append(",");
            if(model.Setup.PKeyDataType.equals(String.class)){
                ValueSB.append("'"+pKeyValues[i].toString()+"'");
            }else{
                ValueSB.append(pKeyValues[i].toString());
            }
        }
        return GetModels(ValueSB.toString());

    }
    /**
     * 获取多个记录   参数值如："1,2,3" 或者"'1','2','3'"
     * @param
     */
    public List<T> GetModels(String pKeyValuesStr){
        ArrayList<T> list= new ArrayList<T>();
        String cmdText="select %s from [%s] where [%s] in (%s)";
        cmdText = String.format(cmdText, GetColumnString(),model.Setup.TableName,model.Setup.PKey,pKeyValuesStr);
        Cursor c = db.rawQuery(cmdText, null);
        while (c.moveToNext()) {
            T m = null;

            try {
                m = classT.newInstance();
                m.Setup = model.Setup;
                m.SetValueByCursor(c);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            list.add(m);
        }
        c.close();
        return list;
    }

    /**
     * 分页获取数据
     * @param pageSize 一页数据记录数
     * @param pageIndex 页面索引号,为0开始。
     * @param where 查询条件注意带where
     * @param orderBy 排序
     */
    public List<T> GetPageModels(int pageSize,int pageIndex,String where,String orderBy)
    {
        ArrayList<T> list= new ArrayList<T>();
        String cmdText = String.format("select %s from [%s] %s %s LIMIT %s OFFSET %s", GetColumnString(),model.Setup.TableName,where,orderBy,pageSize,pageIndex*pageSize);
        Cursor c = db.rawQuery(cmdText, null);
        while (c.moveToNext()) {
            T m = null;

            try {
                m = classT.newInstance();
                m.Setup = model.Setup;
                m.SetValueByCursor(c);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            list.add(m);
        }
        c.close();

        return list;
    }



    /**
     * 获取列查询字符串
     * @param
     */
    private String GetColumnString(){
        StringBuilder colSB = new StringBuilder();
        if(null!=model.Setup.ColumnsAffected){
            for(int i=0;i<model.Setup.ColumnsAffected.size();i++)
            {
                if(i>0)colSB.append(",");
                colSB.append("["+model.Setup.ColumnsAffected.get(i)+"]");
            }
        }
        if(colSB.length()<=0)
        {
            colSB.append("*");
        }
        return colSB.toString();
    }

}
