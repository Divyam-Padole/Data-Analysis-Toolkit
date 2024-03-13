package Table_Formation;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.util.Objects;

public class ObservableTablesawRow {

    private final ObservableMap<String, SimpleStringProperty> values = FXCollections.observableHashMap();
    private final ObservableMap<String, SimpleDoubleProperty> double_values = FXCollections.observableHashMap();

    private final ObservableMap<String, SimpleIntegerProperty> integer_values = FXCollections.observableHashMap();

    private final ObservableMap<String, SimpleBooleanProperty> bool_values = FXCollections.observableHashMap();




    public ObservableTablesawRow(Table table, int rowIndex) {
        Row row = table.row(rowIndex);
        table.columnNames().forEach(columnName -> {
            if (Objects.equals(table.column(columnName).type().toString(), "STRING")){
                String value = row.getString(columnName);
                SimpleStringProperty property = new SimpleStringProperty(value);
                values.put(columnName, property);
            }
            else if (Objects.equals(table.column(columnName).type().toString(), "DOUBLE")){
                double value = row.getDouble(columnName);
                SimpleDoubleProperty property = new SimpleDoubleProperty(value);
                double_values.put(columnName,property);
            }
            else if (Objects.equals(table.column(columnName).type().toString(), "INTEGER")){
                int value = row.getInt(columnName);
                SimpleIntegerProperty property = new SimpleIntegerProperty(value);
                integer_values.put(columnName, property);
            }
            else if (Objects.equals(table.column(columnName).type().toString(), "BOOLEAN")){
                boolean value = row.getBoolean(columnName);
                SimpleBooleanProperty property = new SimpleBooleanProperty(value);
                bool_values.put(columnName, property);

            }

        });
    }

    public ObservableValue<String> get(String columnName) {

        if(values.containsKey(columnName)) {
            return values.get(columnName);
        }else if (double_values.containsKey(columnName)) {
            return double_values.get(columnName).asString();
        } else if (integer_values.containsKey(columnName)) {
            return integer_values.get(columnName).asString();
        } else if (bool_values.containsKey(columnName)) {
            return bool_values.get(columnName).asString();
        }
        return null;
    }

}
