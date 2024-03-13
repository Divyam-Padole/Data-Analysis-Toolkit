package Controller.Algorithms.Transformation;

import Controller.frequencyencoding_dialog;
import Controller.onehotencoding_dialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class Transformation {

    private List<StringColumn> categoricalColumns;
    private int threshold = 10;


    // User input for opertion
    // this need to fianly change to the gui as filed or drop down
    // the below funtion return arraylist

    private List<String> getUserInputForColumns() {
        List<String> columnsToEncode = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter column names (comma-separated) to apply encoding:");
        String input = scanner.nextLine();
        String[] inputColumns = input.split(",");

        for (String column : inputColumns) {
            columnsToEncode.add(column.trim());
        }

        return columnsToEncode;
    }


    public Table labelEncode(Table data) {
        categoricalColumns = new ArrayList<>();

        for (StringColumn column : data.stringColumns()) {
            if (column.unique().size() <= threshold) {
                categoricalColumns.add(column);
            }
        }

        for (StringColumn column : categoricalColumns) {
            if (!column.name().equals("target_column")) {
                Map<String, Integer> labelMap = new HashMap<>();
                int labelCounter = 0;

                for (String category : column.unique()) {
                    labelMap.put(category, labelCounter++);
                }

                String newColumnName = column.name();
                StringColumn encodedColumn = StringColumn.create(newColumnName);

                for (int i = 0; i < data.rowCount(); i++) {
                    encodedColumn.append(String.valueOf(labelMap.get(column.getString(i))));
                }

                data.removeColumns(column.name());
                data.addColumns(encodedColumn);
            }
        }

        return data;
    }


    // one hot encoding


    public Table oneHotEncode(Table data) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("oneHotEncoding.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("One Hot Encoding Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        onehotencoding_dialog onehotencoding_dialog = fxmlLoader.getController();


        categoricalColumns = new ArrayList<>();

        // Get user input for columns to one-hot encode
        List<String> columnsToEncode = onehotencoding_dialog.getSelectedColumns();

        // Iterate through the specified columns
        for (String columnName : columnsToEncode) {
            StringColumn column = data.stringColumn(columnName);

            // Skip encoding for the "target_column"
            if (!column.name().equals("target_column")) {
                List<Column<?>> oneHotEncodedColumns = new ArrayList<>();

                for (String category : column.unique()) {
                    String newColumnName = column.name() + "_" + category;
                    StringColumn oneHotEncodedColumn = StringColumn.create(newColumnName);

                    for (int i = 0; i < data.rowCount(); i++) {
                        if (column.getString(i).equals(category)) {
                            oneHotEncodedColumn.append("1");
                        } else {
                            oneHotEncodedColumn.append("0");
                        }
                    }

                    oneHotEncodedColumns.add(oneHotEncodedColumn);
                }

                data.removeColumns(column.name());
                for (Column<?> oneHotEncodedColumn : oneHotEncodedColumns) {
                    data.addColumns(oneHotEncodedColumn);
                }
            }
        }

        return data;
    }

    // user input for the oneHotEncoding


    /// Frequncy encoding

    public Table frequency_encoding(Table data) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frequencyEncoding.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Frequency Encoding Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        frequencyencoding_dialog frequencyencoding = fxmlLoader.getController();


        categoricalColumns = new ArrayList<>();

        // Get user input for columns to frequency encode
        List<String> columnsToEncode = frequencyencoding.getSelectedColumns();

        for (StringColumn column : data.stringColumns()) {
            if (columnsToEncode.contains(column.name())) {
                categoricalColumns.add(column);
            }
        }

        for (StringColumn column : categoricalColumns) {
            if (!column.name().equals("target_column")) {
                Map<String, Integer> labelMap = new HashMap<>();
                int labelCounter = 0;

                for (String category : column.unique()) {
                    //counting the frequency of each category
                    int  col_count = column.count(Predicate.isEqual(category));
                    //if  frequency is same then increase the  value by 1  till the frequency is not unique in labelMap valuse
                    while (labelMap.containsValue(col_count)){
                        col_count++;
                    }
                    labelMap.put(category, col_count);
                }

                String newColumnName = column.name();
                StringColumn encodedColumn = StringColumn.create(newColumnName);

                for (int i = 0; i < data.rowCount(); i++) {
                    encodedColumn.append(String.valueOf(labelMap.get(column.getString(i))));
                }

                data.removeColumns(column.name());
                data.addColumns(encodedColumn);
            }
        }

        return data;
    }

    // ordinal encoding
    public Table ordinal_encode(Table data) {
        categoricalColumns = new ArrayList<>();

        for (StringColumn column : data.stringColumns()) {
            if (column.unique().size() <= threshold) {
                categoricalColumns.add(column);
            }
        }

        for (StringColumn column : categoricalColumns) {
            if (!column.name().equals("target_column")) {
                Map<String, Integer> labelMap = new HashMap<>();
                int labelCounter = 0;

                Set<String> uniqueValues = new LinkedHashSet<>(); // Maintain order of appearance

                // Iterate through the column to maintain the order
                for (String value : column) {
                    uniqueValues.add(value);
                }

                for (String category : uniqueValues) {
                    labelMap.put(category, labelCounter++);
                }

                String newColumnName = column.name() + "_encoded";
                StringColumn encodedColumn = StringColumn.create(newColumnName);

                for (int i = 0; i < data.rowCount(); i++) {
                    encodedColumn.append(String.valueOf(labelMap.get(column.getString(i))));
                }

                data.removeColumns(column.name());
                data.addColumns(encodedColumn);
            }
        }

        return data;
    }



}
