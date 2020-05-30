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

public class mdllogdata extends ModelBase {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //private long M__id ;
    public void Set__id(long _id){
        super.ValidityColumn("_id");
        //super.ColumnsAffectedDic.put("_id", _id);
        super.ColumnsAffectedCV.put("_id", _id);
        //M__id=_id;
        super.PKeyValue = _id;
    }
    public long Get__id(){
        //return M__id;
        super.ValidityColumn("_id");
        return super.ColumnsAffectedCV.getAsLong("_id");
    }

    //private String M_logtype ;
    public void Set_logtype(String logtype)
    {
        super.ValidityColumn("logtype");
        //super.ColumnsAffectedDic.put("logtype", logtype);
        super.ColumnsAffectedCV.put("logtype", logtype);
        //M_logtype = logtype;
    }
    public String Get_logtype()
    {
        //return M_logtype;
        super.ValidityColumn("logtype");
        return super.ColumnsAffectedCV.getAsString("logtype");
    }

    //private String M_logtitle ;
    public void Set_logtitle(String logtitle){
        super.ValidityColumn("logtitle");
        //super.ColumnsAffectedDic.put("logtitle", logtitle);
        super.ColumnsAffectedCV.put("logtitle", logtitle);
        //M_logtitle = logtitle;
    }
    public String Get_logtitle(){
        //return M_logtitle;
        super.ValidityColumn("logtitle");
        return super.ColumnsAffectedCV.getAsString("logtitle");
    }

    //private String M_logcontent ;
    public void Set_logcontent(String logcontent)
    {
        super.ValidityColumn("logcontent");
        //super.ColumnsAffectedDic.put("logcontent", logcontent);
        super.ColumnsAffectedCV.put("logcontent", logcontent);
        //M_logcontent=logcontent;
    }
    public String Get_logcontent(){
        //return M_logcontent;
        super.ValidityColumn("logcontent");
        return super.ColumnsAffectedCV.getAsString("logcontent");
    }

    //private long M_appeartime ;
    public void Set_appeartime(long appeartime)
    {
        super.ValidityColumn("appeartime");
        //super.ColumnsAffectedDic.put("appeartime", appeartime);
        super.ColumnsAffectedCV.put("appeartime", appeartime);
        //M_appeartime = appeartime;
    }
    public long Get_appeartime(){
        //return M_appeartime;
        super.ValidityColumn("appeartime");
        return super.ColumnsAffectedCV.getAsLong("appeartime");
    }

    //private Date M_createtime ;
    public void Set_createtime(Date createtime){
        super.ValidityColumn("createtime");
        //super.ColumnsAffectedDic.put("createtime", createtime);

        super.ColumnsAffectedCV.put("createtime", sdf.format(createtime));
        //M_createtime = createtime ;
    }
    public Date Get_createtime(){
        //return M_createtime;
        super.ValidityColumn("createtime");
        try {
            return sdf.parse(super.ColumnsAffectedCV.getAsString("createtime")) ;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    //private Date M_edittime ;
    public void Set_edittime(Date edittime)
    {
        super.ValidityColumn("edittime");
        //super.ColumnsAffectedDic.put("edittime", edittime);
        super.ColumnsAffectedCV.put("edittime",sdf.format(edittime) );
        //M_edittime = edittime;
    }
    public Date Get_edittime(){
        //return M_edittime;
        super.ValidityColumn("edittime");
        try {
            return sdf.parse(super.ColumnsAffectedCV.getAsString("edittime")) ;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    public  void SetValueByCursor(Cursor c)
    {
        Method aMethod;
        try {
            if(super.Setup.ColumnsAffected==null||super.Setup.ColumnsAffected.size()==0)
            {
                super.Setup.ColumnsAffected = new ArrayList<String>();
                for(enum_logdata_Columns col:enum_logdata_Columns.values())
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
                    }
                    else if(t.equals(int.class))
                    {
                        aMethod.invoke(this,c.getInt(c.getColumnIndex(col)));
                    }
                    else if(t.equals(long.class))
                    {
                        aMethod.invoke(this,c.getLong(c.getColumnIndex(col)));
                    }
                    else if(t.equals(Date.class))
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

    public mdllogdata(){
        super.PKeyValue="";
        super.Setup = new ModelSetup();
        super.Setup.TableName = "logdata";
        super.Setup.PKey = "_id";
        super.Setup.PKeyDataType = long.class;
    }

    /**
     * 设置影响的列
     * @param
     */
    public void SetAffectedColumns(enum_logdata_Columns... columns){
        for(enum_logdata_Columns col:columns){
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
    public void SetAffectedColumnsExclude(enum_logdata_Columns... columns ){
        List<enum_logdata_Columns> list = new ArrayList<enum_logdata_Columns>();
        for(enum_logdata_Columns col :columns)
        {
            list.add(col);
        }

        for (enum_logdata_Columns col : enum_logdata_Columns.values())
        {
            if(!list.contains(col))
            {
                super.Setup.ColumnsAffected.add(col.toString());
                //super.ColumnsAffectedDic.put(col.toString(), null);
                super.ColumnsAffectedCV.put(col.toString(), "");
            }
        }
    }

    public static enum enum_logdata_Columns{
        _id
        /**
         * 日志类型
         */
        ,logtype
        /**
         * 日志标题
         */
        ,logtitle
        /**
         * 日志内容
         */
        ,logcontent
        /**
         * 重复次数
         */
        ,appeartime
        /**
         * 创建时间
         */
        ,createtime
        /**
         * 修改时间
         */
        ,edittime
    }

}