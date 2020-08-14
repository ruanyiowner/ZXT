package com.yjedu.zxt;

import android.app.DownloadManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.yjedu.zxt.act.sys.AppInfoActivity;
import com.yjedu.zxt.act.testPaper.DownloadActivity;
import com.yjedu.zxt.sys.AppContext;
import com.yjedu.zxt.sys.CheckAndInstallNewVer;
import com.yjedu.zxt.sys.appConfig;

import java.io.File;
import java.text.SimpleDateFormat;

public class MainActivity extends TabActivity   {
    private MyBroadcastReceiver receiver ;
    private ImageButton mBtn;
    private SlideHolder mSlideHolder;
    public static appConfig appconfig = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        AppContext.InitialSelf(this);
        appconfig =  new appConfig(this);
        if(appconfig.getConnectionType().equals("dynamic"))
        {
            appconfig.tryToReloadIpInfo(this, handlerResult);
        }

        Resources res = getResources(); // Resource object to get Drawables
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置没标题
        setContentView(R.layout.activity_main);


        //创建缓存图片目录
        String file_pic_path = Environment.getExternalStorageDirectory().getAbsolutePath()+res.getString(R.string.file_pic_path);
        File file = new File(file_pic_path);
        if (!file.exists()) {
            file.mkdirs();// 创建自定义目录
        }
       // mSlideHolder.toggle();

        mBtn = (ImageButton) findViewById(R.id.imageButton);
        mSlideHolder = (SlideHolder) findViewById(R.id.holder);

        /**
         * 点击展开或隐藏侧滑菜单
         */

        mBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(android.os.Build.VERSION.SDK_INT>=28) {
                    mSlideHolder.toggle(true); //true 为立刻
                }else{
                    mSlideHolder.toggle();
                }
                // mSlideHolder.toggleImmediately();
            }
        });


        Button menuBtnShareApp = (Button) this.findViewById(R.id.menuBtnShareApp);
        menuBtnShareApp.setOnClickListener(OnMenuClickListener);

        Button menuBtnAppInfo = (Button)this.findViewById(R.id.menuBtnAppInfo);
        menuBtnAppInfo.setOnClickListener(OnMenuClickListener);

        Button menuBtnTestDownload = (Button)this.findViewById(R.id.menuBtnTestDownload);
        menuBtnTestDownload.setOnClickListener(OnMenuClickListener);

        Button menuLogBtn = (Button)this.findViewById(R.id.log);
        menuLogBtn.setOnClickListener(OnMenuClickListener);


        TabHost tabHost = getTabHost();  // The activity TabHost
        //TabHost tabHost = (TabHost)this.findViewById(R.id.tabhost);

        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab


        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, HOMEActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("首页").setIndicator("",
                res.getDrawable(R.drawable.home_btn_ico))
                .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs

        intent = new Intent().setClass(this, SJKActivity.class);
        spec = tabHost.newTabSpec("试卷库").setIndicator("",
                res.getDrawable(R.drawable.sjk_btn_ico))
                .setContent(intent);

        tabHost.addTab(spec);


        intent = new Intent().setClass(this, STKActivity.class);
        spec = tabHost.newTabSpec("试题库").setIndicator("",
                res.getDrawable(R.drawable.stk_btn_ico))
                .setContent(intent);

        tabHost.addTab(spec);

        tabHost.setCurrentTab(1);

        /*
        apkInstall.DownloadReceiver mDownloaderReceiver = new apkInstall.DownloadReceiver();
        //注册下载完成广播
        this.registerReceiver(mDownloaderReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        CheckAndInstallNewVer(); // 检查与安装新版本的App
        */
        CheckAndInstallNewVer.Start(this);
       // registerMyReceiver();//在activity创建的时候进行注册监听


        // 通过该接口可以控制敏感数据采集，true表示可以采集，false表示不可以采集，
        // 该方法一定要最优先调用，请在StatService.start(this)之前调用，采集这些数据可以帮助App运营人员更好的监控App的使用情况，
        // 建议有用户隐私策略弹窗的App，用户未同意前设置false,同意之后设置true
       // StatService.setAuthorizedState(Context context,boolean false)
        StatService.setAuthorizedState(this,true);
        // setSendLogStrategy已经@deprecated，建议使用新的start接口
        // 如果没有页面和自定义事件统计埋点，此代码一定要设置，否则无法完成统计
        // 进程第一次执行此代码，会导致发送上次缓存的统计数据；若无上次缓存数据，则发送空启动日志
        // 由于多进程等可能造成Application多次执行，建议此代码不要埋点在Application中，否则可能造成启动次数偏高
        // 建议此代码埋点在统计路径触发的第一个页面中，若可能存在多个则建议都埋点
        StatService.start(this);


    }
    private void registerMyReceiver() {
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);//创建IntentFilter对象
        filter.addAction(Intent.ACTION_SCREEN_OFF);//IntentFilter对象中添加要接收的关屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);//添加点亮屏幕广播

        registerReceiver(receiver, filter);
    }

    private void unRegisterMyReceiver(){
        if(receiver != null){
            unregisterReceiver(receiver);//反注册广播，也就是注销广播接收者，使其不起作用
        }
    }
    private OnClickListener OnMenuClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menuBtnShareApp:
                    Intent i  = new Intent(v.getContext(),ShareAppActivity.class);
                    startActivity(i);
                    break;
                case  R.id.menuBtnAppInfo:
                    startActivity(new Intent(v.getContext(), AppInfoActivity.class));
                    break;
                case R.id.menuBtnTestDownload:
                    startActivity(new Intent(v.getContext(), DownloadActivity.class));
                    break;
                case R.id.log:
                    Intent intent=new Intent(v.getContext(),LogDataActivity.class);
                    startActivity(intent);

                    break;
                default:
                    break;
            }

        }
    };



    /**
     * 请求结果数据的处理。
     */

    private Handler handlerResult = new Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0: // 保存当前动态IP信息
                    appconfig.saveDynamicIPInfo(getApplicationContext(), msg.obj.toString());

                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG);
                    break;
                case 900:
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG);
                    break;
            }
        }
    };

    // 自动检测升级新版本 Start

    /**
     * 检查与安装新版本的App

    private void CheckAndInstallNewVer()
    {
         new Thread(
                 new Runnable() {
                     @Override
                     public void run() {
                         Message message = new Message();
                         String reqUrl = "http://s.ruanyi.online:8008/api/DownLoadPublishSoft.ashx?m=getSoftFileInfo&SoftName=StockManageApp&vertype=lastversion";
                         ResultInfo resultInfo = HttpHelper.ReqURLToResultInfo(reqUrl, 5000, 5000);
                         message.what=0;
                         message.obj = resultInfo;
                         handlerJsonResult.sendMessage(message);
                     }
                 }
         ).start();
    }
     */

    /**
     * 请求的Json结果数据的处理。

    private Handler handlerJsonResult = new Handler(){
        public void handleMessage(Message msg)
        {

            switch(msg.what)
            {
                case 0: // 加载试最后版本信息
                    ResultInfo resultInfo = (ResultInfo)msg.obj;
                    if(resultInfo.IsSuccess){
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(resultInfo.dataString);
                        //JsonArray arr =  jsonElement.getAsJsonArray();
                        JsonObject o = jsonElement.getAsJsonObject();
                        String lastVername = o.get("softversion").getAsString();
                        String remark = o.get("remark").getAsString();
                        String createdate = o.get("createtime").getAsString();
                        final String lastVerUrl = o.get("url").getAsString();
                        String currentVerName =  appConfig.getLocalVersionName(getApplicationContext());

                        if(!lastVername.equals(currentVerName))
                        {
                            Toast.makeText(getApplicationContext(),"有新版本:"+lastVername,
                                    Toast.LENGTH_LONG).show();

                            makeSureUpdate(lastVerUrl,  lastVername,  remark,  createdate,currentVerName);


                        }

                    }
                    break;

            }
        }
    };
     */
    /**
     * 确定升级
     * @param lastVerUrl
     * @param lastVername
     * @param remark
     * @param createdate
     * @param currentVerName

    private void makeSureUpdate(final String lastVerUrl, String lastVername, String remark,
                     String createdate,String currentVerName)
    {

        AlertDialog.Builder adialog = new AlertDialog.Builder(MainActivity.this);
        adialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                apkInstall.install(getApplicationContext(),lastVerUrl,"StockManageApp",false);
                return;
            }
        });

        adialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adialog.setMessage("新版本:"+lastVername+"\n"+remark+"\n发布时间："+createdate+"\n当前版本："+currentVerName);
        adialog.setTitle("有新的版本，需要升级吗?");
        adialog.show();

    }
     */
    // 自动检测升级新版本 End
}
