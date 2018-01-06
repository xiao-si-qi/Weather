package com.example.xiaosiqi.tianqi.gon_ju;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.DefaultYAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaosiqi on 2018/1/5.
 * 图表数据的适配级调整图表样式
 */

public class SetChartData {
    private List<Integer> highWengDu=new ArrayList<>();
    private List<Integer> lowWengDu=new ArrayList<>();
    private List<String> riQi=new ArrayList<>();



    public void setRiQi(List<String> riQi) {
        this.riQi = riQi;
    }


    public void setHighWengDu(List<Integer> highWengDu) {
        this.highWengDu = highWengDu;
    }


    public void setLowWengDu(List<Integer> lowWengDu) {
        this.lowWengDu = lowWengDu;
    }

    public LineData getLineData ()
    {
        List<String> xValue =new ArrayList<>();//x轴数据，日期
        xValue.add("昨天");
        xValue.add("今天");
        xValue.add("明天");
        for (int i = 3; i < riQi.size(); i++) {
            xValue.add(riQi.get(i));
        }

        List<Entry> yValue =new ArrayList<>();  //第一条y轴数据，最低温
        for (int i=0;i<xValue.size();i++)
        {
            yValue.add(new Entry(lowWengDu.get(i),i));
        }
        LineDataSet lineDataSet=new LineDataSet(yValue,"最低温");
        lineDataSet.setValueTextSize(10);  //设置第1条数据的样式
        lineDataSet.setValueFormatter(new MyYAxisValueFormatter());//自定义y轴数据格式
        List<Entry> yValue2 =new ArrayList<>();  //第二条y轴数据，最低温
        for (int i=0;i<xValue.size();i++)
        {
            yValue2.add(new Entry(highWengDu.get(i),i));
        }
        LineDataSet lineDataSet2=new LineDataSet(yValue2,"最高温");//
        lineDataSet2.setValueTextSize(10);//设置第2条数据的样式
        lineDataSet2.setCircleColor(Color.RED);
        lineDataSet2.setValueFormatter(new MyYAxisValueFormatter());//自定义y轴数据格式
        lineDataSet2.setColor(Color.RED);
        List< LineDataSet> lineDataSets=new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet2);
        LineData lineData=new LineData(xValue,lineDataSets);
        return lineData;
    }

    public void showChart(LineChart lineChart,LineData lineData, int color) {
        lineChart.setData(lineData);
        lineChart.setTouchEnabled(false); // 设置是否可以触摸
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getAxisLeft().setEnabled(false); // 隐藏左边 的坐标轴

        lineChart.setDescription("");  //设置描述文字
        lineChart.setScaleEnabled(false);  //是否可以缩放
        lineChart.setDrawGridBackground(false);//是否显示背景


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());//自定义Y轴数据格式

        leftAxis.setStartAtZero(false);//设置Y轴的数据不是从0开始
        leftAxis.setDrawTopYLabelEntry(true);
        XAxis xAxis=lineChart.getXAxis();      //x轴的样式
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(color);
        xAxis.setTextSize(7);  //x轴字体大小
        xAxis.setAxisLineWidth(5);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        lineChart.animateXY(1000,1000);
        lineChart.invalidate();
    }


}
