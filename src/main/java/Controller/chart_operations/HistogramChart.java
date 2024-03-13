package Controller.chart_operations;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class HistogramChart {

    private Table data1,data;
    private int  binSize;

    private String column_name;

    private List<?> uniqueValues;

    private  int [] frequency ;

    private double mina;

    double binWidth ;

    public void setBinSize(int binSize) {
        this.binSize = binSize;
    }

    public void setData(Table data) {
        this.data1 = data;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public int[] getFrequency() {
        return frequency;
    }

    public void logic() {
        data1= data1.dropDuplicateRows();
        data = Table.create("data").addColumns(data1.column(column_name));
        data=data.dropRowsWithMissingValues();

//        System.out.println(data.rowCount());
//        System.out.println(data.first(5));
//        Column<?> [] column = data.columns().toArray(new Column<?>[0]);
        Column<?> column = data.column(column_name);
//        for (Column<?> column : columns) {
//            System.out.println(column.summary());
            if (column.type().toString().equals("STRING")) {
                uniqueValues = column.unique().asList();
//                System.out.println("Unique Values: ");
//                for (Object uniqueValue : uniqueValues) {
//                    System.out.println(uniqueValue);
//                }
//                System.out.println("Frequency: ");
                frequency = new int[uniqueValues.size()];
                for (int i = 0; i < frequency.length; i++) {
                    frequency[i] = 0;
                }
                for (int i = 0; i < column.size(); i++) {
                    if(!column.isMissing(i)){
                        for (int j = 0; j < uniqueValues.size(); j++) {
                            if (column.get(i).equals(uniqueValues.get(j))) {
                                frequency[j]++;
                            }
                        }
                    }
                }
//                for (int k : frequency) {
//                    System.out.println(k);
//                }
//                System.out.println("Histogram: ");
//                for (int i = 0; i < frequency.length; i++) {
//                    System.out.print(uniqueValues.get(i) + " | ");
//                    for (int j = 0; j < frequency[i]; j++) {
//                        System.out.print("*");
//                    }
//                    System.out.println();
//                }

            }
            else{
                NumericColumn<?> numericColumn = (NumericColumn<?>) column;
                 mina = numericColumn.min();
//                System.out.println("Min: " + mina);
                double maxa = numericColumn.max();
                double range = maxa - mina;
                binWidth = range / binSize;
//                System.out.println("Bin Width: " + binWidth);
//                System.out.println(column.name());
                double [] bins = new double[binSize];

                for (int i = 0; i < binSize; i++) {
                    bins[i] = mina + binWidth * i;
                }
                frequency = new int[binSize];
                for (int i = 0; i < frequency.length; i++) {
                    frequency[i] = 0;
                }
                for (int i = 0; i < numericColumn.size(); i++) {
                    if(!numericColumn.isMissing(i)) {
                        for (int j = 0; j < bins.length; j++) {

                            if (numericColumn.type().toString().equals("DOUBLE")) {
                                if ((double) numericColumn.get(i) >= bins[j] && (double) numericColumn.get(i) <= bins[j] + binWidth && j == bins.length - 1) {
                                    frequency[j]++;
                                } else if ((double) numericColumn.get(i) >= bins[j] && (double) numericColumn.get(i) < bins[j] + binWidth) {
                                    frequency[j]++;
                                }

                            } else {
                                if ((int) numericColumn.get(i) >= bins[j] && (int) numericColumn.get(i) <= bins[j] + binWidth && j == bins.length - 1) {
                                    frequency[j]++;
                                } else if ((int) numericColumn.get(i) >= bins[j] && (int) numericColumn.get(i) < bins[j] + binWidth) {
                                    frequency[j]++;
                                }
                            }

                        }
                    }
                }

//                for (int k : frequency) {
//                    System.out.println(k);
//                }
                //to returns bins and frequencies of each bins
            }

    }


    public XYChart.Series<String,Number>create_series()
    {
        DecimalFormat df_obj = new DecimalFormat("#.##");
        XYChart.Series <String,Number>series1 = new XYChart.Series<>();
        if(data.column(column_name).type().toString().equals("STRING")) {
            for (int i = 0; i < uniqueValues.size(); i++) {
                series1.getData().add(new XYChart.Data<>(uniqueValues.get(i).toString(), frequency[i]));
            }
        }
        else{
            for (int i = 0; i < binSize; i++) {
                series1.getData().add(new XYChart.Data<>(df_obj.format(mina+binWidth*i) + "-"+df_obj.format(mina+binWidth*(i+1)), frequency[i]));
            }
        }

        return series1;
    }

}
