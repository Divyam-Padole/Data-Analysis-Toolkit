package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

import java.util.Arrays;

public class Dialog_OutlierController {

    @FXML
    private Button buttonOK;

    @FXML
    private ChoiceBox<String> column;

    String column_name;

    public void initialize() {
        Descriptor obj = new Descriptor();
        Table table = obj.get_Table();
        NumericColumn<?>[] numericColumns = table.numericColumns().toArray(new NumericColumn[0]);
        //string array to store the column names
        String[] columnNames = new String[numericColumns.length];
        for (int i = 0; i < numericColumns.length; i++) {
            columnNames[i] = numericColumns[i].name();
        }
        column.getItems().addAll(columnNames);
    }

    @FXML
    void Submit(MouseEvent event) {
        column_name = column.getValue();
        //close the dialog
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();

    }
    public String getColumn() {
        return column_name;
    }

}
