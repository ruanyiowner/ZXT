package com.yjedu.zxt.dataAdapter;

import java.util.List;
import com.yjedu.zxt.act.testPaper.ExamActivity;
import com.yjedu.zxt.R;
import com.yjedu.zxt.model.mdlSJK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewSJKAdapter extends BaseAdapter {
    private List<mdlSJK> items;
    private LayoutInflater inflater;
    public Context context;
    public ListViewSJKAdapter(Context context,List<mdlSJK> items)
    {
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_sjk, null);
        }
        final TextView text = (TextView) view.findViewById(R.id.list_item_text_sjk);
        TextView remark = (TextView)view.findViewById(R.id.list_item_Remark_sjk);

        text.setText(items.get(position).PaperName);
        text.setTag(items.get(position).PaperID);

        remark.setText(items.get(position).Remark);

        text.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                TextView txtView = (TextView)view;
                String PaperName = (String) txtView.getText();
                String PaperID = txtView.getTag().toString();

                Intent intent = new Intent().setClass(context, ExamActivity.class);
                intent.putExtra("PaperID",PaperID);

                //为了接受SecondActivity中的值，不用startAcitivity(intent)
                ((Activity)context).startActivityForResult(intent,1000);
                //context.startActivity(intent);

                //两个动画之间的动画描述
                ((Activity)context).getParent().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left );

            }
        });
        remark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                text.callOnClick();
            }
        });
        return view;
    }

    public List<mdlSJK> GetAllItems(){
        return this.items;
    }
    public void AddItem(mdlSJK item)
    {
        this.items.add(item);
    }
}
