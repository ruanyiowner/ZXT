package com.yjedu.zxt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yjedu.zxt.R;
import com.yjedu.zxt.dataAdapter.ListViewCommodityAdapter;
import com.yjedu.zxt.model.mdlCommodity;

import tools.file.FileUtil;
import tools.file.SDCardHelper;
import tools.image.AsynImageLoader;
import tools.image.PicUtil;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
// 全局
public class HOMEActivity extends ListActivity implements OnScrollListener   {

    private ListView listView;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private ListViewCommodityAdapter adapter;
    private View loadMoreView;
    private Button loadMoreButton;
    private Handler handler = new Handler();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qj);


        loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);

        listView = getListView();               //获取id是list的ListView

        listView.addFooterView(loadMoreView);   //设置列表底部视图


        LoadCommodity("http://www.jtxx.net.cn/HpptAPI/JT/GetPro.ashx?ReqName=AllPro",0);
        listView.setOnScrollListener(this);     //添加滑动监听


    }

    /**
     * 初始化适配器
     */
    private void initAdapter(ArrayList<mdlCommodity> commodityItems) {
        adapter = new ListViewCommodityAdapter(this, commodityItems);
    }

    /**
     * 滑动时被调用
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;


    }

    /**
     * 滑动状态改变时被调用
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引
        int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex)
        {
            //如果是自动加载,可以在这里放置异步加载数据的代码
            Log.i("LOADMORE", "loading...");
            loadMore(view);
        }
    }

    /**
     * 点击按钮事件
     * @param view
     */
    public void loadMore(View view) {
        loadMoreButton.setText("加载中...");   //设置按钮文字loading
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadData();

                adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项

                loadMoreButton.setText("加载更多...");    //恢复按钮文字
            }
        }, 500);
    }

    /**
     * 模拟加载数据
     */
    private void loadData() {
        int count = adapter.getCount();
        for (int i = count; i < count + 10; i++) {
            mdlCommodity model = new mdlCommodity();
            model.CName = "cname"+i;
            model.ImageUrl="http://c.hiphotos.baidu.com/image/h%3D360/sign=ad6d78f77b310a55db24d8f287444387/09fa513d269759ee673682cfb0fb43166d22df54.jpg";
            adapter.addItem(model);

        }

    }


    private void LoadCommodity(final String url,final int what){

        new Thread( new Runnable(){

            @Override
            public void run(){
                String jsonString = HttpHelper.ReqURL(url, 8000, 8000);
                Message message = new Message();
                message.what=what;
                message.obj = jsonString;
                handlerJson.sendMessage(message);

            }
        }).start();
    }

    private Handler handlerJson = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what)
            {
                case 0:
                    String jsonString = (String)msg.obj;
                    if(null==jsonString||jsonString=="") return;
                    //String jsonString = "[{\"temprownumber\":1,\"CommodityID\":475,\"CName\":\"\u5B88\u671B\u8005 \u5C0F\u7C73\u7EA2\u7C731S\u624B\u673A\u59571S\u4FDD\u62A4\u58F3\u6E05\u6C34\u5957\u7EA2\u7C734G\u7248\u81EA\u5E26\u9632\u5C18\u585E\u8F6F\u58F3 \",\"ImageUrl\":\"http://img5.imgtn.bdimg.com/it/u=2296905337,504609796&fm=21&gp=0.jpg\",\"Url\":\"http://detail.tmall.com/item.htm?id=39557692552&spm=a1z0k.7385961.1997985097.d4918993.WILp1l&_u=81ff90684a29&mt=\",\"IsAllianceProduct\":true,\"IsPresenterUrl\":true,\"OriginalUrl\":\"http://detail.tmall.com/item.htm?id=39557692552&spm=a1z0k.7385961.1997985097.d4918993.WILp1l&_u=81ff90684a29&mt=\",\"CommodityClassID\":46,\"SourceWebsite\":\"\u5929\u732B\",\"SourceWebsiteUrl\":\"http://www.tmall.com\",\"Recommend\":\"\",\"NotRecommend\":\"\",\"ClickRate\":\"\",\"Price\":\"4.9000\",\"Description\":\"\u5168\u7F51\u6700\u597D\u7684\u7EA2\u7C73\u534A\u900F\u660E\u624B\u673A\u8F6F\u5957\uFF0C\u8FDB\u53E3\u6750\u8D28\u624B\u611F\u597D\uFF0C\u6709\u81EA\u5E26\u9632\u5C18\u529F\u80FD~100%\u5B89\u5168\u8FDB\u53E3\u6750\u6599\u9632\u8FC7\u654F~\u53EA\u6C42\u91CF\u4E0D\u6C42\u4EF7\uFF0C\u4E3A\u7EA2\u7C73\u624B\u673A\u91CF\u8EAB\u5B9A\u505A\u7684\u597D\u4EA7\u54C1\uFF01\u73B0\u5728\u4E702\u4E2A\u8FD8\u90012\u5F20\u8D34\u819C 2\u4E2A\u64E6\u5E03 \u5C0F\u652F\u67B6\u4E00\u679A\",\"IsShow\":true,\"CreateTime\":\"\",\"ModifyTime\":\"\",\"ordernum\":\"\"},{\"temprownumber\":2,\"CommodityID\":474,\"CName\":\"Apple/\u82F9\u679C iPhone 4s\u5168\u65B0\u65E0\u9501\u539F\u88C5\u7F8E\u7248\u624B\u673A16G 32G \u6B63\u54C1\u5305\u90AE\",\"ImageUrl\":\"http://img5.imgtn.bdimg.com/it/u=2296905337,504609796&fm=21&gp=0.jpg\",\"Url\":\"http://item.taobao.com/item.htm?spm=a2106.m872.1000384.51.Qx7S3A&id=40180053094&scm=1029.newlist-0.1.1512&ppath=&sku=&ug=#detail\",\"IsAllianceProduct\":false,\"IsPresenterUrl\":false,\"OriginalUrl\":\"http://item.taobao.com/item.htm?spm=a2106.m872.1000384.51.Qx7S3A&id=40180053094&scm=1029.newlist-0.1.1512&ppath=&sku=&ug=#detail\",\"CommodityClassID\":46,\"SourceWebsite\":\"\u6DD8\u5B9D\u7F51\",\"SourceWebsiteUrl\":\"http://www.taobao.com\",\"Recommend\":\"\",\"NotRecommend\":\"\",\"ClickRate\":\"\",\"Price\":\"2000.0000\",\"Description\":\"\",\"IsShow\":true,\"CreateTime\":\"\",\"ModifyTime\":\"\",\"ordernum\":\"\"}]";
                    ArrayList<mdlCommodity> commodityItems = JsonToCommodityItems(jsonString);
                    initAdapter(commodityItems);
                    setListAdapter(adapter);   //自动为id是list的ListView设置适配器

            }
        }
    };


    private ArrayList<mdlCommodity> JsonToCommodityItems(String jsonString)
    {
        ArrayList<mdlCommodity> items = new ArrayList<mdlCommodity>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);

        JsonArray arr =  jsonElement.getAsJsonArray();


        for(JsonElement e:arr)
        {
            mdlCommodity model = new mdlCommodity();
            JsonObject o = e.getAsJsonObject();
            model.CName =  o.get("CName").toString();
            model.ImageUrl = o.get("ImageUrl").getAsString();
            items.add(model);

        }

        return items;
    }

}