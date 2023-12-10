package com.teger.everchart.layout;

import com.teger.everchart.data.Axis;
import com.teger.everchart.data.ChartData;
import com.teger.everchart.data.ChartType;
import com.teger.everchart.data.Location;
import com.teger.everchart.exception.EverChartException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class EverChart extends JPanel{
    //region Member Variable
    private final String title;
    private final int dataYMin, dataXMin, dataYMax, dataXMax;
    private final int dataYStep, dataXStep;
    private final Color background;
    private final Color line;
    private final Color text;
    private final Location dataOverlayLocation;
    private final Location legendLocation;
    private final boolean animation;
    private final Axis[] showSubAxis;
    private final ChartType type;
    private final java.util.List<ChartData> dataList;
    private int mouseX, mouseY;
    //endregion
    //region Constructor
    public EverChart(EverChartBuilder builder) throws EverChartException {
        //region Member Variable Setting
        this.title = builder.getTitle();
        this.setLocation(builder.getX(), builder.getY());
        this.setSize(builder.getWidth(), builder.getHeight());
        this.dataYMin = builder.getDataYMin();
        dataYMax = builder.getDataYMax();
        dataXMin = builder.getDataXMin();
        dataXMax = builder.getDataXMax();
        dataYStep = builder.getDataYStep();
        dataXStep = builder.getDataXStep();
        this.setBackground(this.background = builder.getBackground());
        this.line = builder.getLine();
        this.text = builder.getText();
        this.dataOverlayLocation = builder.getDataOverlayLocation();
        this.legendLocation = builder.getLegendLocation();
        this.animation = builder.isAnimation();
        this.showSubAxis = builder.isShowSubAxis();
        this.type = builder.getType();
        this.dataList = builder.getDataList();
        //endregion
        //region throw exception
        if(builder.getTitle() == null) throw new EverChartException("title can not be null.");
        if(dataXMin >= dataXMax) throw new EverChartException("dataXMax must be greater than dataXMin.");
        if(dataYMin >= dataYMax) throw new EverChartException("dataYMax must be greater than dataYMin.");
        if(dataXStep <= 0) throw new EverChartException("dataXStep must be positive.");
        if(dataYStep <= 0) throw new EverChartException("dataYStep must be positive.");
        if(builder.getBackground() == null) throw new EverChartException("background can not be null.");
        if(builder.getLine() == null) throw new EverChartException("line can not be null.");
        if(builder.getText() == null) throw new EverChartException("text can not be null.");
        if(builder.isShowSubAxis() == null) throw new EverChartException("sub axis can not be null.");
        if(builder.getType() == null) throw new EverChartException("type can not be null.");
        if((double)(getHeight()-65) / (double)(dataYMax - dataYMin) * dataYStep <= 1){
            int recommendedStep = (int) Math.ceil((double)(dataYMax - dataYMin) / ((double) getHeight()-65));
            throw new EverChartException("The step on the Y-axis is too small, it must be set to be greater than or equal to at least "+recommendedStep+" to display normally.");
        }
        if((double)(getWidth()-65) / (double)(dataXMax - dataXMin) * dataXStep <= 1){
            int recommendedStep = (int) Math.ceil((double)(dataXMax - dataXMin) / ((double) getWidth()-65));
            throw new EverChartException("The step on the X-axis is too small, it must be set to be greater than or equal to at least "+recommendedStep+" to display normally.");
        }
        //endregion
        //region Motion event add
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });
        //endregion
        //region Set Layout
        setLayout(null);
        //endregion
    }
    //endregion
    //region Renderer

    void drawLineData(Graphics2D g2d){
        double dy = (double)(getHeight()-65) / (double)((dataYMax - dataYMin));
        double dx = (double)(getWidth()-65) / (double)((dataXMax - dataXMin));
        for(ChartData data : dataList){
            int[] previousLoc = null;
            for(int i = 0; i < Math.min(data.getX().length, data.getY().length); i ++){
                double centerX = 35 + (dx*(data.getX()[i]-dataXMin));
                double centerY = getHeight()-35-(dy*(data.getY()[i]-dataYMin));
                g2d.setColor(new Color(data.getDataColor().getRed(),
                        data.getDataColor().getGreen(),
                        data.getDataColor().getBlue(), 80));
                g2d.fillOval((int) (centerX-(8 /2)), (int) (centerY-(8 /2)), 8, 8);
                g2d.setColor(data.getDataColor());
                g2d.drawOval((int) (centerX-(8 /2)), (int) (centerY-(8 /2)), 8, 8);
                if(previousLoc != null){
                    g2d.drawLine((int) centerX, (int) centerY, previousLoc[0], previousLoc[1]);
                }
                previousLoc = new int[]{(int) centerX, (int) centerY};
            }
        }
    }
    void drawBarData(Graphics2D g2d){
        double dy = (double)(getHeight()-65) / (double)((dataYMax - dataYMin));
        double dx = (double)(getWidth()-65) / (double)((dataXMax - dataXMin));
        int minimumStep = Integer.MAX_VALUE;
        for(ChartData data : dataList){
            for(int i = 1; i < Math.min(data.getX().length, data.getY().length); i ++){
                int step = Math.abs(data.getX()[i] - data.getX()[i-1]);
                if(minimumStep > step){
                    minimumStep = step;
                }
            }
        }

        int idx = 0;
        double totalWidth = dx*minimumStep*9/10;
        double width = (totalWidth - (dataList.size()*2)) / dataList.size();
        for(ChartData data : dataList){
            for(int i = 0; i < Math.min(data.getX().length, data.getY().length); i ++){
                double startX = 35 + (dx*(data.getX()[i]-dataXMin)) - (totalWidth / 2) + (idx * width);
                double centerY = getHeight()-35-(dy*(data.getY()[i]-dataYMin));
                g2d.setColor(new Color(data.getDataColor().getRed(),
                        data.getDataColor().getGreen(),
                        data.getDataColor().getBlue(), 80));
                g2d.fillRect((int) startX, (int) centerY, (int) width, (int) (dy*(data.getY()[i]-dataYMin)));
                g2d.setColor(data.getDataColor());
                g2d.drawRect((int) startX, (int) centerY, (int) width, (int) (dy*(data.getY()[i]-dataYMin)));
            }
            idx++;
        }
    }
    void drawScatterData(Graphics2D g2d){
        double dy = (double)(getHeight()-65) / (double)((dataYMax - dataYMin));
        double dx = (double)(getWidth()-65) / (double)((dataXMax - dataXMin));
        for(ChartData data : dataList){
            for(int i = 0; i < Math.min(data.getX().length, data.getY().length); i ++){
                double centerX = 35 + (dx*(data.getX()[i]-dataXMin));
                double centerY = getHeight()-35-(dy*(data.getY()[i]-dataYMin));
                g2d.setColor(new Color(data.getDataColor().getRed(),
                        data.getDataColor().getGreen(),
                        data.getDataColor().getBlue(), 80));
                g2d.fillOval((int) (centerX-(8 /2)), (int) (centerY-(8 /2)), 8, 8);
                g2d.setColor(data.getDataColor());
                g2d.drawOval((int) (centerX-(8 /2)), (int) (centerY-(8 /2)), 8, 8);
            }
        }
    }
    void drawLineBarData(Graphics2D g2d){
        double dy = (double)(getHeight()-65) / (double)((dataYMax - dataYMin));
        double dx = (double)(getWidth()-65) / (double)((dataXMax - dataXMin));
        int minimumStep = Integer.MAX_VALUE;
        for(ChartData data : dataList){
            for(int i = 1; i < Math.min(data.getX().length, data.getY().length); i ++){
                int step = Math.abs(data.getX()[i] - data.getX()[i-1]);
                if(minimumStep > step){
                    minimumStep = step;
                }
            }
        }

        int idx = 0;
        double totalWidth = dx*minimumStep*9/10;
        double width = (totalWidth - (dataList.size()*2)) / dataList.size();
        for(ChartData data : dataList){
            int[] previousLoc = null;
            for(int i = 0; i < Math.min(data.getX().length, data.getY().length); i ++){
                double startX = 35 + (dx*(data.getX()[i]-dataXMin)) - (totalWidth / 2) + (idx * width);
                double centerX = startX + (width/2);
                double centerY = getHeight()-35-(dy*(data.getY()[i]-dataYMin));
                g2d.setColor(new Color(data.getDataColor().getRed(),
                        data.getDataColor().getGreen(),
                        data.getDataColor().getBlue(), 80));
                g2d.fillRect((int) startX, (int) centerY, (int) width, (int) (dy*(data.getY()[i]-dataYMin)));
                g2d.setColor(data.getDataColor());
                g2d.drawRect((int) startX, (int) centerY, (int) width, (int) (dy*(data.getY()[i]-dataYMin)));
                if(previousLoc != null){
                    g2d.drawLine((int) centerX, (int) centerY, previousLoc[0], previousLoc[1]);
                }
                previousLoc = new int[]{(int) centerX, (int) centerY};
            }
            idx++;
        }
    }
    void drawData(Graphics2D g2d){
        switch(type){
            case LINE -> drawLineData(g2d);
            case BAR -> drawBarData(g2d);
            case SCATTER -> drawScatterData(g2d);
            case LINE_BAR -> drawLineBarData(g2d);
        }
    }
    void drawAxis(Graphics2D g2d){
        g2d.setColor(line);
        g2d.drawLine(35, 35, 35, getHeight()-35);
        g2d.drawLine(35, getHeight()-35, getWidth()-35, getHeight()-35);
    }
    void drawMouse(Graphics2D g2d){
        Graphics2D copy = (Graphics2D) g2d.create();
        if(mouseX == 0 && mouseY == 0) return;
        if(mouseX < 35) return;
        if(mouseX > getWidth()-35) return;
        if(mouseY < 35) return;
        if(mouseY > getHeight()-35) return;
        double[] near = findNearPoint(mouseX, mouseY, 30);
        ChartData data = findNearData(mouseX, mouseY, 30);
        if(near == null){
            g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,new float[]{5,1f},0));
            g2d.setColor(line);
            g2d.drawLine(35, mouseY, getWidth()-35, mouseY);
            g2d.drawLine(mouseX, 35, mouseX, getHeight()-35);
        } else {
            g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,new float[]{5,1f},0));
            g2d.setColor(line);
            g2d.drawLine(35, (int) near[1], getWidth()-35, (int) near[1]);
            g2d.drawLine((int) near[0], 35, (int) near[0], getHeight()-35);
            new OverlayDataPanel(data, (int) near[2], background, text).paint(copy, (int) near[0], (int) near[1], dataOverlayLocation);
        }
    }
    ChartData findNearData(int x, int y, int range){
        double cost = Double.MAX_VALUE;
        ChartData target = null;

        double dy = (double)(getHeight()-65) / (double)((dataYMax - dataYMin));
        double dx = (double)(getWidth()-65) / (double)((dataXMax - dataXMin));
        int minimumStep = Integer.MAX_VALUE;
        for(ChartData data : dataList){
            for(int i = 1; i < Math.min(data.getX().length, data.getY().length); i ++){
                int step = Math.abs(data.getX()[i] - data.getX()[i-1]);
                if(minimumStep > step){
                    minimumStep = step;
                }
            }
        }

        int idx = 0;
        double totalWidth = dx*minimumStep*9/10;
        double width = (totalWidth - (dataList.size()*2)) / dataList.size();

        switch(type){
            case LINE, SCATTER -> {
                for(ChartData data : dataList) {
                    for (int i = 0; i < Math.min(data.getX().length, data.getY().length); i++) {
                        double centerX = 35 + (dx * (data.getX()[i]-dataXMin));
                        double centerY = getHeight() - 35 - (dy * (data.getY()[i]-dataYMin));
                        double distance = Math.sqrt(Math.pow(centerX-x, 2) + Math.pow(centerY-y, 2));
                        if(cost > distance){
                            cost = distance;
                            target = data;
                        }
                    }
                }
            }
            case BAR, LINE_BAR -> {
                for(ChartData data : dataList) {
                    for (int i = 0; i < Math.min(data.getX().length, data.getY().length); i++) {
                        double startX = 35 + (dx * (data.getX()[i]-dataXMin)) - (totalWidth / 2) + (idx * width);
                        double centerX = startX + (width / 2);
                        double centerY = getHeight() - 35 - (dy * (data.getY()[i]-dataYMin));
                        double distance = Math.sqrt(Math.pow(centerX-x, 2) + Math.pow(centerY-y, 2));
                        if(cost > distance){
                            cost = distance;
                            target = data;
                        }
                    }
                    idx++;
                }
            }
        }
        if(cost > range) return null;
        return target;
    }
    double[] findNearPoint(int x, int y, int range){
        double cost = Double.MAX_VALUE;
        double[] target = null;

        double dy = (double)(getHeight()-65) / (double)((dataYMax - dataYMin));
        double dx = (double)(getWidth()-65) / (double)((dataXMax - dataXMin));
        int minimumStep = Integer.MAX_VALUE;
        for(ChartData data : dataList){
            for(int i = 1; i < Math.min(data.getX().length, data.getY().length); i ++){
                int step = Math.abs(data.getX()[i] - data.getX()[i-1]);
                if(minimumStep > step){
                    minimumStep = step;
                }
            }
        }

        int idx = 0;
        double totalWidth = dx*minimumStep*9/10;
        double width = (totalWidth - (dataList.size()*2)) / dataList.size();

        switch(type){
            case LINE, SCATTER -> {
                for(ChartData data : dataList) {
                    for (int i = 0; i < Math.min(data.getX().length, data.getY().length); i++) {
                        double centerX = 35 + (dx * (data.getX()[i]-dataXMin));
                        double centerY = getHeight() - 35 - (dy * (data.getY()[i]-dataYMin));
                        double distance = Math.sqrt(Math.pow(centerX-x, 2) + Math.pow(centerY-y, 2));
                        if(cost > distance){
                            cost = distance;
                            target = new double[]{centerX, centerY, i};
                        }
                    }
                }
            }
            case BAR, LINE_BAR -> {
                for(ChartData data : dataList) {
                    for (int i = 0; i < Math.min(data.getX().length, data.getY().length); i++) {
                        double startX = 35 + (dx * (data.getX()[i]-dataXMin)) - (totalWidth / 2) + (idx * width);
                        double centerX = startX + (width / 2);
                        double centerY = getHeight() - 35 - (dy * (data.getY()[i]-dataYMin));
                        double distance = Math.sqrt(Math.pow(centerX-x, 2) + Math.pow(centerY-y, 2));
                        if(cost > distance){
                            cost = distance;
                            target = new double[]{centerX, centerY, i};
                        }
                    }
                    idx++;
                }
            }
        }
        if(cost > range) return null;
        return target;
    }
    void drawSubAxis(Graphics2D g2d){
        Stroke dashed = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,new float[]{7,1f},0);
        boolean horizontal = Arrays.stream(showSubAxis).anyMatch(axis -> axis == Axis.HORIZONTAL);
        boolean vertical = Arrays.stream(showSubAxis).anyMatch(axis -> axis == Axis.VERTICAL);
        g2d.setStroke(dashed);
        g2d.setColor(line);
        double dy = (double)(getHeight()-65) / (double)(dataYMax - dataYMin);
        for(int i = dataYMin; i <= dataYMax; i += dataYStep){
            g2d.drawLine(30, (int) (getHeight()-35-((i-dataYMin)*dy)), horizontal ? getWidth()-35 : 40, (int) (getHeight()-35-((i-dataYMin)*dy)));
        }
        double dx = (double)(getWidth()-65) / (double)(dataXMax - dataXMin);
        for(int i = dataXMin; i <= dataXMax; i += dataXStep){
            g2d.drawLine((int) (35 + ((i-dataXMin) * dx)), vertical ? 35 : getHeight()-40, (int) (35 + ((i-dataXMin) * dx)), getHeight()-30);
        }
    }
    void drawDataText(Graphics2D g2d){
        int fontSize = Integer.MAX_VALUE;
        int idx = 0;
        for(int i = dataYMin; i <= dataYMax; i += dataYStep) {
            String ydata = Integer.toString(dataYMin + (dataYStep * idx));
            while (g2d.getFontMetrics().stringWidth(ydata) > 30) {
                Font font = g2d.getFont();
                g2d.setFont(new Font(font.getFontName(), Font.PLAIN, font.getSize() - 1));
            }
            if(fontSize > g2d.getFont().getSize()){
                fontSize = g2d.getFont().getSize();
            }
            idx++;
        }
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, fontSize));
        g2d.setColor(text);
        double dy = (double) (getHeight()-65) / (double)((dataYMax - dataYMin));
        idx = 0;
        for(int i = dataYMin; i <= dataYMax; i += dataYStep){
            String ydata = Integer.toString(dataYMin + (dataYStep * idx));
            g2d.drawString(ydata, 30 - g2d.getFontMetrics().stringWidth(ydata), (int) (getHeight()-35-(dy*(i-dataYMin))+(g2d.getFont().getStringBounds(ydata, g2d.getFontRenderContext()).getHeight() / 2)));
            idx++;
        }
        double dx = (double) (getWidth()-65) / (double) ((dataXMax - dataXMin));
        idx = 0;
        for(int i = dataXMin; i <= dataXMax; i += dataXStep){
            String xdata = Integer.toString(dataXMin + (dataXStep * idx));
            g2d.drawString(xdata, (int) (35+((i-dataXMin)*dx)-(g2d.getFontMetrics().stringWidth(xdata) / 2)), (int) (getHeight()-35 + (g2d.getFont().getStringBounds(xdata, g2d.getFontRenderContext()).getHeight())));
            idx++;
        }
    }
    void drawTitle(Graphics2D g2d){
        g2d.setColor(text);
        g2d.setFont(new Font("LucidaGrande", Font.BOLD, 20));
        g2d.drawString(this.title, getWidth()/2 - (g2d.getFontMetrics().stringWidth(this.title)/2), 20);
    }
    void drawChart(Graphics g){
        drawTitle((Graphics2D) g.create());
        drawAxis((Graphics2D) g.create());
        drawSubAxis((Graphics2D) g.create());
        drawDataText((Graphics2D) g.create());
        drawData((Graphics2D) g.create());
        drawMouse((Graphics2D) g.create());
    }
    void drawLegend(Graphics g){
        OverlayLegendPanel panel = new OverlayLegendPanel(dataList, background, text);
        panel.paint(this, g, legendLocation);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawChart(g);
        drawLegend(g);
    }
    //endregion

    public void saveChartToImage(File file) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawChart(g);
        drawLegend(g);
        ImageIO.write(image, "png", file);
    }
    public static EverChartBuilder builder(){
        return new EverChartBuilder();
    }

}
