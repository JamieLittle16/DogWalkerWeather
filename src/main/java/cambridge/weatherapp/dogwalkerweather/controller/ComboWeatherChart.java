package cambridge.weatherapp.dogwalkerweather.controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public class ComboWeatherChart extends StackPane {

    private BarChart<String, Number> initRainChart() {
        final CategoryAxis barXAxis = new CategoryAxis();
        final NumberAxis barYAxis = new NumberAxis();

        BarChart<String, Number> bar = new BarChart<>(barXAxis, barYAxis);
        bar.setCategoryGap(0.5);
        bar.setBarGap(0);

        XYChart.Series<String, Number> rain = new XYChart.Series();

        rain.getData().add(new XYChart.Data<>("0:00", 82));
        rain.getData().add(new XYChart.Data<>("1:00", 63));
        rain.getData().add(new XYChart.Data<>("2:00", 74));
        rain.getData().add(new XYChart.Data<>("3:00", 74));
        rain.getData().add(new XYChart.Data<>("4:00", 70));
        rain.getData().add(new XYChart.Data<>("5:00", 72));
        rain.getData().add(new XYChart.Data<>("6:00", 68));
        rain.getData().add(new XYChart.Data<>("7:00", 67));
        rain.getData().add(new XYChart.Data<>("8:00", 60));
        rain.getData().add(new XYChart.Data<>("9:00", 100));

        bar.getData().add(rain);

        bar.setBackground(Background.EMPTY);
        bar.lookupAll(".chart-bar").forEach(node ->
                node.setStyle("-fx-bar-fill: #22bad9;")
        );
        return bar;
    }

    private LineChart<Number, Number> initTemperatureChart() {
        final NumberAxis xAxis = new NumberAxis();

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);


        XYChart.Series series = new XYChart.Series();

        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        lineChart.getData().add(series);

        lineChart.setBackground(Background.EMPTY);
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent; ");
        lineChart.lookup(".default-color0.chart-series-line").setStyle("-fx-stroke: red;");

        lineChart.getXAxis().setVisible(false);
        lineChart.getXAxis().setTickLabelsVisible(false);

        lineChart.getYAxis().setSide(Side.RIGHT);

        return lineChart;
    }

    public ComboWeatherChart() {

        BarChart rainChart = initRainChart();

        LineChart tempChart = initTemperatureChart();

        this.getChildren().add(rainChart);
        this.getChildren().add(tempChart);

        Platform.runLater(
                () -> {
                    double rainYAxisWidth = rainChart.getYAxis().getWidth();
                    double tempYAxisWidth = tempChart.getYAxis().getWidth();

                    rainChart.setPadding(new Insets(0, tempYAxisWidth, 0, 0));
                    tempChart.setPadding(new Insets(0, 0, 0, rainYAxisWidth));
                }
        );
    }
}
