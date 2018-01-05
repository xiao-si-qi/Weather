package com.example.xiaosiqi.tianqi.gon_ju;

import android.content.Context;
import android.util.Log;

import com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui.CityClass;
import com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui.CountyClass;
import com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui.ProvinceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaosiqi on 2017/12/19.
 * 解析json里的城市列表
 * getProvince()得到省份
 * getCity() 得到城市
 * getCounty() 得到县
 *
 */

public class CityJeXi {
    String cityJson;
    Context context;
    ProvinceClass provinceClass[]=new ProvinceClass[34];

    public CityJeXi(String cityJson) {
        this.cityJson = cityJson;
    }

    public ProvinceClass[] getProvince()
   {
       try {
           JSONObject jsonObject=new JSONObject(cityJson);
           JSONArray jsonArray=new JSONArray(jsonObject.getString("zone"));
           Log.d(TAG, jsonArray.length()+"个省份");

           for (int i=0;i<jsonArray.length();i++)
           {   JSONObject object=new JSONObject( jsonArray.get(i).toString());

               provinceClass[i]=new ProvinceClass(object.getInt("id"),object.getString("name"),object.getString("zone"));
           }

       } catch (JSONException e) {
           e.printStackTrace();
       }

       return  provinceClass;
   }
    public List<CityClass> getCity(ProvinceClass provinceClass)
    {
        try {
            JSONArray jsonArray=new JSONArray(provinceClass.getZone().toString());
            List<CityClass> cityClassList=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++)
            {   JSONObject object=new JSONObject( jsonArray.get(i).toString());
           //     Log.d(TAG, object.getString("name"));
                cityClassList.add(new CityClass (object.getInt("id"),object.getString("name"),object.getString("zone")));
            }
            return cityClassList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<CountyClass> getCounty(CityClass cityClass)
    {
        try {
            JSONArray jsonArray=new JSONArray(cityClass.getZone().toString());
            List<CountyClass> countyClasses =new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++)
            {   JSONObject object=new JSONObject( jsonArray.get(i).toString());
               // Log.d(TAG, object.getString("name"));
                countyClasses.add(new CountyClass(object.getInt("id"),object.getString("name"),object.getString("code")));
            }
            return countyClasses;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    }



