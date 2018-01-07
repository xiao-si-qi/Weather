package com.example.xiaosiqi.tianqi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaosiqi.tianqi.gon_ju.CityJeXi;
import com.example.xiaosiqi.tianqi.gon_ju.LocalJsonResolutionUtils;
import com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui.CityClass;
import com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui.CountyClass;
import com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui.ProvinceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    private static final String TAG = "CityActivity";
    private Spinner spProvince;   //选择省份的下拉列表
    private Spinner spCity;       //选择城市的下拉列表
    private Spinner spCounty;  //选择县/市的下拉列表
    private Button btSet;      //设置按钮
    private SharedPreferences setDataSP;   //存储设置信息
    private SharedPreferences.Editor setDataSPEditor;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context = this;
    private LinearLayout title;
    List<CityClass> cityClassList = new ArrayList<>();  //城市列表
    List<CountyClass> countyClasses = new ArrayList<>();//县市列表
    private ProgressDialog loading;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Class decorViewClazz = null;
            try {
                decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setContentView(R.layout.activity_city);
        title= (LinearLayout) findViewById(R.id.Title);
        setDataSP=getSharedPreferences("setData",MODE_PRIVATE);
        setDataSPEditor=setDataSP.edit();
        int zhuTi = setDataSP.getInt("ZhuTi",getResources().getColor(R.color.zhuti1));
        title.setBackgroundColor(zhuTi);
        spProvince = (Spinner) findViewById(R.id.spProvince);
        spCity = (Spinner) findViewById(R.id.spCity);
        spCounty = (Spinner) findViewById(R.id.spounty);
        btSet = (Button) findViewById(R.id.btSet);
        sp = getSharedPreferences("cityData", MODE_PRIVATE);
        editor = sp.edit();
        new MyAsyncTask().execute();
        loading=new ProgressDialog(context);
        loading.setMessage("加载城市数据中");
        loading.setCancelable(true);
        loading.show();


        ImageView imageView= (ImageView) findViewById(R.id.fanhui);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int) spCounty.getSelectedItemId();
                editor.putString("cityID", countyClasses.get(id).getCode());
                editor.putString("cityMing",countyClasses.get(id).getName());
                Log.d(TAG, "onClick: "+countyClasses.get(id).getName());
                editor.putLong("province",spProvince.getSelectedItemId());
                editor.putLong("city",spCity.getSelectedItemId());
                editor.putLong("county",spCounty.getSelectedItemId());
                editor.commit();
                setDataSPEditor.putBoolean("刷新标记",true);//如果是点击了按钮，就应该刷新数据
                setDataSPEditor.commit();
             //   Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
    public class MyAsyncTask extends AsyncTask<Void, Void, String> {       //使用异步类
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {              //更新界面的方法
            super.onPostExecute(s);
            final CityJeXi cityJeXi = new CityJeXi(s);         //得到异步类中加载好的数据
            final ProvinceClass provinceClass[] = cityJeXi.getProvince();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < provinceClass.length; i++) {
                list.add(provinceClass[i].getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, list);
            spProvince.setAdapter(adapter);
            adapter.notifyDataSetChanged();       //通知spinner刷新数据
            spProvince.setSelection((int)sp.getLong("province",0));



            spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {         //省名下拉列表
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    cityClassList = cityJeXi.getCity(provinceClass[i]);
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < cityClassList.size(); j++) {
                        list.add(cityClassList.get(j).getName());
                        //  Log.d(TAG, "城市" + cityClassList.get(j).getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, list);

                    spCity.setAdapter(adapter);
                    adapter.notifyDataSetChanged();       //通知spinner刷新数据
                    spCity.setSelection((int)sp.getLong("city",0));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {       //c城市下拉列表
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    countyClasses = cityJeXi.getCounty(cityClassList.get(i));
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < countyClasses.size(); j++) {
                        list.add(countyClasses.get(j).getName());
                        //  Log.d(TAG, "县市：" + countyClasses.get(j).getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, list);

                    spCounty.setAdapter(adapter);
                    adapter.notifyDataSetChanged();       //通知spinner刷新数据
                    spCounty.setSelection((int)sp.getLong("county",0));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            loading.dismiss();

        }
        @Override
        protected String doInBackground(Void... voids) {
            String fileName = "city.json";
            String cityJson = LocalJsonResolutionUtils.getJson(context, fileName);//加载json文件,这个过在低端手机中程耗时较长，影响体验,所以使用异步加载
            return cityJson;
        }
    }
}
