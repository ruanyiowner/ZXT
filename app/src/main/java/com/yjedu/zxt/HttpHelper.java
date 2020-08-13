package com.yjedu.zxt;

import android.os.Handler;

import com.yjedu.zxt.api.ResultInfo;
import com.yjedu.zxt.sys.appConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper  {


    /**
     * 根据Url获取返回内容。
     * @param urlString
     * @param connectTimeout 毫秒
     * @param readTimeout 毫秒
     * @return
     */
    public static String ReqURL(String urlString,int connectTimeout,int readTimeout)
    {
        String resultString="";
        HttpURLConnection connection = null ;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            InputStream in = connection.getInputStream();
            // 对输入流进行读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder respone = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null)
            {
                respone.append(line);
            }
            resultString=respone.toString();
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return resultString;
    }


    /**
     * 读取Url返回的信息
     * @param urlString
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static ResultInfo ReqURLToResultInfo(String urlString,int connectTimeout,int readTimeout)
    {
        ResultInfo resultInfo = new ResultInfo();
        String resultString="";
        HttpURLConnection connection = null ;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            InputStream in = connection.getInputStream();
            // 对输入流进行读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder respone = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null)
            {
                respone.append(line);
            }
            resultString=respone.toString();
            resultInfo.IsSuccess = true;
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            resultInfo.IsSuccess = false;
            resultInfo.errMsg="URL地址格式有误！";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            resultInfo.IsSuccess = false;
            resultInfo.errMsg="获取数据失败！网络可能已经断开。";
            //appConfig config = new appConfig(appConfig._context);
            //config.setDynamicIPError();

        }finally{
            connection.disconnect();
        }
        resultInfo.dataString = resultString;
        return resultInfo;
    }

    public static ResultInfo ReqURLToResultInfo(Handler handler,String urlString,int connectTimeout,int readTimeout)
    {
        ResultInfo resultInfo = new ResultInfo();
        String resultString="";
        HttpURLConnection connection = null ;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            InputStream in = connection.getInputStream();
            // 对输入流进行读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder respone = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null)
            {
                respone.append(line);
            }
            resultString=respone.toString();
            resultInfo.IsSuccess = true;
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            resultInfo.IsSuccess = false;
            resultInfo.errMsg="URL地址格式有误！";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            resultInfo.IsSuccess = false;
            //resultInfo.errMsg="获取数据失败！网络可能已经断开。"+e.getMessage()+urlString;
            resultInfo.errMsg="网络可能已经断开，点击刷新。"+e.getMessage();
            appConfig config = new appConfig(appConfig._context);
            config.setDynamicIPErrorTimes(handler);

        }finally{
            connection.disconnect();
        }
        resultInfo.dataString = resultString;
        return resultInfo;
    }

}