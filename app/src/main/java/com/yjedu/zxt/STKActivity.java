package com.yjedu.zxt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yjedu.zxt.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class STKActivity extends Activity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fj);



        sendRequestWithHttpURLConnection();

        // String jsonString="[{\"name\":\"lhy\",\"age\":22,\"qq\":5623655},{\"name\":\"lhy_2\",\"age\":33,\"qq\":999999}]";

        // parseJSONWithJSONObject(jsonString);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fj, menu);
        return true;
    }



    private void parseJSONWithJSONObject(String jsondata)
    {
        try{

			/*JSONArray jsonArray = new JSONArray(jsondata);
			JSONObject jsonObject = jsonArray.getJSONObject(1);
			String name = jsonObject.getString("name");
			int age = jsonObject.getInt("age");
			String qq = jsonObject.getString("qq");
			*/
            tv.setText(jsondata);


        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }



    private void sendRequestWithHttpURLConnection(){
        new Thread( new Runnable(){
            @Override
            public void run(){
                String ss = HttpHelper.ReqURL("http://10.99.74.46:808/HpptAPI/JT/GetPro.ashx?ReqName=AllPro", 8000, 8000);
                Message message = new Message();
                message.what=0;
                message.obj = ss;
                //message.obj = respone.toString();

                handler.sendMessage(message);

            }
        }).start();
    }


    private String GetValue(){
        String dd="";
        HttpURLConnection connection = null ;
        try {

            URL url = new URL("http://10.99.74.46:808/HpptAPI/JT/GetPro.ashx");
            // URL url = new URL("http://www.baidu.com");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(5000);
            InputStream in = connection.getInputStream();


            // 对输入流进行读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder respone = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null)
            {
                respone.append(line);
            }



            //Message message = new Message();
            //message.what=0;
            //message.obj = "dsssss";
            //message.obj = respone.toString();

            //handler.sendMessage(message);

            dd=respone.toString();

        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            connection.disconnect();

        }
        return dd;
    }





    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what)
            {
                case 0:
                    String response = (String)msg.obj;

                    parseJSONWithJSONObject(response);
                    //tv.setText(response);
            }
        }
    };

}
