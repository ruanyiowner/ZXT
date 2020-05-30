package com.yjedu.zxt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yjedu.zxt.R;

import tools.file.FileUtil;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.yjedu.zxt.sys.AppContext;
import com.yjedu.zxt.sys.appConfig;
import com.yjedu.zxt.api.AccessAuthentication;
import com.yjedu.zxt.api.ResultInfo;

public class MainActivity extends TabActivity   {

    private ImageButton mBtn;
    private SlideHolder mSlideHolder;
    private appConfig appconfig = null;
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


        mBtn = (ImageButton) findViewById(R.id.imageButton);
        mSlideHolder = (SlideHolder) findViewById(R.id.holder);

        /**
         * 点击展开或隐藏侧滑菜单
         */

        mBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideHolder.toggle();
                // mSlideHolder.toggleImmediately();
            }
        });


        Button menuBtn = (Button) this.findViewById(R.id.menu1);
        menuBtn.setOnClickListener(OnMenuClickListener);

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



    }

    private OnClickListener OnMenuClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu1:

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

}
