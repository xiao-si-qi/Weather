package com.example.xiaosiqi.tianqi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaosiqi.tianqi.gon_ju.IconTianQI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaosiqi on 2017/12/18.
 */

public class TianQIAdapter extends BaseAdapter {
    private Context context;
    private List<TianQi> list=new ArrayList<>();
    private LayoutInflater inflater;
    private String yue;
    private static final String TAG = "TianQIAdapter";
    public TianQIAdapter(Context context, List<TianQi> list) {
        this.context = context;
        this.list = list;
    }

    public TianQIAdapter(Context context, List<TianQi> list, String yue) {
        this.context = context;
        this.list = list;
        this.yue = yue;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null)
        {
            inflater=LayoutInflater.from(context);
            view=inflater.inflate(R.layout.line,null);
        }

        TextView textDate= (TextView) view.findViewById(R.id.textDate);
        TextView textHigh= (TextView) view.findViewById(R.id.textHigh);
        TextView textlow= (TextView) view.findViewById(R.id.textlow);
        ImageView imageType= (ImageView) view.findViewById(R.id.imageType);
        TextView textFengxiang= (TextView) view.findViewById(R.id.textFengxiang);
        TextView textFengli= (TextView) view.findViewById(R.id.textFengli);
        TextView textType= (TextView) view.findViewById(R.id.textType);

        TianQi tianQi=list.get(i);
        IconTianQI iconTianQI=new IconTianQI();

        textDate.setText(yue+tianQi.getDate());
        textHigh.setText(tianQi.getHige());
        textlow.setText(tianQi.getLow());
        textType.setText(tianQi.getType());

        imageType.setImageResource( iconTianQI.tianQiTextToIcon(tianQi.getType()));  //调用相应天气转换为图标的方法

        textFengxiang.setText(tianQi.getFengxiang());
        textFengli.setText(tianQi.getFengli());
        return view;
    }
}
