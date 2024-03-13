package Table_Formation;

import tech.tablesaw.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Summary_Table {

public Table formation(Table data)
    {
        Table formation = data.structure();
        int row_count= data.rowCount();
        List<String> columnames =  data.columnNames();
        StringColumn nan_count = StringColumn.create("Missing Count");
        StringColumn boo = StringColumn.create("IsMissing");
        StringColumn mean = StringColumn.create("Mean");
        StringColumn median = StringColumn.create("Median (Q2)");
        StringColumn mode_column = StringColumn.create("Mode");
        StringColumn unique = StringColumn.create("Unique");
        StringColumn range_column = StringColumn.create("Range");
        StringColumn minimum = StringColumn.create("Minimum");
        StringColumn maximum = StringColumn.create("Maximum");
        StringColumn quartile1 = StringColumn.create("Quartile 1 (Q1)");
        StringColumn quartiele3 = StringColumn.create("Quartile 3 (Q3)");
        StringColumn stdeviation = StringColumn.create("Std. Deviation");
        StringColumn variance = StringColumn.create("Variance");



        NumericColumn<?>[] numericColumns = data.numericColumns().toArray(new NumericColumn[0]);
        for(int i=0;i< columnames.size();i++)
        {
            // missing count
            int n = data.column(columnames.get(i)).countMissing();
            nan_count.append(n +"/"+row_count);

            //ismissing calculation
            if(n>0)
            {
                boo.append("True");
            }
            else {
                boo.append("False");
            }

            unique.append(data.column(columnames.get(i)).countUnique() +"/"+row_count);
            mean.append("Type Mismatch");
            median.append("Type Mismatch");
            mode_column.append("Type Mismatch");
            range_column.append("Type Mismatch");
            minimum.append("Type Mismatch");
            maximum.append("Type Mismatch");
            quartile1.append("Type Mismatch");
            quartiele3.append("Type Mismatch");
            stdeviation.append("Type Mismatch");
            variance.append("Type Mismatch");
        }
        
        for(NumericColumn<?> column : numericColumns) {
            //mean
            mean.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.mean()));

            //median
            median.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.median()));

            //min
            range_column.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.range()));

            //max
            minimum.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.min()));

            //range
            maximum.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.max()));

            //Q1
            quartile1.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.quartile1()));

            //Q3
            quartiele3.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.quartile3()));

            //standard devi
            stdeviation.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.standardDeviation()));

            //variance
            variance.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", column.variance()));

            //mode
            Map<Object, Integer> valueCounts = new HashMap<>();
            int maxCount = 0;
            if (column.type().toString().equals("INTEGER")) {
                int mode1 = 0;
                for (Object value : column) {
                    int a = (int) value;
                    if (a != -2147483648) {
                        int count = valueCounts.getOrDefault(value, 0) + 1;
                        valueCounts.put(value, count);

                        if (count > maxCount) {
                            maxCount = count;
                            mode1 = (int) value;
                        }
                    }

                }
                mode_column.set(formation.column("Column Name").indexOf(column.name()), String.valueOf(mode1));

            }
            else {
                double mode2 = 0;
                for (Object value : column) {
                    mode2 = 0;
                    double a = (double) value;
                    if (a != -2147483648) {
                        int count = valueCounts.getOrDefault(value, 0) + 1;
                        valueCounts.put(value, count);

                        if (count > maxCount) {
                            maxCount = count;
                            mode2 = (double) value;
                        }
                    }

                }
                mode_column.set(formation.column("Column Name").indexOf(column.name()), String.format("%.3f", mode2));
            }


        }
        formation.addColumns(boo,unique,nan_count,mean,median,mode_column,minimum,maximum,range_column,quartile1,quartiele3,stdeviation,variance);

        return formation;
    }


    public static void main(String[] args) {
        Table data = Table.read().csv("/home/frostknight/Cluster -  Chinese Prefecture copy.csv");
        Summary_Table s = new Summary_Table();
        s.formation(data);
    }


}
