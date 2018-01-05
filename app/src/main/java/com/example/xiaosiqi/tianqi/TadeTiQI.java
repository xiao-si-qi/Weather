package com.example.xiaosiqi.tianqi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaosiqi on 2017/12/18.
 */

public class TadeTiQI {



    public TianQi getTianQi(String aDay)
    {   TianQi tianQi=new TianQi();
        try {
            JSONObject jsonObject=new JSONObject(aDay);
            tianQi.setDate(jsonObject.getString("date"));
            tianQi.setHige(jsonObject.getString("high"));
            tianQi.setLow(jsonObject.getString("low"));
            tianQi.setFengxiang(jsonObject.getString("fengxiang"));
            tianQi.setFengli(jsonObject.getString("fengli"));
            tianQi.setType(jsonObject.getString("type"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tianQi;
    }
    public TianQi getYesterdayTianQi(String aDay)
    {   TianQi tianQi=new TianQi();
        try {
            JSONObject jsonObject=new JSONObject(aDay);
            tianQi.setDate(jsonObject.getString("date"));
            tianQi.setHige(jsonObject.getString("high"));
            tianQi.setLow(jsonObject.getString("low"));
            tianQi.setFengxiang(jsonObject.getString("fx"));
            tianQi.setFengli(jsonObject.getString("fl"));
            tianQi.setType(jsonObject.getString("type"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tianQi;
    }
}
