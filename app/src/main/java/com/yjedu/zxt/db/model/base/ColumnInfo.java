package com.yjedu.zxt.db.model.base;
import java.lang.reflect.Type;
public class ColumnInfo {
    public String ColumnName="";
    public Type ColumnType=null;
    public ColumnInfo(){}
    public ColumnInfo(String columnName,Type columnType){
        ColumnName = columnName;
        ColumnType = columnType;
    }
}
