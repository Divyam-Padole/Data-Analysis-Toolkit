package Controller.Algorithms;

import java.util.Objects;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class findingCorrelationMatrix {
    public static int getContinuousVariableCount(Table table) {
        //Creating variables
        Table structureOfTable=table.structure();
        int contVariCount=structureOfTable.stringColumn(2).isEqualTo("INTEGER").size();
        contVariCount+=structureOfTable.stringColumn(2).isEqualTo("DOUBLE").size();
        return contVariCount;
    }
    public static String[] getContinuousVariableNames(Table table) {
        //Creating variables
        Table structureOfTable=table.structure();
        int contVariCount=getContinuousVariableCount(table);

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
        return contVariNames;
    }
    public static double[][] getCorrelationMatrix(Table table) {
        //Creating variables
        int variRows=0;
        Table structureOfTable=table.structure();
        int totalRows=table.rowCount();

        //Storing attributes
        String[] attr=table.columnNames().toArray(new String[0]);
        int contVariCount=getContinuousVariableCount(table);

        //Storing continuous variable name
        String[] contVariNames=getContinuousVariableNames(table);

        //Creating array to store stats
        double[] mean=new double[contVariCount];
        double[] vari=new double[contVariCount];
        double[] stdDevi=new double[contVariCount];
        double[] sumiOfSqurOfDeviFromMean=new double[contVariCount];
        double[][] deviFromMean=new double[contVariCount][table.rowCount()];
        double[][] corrMatr=new double[contVariCount][contVariCount];

        //Calculating mean and deviation from mean
        for(int i=0;i<contVariCount;i++) {
            variRows=0;mean[i]=0;
            for(int j=0;j<totalRows;j++) {
                if(!table.column(contVariNames[i]).isMissing(j)) {
                    Column<?> desiVari=table.column(contVariNames[i]);
                    String desiType=desiVari.type().name();
                    if(desiType.equals("INTEGER")) {
                        mean[i]+=table.intColumn(contVariNames[i]).getInt(j);
                    }
                    if(desiType.equals("DOUBLE")) {
                        mean[i]+=table.doubleColumn(contVariNames[i]).getDouble(j);
                    }
                    variRows+=1;
                }
            }
            mean[i]/=variRows;
            for(int j=0;j<table.rowCount();j++) {
                double temp=0;
                if(!table.column(contVariNames[i]).isMissing(j)) {
                    Column<?> desiVari=table.column(contVariNames[i]);
                    String desiType=desiVari.type().name();
                    if(desiType.equals("INTEGER")) {
                        temp=table.intColumn(contVariNames[i]).getInt(j);
                    }
                    if(desiType.equals("DOUBLE")) {
                        temp=table.doubleColumn(contVariNames[i]).getDouble(j);
                    }
                    deviFromMean[i][j]=mean[i]-temp;
                    vari[i]+=Math.pow(deviFromMean[i][j],2);
                }
            }
            sumiOfSqurOfDeviFromMean[i]=vari[i];
            vari[i]/=(contVariCount-1);
            stdDevi[i]=Math.sqrt(vari[i]);
        }

        //Calculating cross product of deviation
        for(int i=0;i<contVariCount;i++) {
            for(int j=0;j<contVariCount;j++) {
                for(int k=0;k<table.rowCount();k++) {
                    corrMatr[i][j]+=deviFromMean[i][k]*deviFromMean[j][k];
                }
            }
        }

        //Calculating and displaying correlation matrix
        for(int i=0;i<contVariCount;i++) {
            for(int j=0;j<contVariCount;j++) {
                corrMatr[i][j]/=Math.sqrt(sumiOfSqurOfDeviFromMean[i]*sumiOfSqurOfDeviFromMean[j]);
            }
        }
        return corrMatr;
    }
    public static void printCorrelationMatrix(double[][] corrMatr) {
        for(int i=0;i<corrMatr.length;i++) {
            for(int j=0;j<corrMatr.length;j++) {
                System.out.print(String.format("%.2f    ",corrMatr[i][j]));
            }
            System.out.println();
        }
    }
    public static void main(String args[]) {
        //Creating variables
        int variRows=0;
        long startTime01=System.currentTimeMillis();

        //Importing data
        Table table=Table.read().csv("/home/tendopain/IdeaProjects/Mini_Project/Datasets/pokemonStatsData.csv");
        Table structureOfTable=table.structure();
        Table summaryOfTable=table.summary();
        int totalRows=table.rowCount();

        //Displaying content and structure
        /*System.out.println(table);
        System.out.println();
        System.out.println(structureOfTable);
        System.out.println();
        System.out.println(summaryOfTable);
        System.out.println();*/

        //Calculating correlation matrix
        double[][] corrMatr=getCorrelationMatrix(table);

        //Displaying correlation matrix
        printCorrelationMatrix(corrMatr);

        long endTime01=System.currentTimeMillis();
        long executionTime01=endTime01-startTime01;
        System.out.println("Execution time: "+executionTime01+" milliseconds");
    }
}