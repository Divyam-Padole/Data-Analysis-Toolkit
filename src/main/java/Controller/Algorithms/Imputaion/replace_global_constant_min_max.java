package Controller.Algorithms.Imputaion;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.List;

public class replace_global_constant_min_max {

    private List<Object> mini,maxi;

    private NumericColumn<?>[]  numericColumns;
    public Table replace_minimum(Table data)
    {
        mini = new ArrayList<>();
        numericColumns = data.numericColumns().toArray(new NumericColumn[0]);
        for (NumericColumn<?> column : numericColumns) {
            assert false;
            if(column.type().toString().equals("DOUBLE"))
            {
                DoubleColumn temp = (DoubleColumn) data.column(column.name());
                mini.add(column.min());
                Column<Double> temp_col = temp.setMissingTo(column.min());
                data.replaceColumn(column.name(),temp_col);
            }
            if(column.type().toString().equals("INTEGER"))
            {
                IntColumn temp = (IntColumn) data.column(column.name());
                mini.add(column.min());
                Column<Integer> temp_col = temp.setMissingTo((int) column.min());
                data.replaceColumn(column.name(),temp_col);
            }

        }
        return data;
    }

    public Table replace_maximum(Table data)
    {
        maxi = new ArrayList<>();
        numericColumns = data.numericColumns().toArray(new NumericColumn[0]);
        for (NumericColumn<?> column : numericColumns) {
            assert false;
            if(column.type().toString().equals("DOUBLE"))
            {
                DoubleColumn temp = (DoubleColumn) data.column(column.name());
                maxi.add(column.max());
                Column<Double> temp_col = temp.setMissingTo(column.max());
                data.replaceColumn(column.name(),temp_col);
            }
            if(column.type().toString().equals("INTEGER"))
            {
                IntColumn temp = (IntColumn) data.column(column.name());
                maxi.add(column.max());
                Column<Integer> temp_col = temp.setMissingTo((int) column.max());
                data.replaceColumn(column.name(),temp_col);
            }

        }

        return data;
    }

    public List<Object> get_mini()
    {
        return mini;
    }

    public List<Object> get_max()
    {
        return maxi;
    }

    public NumericColumn<?>[] get_numeric_column()
    {
        return numericColumns;
    }



}
