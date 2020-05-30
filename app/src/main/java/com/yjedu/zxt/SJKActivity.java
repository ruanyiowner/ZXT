package com.yjedu.zxt;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yjedu.zxt.R;
import com.yjedu.zxt.sys.AppContext;
import com.yjedu.zxt.sys.appConfig;
import com.yjedu.zxt.api.AccessAuthentication;
import com.yjedu.zxt.api.ResultInfo;
import com.yjedu.zxt.dataAdapter.ListViewSJKAdapter;
import com.yjedu.zxt.dataAdapter.Pager;
import com.yjedu.zxt.model.mdlCommodity;
import com.yjedu.zxt.model.mdlSJK;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SJKActivity  extends ListActivity   implements OnScrollListener  {
    private ListView listView;
    private int visibleLastIndex = 0;   // 最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private ListViewSJKAdapter adapter;
    private View loadMoreView;
    private Button loadMoreButton;
    private Handler handler = new Handler();
    private appConfig appconfig = null;

    ArrayList<mdlSJK> dataListSJK = new ArrayList<mdlSJK>();
    Pager pagerSJK = new Pager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appconfig =  new appConfig(this);
        setContentView(R.layout.activity_sjk);

        loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);

        listView = getListView();               //获取id是list的ListView

        listView.addFooterView(loadMoreView);   //设置列表底部视图

        listView.setOnScrollListener(this);     //添加滑动监听

        loadData_SJK();

    }

    /**
     * 加载试卷库
     */
    private void loadData_SJK()
    {

        new Thread(new Runnable(){
            @Override
            public void run(){
                Message message = new Message();

                String reqParams = String.format("&a=GetPagePapers&pageSize=%s&pageIndex=%s&where=%s&orderBy=%s",pagerSJK.pageSize,pagerSJK.currentPageIndex,"","");
                String reqUrl = null;
                try {
                    reqUrl = AccessAuthentication.getAPIUrlWithAccessAuthParamStr()+reqParams;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ResultInfo resultInfo = HttpHelper.ReqURLToResultInfo(handlerIpBack,reqUrl, 5000, 5000);

                message.what=0;

                message.obj = resultInfo;

                handlerJsonResult.sendMessage(message);

            }
        }).start();
    }
    /**
     * 绑定试卷库数据
     * @param obj
     */
    private void BindSJKData(Object obj)
    {
        ResultInfo resultInfo = (ResultInfo)obj;

        pagerSJK.isLoadedSuccess = resultInfo.IsSuccess;
        if(resultInfo.IsSuccess){
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(resultInfo.dataString);
            JsonArray arr =  jsonElement.getAsJsonArray();

            for(JsonElement e:arr)
            {
                mdlSJK model = new mdlSJK();
                JsonObject o = e.getAsJsonObject();
                model.PaperName = o.get("PaperName")== null ? "" :  o.get("PaperName").toString();
                model.PaperID = o.get("PaperID")== null ? null : o.get("PaperID").getAsLong();
                model.Remark = o.get("Remark") == null ? "" :  o.get("Remark").toString();
                dataListSJK.add(model);
            }
            if(arr.size()<pagerSJK.pageSize)
            {
                pagerSJK.allLoaded = true;
            }

            if(pagerSJK.currentPageIndex==0){
                adapter =  new ListViewSJKAdapter(this, dataListSJK);
                setListAdapter(adapter);  //自动为id是list的ListView设置适配器
            }else{
                if(null!=adapter){
                    adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                }
            }

            //listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
            //listView.setSelection(6);
            if(pagerSJK.allLoaded){
                loadMoreButton.setText("已全部加载完成。");

            }else{
                loadMoreButton.setText("load more.");    //恢复按钮文字
            }
        }else{

            if(null==adapter)
            {
                adapter =  new ListViewSJKAdapter(this, dataListSJK);
                setListAdapter(adapter);  //自动为id是list的ListView设置适配器
            }
            loadMoreButton.setText(resultInfo.errMsg);

        }
    }



    /**
     * 请求的Json结果数据的处理。
     */
    private Handler handlerJsonResult = new Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0: // 加载试卷库数据
                    BindSJKData(msg.obj);
                    break;

            }
        }
    };

    /**
     *
     */
    private Handler handlerIpBack = new Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                    appconfig.saveDynamicIPInfo(getApplicationContext(), msg.obj.toString());
                    Toast.makeText(getApplicationContext(),"保存IP地址："+ msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(),"获取IP地址失败。", Toast.LENGTH_LONG).show();
                    break;
                case 101: //  加载动态IP信息
                    appconfig.loadIpInfo(handlerIpBack);
                    break;

            }
        }
    };




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
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex&&(!pagerSJK.allLoaded))
        {
            //如果是自动加载,可以在这里放置异步加载数据的代码
            //Log.i("LOADMORE", "loading...");

            loadMore(view);

        }
    }

    /**
     * 点击按钮事件
     * @param view
     */
    public void loadMore(View view) {
        if(pagerSJK.allLoaded) return;
        loadMoreButton.setText("loading...");   //设置按钮文字loading
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pagerSJK.isLoadedSuccess){
                    pagerSJK.currentPageIndex++;
                }
                loadData_SJK();
                //adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                // listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项

                // loadMoreButton.setText("load more.");    //恢复按钮文字
            }
        }, 10);
    }


}
