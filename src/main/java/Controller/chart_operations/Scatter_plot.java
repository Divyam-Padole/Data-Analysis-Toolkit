package Controller.chart_operations;

import javafx.scene.chart.XYChart;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class Scatter_plot {
    private Table data1;

    private String column_name1;

    private String column_name2;

    private XYChart.Series<Object,Object> series1;

    public void setColumn_name1(String column_name1) {
        this.column_name1 = column_name1;
    }

    public void setColumn_name2(String column_name2) {
        this.column_name2 = column_name2;
    }
    public void setData(Table data) {
        this.data1 = data;
    }

    public void logic() {
        //create a table with two columns column_name1 and column_name2
        Table data = Table.create("data").addColumns(data1.column(column_name1), data1.column(column_name2));
        data=data.dropRowsWithMissingValues();
        series1 = new XYChart.Series<>();
        for (int i = 0; i < data.rowCount(); i++) {
            series1.getData().add(new XYChart.Data<>(data.column(column_name1).get(i), data.column(column_name2).get(i)));
        }
    }
    public XYChart.Series<Object,Object> getSeries1() {
        return series1;
    }



}
