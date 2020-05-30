package com.yjedu.zxt.dataAdapter;
import java.util.List;

import com.yjedu.zxt.R;
import com.yjedu.zxt.model.mdlCommodity;

import tools.image.AsynImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class ListViewCommodityAdapter extends BaseAdapter  {
    private List<mdlCommodity> items;
    private LayoutInflater inflater;
    public Context context;
    public ListViewCommodityAdapter(Context context, List<mdlCommodity> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.qj_list_item, null);
        }


        AsynImageLoader asynil = new AsynImageLoader();
        ImageView imageView = (ImageView)view.findViewById(R.id.qj_list_item_image);
        asynil.context = context;
        asynil.showImageAsyn(imageView,items.get(position).ImageUrl, R.drawable.home_btn_ico);

        //imageView.setMaxWidth(parent.getWidth()>250?250:parent.getWidth());

        // imageView.setMaxWidth(800);


        if(parent.getWidth()>1300)
        {
            //imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            //imageView.setMaxWidth(800);
        }




        TextView text = (TextView) view.findViewById(R.id.qj_list_item_text);
        text.setText(items.get(position).CName);
        return view;
    }

    /**
     * 添加列表项
     * @param item
     */
    public void addItem(mdlCommodity item) {
        items.add(item);
    }

    /**
     * 返回所有商品列表
     * @return
     */
    public List<mdlCommodity> GetAllItems(){
        return items;
    }

}
