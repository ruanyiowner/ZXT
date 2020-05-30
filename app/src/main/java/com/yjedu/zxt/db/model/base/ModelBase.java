package com.yjedu.zxt.db.model.base;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public abstract class ModelBase {

    /**
     * 主键值
     * @param
     */
    public  Object PKeyValue ="";

    /**
     * 实体配置
     * @param
     */
    public  ModelSetup Setup=null;
    public ContentValues ColumnsAffectedCV = new ContentValues();
    public  void SetValueByCursor(Cursor c)
    {

    }
    public boolean IsEffectiveColumn(String columnName)
    {
        if(Setup.ColumnsAffected!=null&&Setup.ColumnsAffected.size()>0)
        {
            return Setup.ColumnsAffected.contains(columnName);
        }else{
            return true;
        }
    }
    /**
     * 检查列名是否有效，是否受影响的列。
     * @param  columnName
     */
    public void ValidityColumn(String columnName)
    {
        if(!IsEffectiveColumn(columnName))
        {
            try {
                Log.e("出错", columnName+"不是受影响的列,不能使用该列。");
                throw new Exception(columnName+"不是受影响的列,不能使用该列。");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
