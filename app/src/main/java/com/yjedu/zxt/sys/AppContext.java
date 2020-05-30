package com.yjedu.zxt.sys;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
    private static Context instance;
    public static Context getContext()
    {
        return instance;
    }
    public AppContext(Context _context )
    {
        instance = _context;
    }
    public  static void InitialSelf(Context _context )
    {
        instance = _context;
    }
    /**
     * 安卓设备ID号
     */
    private static  String DeviceId="";
    public static String getDeviceId(){
        if(DeviceId=="")
        {
            tools.device.Info deviceInfo = new tools.device.Info(instance);
            DeviceId = deviceInfo.getDeviceId();
        }
        return  DeviceId;
    }



    @Override
    public void onCreate()
    {
        instance = getApplicationContext();
        DeviceId ="";
        super.onCreate();
    }

}
