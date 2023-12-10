# EverChart
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