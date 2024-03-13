package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SaveImagesController {

    @FXML
    private Button button_ok;

    @FXML
    private Button directory;

    @FXML
    private CheckBox histogram_check;

    @FXML
    private TextField histogram_field;

    @FXML
    private CheckBox outlier_check;

    @FXML
    private TextField outlier_field;

    @FXML
    private CheckBox scatter_check;

    @FXML
    private TextField scatter_field;


    @FXML
    private TextField path;


    private String path_name,histogram_name=null,outlier_name=null,scatter_name=null;

    public void  initialize()
    {
        path_name=System.getProperty("user.home");
        path.setText(System.getProperty("user.home"));
        Tooltip.install(path, new Tooltip(path.getText()));
    }

    @FXML
    void Submit(MouseEvent event) {

        if(histogram_check.isSelected())
        {
            if(histogram_field.getText().isEmpty())
            {
                histogram_name="Histogram";
            }
            else {
                histogram_name=histogram_field.getText();
            }
        }
        if(outlier_check.isSelected())
        {
            if(outlier_field.getText().isEmpty())
            {
                outlier_name="Outlier";
            }
            else {
                outlier_name=outlier_field.getText();
            }
        }

        if(scatter_check.isSelected())
        {
            if(scatter_field.getText().isEmpty())
            {
                scatter_name="Scatter";
            }
            else {
                scatter_name=scatter_field.getText();
            }
        }
        // Close the window
        Stage stage = (Stage) button_ok.getScene().getWindow();
        stage.close();

    }

    @FXML
    void directory_choice(MouseEvent event) {

        // Open a file chooser to select the directory to save the images

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select the directory to save the images");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            path.setText(selectedDirectory.getAbsolutePath());
            path_name=selectedDirectory.getAbsolutePath();
            Tooltip.install(path, new Tooltip(path.getText()));
        }

    }

    public String getHistogram_name() {
        return histogram_name;
    }

    public String getOutlier_name() {
        return outlier_name;
    }

    public String getScatter_name() {
        return scatter_name;
    }

    public String getPath_name() {
        return path_name;
    }

}
