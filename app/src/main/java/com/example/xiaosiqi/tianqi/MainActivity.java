package com.example.xiaosiqi.tianqi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaosiqi.tianqi.network.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btGenXinTQ;  //更新天气的按钮
    private TextView textChenShi; //显示当前选择的城市
    private Button btChenShi;//选择城市按钮
    private ImageView btXuanXiang;//展开侧滑菜单的按钮
    private ListView listTianqi;//显示天气的列表
    private SharedPreferences sp;  //得到当前选择的城市信息
    SharedPreferences.Editor editor;//存储天气数据

    //以下为显示第一天的天气数据的控件
    private TextView textDate;    //显示今天的日期
    private TextView textWenDu;   //显示今天的温度
    private TextView textFengxiang;//显示今天的风向
    private TextView textFengli;   //显示今天的风力
    private TextView textHigh;    //显示今天最高温度
    private TextView textlow;     //显示今天的最低温度
    private TextView textGanmao;  //显示感冒指数
    private ImageView imageType;  //显示天气图标
    private TextView textType;    //显示今天天气
    /*侧滑菜单布局*/
    LinearLayout mLlMenu;
    /*侧滑菜单ListView放置菜单项*/
    ImageView mIvContent;
    DrawerLayout mMyDrawable;
    ActionBarDrawerToggle mToggle;

    private Context context = this;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                }
            }
        }
    };

    @Override
    protected void onRestart() {  //
        super.onRestart();
        String cityID = sp.getString("cityID", null);//读取选择的城市
        if (cityID == null || cityID.equals("")) {
            cityID = "101010100";//默认为北京
            new MyAsyncTask(cityID).execute();
        } else {
            new MyAsyncTask(cityID).execute();     //启动网络线程获取数据
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = context.getSharedPreferences("cityData", context.MODE_PRIVATE);
        editor = sp.edit();
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


        setContentView(R.layout.activity_main);
        btGenXinTQ = (Button) findViewById(R.id.btGenXinTQ);
        btChenShi = (Button) findViewById(R.id.btChenShi);
        textChenShi = (TextView) findViewById(R.id.textChenShi);
        listTianqi = (ListView) findViewById(R.id.listTianqi);
        textDate = (TextView) findViewById(R.id.textDate);
        textWenDu = (TextView) findViewById(R.id.textWenDu);
        textFengxiang = (TextView) findViewById(R.id.textFengxiang);
        textFengli = (TextView) findViewById(R.id.textFengli);
        textHigh = (TextView) findViewById(R.id.textHigh);
        textlow = (TextView) findViewById(R.id.textlow);
        textGanmao = (TextView) findViewById(R.id.textGanmao);
        imageType = (ImageView) findViewById(R.id.imageType);
        textType = (TextView) findViewById(R.id.textType);
        mLlMenu = (LinearLayout) findViewById(R.id.llMenu);
        mMyDrawable = (DrawerLayout) findViewById(R.id.dlMenu);
        btXuanXiang = (ImageView) findViewById(R.id.btXuanXiang);


        String cityID = sp.getString("cityID", null);//读取选择的城市
        if (sp.getString("TianQIdata", null) != null)   //判断上次是否缓存了天气数据
        {
            jieXiTianqiData(sp.getString("TianQIdata", null));//读取缓存的天气数据到界面
        } else if (cityID == null || cityID.equals("")) {
            cityID = "101010100";//默认为北京
            new MyAsyncTask(cityID).execute();
        } else {
            new MyAsyncTask(cityID).execute();     //启动网络线程获取数据
        }


        btXuanXiang.setOnClickListener(new View.OnClickListener() {  //处理打开和关闭侧滑菜单的事件
            @Override
            public void onClick(View view) {

                    mMyDrawable.openDrawer(Gravity.LEFT);

            }
        });

        btGenXinTQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityID = sp.getString("cityID", null);
                if (cityID == null || cityID.equals("")) {
                    cityID = "101010100";//默认为北京
                }
                new MyAsyncTask(cityID).execute();


            }
        });

        btChenShi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CityActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.heng_yi_in, R.anim.heng_yi_out);
                mMyDrawable.closeDrawer(Gravity.LEFT);
            }
        });
    }

    class MyAsyncTask extends AsyncTask<Void, Void, String> {
        private String city;

        public MyAsyncTask(String city) {
            this.city = city;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Tools tools = new Tools();
            String chaengshi = tools.HttpUrl(context, city);
            Log.d(TAG, "数据 " + chaengshi);
            String data = "";
            try {
                JSONObject object = new JSONObject(chaengshi);
                data = object.getString("data");

            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {    //获取数据回来进行解析
            super.onPostExecute(s);
            if (!(s == null || s.equals(""))) {
                jieXiTianqiData(s); //解析天气数据
                Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
                editor.putString("TianQIdata", s);  //缓存天气数据
                editor.commit();
            } else {
                Toast.makeText(context, "没有从网络上得到信息，请检查网络", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    void jieXiTianqiData(String data) {
        try {

            JSONObject jsonObjec = new JSONObject(data);     //将得到的数据进行解析
            String city = jsonObjec.getString("city");       //得到城市
            String forecast = jsonObjec.getString("forecast");   //得到天气信息
            String wendu = jsonObjec.getString("wendu");
            String ganmao = jsonObjec.getString("ganmao");
            JSONArray jsonArray = new JSONArray(forecast);
            TadeTiQI tadeTiQI = new TadeTiQI();
            TianQi tianQi[] = new TianQi[6];
            tianQi[0] = tadeTiQI.getYesterdayTianQi(jsonObjec.getString("yesterday"));  //得到昨天的天气数据
            for (int i = 1; i <= jsonArray.length(); i++) {
                tianQi[i] = tadeTiQI.getTianQi(jsonArray.get(i - 1).toString());
            }
            //以下的代码更新今天的天气数据到主界面
            textDate.setText(tianQi[1].getDate());
            textType.setText(tianQi[1].getType());
            textFengxiang.setText(tianQi[1].getFengxiang());
            textFengli.setText(tianQi[1].getFengli());
            textHigh.setText(tianQi[1].getHige());
            textlow.setText(tianQi[1].getLow());
            textWenDu.setText("温度：" + wendu + "℃");
            IconTianQI iconTianQI = new IconTianQI();
            imageType.setImageResource(iconTianQI.tianQiTextToIcon(tianQi[1].getType()));
            textGanmao.setText(ganmao);

            List<TianQi> list = new ArrayList<>();
            for (int i = 2; i < tianQi.length; i++) {//数组0，1是昨天和今天的天气数据
                list.add(tianQi[i]);
            }
            TianQIAdapter tianQIAdapter = new TianQIAdapter(context, list);
            listTianqi.setAdapter(tianQIAdapter);
            textChenShi.setText(city + "天气");

        } catch (JSONException e) {
            e.printStackTrace();

        }


    }


}
