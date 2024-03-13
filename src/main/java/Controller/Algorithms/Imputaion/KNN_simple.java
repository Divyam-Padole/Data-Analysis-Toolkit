package Controller.Algorithms.Imputaion;
import tech.tablesaw.api.*;

public class KNN_simple {

    //// KNN imputation

    public Table KNN_imputation(Table data) {
        NumericColumn<?>[] numericColumns = data.numericColumns().toArray(new NumericColumn[0]);

        for (NumericColumn<?> column : numericColumns) {
            int k = 6;
            if (column.type().toString().equals("DOUBLE")) {
                DoubleColumn temp = (DoubleColumn) data.column(column.name());
                DoubleColumn temp_col = knnImputation(temp, k);
                data.replaceColumn(column.name(), temp_col);
            } else if (column.type().toString().equals("INTEGER")) {
                IntColumn temp = (IntColumn) data.column(column.name());
                IntColumn temp_col = knnImputation(temp, k);
                data.replaceColumn(column.name(), temp_col);
            }
        }

        return data;
    }

    // Helper methods for KNN imputation
    private DoubleColumn knnImputation(DoubleColumn column, int k) {
        DoubleColumn imputedColumn = column.copy();

        for (int i = 0; i < column.size(); i++) {
            if (column.isMissing(i)) {
                imputedColumn.set(i, knnMean(column, i, k));
            }
        }

        return imputedColumn;
    }

    private IntColumn knnImputation(IntColumn column, int k) {
        IntColumn imputedColumn = column.copy();

        for (int i = 0; i < column.size(); i++) {
            if (column.isMissing(i)) {
                imputedColumn.set(i, knnMeanForInt(column, i, k));
            }
        }

        return imputedColumn;
    }

    private double knnMean(DoubleColumn column, int index, int k) {
        double[] distances = new double[column.size()];
        for (int i = 0; i < column.size(); i++) {
            distances[i] = euclideanDistance(column.getDouble(index), column.getDouble(i));
        }

        int[] sortedIndices = sortIndices(distances);
        double sum = 0.0;
        int count = 0;

        for (int i = 0; i < k; i++) {
            int currentIndex = sortedIndices[i];
            if (!column.isMissing(currentIndex)) {
                sum += column.getDouble(currentIndex);
                count++;
            }
        }

        return count > 0 ? sum / count : 0.0;
    }

    private int knnMeanForInt(IntColumn column, int index, int k) {
        // Corrected to use double[] for distances
        double[] distances = new double[column.size()];
        for (int i = 0; i < column.size(); i++) {
            distances[i] = Math.abs(column.getDouble(index) - column.getDouble(i));
        }

        int[] sortedIndices = sortIndices(distances);
        int sum = 0;
        int count = 0;

        for (int i = 0; i < k; i++) {
            int currentIndex = sortedIndices[i];
            if (!column.isMissing(currentIndex)) {
                sum += column.getInt(currentIndex);
                count++;
            }
        }

        return count > 0 ? sum / count : 0;
    }

    private double euclideanDistance(double x, double y) {
        return Math.sqrt(Math.pow(x - y, 2));
    }

    private int[] sortIndices(double[] array) {
        int[] indices = new int[array.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        for (int i = 0; i < indices.length; i++) {
            for (int j = i + 1; j < indices.length; j++) {
                if (array[indices[j]] < array[indices[i]]) {
                    int temp = indices[i];
                    indices[i] = indices[j];
                    indices[j] = temp;
                }
            }
        }

        return indices;
    }
}
