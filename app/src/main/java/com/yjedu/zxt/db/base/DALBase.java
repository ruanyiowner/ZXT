/*
 * 设计者：LHJ
 * 邮箱：869067911@qq.com
 */
package com.yjedu.zxt.db.base;

import java.lang.reflect.Field;

import android.database.Cursor;

public class DALBase<T extends com.yjedu.zxt.db.model.base.ModelBase>    {
    protected T CursorToModel (Cursor cursor,T t) throws IllegalAccessException, IllegalArgumentException
    {
        //Class<?> classType = t.getClass().getSuperclass();
        Field[] fields = t.getClass().getFields();
        for(Field field: fields)
        {
            field.setAccessible(true);
            int ColumnIndex = cursor.getColumnIndex(field.getName());
            if(field.getType()==String.class)
            {
                String str =cursor.getString(ColumnIndex);
                field.set(t, str);

            }else if(field.getType()==int.class)
            {
                int i = cursor.getInt(ColumnIndex);
                field.setInt(t, i);
            }
            // ...

        }
        return t;
    }
}