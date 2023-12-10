# EverChart

![](https://img.shields.io/badge/OpenJDK-17+-F80000?logo=openjdk&logoColor=F80000&style=flat-square)
![](https://img.shields.io/badge/EverChart-0.1-green?style=flat-square)

Data visualization library using Java. It can be inherited from JPanel and used with Java Swing components, and multiple items can be customized.

---

### Install

```
Preparing
```

---

### How to use

```java
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
```

![PREVIEW](https://github.com/jy2694/EverChart/blob/main/README-EXAMPLE.png?raw=true)

---

### EverChart Builder

| method                 | type         | description                                                                |
|------------------------|--------------|----------------------------------------------------------------------------|
| .title()               | String       | Set the chart title.                                                       |
| .width()               | int          | Set the chart width.(px)                                                   |
| .height()              | int          | Set the chart height.(px)                                                  |
| .x()                   | int          | Set the X position in the chart.(px)                                       |
| .y()                   | int          | Set the Y position in the chart.(px)                                       |
| .dataYMin()            | int          | Set the Y-axis minimum value to display in the chart.                      |
| .dataYMax()            | int          | Set the Y-axis maximum value to display in the chart.                      |
| .dataYStep()           | int          | Set the interval between the Y-axis to display in the chart.               |
| .dataXMin()            | int          | Set the X-axis minimum value to display in the chart.                      |
| .dataXMax()            | int          | Set the X-axis maximum value to display in the chart.                      |
| .dataXStep()           | int          | Set the interval between the X-axis to display in the chart.               |
| .background()          | Color        | Set the background color of the chart.                                     |
| .line()                | Color        | Set the line color of the chart.                                           |
| .text()                | Color        | Set the text color of the chart.                                           |
| .dataOverlayLocation() | Location     | Set the detailed view overlay position in the data displayed in the chart. |
| .legendLocation()      | Location     | Set the legend position of the chart.                                      |
| .animation()           | boolean      | Set whether to enable chart animation. (Unsupported Yet)                   |
| .showSubAxis()         | Axis...      | Set the sub axis inside the chart to be drawn.                             |
| .type()                | ChartType    | Set the type of chart (LINE, BAR, LINE_BAR, SCATTER)                       |
| .appendData()          | ChartData... | Add data to the chart.                                                     |

---

#### EverChart Types

* Location (ENUM) : Enumeration that specifies legend location and detail view overlay location.
  + TOP
  + TOP_LEFT
  + TOP_RIGHT
  + MIDDLE_LEFT
  + MIDDLE_RIGHT
  + BOTTOM
  + BOTTOM_LEFT
  + BOTTOM_RIGHT
* Axis (ENUM) : Enumeration that sets up Sub Axis.
  + VERTICAL
  + HORIZONTAL
* ChartType (ENUM) : Enumeration that sets the chart type.
  + LINE
  + SCATTER
  + BAR
  + LINE_BAR
* ChartData (CLASS) : Instance to be added to the chart.
  + Constructor(String label, int[] x, int[] y, Color color)
