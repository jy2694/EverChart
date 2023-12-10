package com.teger.everchart.layout;

import com.teger.everchart.data.ChartData;
import com.teger.everchart.data.Location;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OverlayLegendPanel{

    private List<ChartData> dataList;
    private Color labelColor;
    private Color background;

    public OverlayLegendPanel(List<ChartData> dataList, Color background, Color labelColor){
        this.dataList = dataList;
        this.labelColor = labelColor;
        this.background = background;
    }

    public int[] predictLegendPanelSize(Graphics2D g2d){
        int width = 0;
        int height = 0;
        for(ChartData data : dataList){
            int labelWidth = g2d.getFontMetrics().stringWidth(data.getLabel());
            if(width < labelWidth + 26){
                width = labelWidth + 26;
            }
            int labelHeight = (int) g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight();
            height += 6 + labelHeight;
        }
        return new int[]{width, height};
    }

    public void paint(JPanel panel, Graphics g, Location legendLocation) {
        Graphics2D g2d = (Graphics2D) g;
        int[] predicted = predictLegendPanelSize(g2d);
        int x = 0, y = 0;
        switch (legendLocation){
            case TOP -> { x = panel.getWidth()/2 - predicted[0]/2; y = 40; }
            case TOP_LEFT -> { x = 40; y = 40; }
            case TOP_RIGHT -> { x = panel.getWidth()-40-predicted[0]; y = 40; }
            case MIDDLE_LEFT -> { x = 40; y = panel.getHeight()/2 - predicted[1]/2; }
            case MIDDLE_RIGHT -> { x = panel.getWidth()-40-predicted[0]; y = panel.getHeight()/2 - predicted[1]/2; }
            case BOTTOM_LEFT -> { x = 40; y = panel.getHeight()-40-predicted[1]; }
            case BOTTOM -> { x = panel.getWidth()/2 - predicted[0]/2; y = panel.getHeight()-40-predicted[1]; }
            case BOTTOM_RIGHT -> { x = panel.getWidth()-40- predicted[0]; y = panel.getHeight()-40-predicted[1]; }
            case NONE -> { return; }
        }
        g2d.setColor(new Color(
                background.getRed(),
                background.getGreen(),
                background.getBlue(),
                200
        ));
        g2d.fillRect(x, y, predicted[0], predicted[1]);
        g2d.setColor(labelColor);
        g2d.drawRect(x, y, predicted[0], predicted[1]);


        int cx = 3, cy = 3;
        for(ChartData data : dataList){
            int labelSize = (int) g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight();
            g2d.setColor(new Color(
                    data.getDataColor().getRed(),
                    data.getDataColor().getGreen(),
                    data.getDataColor().getBlue(),
                    80
            ));
            g2d.fillRect(x+cx, y+cy, labelSize, labelSize);
            g2d.setColor(data.getDataColor());
            g2d.drawRect(x+cx, y+cy, labelSize, labelSize);
            g2d.setColor(labelColor);
            g2d.drawString(data.getLabel(), x+cx+labelSize+6, y+cy+(labelSize*4/5));
            cy+=6+labelSize;
        }
    }
}
