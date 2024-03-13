package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.List;

public class frequencyencoding_dialog {

    @FXML
    private Button buttonOK;

    @FXML
    private ListView<CheckBox> column;

    private List<String> selectedColumns;


    @FXML
    void Submit(MouseEvent event) {

        // getting the selected columns in List<String>
        selectedColumns = new java.util.ArrayList<>();
        for (CheckBox cb : column.getItems()) {
            if (cb.isSelected()) {
                selectedColumns.add(cb.getText());
            }
        }
        //close the dialog
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();

    }

    public  void initialize() {
        // adding checkbox in the choice box

        Table table = Descriptor.get_Table_with_index();
        // getting names of  all columns
        String[] columnNames = table.columnNames().toArray(new String[0]);
        //removing Numeric columns from the columnNames
        for (int i = 0; i < columnNames.length; i++) {
            if (table.column(columnNames[i]).type().toString().equals("INTEGER") || table.column(columnNames[i]).type().toString().equals("DOUBLE")) {
                columnNames[i] = null;
            }
        }
        String[] cat_col = Arrays.stream(columnNames).filter(s -> (s != null && !s.isEmpty())).toArray(String[]::new);
        ObservableList<CheckBox> items = FXCollections.observableArrayList();
        for (String s : cat_col) {
            items.add(new CheckBox(s));
        }
        column.setItems(items);
        column.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

}