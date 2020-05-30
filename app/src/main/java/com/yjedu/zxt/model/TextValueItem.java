package com.yjedu.zxt.model;

public class TextValueItem {
    private String Text;
    private String Value = "";

    public TextValueItem() {
        Text = "";
        Value = "";
    }

    public TextValueItem(String _Text, String _Value) {
        Text = _Text;
        Value = _Value;
    }

    @Override
    public String toString() {
        // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
        // TODO Auto-generated method stub
        return Text;
    }

    public String GetText() {
        return Text;
    }

    public String GetValue() {
        return Value;
    }
}
