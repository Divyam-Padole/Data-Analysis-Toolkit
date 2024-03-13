package Controller.Algorithms.Outlier;

import Controller.Descriptor;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.Arrays;
import java.util.Objects;

public class outlierDetectionUsingModifiedZscore {
    public  Table modified_zscore(Table table) {
        Table structureOfTable=Descriptor.get_Summary_Table();
        int totalRows=table.rowCount();


        //Storing attributes
        String[] attr=table.columnNames().toArray(new String[0]);
        int contVariCount=structureOfTable.stringColumn(2).isEqualTo("INTEGER").size();
        contVariCount+=structureOfTable.stringColumn(2).isEqualTo("DOUBLE").size();

        //Storing continuous variable name
        String[] contVariNames=new String[contVariCount];
        for(int i=0,j=0;i<table.columnCount() && j<contVariCount;i++) {
            if(Objects.equals(structureOfTable.column(2).getString(i),"INTEGER")) {
                contVariNames[j]=structureOfTable.column(1).getString(i);
                j+=1;
            }
            if(Objects.equals(structureOfTable.column(2).getString(i),"DOUBLE")) {
                contVariNames[j]=structureOfTable.column(1).getString(i);
                j+=1;
            }
        }


        //Iterating over continuous variables
        for(int i=0;i<contVariCount;i++) {
            int tempRows=0;
            double medi=0,madi=0;
            Column<?> desiVari=table.column(contVariNames[i]);
            String desiType=desiVari.type().name();
            if(desiType.equals("INTEGER")) {
                IntColumn ic=(IntColumn)table.column(contVariNames[i]);
                medi=((IntColumn)table.column(contVariNames[i])).median();
            } else {
                DoubleColumn ic=(DoubleColumn)table.column(contVariNames[i]);
                medi=((DoubleColumn)table.column(contVariNames[i])).median();
            }

            //Iterating over rows
            for(int j=0;j<totalRows;j++) {
                if(!table.column(contVariNames[i]).isMissing(j)) {
                    tempRows+=1;
                    madi+=((NumberColumn<?,?>)table.column(contVariNames[i])).getDouble(j);
                }
            }
            madi/=tempRows;

            //Iterating over rows
            for(int j=0;j<totalRows;j++) {
                if(!table.column(contVariNames[i]).isMissing(j)) {
                    double modZScore=0.6745;
                    if(desiType.equals("INTEGER")) {
                        modZScore*=(((NumberColumn<?,?>)table.column(contVariNames[i])).getDouble(j)-medi)/madi;
                    } else {
                        modZScore*=(((NumberColumn<?,?>)table.column(contVariNames[i])).getDouble(j)-medi)/madi;
                    }
                    if(modZScore<(-3) || modZScore>(3)) {
                        table=table.dropRows(j);
                        totalRows-=1;
                    }
                }
            }
        }
        return  table;
    }
}
