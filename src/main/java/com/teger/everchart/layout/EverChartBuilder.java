package com.teger.everchart.layout;

import com.teger.everchart.data.Axis;
import com.teger.everchart.data.ChartData;
import com.teger.everchart.data.ChartType;
import com.teger.everchart.data.Location;
import com.teger.everchart.exception.EverChartException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EverChartBuilder {
    //region Member Variables
    private String title = "";
    private int width;
    private int height;
    private int x;
    private int y;
    private int dataYMin = 0, dataXMin = 0, dataYMax = 100, dataXMax = 100;
    private int dataYStep = 10, dataXStep = 10;
    private Color background = Color.WHITE;
    private Color line = Color.GRAY;
    private Color text = Color.BLACK;
    private Location dataOverlayLocation = Location.BOTTOM_RIGHT;
    private Location legendLocation = Location.NONE;
    private boolean animation;
    private Axis[] showSubAxis = new Axis[0];
    private ChartType type;
    private final java.util.List<ChartData> dataList = new ArrayList<>();
    //endregion
    //region Builders
    public EverChartBuilder title(String title){
        this.title = title;
        return this;
    }

    public EverChartBuilder x(int x){
        this.x = x;
        return this;
    }

    public EverChartBuilder y(int y){
        this.y = y;
        return this;
    }

    public EverChartBuilder dataXMin(int dataXMin){
        this.dataXMin = dataXMin;
        return this;
    }
    public EverChartBuilder dataYMin(int dataYMin){
        this.dataYMin = dataYMin;
        return this;
    }
    public EverChartBuilder dataXMax(int dataXMax){
        this.dataXMax = dataXMax;
        return this;
    }
    public EverChartBuilder dataYMax(int dataYMax){
        this.dataYMax = dataYMax;
        return this;
    }
    public EverChartBuilder dataYStep(int dataYStep){
        this.dataYStep = dataYStep;
        return this;
    }
    public EverChartBuilder dataXStep(int dataXStep){
        this.dataXStep = dataXStep;
        return this;
    }

    public EverChartBuilder width(int width){
        this.width = width;
        return this;
    }

    public EverChartBuilder height(int height){
        this.height = height;
        return this;
    }

    public EverChartBuilder background(Color background){
        this.background = background;
        return this;
    }

    public EverChartBuilder line(Color line){
        this.line = line;
        return this;
    }

    public EverChartBuilder text(Color text){
        this.text = text;
        return this;
    }

    public EverChartBuilder dataOverlayLocation(Location dataOverlayLocation){
        this.dataOverlayLocation = dataOverlayLocation;
        return this;
    }

    public EverChartBuilder legendLocation(Location legendLocation){
        this.legendLocation = legendLocation;
        return this;
    }

    public EverChartBuilder animation(boolean animation){
        this.animation = animation;
        return this;
    }

    public EverChartBuilder showSubAxis(Axis... showSubAxis){
        this.showSubAxis = showSubAxis;
        return this;
    }

    public EverChartBuilder type(ChartType type){
        this.type = type;
        return this;
    }

    @SafeVarargs
    public final EverChartBuilder appendData(ChartData... data){
        this.dataList.addAll(Arrays.asList(data));
        return this;
    }
    //endregion
    //region Getters
    public String getTitle() {
        return title;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDataYMin() {
        return dataYMin;
    }

    public int getDataXMin() {
        return dataXMin;
    }

    public int getDataYMax() {
        return dataYMax;
    }

    public int getDataXMax() {
        return dataXMax;
    }

    public int getDataYStep() {
        return dataYStep;
    }

    public int getDataXStep() {
        return dataXStep;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getBackground() {
        return background;
    }

    public Color getLine() {
        return line;
    }

    public Color getText() {
        return text;
    }

    public Location getDataOverlayLocation(){
        return dataOverlayLocation;
    }

    public Location getLegendLocation() {
        return legendLocation;
    }

    public boolean isAnimation(){
        return animation;
    }

    public Axis[] isShowSubAxis() {
        return showSubAxis;
    }

    public ChartType getType() {
        return type;
    }

    public List<ChartData> getDataList() {
        return dataList;
    }
    //endregion
    public EverChart build() throws EverChartException {
        return new EverChart(this);
    }
}