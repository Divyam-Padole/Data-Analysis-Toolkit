package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tech.tablesaw.api.Table;

public class Dialog_HistogramController {

    @FXML
    private Button buttonOK;

    @FXML
    private ChoiceBox<String> column;

    @FXML
    private TextField interval;

    int  interval_size;
    String column_name;

    Table table;


    public void initialize() {
        Descriptor obj = new Descriptor();
        table = obj.get_Table();
        String[] columnNames = table.columnNames().toArray(new String[0]);
        column.getItems().addAll(columnNames);

        //action after selecting the column to disable the interval textfield if the column is of type string


    }

    @FXML
    void  Submit(MouseEvent event) {
        if(!interval.isDisable()) {
            try {
                interval_size = Integer.parseInt(this.interval.getText());
            } catch (NumberFormatException e) {
                interval_size = 5;
            }
        }
        column_name = column.getValue();
        //close the dialog
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();
    }


    @FXML
    void called(MouseEvent event1) {
        column.setOnAction(event -> {
            column_name = column.getValue();
            interval.setDisable(table.column(column_name).type().toString().equals("STRING"));
        });
    }

    public int getInterval() {
        return interval_size;
    }

    public String getColumn() {
        return column_name;
    }



}
