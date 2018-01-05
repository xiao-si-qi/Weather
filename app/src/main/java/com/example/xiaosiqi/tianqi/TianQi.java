package com.example.xiaosiqi.tianqi;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaosiqi on 2017/12/18.
 */

public class TianQi {
    private String date;
    private String hige;
    private String low;
    private String fengli;
    private String fengxiang;
    private String type;

    public TianQi(String date, String hige, String low, String fengli, String fengxiang, String type) {
        this.date = date;
        this.hige = hige;
        this.low = low;
        this.fengli = fengli;
        this.fengxiang = fengxiang;
        this.type = type;
    }

    public TianQi() {
    }

    public String getDate() {


        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public String getHige() {
        return hige;
    }

    public void setHige(String hige) {
        String[]  strs=hige.split(" ");
        this.hige =  "最高气温:"+strs[1];
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        String[]  strs=low.split(" ");
        this.low = "最低气温:"+strs[1];
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
        Matcher m = p.matcher(fengli);

        if(m.matches()) {
            this.fengli ="风力"+m.group(1);
        }
        else {
            this.fengli=fengli;
        }

    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
