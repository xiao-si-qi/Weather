package com.example.xiaosiqi.tianqi.network;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by xiaosiqi on 2017/12/15.
 */

public class Tools  {
    private static final String TAG = "Tools";
    public String HttpUrl(Context context,String type) {
        String res="";
        String path="http://wthrcdn.etouch.cn/weather_mini?citykey="+type;
        try {
            URL url=new URL(path);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("content-type","application/json");
            conn.setReadTimeout(5000);
            Log.d(TAG, "HttpUrl: 反回值"+conn.getResponseCode());
            if (conn.getResponseCode()==200)
            {
                InputStreamReader reader=new InputStreamReader(conn.getInputStream(),"utf-8");
                BufferedReader buff=new BufferedReader(reader);
                String line="";
                while ((line=buff.readLine())!=null){
                    res+=line;
                }
                buff.close();
                reader.close();
            }
            else
            {Log.d(TAG, "HttpUrl: 反回值不是200");}
        }catch (Exception e)
        {e.printStackTrace();
            Log.d(TAG, "HttpUrl: 其他错误");}
        return res;

    }
}
