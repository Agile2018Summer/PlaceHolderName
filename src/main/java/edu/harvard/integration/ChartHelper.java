package edu.harvard.integration;

import edu.harvard.integration.Trello.BacklogColumn;
import edu.harvard.integration.Trello.BacklogItem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChartHelper extends Application {
    private static Set<BacklogItem> items = new HashSet<>();
    private static List<Calendar> backlogDates = new ArrayList<>();

    public static void setData(Set<BacklogItem> bItems, List<Calendar> bDates) {
        items = bItems;
        backlogDates = bDates;
    }

    public static class ColumnInfo {
        private int remainingPoints;
        private String label;

        public ColumnInfo(@Nonnull String label, int remainingPoints) {
            this.label = label;
            this.remainingPoints = remainingPoints;
        }

        public int getRemainingPoints() {
            return this.remainingPoints;
        }

        public String getLabel() {
            return this.label;
        }
    }

    @Override
    public void start(Stage stage) {
        List<ColumnInfo> info = ChartHelper.parseItems(items, backlogDates);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        xAxis.setStartMargin(0);
        xAxis.setEndMargin(0);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Story Points");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Burndown Chart");
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
        setChartSettings(barChart);

        barChart.getData().add(getBarData(info));

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        setChartSettings(lineChart);
        lineChart.setCreateSymbols(false);

        lineChart.getData().add(getLineData(info, getTotalPoints(items)));

        StackPane root = new StackPane();
        root.getChildren().addAll(barChart, lineChart);
        new Scene(root, 1920, 1080);

        saveImage(root);
        Platform.exit();
    }

    private void setChartSettings(XYChart<String, Number> chart) {
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.getStylesheets().addAll(getClass().getResource("/chart.css").toExternalForm());
    }

    private void saveImage(Pane root) {
        WritableImage image = root.snapshot(null, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage imageRGB = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.OPAQUE);
        imageRGB.createGraphics().drawImage(bufferedImage, 0, 0, null);
        try {
            ImageIO.write(imageRGB, "png", new File("snapshot.png"));
        } catch (IOException ignored) {
        }
    }

    private XYChart.Series<String, Number> getBarData(List<ColumnInfo> values) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> data = series.getData();
        for (ColumnInfo info : values) {
            XYChart.Data<String, Number> d = new XYChart.Data<>(info.getLabel(), info.getRemainingPoints());
            data.add(d);
            d.nodeProperty().addListener((ov, oldNode, node) -> displayLabelForData(d));
        }
        return series;
    }

    private XYChart.Series<String, Number> getLineData(List<ColumnInfo> values, int maxValue) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> data = series.getData();
        double change = maxValue / (double) (values.size() - 1);
        for (int i = 0; i < values.size(); i++) {
            double y = maxValue - i * change;
            data.add(new XYChart.Data<>(values.get(i).getLabel(), y));
        }
        return series;
    }

    private void displayLabelForData(XYChart.Data<String, Number> data) {
        Node node = data.getNode();
        if (node == null) {
            return;
        }
        Text dataText = new Text(String.valueOf(data.getYValue()));
        node.parentProperty().addListener((ov, oldParent, parent) -> {
            Group parentGroup = (Group) parent;
            parentGroup.getChildren().add(dataText);
        });

        node.boundsInParentProperty().addListener((ov, oldBounds, bounds) -> {
            dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
            dataText.setLayoutY(Math.round(bounds.getMinY() - dataText.prefHeight(-1) * 0.5));
        });
    }

    public static List<ColumnInfo> parseItems(Set<BacklogItem> items, List<Calendar> backlogDates) {
        List<ColumnInfo> info = new ArrayList<>();
        Map<Calendar, Integer> data = new HashMap<>();

        for (BacklogItem item : items) {
            if (item.getColumn().equals(BacklogColumn.DONE)) {
                Calendar dateCompleted = item.getDateCompleted();
                if (dateCompleted == null) {
                    continue;
                }
                if (data.containsKey(dateCompleted)) {
                    data.put(dateCompleted, data.get(dateCompleted) + item.getStoryPoints());
                } else {
                    data.put(dateCompleted, item.getStoryPoints());
                }
            }
        }
        int curTotal = getTotalPoints(items);
        for (Calendar c : backlogDates) {
            int difference = data.getOrDefault(c, 0);
            curTotal -= difference;
            info.add(new ColumnInfo(Utils.formattedDate(c, true), curTotal));
        }
        return info;
    }

    public static int getTotalPoints(Set<BacklogItem> items) {
        int totalPoints = 0;
        for (BacklogItem item : items) {
            totalPoints += item.getStoryPoints();
        }
        return totalPoints;
    }
}