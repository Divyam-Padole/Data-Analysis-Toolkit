package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class Dialog_ScatterController {

    @FXML
    private Button buttonOK;

    @FXML
    private ChoiceBox<String> column;

    @FXML
    private ChoiceBox<String> column1;

    String column_name,column_name1;


    public  void initialize() {
        Descriptor obj = new Descriptor();
        Table table = obj.get_Table();
        String[] columnNames = table.columnNames().toArray(new String[0]);
        column.getItems().addAll(columnNames);
        column1.getItems().addAll(columnNames);

    }

    @FXML
    void Submit(MouseEvent event) {
        column_name = column.getValue();
        column_name1=column1.getValue();
        //close the dialog
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();

    }

    public String getColumn() {
        return column_name;
    }

    public String getColumn1() {
        return column_name1;
    }


}
