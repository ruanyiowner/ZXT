package com.yjedu.zxt.sys;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yjedu.zxt.HttpHelper;
import com.yjedu.zxt.api.ResultInfo;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import tools.file.FileUtil;

public  class appConfig {

    public static Context _context=null;
    public ApplicationInfo appInfo=null;
    public InputStream in = null;
    public Properties property = null;//声明变量
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


    private static appConfig _instance = null;
    public static appConfig getInstance() throws Exception {
        if(null==_instance)
        {
            if(null!=_context)
            {
                _instance = new appConfig(_context);
            }else{

                throw new Exception("_context 为 null 请设置。");
            }
        }
        return _instance;
    }

    public appConfig(Context context)
    {
        this._context = context;
        loadConfig();
        initialApplicationInfo();
        _instance = this;
    }

    /**
     * 加载开发环境的配置文件
     * @author zzw
     * **/
    private void loadConfig()
    {

        property = new Properties();
        try {
            //in = this.context.getAssets().open("appConfig.properties");
            in = this._context.getAssets().open("appConfig.properties");

            property.load(in);


        } catch (IOException e) {
            Log.e("TAG", "load properties error",e);
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }



    private void initialApplicationInfo()
    {
        try {
            //获取meta-data 属性
            appInfo=  this._context.getPackageManager().getApplicationInfo(this._context.getPackageName(),  PackageManager.GET_META_DATA);
            //获取meta-data 下面DEV_MODEL 的值
            //msg  = appInfo.metaData.getString("DEV_MODEL");
        }
        catch (PackageManager.NameNotFoundException e)
        {
            //Log.e("TAG", "获取开发模式失败", e);
        }
    }


    /**
     * 获取开发模式
     * @return 配置文件对应 的值。
     * **/
    public String getDevModelValue(String key){
        //ApplicationInfo appInfo=null;
        String msg = "DEBUG";//默认是开发模式
	        /*
	        if(null == appInfo)
	        {
		        try {

		            //获取meta-data 属性
		            appInfo=  this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(),  PackageManager.GET_META_DATA);
		            //获取meta-data 下面DEV_MODEL 的值
		            //msg  = appInfo.metaData.getString("DEV_MODEL");
		        }
		        catch (PackageManager.NameNotFoundException e)
		        {
		            //Log.e("TAG", "获取开发模式失败", e);
		        }
	        }
	        */

        msg  = appInfo.metaData.getString("DEV_MODEL");

        //将Applicaition里面配置的（pro，test，debug）拼接成 UPDATE_PHOTO_URL _TEST
        String configKey = key+"_"+ msg;
        //获取配置文件对应的值
        return property.getProperty(configKey.toUpperCase());

    }
    /**
     * 获取网络连接类型
     * @return
     */
    public String getConnectionType(){
        //ApplicationInfo appInfo=null;
	    	/*
	    	String ConnectionType="";
	    	try {
	            //获取meta-data 属性
	            appInfo=  this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(),  PackageManager.GET_META_DATA);
	            //获取meta-data 下面DEV_MODEL 的值
	            ConnectionType  = appInfo.metaData.getString("ConnectionType");
	        }
	        catch (PackageManager.NameNotFoundException e)
	        {
	           // Log.e("TAG", "获取网络连接类型失败", e);
	        }
	        */
        return appInfo.metaData.getString("ConnectionType");
        //return ConnectionType;
    }

    public String getValue(String key)
    {
        return property.getProperty(key);
    }

    /**
     * 获取动态主机
     * @return
     */
    public String getDynamicHost()
    {
        String host = "";
        String ipInfo = FileUtil.GetFileContent(_context, "ip.txt");
        if(ipInfo!="")
        {
            String[] arr = ipInfo.split("\\|");
            if(arr.length==3)
            {
                host = arr[0];
            }
        }
        return host;
    }
    /**
     * 保存动态IP信息
     * @param context
     * @param host
     */
    public void saveDynamicIPInfo(Context context,String host)
    {
        String ipInfoStr = host+"|"+df.format(new Date())+"|0";
        FileUtil.SaveFileContent(context, "ip.txt", ipInfoStr);
    }

	     /*
	    /**
	     * 加载动态主机地址
	     * /
	    public  void loadAndSetDynamicHostAddr(String key){
	    	final String str =  getDevModelValue(key);
	    	new Thread(new Runnable(){
	        	 @Override
	        	 public void run(){
			    	String[] arr = str.split("\\|");
			    	if(arr.length==2)
			    	{
			    		String reqUrl = arr[0];
			    		String port = arr[1];
			    		ResultInfo resultInfo = HttpHelper.ReqURLToResultInfo(reqUrl, 8000, 8000);
			    		Message message = new Message();
		 				message.what=0;
		 				//message.obj = jsonString;
		 				message.obj = resultInfo.dataString+":"+port;
		 				handlerResult.sendMessage(message);
			    	}
	        	 }
	         }).start();

	    }

	    private  Handler handlerResult = new Handler(){
	    	 public void handleMessage(Message msg)
	    	 {
	    		 switch(msg.what)
	    		 {
	    		 case 0: // 加载动态主机地址
	    			 // appInfo.metaData.putString("DynamicHostAddr", msg.obj.toString());
	    			// property.setProperty("DynamicHostAddr", msg.obj.toString());
	    			 String str = "";
	    			 break;
	    		 }
	    	 }
	     };
	      */


    public void setDynamicIPErrorTimes(Handler handler)
    {
        String ipInfo = FileUtil.GetFileContent(_context, "ip.txt");
        if(ipInfo!="")
        {
            String[] arr = ipInfo.split("\\|");
            if(arr.length==3)
            {
                int errTimes = Integer.parseInt(arr[2]) ;
                errTimes ++;
                if(errTimes>=1) // 断网一次，就更新动态IP地址
                {
                    if(null!=handler)
                    {
                        Message message = new Message();
                        message.what=101;
                        message.obj = "";
                        handler.sendMessage(message);
                    }
                    return;
                }
                arr[2] = String.valueOf(errTimes);
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<arr.length;i++)
                {
                    if(i>0) sb.append("|");
                    sb.append(arr[i]);
                }
                FileUtil.SaveFileContent(_context, "ip.txt", sb.toString()) ;

            }
        }
    }

