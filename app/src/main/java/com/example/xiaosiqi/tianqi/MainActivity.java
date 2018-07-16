package com.example.xiaosiqi.tianqi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaosiqi.tianqi.gon_ju.IconTianQI;
import com.example.xiaosiqi.tianqi.gon_ju.SetChartData;
import com.example.xiaosiqi.tianqi.network.IPdinWei;
import com.example.xiaosiqi.tianqi.network.Tools;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView btGenXinTQ;  //更新天气的按钮
    private TextView textChenShi; //显示当前选择的城市
    private TextView btChenShi;//选择城市按钮
    private ImageView btXuanXiang;//展开侧滑菜单的按钮
    private ListView listTianqi;//显示天气的列表
    private ImageView dinWei;  //定位按钮
    private SharedPreferences sp;  //得到当前选择的城市信息
    private ScrollView scrollView;//滑动视图
    SharedPreferences.Editor editor;//存储天气数据
    ImageView zhuTiPNG;
    RelativeLayout tbHeadBar;

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
    //以下为图表控件的定义
    private LineChart wengDuLineChart;
    /*侧滑菜单布局*/
    LinearLayout mLlMenu;
    /*侧滑菜单ListView放置菜单项*/
    ImageView mIvContent;
    DrawerLayout mMyDrawable;
    ActionBarDrawerToggle mToggle;
    private Context context = this;

    private SharedPreferences setDataSP;   //存储设置信息
    private SharedPreferences.Editor setDataSPEditor;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj!=null)
            {
                Toast toast=Toast.makeText(context,"定位成功",Toast.LENGTH_SHORT);
                showMyToast(toast, 1*1000);
                editor.putString("cityMing",    msg.obj.toString());
                editor.commit();
                new MyAsyncTask(msg.obj.toString(),2).execute(); //请求天气数据
            }
            else {
                Toast.makeText(context,"定位失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onRestart() {  //界面被重新加载时刷新数据

        super.onRestart();
        setDataSP = getSharedPreferences("setData", MODE_PRIVATE);
        setDataSPEditor = setDataSP.edit();
        setDataSP.getBoolean("刷新标记", true);
        if (setDataSP.getBoolean("刷新标记", true)) {
            String cityID = sp.getString("cityMing", null);//读取选择的城市
            if (cityID == null || cityID.equals("")) {
                cityID = "北京";//默认为北京
                new MyAsyncTask(cityID,2).execute();
            } else {
                new MyAsyncTask(cityID,2).execute();     //启动网络线程获取数据
            }
            setDataSPEditor.putBoolean("刷新标记", false);
            setDataSPEditor.commit();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = context.getSharedPreferences("cityData", context.MODE_PRIVATE);
        editor = sp.edit();
        setDataSP = getSharedPreferences("setData", MODE_PRIVATE);
        setDataSPEditor = setDataSP.edit();

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
        btGenXinTQ = (TextView) findViewById(R.id.btGenXinTQ);
        btChenShi = (TextView) findViewById(R.id.btChenShi);
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
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        wengDuLineChart = (LineChart) findViewById(R.id.wenDuLineChart);
        dinWei= (ImageView) findViewById(R.id.dingwei);
        ImageView zhuTi1 = (ImageView) findViewById(R.id.zhuTi1);
        ImageView zhuTi2 = (ImageView) findViewById(R.id.zhuTi2);
        ImageView zhuTi3 = (ImageView) findViewById(R.id.zhuTi3);
        ImageView zhuTi4 = (ImageView) findViewById(R.id.zhuTi4);
        zhuTiPNG = (ImageView) findViewById(R.id.zhuTiPNG);
        tbHeadBar = (RelativeLayout) findViewById(R.id.tbHeadBar);
        final int zhuTi = setDataSP.getInt("ZhuTi", getResources().getColor(R.color.zhuti1));
        tbHeadBar.setBackgroundColor(zhuTi);
        listTianqi.setFocusable(false);//   去掉list的焦点，使ScrollView默认位置在顶部

        if (sp.getString("cityMing", null)==null)
        {
            DinWeiThread dinWeiThread=new DinWeiThread();
            dinWeiThread.start();
        }
        String cityID = sp.getString("cityMing", null);//读取选择的城市
        if (sp.getString("TianQIdata", null) != null)   //判断上次是否缓存了天气数据
        {
            jieXiTianqiData(sp.getString("TianQIdata", null));//读取缓存的天气数据到界面
        } else if (cityID == null || cityID.equals("")) {
            cityID = "北京";//默认为北京
            new MyAsyncTask(cityID,2).execute();
        } else {
            new MyAsyncTask(cityID,2).execute();     //启动网络线程获取数据
        }
            //读取主题
        int zhuTiID = setDataSP.getInt("ZhuTiID", 1);
        switch (zhuTiID) {
            case 1: {
                setZhuTi(1,R.mipmap.zhuti1png,ContextCompat.getColor(context, R.color.zhuti1));
            }
            break;
            case 2: {
                setZhuTi(2,R.mipmap.zhuti2png,ContextCompat.getColor(context, R.color.zhuti2));
            }
            break;
            case 3: {
                setZhuTi(3,R.mipmap.zhuti3png,ContextCompat.getColor(context, R.color.zhuti3));
            }
            break;
            case 4: {
                setZhuTi(4,R.mipmap.zhuti4png,ContextCompat.getColor(context, R.color.zhuti4));
            }
            break;

        }


        btXuanXiang.setOnClickListener(new View.OnClickListener() {  //处理打开和关闭侧滑菜单的事件
            @Override
            public void onClick(View view) {

                mMyDrawable.openDrawer(Gravity.LEFT);

            }
        });
        dinWei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DinWeiThread dinWeiThread=new DinWeiThread();
                dinWeiThread.start();
            }
        });

        btGenXinTQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //刷新天气按钮
                String cityID = sp.getString("cityMing", null);
        if (cityID == null || cityID.equals("")) {
            cityID = "北京";//默认为北京
        }
        new MyAsyncTask(cityID,2).execute();
    }
});

        btChenShi.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {//展开策划菜单

        Intent intent = new Intent(context, CityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.heng_yi_in, R.anim.heng_yi_out);
        mMyDrawable.closeDrawer(Gravity.LEFT);
        }
        });
        //设置主题
        zhuTi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZhuTi(1,R.mipmap.zhuti1png, ContextCompat.getColor(context, R.color.zhuti1));
                setChengGong();
            }
        });
        zhuTi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZhuTi(2,R.mipmap.zhuti2png,ContextCompat.getColor(context, R.color.zhuti2));
                setChengGong();
            }
        });
        zhuTi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZhuTi(3,R.mipmap.zhuti3png,ContextCompat.getColor(context, R.color.zhuti3));
                setChengGong();
            }
        });
        zhuTi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZhuTi(4,R.mipmap.zhuti4png,ContextCompat.getColor(context, R.color.zhuti4));
                setChengGong();
            }
        });
        TextView guanyu= (TextView) findViewById(R.id.btGuanYu);
        guanyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,GuanYuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void  setChengGong()
    {
        Toast toast=Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT);
        showMyToast(toast, 1*1000);
    }
    private void setZhuTi(int id, int zhuTi, int zhuTiColor) {  //主题设置方法
        zhuTiPNG.setBackgroundResource(zhuTi);
        setDataSPEditor.putInt("ZhuTi",zhuTiColor);
        setDataSPEditor.putInt("ZhuTiID", id);
        setDataSPEditor.commit();
        tbHeadBar.setBackgroundColor( zhuTiColor);
        jieXiTianqiData(sp.getString("TianQIdata", null));//更新界面

    }


    class MyAsyncTask extends AsyncTask<Void, Void, String> {
        private String city;
        private int ID;

        /***
         *
         * @param city  城市名字或城市ID
         * @param ID   ID为1是城市id，id为2是城市名字
         */
        public MyAsyncTask(String city, int ID) {
            this.city = city;
            this.ID = ID;
        }

        @Override
        protected String doInBackground(Void... voids) {

            Tools tools = new Tools();
            String chaengshi = tools.HttpUrl(ID, city);
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
                Toast toast=Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT);
                showMyToast(toast,1*1000);
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
            SimpleDateFormat formatter2 = new SimpleDateFormat("M月");
            Date curDate = new Date(System.currentTimeMillis());//获取当前日期
            String xiTongRiQI= formatter.format(curDate);
            String xiTongYue= formatter2.format(curDate);


            textDate.setText(xiTongRiQI+tianQi[1].getDate());
            textType.setText(tianQi[1].getType());
            textFengxiang.setText(tianQi[1].getFengxiang());
            textFengli.setText(tianQi[1].getFengli());
            textHigh.setText(tianQi[1].getHige());
            textlow.setText(tianQi[1].getLow());
            textWenDu.setText( wendu);
            IconTianQI iconTianQI = new IconTianQI();
            imageType.setImageResource(iconTianQI.tianQiTextToIcon(tianQi[1].getType()));
            textGanmao.setText(ganmao);
            //将温度的数据写到图表
            SetChartData setChartData = new SetChartData();
            List<Integer> low = new ArrayList<>();
            List<Integer> hige = new ArrayList<>();
            List<String> riQi = new ArrayList<>();
            for (int i = 0; i < tianQi.length; i++) {
                low.add(tianQi[i].getIntLow());
                hige.add(tianQi[i].getIntHige());
                riQi.add(tianQi[i].getXinQi());
                Log.d(TAG, "jieXiTianqiData: " + tianQi[i].getDate());
            }
            setChartData.setLowWengDu(low);
            setChartData.setHighWengDu(hige);
            setChartData.setRiQi(riQi);
            LineData lineData = setChartData.getLineData();
            int zhuTi = setDataSP.getInt("ZhuTi", getResources().getColor(R.color.zhuti1));
            setChartData.showChart(wengDuLineChart, lineData, zhuTi);


            List<TianQi> list = new ArrayList<>();
            for (int i = 2; i < tianQi.length; i++) {//数组0，1是昨天和今天的天气数据
                list.add(tianQi[i]);
            }
            list.get(0).setDate(list.get(0).getDate()+"  \n明天");
            TianQIAdapter tianQIAdapter = new TianQIAdapter(context,list,xiTongYue);
            listTianqi.setAdapter(tianQIAdapter);
            textChenShi.setText(city + "天气");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMyToast(final Toast toast, final int cnt) {//
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
    class DinWeiThread extends Thread{
       @Override
       public void run() {
           super.run();
           super.run();
           IPdinWei iPdinWei=new IPdinWei();
           String weiZhi = iPdinWei.getWeiZhi();
           Log.d(TAG, "我的位置 " +weiZhi);
           Message message=new Message();
           message.obj=weiZhi;
           message.what=1;
           handler.sendMessage(message);
       }
   }
}
