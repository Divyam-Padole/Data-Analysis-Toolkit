package Controller.chart_operations;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

public class Scatter_Outlier {

    private Table data1;

    private String column_name;

    private XYChart.Series<Number,Number> series1;
    private XYChart.Series<Number,Number> series2;

    private XYChart.Series<Number,Number> series3;

    public void setData(Table data) {
        this.data1 = data;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public void logic() {
        Table data = Table.create("data").addColumns(data1.column(column_name));
        data=data.dropRowsWithMissingValues();

        NumericColumn<?> column = (NumericColumn<?>) data.column(column_name);

        double q1 = column.quartile1();
        double q3 = column.quartile3();
        double iqr = q3 - q1;
        double lower_bound = q1 - 1.5 * iqr;
        double upper_bound = q3 + 1.5 * iqr;
        double q2 = column.median();

        series1 = new XYChart.Series<>();
        series2 = new XYChart.Series<>();
        //new series for quartiles
         series3 = new XYChart.Series<>();
        series3.getData().add(new XYChart.Data<>(lower_bound, lower_bound));
        series3.getData().add(new XYChart.Data<>(q1, q1));
        series3.getData().add(new XYChart.Data<>(q2, q2));
        series3.getData().add(new XYChart.Data<>(q3, q3));
        series3.getData().add(new XYChart.Data<>(upper_bound, upper_bound));

        column= (NumericColumn<?>) column.removeMissing();
        if(column.type().toString().equals("INTEGER"))
        {
            for (int i = 0; i < column.size(); i++) {
                if (column.get(i) .intValue()< lower_bound || column.get(i).intValue() > upper_bound) {
                    series1.getData().add(new XYChart.Data<>(column.get(i).intValue(), column.get(i).intValue()));
                }
                else
                {

                    series2.getData().add(new XYChart.Data<>(column.get(i).intValue(),column.get(i).intValue()));
                }
            }
        }
        else
        {
            for (int i = 0; i < column.size(); i++) {
                if (column.get(i).doubleValue() < lower_bound || column.get(i).doubleValue() > upper_bound) {
                    series1.getData().add(new XYChart.Data<>(column.get(i).doubleValue(), column.get(i).doubleValue()));
                } else {
                    series2.getData().add(new XYChart.Data<>(column.get(i).doubleValue(), column.get(i).doubleValue()));
                }
            }
        }

    }
    public XYChart.Series<Number,Number> getSeries1() {
        return series1;
    }

    public   XYChart.Series<Number,Number> getSeries2() {
        return series2;
    }

    public XYChart.Series<Number,Number> getSeries3() {
        return series3;
    }
}

