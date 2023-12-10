package com.teger.example;

import com.teger.everchart.data.Axis;
import com.teger.everchart.data.ChartData;
import com.teger.everchart.data.ChartType;
import com.teger.everchart.data.Location;
import com.teger.everchart.exception.EverChartException;
import com.teger.everchart.layout.EverChart;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws EverChartException, IOException {
        JFrame frame = new JFrame("test");
        Random r = new Random();
        int[] yData1 = new int[10];
        for(int i = 0; i < 10; i ++) yData1[i] = r.nextInt(0,120);
        int[] yData2 = new int[10];
        for(int i = 0; i < 10; i ++) yData2[i] = r.nextInt(0,120);

        ChartData data1 = new ChartData(
                "TEST1",
                new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100},
                yData1,
                new Color(0, 0, 0)
        );

        ChartData data2 = new ChartData(
                "TEST2",
                new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100},
                yData2,
                new Color(255, 128, 128)
        );

        EverChart chart = EverChart
                .builder()
                .title("EVERCHART")
                .x(0)
                .y(0)
                .width(1000)
                .height(1000)
                .dataYMin(0)
                .dataYMax(120)
                .dataYStep(5)
                .type(ChartType.LINE)
                .showSubAxis(Axis.HORIZONTAL, Axis.VERTICAL)
                .legendLocation(Location.TOP_LEFT)
                .appendData(data1, data2)
                .build();

        frame.setLayout(null);
        frame.setSize(1000, 1028);
        frame.add(chart);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chart.saveChartToImage(new File("test.png"));
    }
}