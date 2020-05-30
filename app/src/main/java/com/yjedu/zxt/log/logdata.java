package com.yjedu.zxt.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.yjedu.zxt.db.DBManager;
import com.yjedu.zxt.db.model.mdllogdata;
import com.yjedu.zxt.model.TextValueItem;

public class logdata {
    // 日志类型
    public static enum enumLogType{
        Error,
        General,
        Warning
    }
    // 获取日志类型列表
    public static List<TextValueItem> GetLogTypeList(){
        List<TextValueItem> list = new ArrayList<TextValueItem>();
        list.add(new TextValueItem("全部",""));
        list.add(new TextValueItem("错误!",enumLogType.Error.toString()));
        list.add(new TextValueItem("一般",enumLogType.General.toString()));
        list.add(new TextValueItem("警告!",enumLogType.Warning.toString()));
        return list;
    }


    public static void WriteLog(Context context,enumLogType logType,String logtitle,String logcontent,boolean IsCover )
    {
        boolean IsAdd=true;
        DBManager dbm = new DBManager(context);
        mdllogdata model = null;

        if(IsCover)
        {
            model = dbm.logdata_getFirstModel(" where logtitle='"+logtitle+"' ", " order by _id desc");
            if(null!=model)
            {
                IsAdd = false;

                model.Set_appeartime(model.Get_appeartime()+1);
            }
        }

        if(null==model)
        {
            model = new mdllogdata();
        }

        model.Set_logtype(logType.toString());

        model.Set_logtitle(logtitle);

        model.Set_logcontent(logcontent);

        model.Set_edittime(new Date());

        if(IsAdd)
        {

            model.Set_createtime(new Date());

            model.Set_appeartime(1);
            dbm.logdata_add(model);

        }else{
            dbm.logdata_edit(model);
        }
    }

}
