package com.example.xiaosiqi.tianqi.network;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
 * 得到城市代码
 */

public class IPdinWei {
    private static final String TAG = "Tools";

    public String getWeiZhi() {
        String res = "";
        String path = "http://whois.pconline.com.cn/ipJson.jsp";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("content-type", "application/json");
            conn.setReadTimeout(5000);
            Log.d(TAG, "HttpUrl: 反回值" + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "GBK");
                BufferedReader buff = new BufferedReader(reader);
                String line = "";
                while ((line = buff.readLine()) != null) {
                    res += line;
                }
                buff.close();
                reader.close();
            } else {
                Log.d(TAG, "HttpUrl: 反回值不是200");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "HttpUrl: 其他错误");
        }
        Log.d(TAG, "getWeiZhi: "+res);

        try {
            String jsonData = res.substring(res.indexOf("{", res.indexOf("{") + 1)/*左起第二个话括弧*/, res.indexOf("}") + 1/*第一个反括号结束*/);
            JSONObject jsonObject = new JSONObject(jsonData);
            String cityCode = jsonObject.getString("city");
            return cityCode;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
