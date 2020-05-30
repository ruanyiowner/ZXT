package com.yjedu.zxt.api;

import com.yjedu.zxt.sys.AppContext;

import android.content.Context;

public class AccessAuthentication {
    /**
     * 获取API的地址，如：http://127.0.0.1:8080/YJEDUWebApi/api
     * @param context
     * @return
     */
    public static String getAPIUrl(Context context) throws Exception {
       // com.yjedu.zxt.sys.appConfig config = new com.yjedu.zxt.sys.appConfig(context);
        com.yjedu.zxt.sys.appConfig config = com.yjedu.zxt.sys.appConfig.getInstance();
        String host = "";
        if(config.getConnectionType().equals("dynamic"))
        {
            host = config.getDynamicHost();
        }else{
            host = config.getDevModelValue("HOST");
        }
        return "http://"+host+"/YJEDUWebApi/api";
    }

    /**
     * 获取API的地址(包括安全访问的参数)，如：http://127.0.0.1:8080/YJEDUWebApi/api?appid=&nonce=&timestamp=&sign=
     * @return
     */
    public static String getAPIUrlWithAccessAuthParamStr() throws Exception {
        //TODO
        String  appid =   AppContext.getDeviceId();

        String nonce = java.util.UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();

        String md5key="edu@klecwEx$128";
        String sign = tools.security.MD5.MD5_Encrypt(appid+nonce+timestamp+md5key);

        StringBuilder strSB = new StringBuilder();
        //strSB.append(getAPIUrl(AppContext.getContext()));
        strSB.append(getAPIUrl(AppContext.getContext()));

        strSB.append("?appid="+appid);
        strSB.append("&nonce="+nonce);
        strSB.append("&timestamp="+timestamp);
        strSB.append("&sign="+sign);

        return strSB.toString();

    }

}
