package Controller.Algorithms.Outlier;

import tech.tablesaw.api.*;
import java.util.HashSet;
import java.util.Set;

public class outlierDetectionUsingIQR {
    public Table iqr(Table table) {
        //Importing data
        int totalRows=table.rowCount();
        //Storing continuous variable name
        NumericColumn<?>[] numericColumns =  table.numericColumns().toArray(new NumericColumn[0]);
        Set<Integer> rowsToBeRemoved = new HashSet<>();

        //Iterating over continuous variables
        for(NumericColumn<?> column : numericColumns)
        {
            double q1 = column.quartile1();
            double q3 = column.quartile3();
            double iqr = q3 - q1;
            double lowerBound = q1 - (1.5 * iqr);
            double upperBound = q3 + (1.5 * iqr);
            //storing rows to be removed


            //iterating over rows to check for outliers
            if(column.type().toString().equals("INTEGER") && !column.name().equals("Index") ) {
                for (int i = 0; i < totalRows; i++) {
                    if(!column.isMissing(i))
                    {
                        if ((column.get(i).intValue() < lowerBound || column.get(i).intValue() > upperBound)) {
                            rowsToBeRemoved.add(i);
                        }
                    }
                }
            }
            else {
                for (int i = 0; i < totalRows; i++) {
                    if(!column.isMissing(i) && !column.name().equals("Index"))
                        if ((column.get(i).doubleValue() < lowerBound || column.get(i).doubleValue() > upperBound)) {
                            rowsToBeRemoved.add(i);
                        }
                    }
                }
            }


        Table table1 = table.copy();
        //Removing all rows from table1
        table1.clear();

        for (int i = 0; i < totalRows; i++) {
            if (!rowsToBeRemoved.contains(i)) {
                //copy row to new table
                    table1 = table1.append(table.row(i));
            }
        }
        return table1;
    }
}
