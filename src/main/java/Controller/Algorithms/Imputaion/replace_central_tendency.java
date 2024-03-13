package Controller.Algorithms.Imputaion;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class replace_central_tendency {
    private List<Double> median,meana;
    private List<String> colu;
    private List<Object> modea;
    private NumericColumn<?>[] numericColumns;
    public Table replace_mean(Table data)
    {
        meana = new ArrayList<>();
        numericColumns = data.numericColumns().toArray(new NumericColumn[0]);
        for (NumericColumn<?> column : numericColumns) {
            assert false;
            if(column.type().toString().equals("DOUBLE"))
            {
                DoubleColumn temp = (DoubleColumn) data.column(column.name());
                meana.add(column.mean());
                Column<Double> temp_col = temp.setMissingTo(Double.valueOf(String.format("%.40f",column.mean())));
                data.replaceColumn(column.name(),temp_col);
            }
            if(column.type().toString().equals("INTEGER"))
            {
                IntColumn temp = (IntColumn) data.column(column.name());
                meana.add(column.mean());
                Column<Integer> temp_col = temp.setMissingTo((int) column.mean());
                data.replaceColumn(column.name(),temp_col);
            }

        }
        return data;
    }
    public Table replace_median(Table data)
    {
        median = new ArrayList<>();
        numericColumns = data.numericColumns().toArray(new NumericColumn[0]);
        for (NumericColumn<?> column : numericColumns) {
            assert false;
            if(column.type().toString().equals("DOUBLE"))
            {
                DoubleColumn temp = (DoubleColumn) data.column(column.name());
                median.add(column.median());
                Column<Double> temp_col = temp.setMissingTo(Double.valueOf(String.format("%.40f", column.median())));
                data.replaceColumn(column.name(),temp_col);
            }
            if(column.type().toString().equals("INTEGER"))
            {
                IntColumn temp = (IntColumn) data.column(column.name());
                median.add(column.median());
                Column<Integer> temp_col = temp.setMissingTo((int) column.median());
                data.replaceColumn(column.name(),temp_col);
            }

        }
        return data;
    }

    public Table replace_mode(Table table)
    {

        modea = new ArrayList<>();
        colu = table.columnNames();
        for (Column<?> column : table.columns()) {

            if (column.type().toString().equals("DOUBLE")) {
                Map<Double, Integer> valueCounts = new HashMap<>();
                double mode = Double.NaN;
                int maxCount = 0;

                for (Object value : column) {
                    if (!((Double) value).isNaN()) {
                        int count = valueCounts.getOrDefault(value, 0) + 1;
                        valueCounts.put((Double) value, count);

                        if (count > maxCount) {
                            maxCount = count;
                            mode = (double) value;
                        }
                    }
                }
                modea.add(mode);
                DoubleColumn temp = (DoubleColumn) table.column(column.name());
                temp.setMissingTo(Double.valueOf(String.format("%.40f", mode)));
            }

            //integer

            if (column.type().toString().equals("INTEGER")) {
                Map<Integer, Integer> valueCounts = new HashMap<>();
                int mode = 0;
                int maxCount = 0;

                for (Object value : column) {
                    if ((Integer) value != -2147483648) {
                        int count = valueCounts.getOrDefault(value, 0) + 1;
                        valueCounts.put((Integer) value, count);

                        if (count > maxCount) {
                            maxCount = count;
                            mode = (int) value;
                        }
                    }
                }
                modea.add(mode);
                IntColumn temp = (IntColumn)table.column(column.name());
                temp.setMissingTo(mode);
//                data.replaceColumn(column.name(),temp_col);

            }


            if (column.type().toString().equals("STRING")) {
                Map<String, Integer> valueCounts = new HashMap<>();
                String mode = null;
                int maxCount = 0;
                column = column.removeMissing();

                for (Object value : column) {
                    if (value != null) {
                        int count = valueCounts.getOrDefault(value, 0) + 1;
                        valueCounts.put((String) value, count);

                        if (count > maxCount) {
                            maxCount = count;
                            mode = (String) value;
                        }
                    }
                }
                modea.add(mode);
                StringColumn temp = (StringColumn) table.column(column.name());
                temp.setMissingTo(mode);
            }
        }
        return table;

    }

    public  List<Double> get_median()
    {
        return median;
    }
    public  List<Double> get_mean()
    {
        return meana;
    }

    public List<Object>  get_mode(){
        return modea;
    }

    public NumericColumn<?>[] get_numeric_column()
    {
        return numericColumns;
    }

    public List<String> get_columns()
    {
        return colu;
    }
}
