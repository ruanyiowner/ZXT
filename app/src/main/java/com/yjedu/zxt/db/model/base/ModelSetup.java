/*
 * 设计者：LHJ
 * 邮箱：869067911@qq.com
 */
package com.yjedu.zxt.db.model.base;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

public class ModelSetup {
    public  String TableName = "";
    public  String PKey = "";
    public  Type PKeyDataType;

    /**
     * 受影响的列。
     * @param
     */
    public List<String> ColumnsAffected = new ArrayList<String>(); // 受影响的列
    //public List<ColumnInfo> ColumnsAffected; // 受影响的列
    //public Dictionary<String,Object> ColumnsAffectedDic = new Hashtable<String,Object>();

    //public abstract String AddSQL();
}