    public void loadIpInfo(final Handler handler)
    {
        new Thread(new Runnable(){
            @Override
            public void run(){
                String reqUrl = getDevModelValue("HOST");
                String[] arr = reqUrl.split("\\|");
                if(arr.length>=2)
                {
                    Message message = new Message();
                    try{
                        ResultInfo resultInfo = HttpHelper.ReqURLToResultInfo(arr[0], 8000, 8000);
                        if(resultInfo.IsSuccess){
                            message.what=0; // 获取IP地址成功。
                            message.obj = resultInfo.dataString+":"+arr[1];
                        }else{
                            message.what=1; // 获取IP地址失败，返回空。
                            message.obj = "";
                        }
                        //handler.sendMessage(message);
                    }catch(Exception ex)
                    {
                        message.what=900;
                        message.obj = ex.getMessage();
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    public void tryToReloadIpInfo(Context context,final Handler handler)
    {
        String ipInfo = FileUtil.GetFileContent(context, "ip.txt");
        int ipErrTimes =0;
        Date date =  new Date();
        long dateDiff = 0;
        if(!ipInfo.equals(""))
        {
            ipErrTimes = Integer.parseInt(ipInfo.split("\\|")[2]);
            try {
                date = df.parse(ipInfo.split("\\|")[1]);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            dateDiff = (new Date()).getTime()-date.getTime();
        }
        if(ipInfo.equals("")||(ipErrTimes>=1)||(dateDiff>1000*60*60*12))
        {
            loadIpInfo(handler);
				 /*
		    	new Thread(new Runnable(){
		        	 @Override
		        	 public void run(){
		        		    String reqUrl = getDevModelValue("HOST");
		        		    String[] arr = reqUrl.split("\\|");
		        		    if(arr.length==2)
		        		    {
				                ResultInfo resultInfo = HttpHelper.ReqURLToResultInfo(arr[0], 8000, 8000);
				        		Message message = new Message();
				 				message.what=0;
				 				message.obj = resultInfo.dataString+":"+arr[1];
				 				handler.sendMessage(message);
		        		    }
		        	 }
		         }).start();
		         */
        }
    }

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
           // LogUtil.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            //LogUtil.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
