package com.yjedu.zxt.db.model;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.database.Cursor;

import com.yjedu.zxt.db.model.base.ModelBase;
import com.yjedu.zxt.db.model.base.ModelSetup;

public class mdlperson extends ModelBase {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public void Set__id(long _id){
        super.ValidityColumn("_id");
        super.ColumnsAffectedCV.put("_id", _id);
        super.PKeyValue = _id;
    }
    public long Get__id(){
        super.ValidityColumn("_id");
        return super.ColumnsAffectedCV.getAsLong("_id");
    }


    public void Set_name(String name){
        super.ValidityColumn("name");
        super.ColumnsAffectedCV.put("name", name);
        super.PKeyValue = name;
    }
    public String Get_name(){
        super.ValidityColumn("name");
        return super.ColumnsAffectedCV.getAsString("name");
    }


    public void Set_age(long age){
        super.ValidityColumn("age");
        super.ColumnsAffectedCV.put("age", age);
        super.PKeyValue = age;
    }
    public long Get_age(){
        super.ValidityColumn("age");
        return super.ColumnsAffectedCV.getAsLong("age");
    }


    public void Set_info(String info){
        super.ValidityColumn("info");
        super.ColumnsAffectedCV.put("info", info);
        super.PKeyValue = info;
    }
    public String Get_info(){
        super.ValidityColumn("info");
        return super.ColumnsAffectedCV.getAsString("info");
    }


    public  void SetValueByCursor(Cursor c)
    {
        Method aMethod;
        try {
            if(super.Setup.ColumnsAffected==null||super.Setup.ColumnsAffected.size()==0)
            {
                super.Setup.ColumnsAffected = new ArrayList<String>();
                for(enum_person_Columns col:enum_person_Columns.values())
                {
                    super.Setup.ColumnsAffected.add(col.toString());
                }
            }

            for(String col:super.Setup.ColumnsAffected)
            {
                //aMethod = getClass().getMethod("Get_"+col,null);
                aMethod = getClass().getMethod("Get_"+col,new Class[0]);
                Type t = aMethod.getReturnType();

                aMethod = getClass().getMethod("Set_"+col,aMethod.getReturnType());

                try {
                    if(t.equals(String.class)){
                        aMethod.invoke(this,c.getString(c.getColumnIndex(col)));
                    }else if(t.equals(int.class))
                    {
                        aMethod.invoke(this,c.getInt(c.getColumnIndex(col)));
                    }else if(t.equals(long.class))
                    {
                        aMethod.invoke(this,c.getLong(c.getColumnIndex(col)));
                    }else if(t.equals(Date.class))
                    {
                        try {
                            String dateStr = c.getString(c.getColumnIndex(col));
                            if(null!=dateStr&&dateStr.trim()!=""){
                                aMethod.invoke(this,sdf.parse(dateStr));
                            }
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            throw new Exception("未知的类型。");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public mdlperson(){
        super.PKeyValue="";
        super.Setup = new ModelSetup();
        super.Setup.TableName = "person";
        super.Setup.PKey = "_id";
        super.Setup.PKeyDataType = String.class;
    }


    /**
     * 设置影响的列
     * @param
     */
    public void SetAffectedColumns(enum_person_Columns... columns){
        for(enum_person_Columns col:columns){
            if(!super.Setup.ColumnsAffected.contains(col.toString()))
            {
                super.Setup.ColumnsAffected.add(col.toString());
                //super.ColumnsAffectedDic.put(col.toString(), null);
                super.ColumnsAffectedCV.put(col.toString(), "");
            }
        }
        if(!super.Setup.ColumnsAffected.contains(super.Setup.PKey))
        {
            super.Setup.ColumnsAffected.add(super.Setup.PKey);
            //super.ColumnsAffectedDic.put(col.toString(), null);
            super.ColumnsAffectedCV.put(super.Setup.PKey, "");
        }
    }

    /**
     * 设置排除影响的列
     * @param
     */
    public void SetAffectedColumnsExclude(enum_person_Columns... columns ){
        List<enum_person_Columns> list = new ArrayList<enum_person_Columns>();
        for(enum_person_Columns col :columns)
        {
            list.add(col);
        }

        for (enum_person_Columns col : enum_person_Columns.values())
        {
            if(!list.contains(col))
            {
                super.Setup.ColumnsAffected.add(col.toString());
                //super.ColumnsAffectedDic.put(col.toString(), null);
                super.ColumnsAffectedCV.put(col.toString(), "");
            }
        }
    }

    public static enum enum_person_Columns
    {
        _id

        ,name

        ,age

        ,info

    }
}