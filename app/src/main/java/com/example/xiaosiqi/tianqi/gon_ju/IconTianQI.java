package com.example.xiaosiqi.tianqi.gon_ju;

import com.example.xiaosiqi.tianqi.R;

/**
 * Created by xiaosiqi on 2017/12/18.
 * 得到天气所对应的图标
 */
//晴、云、阴、阵雨、雷阵雨、雷阵雨伴冰雹、雨夹雪、雨、雨、雨、暴雨、暴雨、
//        特暴雨、阵雪、雪、雪、雪、暴雪、雾、冻雨、沙尘暴、雨、雨、暴雨、暴雨
//        暴雨、暴雨特暴雨、雪、 雪、暴雪、浮尘、扬沙、强沙尘暴、霾
public class IconTianQI {
    private int 晴 = R.drawable.sunny;
    private int 云 = R.drawable.cloudy3;
    private int 阴 = R.drawable.cloudy5;
    private int 小雨 = R.drawable.light_rain;
    private int 阵雨 = R.drawable.shower3;
    private int 雷阵雨 = R.drawable.tstorm3;
    private int 雨夹雪 = R.drawable.sleet;
    private int 雪 = R.drawable.snow5;
    private int 小雪 = R.drawable.snow4;


    public int tianQiTextToIcon(String type) {
        switch (type) {
            case "晴":
                return 晴;
            case "多云":
                return 云;
            case "阴":
                return 阴;
            case "小雨":
                return 小雨;
            case "阵雨":
                return 阵雨;
            case "雷阵雨":
                return 雷阵雨;
            case "雪":
                return 雪;
            case "小雪":
                return 小雪;
            case "雨夹雪":
                return 雨夹雪;
            default:
                return 晴;

        }

    }


}
