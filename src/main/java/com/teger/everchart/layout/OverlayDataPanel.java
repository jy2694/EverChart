package com.teger.everchart.layout;

import com.teger.everchart.data.ChartData;
import com.teger.everchart.data.Location;

import java.awt.*;

public class OverlayDataPanel {

    private ChartData data;
    private int selected;
    private Color labelColor;
    private Color background;

    //Color Square : Width, Height = textHeight, Margin = 3px
    //Text : Margin = 3px

    public OverlayDataPanel(ChartData data, int selected, Color background, Color labelColor){
        this.data = data;
        this.labelColor = labelColor;
        this.background = background;
        this.selected = selected;
    }

    public int[] predictDataPanelSize(Graphics2D g2d){
        String label = data.getLabel();
        String xData = "X : " + data.getX()[selected];
        String yData = "Y : " + data.getY()[selected];
        int width = g2d.getFontMetrics().stringWidth(label)+9+((int) g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight());
        final int height = (int) (g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight()
                + g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight()
                + g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight()
                + 12);
        if(width < g2d.getFontMetrics().stringWidth(xData)+6){
            width = g2d.getFontMetrics().stringWidth(xData)+6;
        }
        if(width < g2d.getFontMetrics().stringWidth(yData)+6){
            width = g2d.getFontMetrics().stringWidth(yData)+6;
        }
        return new int[] {width, height};
    }

    public void paint(Graphics2D g2d, int x, int y, Location dataOverlayLocation) {
        String xData = "X : " + data.getX()[selected];
        String yData = "Y : " + data.getY()[selected];
        int labelSize = (int) g2d.getFont().getStringBounds(data.getLabel(), g2d.getFontRenderContext()).getHeight();
        int[] predicted = predictDataPanelSize(g2d);

        switch(dataOverlayLocation){
            case TOP -> { x -= predicted[0]/2; y -= predicted[1]; }
            case TOP_LEFT -> { x -= predicted[0]; y -= predicted[1]; }
            case TOP_RIGHT -> y -= predicted[1];
            case MIDDLE_LEFT -> { x -= predicted[0]; y -= predicted[1]/2; }
            case MIDDLE_RIGHT -> y -= predicted[1]/2;
            case BOTTOM -> x -= predicted[0]/2;
            case BOTTOM_LEFT -> x -= predicted[0];
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

        //Draw Color
        g2d.setColor(new Color(
                data.getDataColor().getRed(),
                data.getDataColor().getGreen(),
                data.getDataColor().getBlue(),
                80
        ));
        g2d.fillRect(x+3, y+3,
                labelSize,
                labelSize);
        g2d.setColor(data.getDataColor());
        g2d.drawRect(x+3, y+3,
                labelSize,
                labelSize);

        //Draw Title
        g2d.setColor(labelColor);
        g2d.drawString(data.getLabel(), x+3+labelSize+6, y+3+(labelSize*4/5));

        //Draw X
        g2d.drawString(xData, x+3, y+3+labelSize+6+(labelSize*4/5));
        //Draw Y
        g2d.drawString(yData, x+3, y+3+(labelSize+3)*2+(labelSize*4/5));
    }
}
