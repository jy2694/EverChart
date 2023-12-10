package com.teger.everchart.data;

import java.awt.*;

public class ChartData {
    private String label;
    private int[] x;
    private int[] y;
    private Color dataColor;

    public ChartData(String label, int[] x, int[] y, Color dataColor) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.dataColor = dataColor;
    }

    public String getLabel() {
        return label;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public Color getDataColor() {
        return dataColor;
    }
}
