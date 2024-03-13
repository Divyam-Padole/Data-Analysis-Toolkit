package Controller;

import Controller.Algorithms.Imputaion.*;
import Controller.Algorithms.Outlier.outlierDetectionUsingIQR;
import Controller.Algorithms.Outlier.outlierDetectionUsingModifiedZscore;
import Controller.Algorithms.Normalization.Normalization;
import Controller.Algorithms.Outlier.outlierDetectionUsingZscore;
import Controller.Algorithms.Transformation.Transformation;
import Controller.chart_operations.HistogramChart;
import Controller.chart_operations.Scatter_Outlier;
import Controller.chart_operations.Scatter_plot;
import Table_Formation.Row_Indexer;
import Table_Formation.Summary_Table;
import javafx.embed.swing.SwingFXUtils;
import com.almasb.fxgl.entity.action.Action;
import com.almasb.fxgl.profile.SaveFile;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;



import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Table_Formation.ObservableTablesawRow;
import tech.tablesaw.api.Table;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class Descriptor {
    public static Table dataframe;

    public static Table dataframe_with_index;

    @FXML
    private TableView<ObservableTablesawRow> sample_table;

    @FXML
    private TextArea display;

    @FXML
    private TableView<ObservableTablesawRow> summary_table;

    @FXML
    private Button openFile;

    @FXML
    private ScatterChart<Number, Number> scatter_outlier;


    @FXML
    private TableView<ObservableTablesawRow> tableView;

    @FXML
    private BarChart<String, Number> histogram;

    @FXML
    private ScatterChart<Object, Object> scatter;

    @FXML
    private ScatterChart<Object, Object> scatter_cat;

    @FXML
    private ScatterChart<Object, Object> scatter_cat_num;

    @FXML
    private ScatterChart<Object, Object> scatter_num_cat;

    @FXML
    private ChoiceBox<String> imputation;

    @FXML
    private ChoiceBox<String> normalization;

    @FXML
    private Tab preprocessing_tab;


    @FXML
    private Tab descriptor_tab;

    @FXML
    private ChoiceBox<String> transformation;

    @FXML
    private ChoiceBox<String> outlier_detection;

    @FXML
    private Label outlier_main_label;


    @FXML
    private Label outlier_secondary;

    @FXML
    private CheckBox check_outlier_handle;


    @FXML
    private Label imputation_label;

    @FXML
    private CheckBox check_imputation;


    @FXML
    private Button outlier_graph;

    @FXML
    private Button save_img;

    @FXML
    private Button refresh;


    @FXML
    private Button add_report;

    @FXML
    private Button start;


    @FXML
    private TabPane tabpane;


    @FXML
    private TextArea suggest;

    private HistogramChart obj;

    private Scatter_Outlier obj_outlier;

    private Scatter_plot obj_scatter;

    private Table result;


    int flag = 0;

    private static Table summary_tab;

    public Table get_Table() {
        return dataframe;
    }

    public static Table get_Table_with_index() {
        return dataframe_with_index;
    }

    public static Table get_Summary_Table() {
        return summary_tab;
    }


    public void tableView_loader(Table table, TableView<ObservableTablesawRow> view, int length) {
        view.setFixedCellSize(25);
//

        // Add columns dynamically based on Tablesaw columns

        for (String columnName : table.columnNames()) {
            TableColumn<ObservableTablesawRow, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(data -> data.getValue().get(columnName));
            view.getColumns().add(column);
        }

        // Add rows to TableView
        for (int rowIndex = 0; rowIndex < length; rowIndex++) {
            ObservableTablesawRow observableRow = new ObservableTablesawRow(table, rowIndex);
            view.getItems().add(observableRow);
        }

    }

    @FXML
    void Fileloader(MouseEvent event) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        File csvFile = fileChooser.showOpenDialog(new Stage());

        if (csvFile != null) {
            String location_of_file = csvFile.getPath();

            String extension = location_of_file.substring(location_of_file.lastIndexOf(".") + 1);

            if (extension.equals("csv")) {
                dataframe = Table.read().csv(location_of_file);

                dataframe_with_index = new Row_Indexer().indexer(dataframe.copy());


                //loading sample data table in tableview with 20 rows
                tableView_loader(dataframe_with_index, sample_table, 20);

                // Summary table formation code

                summary_tab = new Summary_Table().formation(dataframe);
                tableView_loader(summary_tab, summary_table, summary_tab.rowCount());


                //displaying histogram
                obj = new HistogramChart();
                obj.setData(dataframe);
                obj.setBinSize(6);
                String column_name = dataframe.column(2).name();
                obj.setColumn_name(column_name);
                obj.logic();
                properties_histogram(obj, column_name);


                //displaying outlier plot
                obj_outlier = new Scatter_Outlier();
                obj_outlier.setData(dataframe_with_index);
                String scatter_outlier_col_name = dataframe_with_index.column(0).name();
                obj_outlier.setColumn_name(scatter_outlier_col_name);
                obj_outlier.logic();
                properties_outlier(obj_outlier, scatter_outlier_col_name);


                //displaying   scatter plot
                obj_scatter = new Scatter_plot();
                obj_scatter.setData(dataframe);
                String scatter_col_name1 = dataframe.column(0).name();
                String scatter_col_name2 = dataframe.column(1).name();
                obj_scatter.setColumn_name1(scatter_col_name1);
                obj_scatter.setColumn_name2(scatter_col_name2);
                obj_scatter.logic();
                properties_scatter(obj_scatter, scatter_col_name1, scatter_col_name2);

                //enabling the preprocessing tab
                preprocessing_tab.setDisable(false);

                //enabling  add_report and save graphs button

                add_report.setDisable(false);
                save_img.setDisable(false);

            }
        } else {
            //error pop up window
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Error");

            // Create a label with the error message
            Label label = new Label("Please select a csv file");

            // Set up the layout for the pop-up
            StackPane layout = new StackPane();
            layout.getChildren().add(label);

            // Set up the scene and attach it to the stage
            Scene scene = new Scene(layout, 250, 150);
            popupStage.setScene(scene);

            // Show the pop-up window
            popupStage.showAndWait();

        }


    }

    @FXML
    void explorer_tab(Event event) {

        if (flag == 0) {
            // load  the data table in the output table of preprocessing
            tableView_loader(dataframe_with_index, tableView, dataframe_with_index.rowCount());
            //loading methods for imputation and normalization

            imputation.getItems().addAll("Remove rows with maximum missing value", "Remove a column with maximum missing value", "Minimum", "Maximum", "Mean", "Median", "Mode", "Simple Linear Regression", "Multiple Linear Regression", "KNN ", "Weighted KNN", "None of the above");
            normalization.getItems().addAll("Min-Max Scaler", "Z-Score Normalization", "Robust Scaling", "Log Transformation", "Box-Cox Transformation", "None of the above");
            transformation.getItems().addAll("Label Encoding", "Ordinal Encoding", "One-Hot Encoding", "Frequency Encoding", "None of the above");
            outlier_detection.getItems().addAll("Standard Z-Score ", "Modified Z-Score", "IQR", "None of the above");

            flag = 1;
        }
        imputation.setOnAction(this::imputation_select);
        normalization.setOnAction(this::normalize);
        transformation.setOnAction(this::transformation_select);
        outlier_detection.setOnAction(this::outlier_detect);
    }

    @FXML
    void modify_histogram(MouseEvent event) throws IOException {
        //popup  .fxml   window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dialog_Histogram.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Histogram Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        //getting the parameters from the dialog box
        Dialog_HistogramController controller = fxmlLoader.getController();
        int interval = controller.getInterval();
        String column_name = controller.getColumn();
        obj.setBinSize(interval);
        obj.setColumn_name(column_name);
        obj.logic();
        properties_histogram(obj, column_name);

    }

    @FXML
    void modify_outlier(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dialog_Outlier.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Outlier Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        //getting the parameters from the dialog box
        Dialog_OutlierController controller = fxmlLoader.getController();
        String column_name = controller.getColumn();
        obj_outlier.setColumn_name(column_name);
        obj_outlier.logic();
        properties_outlier(obj_outlier, column_name);

    }

    public void properties_histogram(HistogramChart obj, String column_name) {
        //calling  create_series  from HistogramChart class
        XYChart.Series<String, Number> series = obj.create_series();
        histogram.getData().clear();
        histogram.setTitle("Histogram");
        histogram.getXAxis().setLabel(column_name);
        histogram.getYAxis().setLabel("Values");
        histogram.getData().add(series);
        histogram.setLegendVisible(false);

        //Bins height  on hover  of the bar
        int[] j = obj.getFrequency();
        for (int i = 0; i < j.length; i++) {
            XYChart.Data<String, Number> data = series.getData().get(i);
            Tooltip.install(data.getNode(), new Tooltip("Frequency: " + j[i]));
        }
    }

    public void properties_outlier(Scatter_Outlier obj_outlier, String column_name) {
        //calling  create_series  from Scatter_Outlier class
        XYChart.Series<Number, Number> series1 = obj_outlier.getSeries1();
        XYChart.Series<Number, Number> series2 = obj_outlier.getSeries2();
        XYChart.Series<Number, Number> series3 = obj_outlier.getSeries3();
        scatter_outlier.getData().clear();
        scatter_outlier.setTitle("Outlier  Plot");
        scatter_outlier.getXAxis().setLabel(column_name);
        scatter_outlier.getYAxis().setLabel(column_name);
        scatter_outlier.getData().add(series1);
        scatter_outlier.getData().add(series2);
        scatter_outlier.getData().add(series3);
        scatter_outlier.setLegendVisible(true);

        // Outliers on hover
        for (int i = 0; i < series1.getData().size(); i++) {
            XYChart.Data<Number, Number> data = series1.getData().get(i);
            Tooltip.install(data.getNode(), new Tooltip("Outlier: " + data.getXValue()));
        }

        // Data on hover
        for (int i = 0; i < series2.getData().size(); i++) {
            XYChart.Data<Number, Number> data = series2.getData().get(i);
            Tooltip.install(data.getNode(), new Tooltip("Data: " + data.getXValue()));
        }

        // Quartiles on hover
        for (int i = 0; i < series3.getData().size(); i++) {
            XYChart.Data<Number, Number> data = series3.getData().get(i);
            if (i == 0) {
                Tooltip.install(data.getNode(), new Tooltip("Lower Bound: " + data.getXValue()));
            }
            if (i == 1) {
                Tooltip.install(data.getNode(), new Tooltip("Q1: " + data.getXValue()));
            }
            if (i == 2) {
                Tooltip.install(data.getNode(), new Tooltip("Median: " + data.getXValue()));
            }
            if (i == 3) {
                Tooltip.install(data.getNode(), new Tooltip("Q3: " + data.getXValue()));
            }
            if (i == 4) {
                Tooltip.install(data.getNode(), new Tooltip("Upper Bound: " + data.getXValue()));
            }
        }
        // frequency of series name on hover
        int a = series1.getData().size();
        int b = series2.getData().size();
        int c = series3.getData().size();

        //frequency  display hover on legend
        series1.setName("Outliers: " + a);
        series2.setName("Data: " + b);
        series3.setName("Quartiles: " + c);

    }

    public void properties_scatter(Scatter_plot obj_scatter, String column_name1, String column_name2) {
        //calling  create_series  from Scatter_plot class
        XYChart.Series<Object, Object> series1 = obj_scatter.getSeries1();
        if (dataframe.column(column_name1).type().toString().equals("STRING") && dataframe.column(column_name2).type().toString().equals("STRING")) {
            scatter_num_cat.setVisible(false);
            scatter_num_cat.setDisable(true);
            scatter_num_cat.setOpacity(0);
            //
            scatter_cat_num.setVisible(false);
            scatter_cat_num.setDisable(true);
            scatter_cat_num.setOpacity(0);
            //
            scatter.setVisible(false);
            scatter.setDisable(true);
            scatter.setOpacity(0);
            //scatter category  and category
            scatter_cat.getData().clear();
            scatter_cat.setTitle("Scatter Plot");
            scatter_cat.getXAxis().setLabel(column_name1);
            scatter_cat.getYAxis().setLabel(column_name2);
            scatter_cat.getData().add(series1);
            scatter_cat.setLegendVisible(false);
            scatter_cat.setVisible(true);
            scatter_cat.setDisable(false);
            scatter_cat.setOpacity(1);


        } else if ((dataframe.column(column_name1).type().toString().equals("INTEGER") && dataframe.column(column_name2).type().toString().equals("INTEGER")) || (dataframe.column(column_name1).type().toString().equals("DOUBLE") && dataframe.column(column_name2).type().toString().equals("DOUBLE"))) {

            scatter_num_cat.setVisible(false);
            scatter_num_cat.setDisable(true);
            scatter_num_cat.setOpacity(0);
            //
            scatter_cat_num.setVisible(false);
            scatter_cat_num.setDisable(true);
            scatter_cat_num.setOpacity(0);
            //
            scatter_cat.setVisible(false);
            scatter_cat.setDisable(true);
            scatter_cat.setOpacity(0);
            //scatter  number and number
            scatter.getData().clear();
            scatter.setTitle("Scatter Plot");
            scatter.getXAxis().setLabel(column_name1);
            scatter.getYAxis().setLabel(column_name2);
            scatter.getData().add(series1);
            scatter.setLegendVisible(false);
            scatter.setVisible(true);
            scatter.setDisable(false);
            scatter.setOpacity(1);


        } else if ((dataframe.column(column_name1).type().toString().equals("STRING") && dataframe.column(column_name2).type().toString().equals("INTEGER")) || (dataframe.column(column_name1).type().toString().equals("INTEGER") && dataframe.column(column_name2).type().toString().equals("STRING")) || (dataframe.column(column_name1).type().toString().equals("DOUBLE") && dataframe.column(column_name2).type().toString().equals("STRING")) || (dataframe.column(column_name1).type().toString().equals("STRING") && dataframe.column(column_name2).type().toString().equals("DOUBLE"))) {
            if ((dataframe.column(column_name1).type().toString().equals("STRING") && dataframe.column(column_name2).type().toString().equals("INTEGER")) || (dataframe.column(column_name1).type().toString().equals("STRING") && dataframe.column(column_name2).type().toString().equals("DOUBLE"))) {
                scatter_num_cat.setVisible(false);
                scatter_num_cat.setDisable(true);
                scatter_num_cat.setOpacity(0);
                //
                scatter_cat.setVisible(false);
                scatter_cat.setDisable(true);
                scatter_cat.setOpacity(0);
                //
                scatter.setVisible(false);
                scatter.setDisable(true);
                scatter.setOpacity(0);
                //
                scatter_cat_num.getData().clear();
                scatter_cat_num.setTitle("Scatter Plot");
                scatter_cat_num.getXAxis().setLabel(column_name1);
                scatter_cat_num.getYAxis().setLabel(column_name2);
                scatter_cat_num.getData().add(series1);
                scatter_cat_num.setLegendVisible(false);
                scatter_cat_num.setVisible(true);
                scatter_cat_num.setDisable(false);
                scatter_cat_num.setOpacity(1);
            } else {
                scatter_cat_num.setVisible(false);
                scatter_cat_num.setDisable(true);
                scatter_cat_num.setOpacity(0);
                //
                scatter_cat.setVisible(false);
                scatter_cat.setDisable(true);
                scatter_cat.setOpacity(0);
                //
                scatter.setVisible(false);
                scatter.setDisable(true);
                scatter.setOpacity(0);
                //
                scatter_num_cat.getData().clear();
                scatter_num_cat.setTitle("Scatter Plot");
                scatter_num_cat.getXAxis().setLabel(column_name1);
                scatter_num_cat.getYAxis().setLabel(column_name2);
                scatter_num_cat.getData().add(series1);
                scatter_num_cat.setLegendVisible(false);
                scatter_num_cat.setVisible(true);
                scatter_num_cat.setDisable(false);
                scatter_num_cat.setOpacity(1);
            }
        }
        // Data on hover
        for (int i = 0; i < series1.getData().size(); i++) {
            XYChart.Data<Object, Object> data = series1.getData().get(i);
            Tooltip.install(data.getNode(), new Tooltip("Data: " + data.getXValue() + "," + data.getYValue()));
        }
    }

    @FXML
    void modify_scatter(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dialog_Scatter.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Scatter Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        //getting the parameters from the dialog box
        Dialog_ScatterController controller = fxmlLoader.getController();
        String column_name1 = controller.getColumn();
        String column_name2 = controller.getColumn1();
        obj_scatter.setColumn_name1(column_name1);
        obj_scatter.setColumn_name2(column_name2);
        obj_scatter.logic();
        properties_scatter(obj_scatter, column_name1, column_name2);
    }


    @FXML
    void imputation_select(ActionEvent event) {
        String method = imputation.getValue();
        switch (method) {
            case "Remove rows with maximum missing value" -> {
                drop_row_column dr = new drop_row_column();
                Table data = dataframe_with_index.copy();
                result = dr.drop_row(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tMissing Rows with missing values exceeding 60% are selectively removed from the dataset as part of the imputation strategy. This selective removal helps maintain data integrity by excluding instances with extensive missing data.";
                suggest.setText(sugess);

            }
            case "Remove a column with maximum missing value" -> {
                drop_row_column dr = new drop_row_column();
                Table data = dataframe_with_index.copy();
                result = dr.drop_column(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tColumns with missing values surpassing 60% are omitted from the dataset during the imputation process. This approach ensures data quality by excluding features with substantial missing data.";
                suggest.setText(sugess);
            }
            case "Minimum" -> {
                replace_global_constant_min_max q = new replace_global_constant_min_max();
                Table data = dataframe_with_index.copy();
                result = q.replace_minimum(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tMissing values are substituted with the minimum value of the available data for that feature. This approach ensures that missing values are replaced with the smallest observed value.";
                suggest.setText(sugess);
            }
            case "Maximum" -> {
                replace_global_constant_min_max q = new replace_global_constant_min_max();
                Table data = dataframe_with_index.copy();
                result = q.replace_maximum(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\t Missing values are replaced with the maximum value of the available data for that feature. This method ensures that missing values are substituted with the highest observed value.";
                suggest.setText(sugess);
            }

//        else if(method.equals("Global Constant"))
//        {
//            Table data = dataframe.impute().globalConstant(0);
//            tableView_loader(data,tableView,data.rowCount());
//        }
            case "Mean" -> {
                replace_central_tendency ten = new replace_central_tendency();
                Table data = dataframe_with_index.copy();
                result = ten.replace_mean(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tMissing values are replaced with the mean of the available data for that feature. This method helps to preserve the overall distribution of the dataset while filling in missing values.";
                suggest.setText(sugess);

            }
            case "Median" -> {
                replace_central_tendency ten = new replace_central_tendency();
                Table data = dataframe_with_index.copy();
                result = ten.replace_median(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\t Missing values are substituted with the median of the available data for that feature. This technique offers robustness against outliers and helps maintain the central tendency of the dataset.";
                suggest.setText(sugess);
            }
            case "Mode" -> {
                replace_central_tendency ten = new replace_central_tendency();
                Table data = dataframe_with_index.copy();
                result = ten.replace_mode(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tMissing values are filled with the mode, or the most frequently occurring value, of the available data for that feature. This method is particularly useful for categorical variables and ensures that missing values are replaced with the most common value, preserving the characteristic distribution of the dataset.";
                suggest.setText(sugess);
            }

            case "Simple Linear Regression" -> {
                Table data = dataframe_with_index.copy();
                result = SLR.fillMissingValuesUsingSLR(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tSimple Linear Regression, missing values are estimated based on the relationship between the target feature and one or more predictor features. The algorithm fits a linear model to the available data and predicts missing values for the target feature using the observed values of the predictor features.";
                suggest.setText(sugess);
            }

//        else if(method.equals("Multiple Linear Regression"))
//        {
//            Table data = dataframe.impute().multipleLinearRegression();
//            tableView_loader(data,tableView,data.rowCount());
//        }
            case "KNN " -> {
                KNN_simple knn = new KNN_simple();
                Table data = dataframe_with_index.copy();
                result = knn.KNN_imputation(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess = "Description:\n\n\tKNN (K-Nearest Neighbors) with Euclidean distance, missing values are estimated based on the values of their nearest neighbors. The algorithm calculates the distance between data points, considering their feature values, and replaces missing values with those of the nearest neighbors.";
                suggest.setText(sugess);
            }

            case "Weighted KNN" -> {
//            Table data = dataframe.impute().weightedKnn(3);
//            tableView_loader(data,tableView,data.rowCount());
                String sugess = "Description:\n\n\tKNN (K-Nearest Neighbors) with weighted distance, missing values are estimated by considering the values of their nearest neighbors, with closer neighbors exerting more influence. The algorithm calculates the distances between data points, incorporating weights to give higher priority to closer neighbors. Missing values are then imputed based on a weighted average of neighboring values.";
                suggest.setText(sugess);
            }
        }
    }


    void normalize(ActionEvent event) {
        String method = normalization.getValue();
        switch (method) {
            case "Min-Max Scaler" -> {
                Normalization nor = new Normalization();
                Table data = dataframe_with_index.copy();
                result = nor.minMaxScaler(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tMin-Max Scaler transforms each feature to a specified range, typically between 0 and 1. This method preserves the original distribution of the data while ensuring that all features are on the same scale. It is particularly useful when the features have varying ranges and helps to prevent any single feature from dominating the analysis.";
                suggest.setText(sugess);

            }
            case "Z-Score Normalization" -> {
                Normalization nor = new Normalization();
                Table data = dataframe_with_index.copy();
                result = nor.standardized(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tZ-score normalization, also known as standardization, transforms each data point to its z-score, indicating its deviation from the mean in terms of standard deviations. This process rescales the data to have a mean of 0 and a standard deviation of 1, making it easier to compare variables with different scales and distributions.";
                suggest.setText(sugess);
            }
            case "Robust Scaling" -> {
                Normalization nor = new Normalization();
                Table data = dataframe_with_index.copy();
                result = nor.robustScaling(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tRobust scaling, also known as robust standardization, scales each feature in a dataset by removing the median and scaling to the interquartile range (IQR). Unlike standardization, robust scaling is less affected by outliers, making it suitable for datasets with skewed or non-normal distributions. This method preserves the relative ordering of data points while reducing the influence of extreme values, ensuring robustness.";
                suggest.setText(sugess);
            }
            case "Log Transformation"->{
                Normalization nor = new Normalization();
                Table data = dataframe_with_index.copy();
                result = nor.logTransformation(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tLog transformation involves applying the natural logarithm function to data points within a feature. This technique is useful for addressing skewed data distributions, stabilizing variance, and promoting linearity in relationships between variables, making it valuable for various statistical analyses and machine learning applications.";
                suggest.setText(sugess);

            }
case "Box-Cox Transformation"->{
                Normalization nor = new Normalization();
                Table data = dataframe_with_index.copy();
                result = nor.boxCoxTransformation(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tBox-Cox Transformation is a data transformation technique used to normalize non-normally distributed data by applying a power transformation. It adjusts the distribution of the data to make it closer to a normal distribution, which can improve the performance of statistical models. This method is particularly useful for variables with skewed distributions, allowing for better interpretation and analysis.";
                suggest.setText(sugess);
            }

        }
    }

    void outlier_detect(ActionEvent event) {
        String method = outlier_detection.getValue();
        switch (method) {
            case "Standard Z-Score " -> {
                outlier_graph.setDisable(false);
                Table data = dataframe_with_index.copy();
                result = new outlierDetectionUsingZscore().zscore(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());

                //suggestion sugest set text z-score information
                String sugess="Description:\n\n\tThe z-score measures how far a data point is from the mean in terms of standard deviations. In outlier detection, a high z-score indicates a significant deviation from the norm, suggesting a potential outlier.";
                suggest.setText(sugess);

            }
            case "Modified Z-Score" -> {
                outlier_graph.setDisable(false);
                Table data = dataframe_with_index.copy();
                result = new outlierDetectionUsingModifiedZscore().modified_zscore(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tThe modified z-score, unlike the traditional z-score, uses the median and median absolute deviation (MAD) instead of the mean and standard deviation. This makes it more robust to outliers. ";
                suggest.setText(sugess);
            }
            case "IQR" -> {
                outlier_graph.setDisable(false);
                Table data = dataframe_with_index.copy();
                result = new outlierDetectionUsingIQR().iqr(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tThe Inter Quartile Range (IQR) is a robust measure of statistical dispersion that calculates the range between the first quartile (Q1) and the third quartile (Q3) of a dataset. In outlier detection, data points lying below Q1 - 1.5 * IQR or above Q3 + 1.5 * IQR are considered potential outliers.";
                suggest.setText(sugess);
            }
        }
    }


    @FXML
    void done_outlier(ActionEvent event) {
        try {
            if (check_outlier_handle.isSelected() && !outlier_detection.getValue().isEmpty()) {
                refresh.setDisable(false);
                outlier_main_label.setDisable(true);
                outlier_secondary.setDisable(true);
                outlier_detection.setDisable(true);
                outlier_graph.setDisable(true);
                check_outlier_handle.setDisable(true);

                //enable missing value handling
                imputation.setDisable(false);
                imputation_label.setDisable(false);
                check_imputation.setDisable(false);
                if (!outlier_detection.getValue().equals("None of the above")) {
                    dataframe_with_index = result;
                }
            }
        } catch (NullPointerException e) {
            //error pop up window
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Error");

            // Create a label with the error message
            Label label = new Label("Please select a method for outlier detection and check the checkbox");

            // Set up the layout for the pop-up
            StackPane layout = new StackPane();
            layout.getChildren().add(label);

            // Set up the scene and attach it to the stage
            Scene scene = new Scene(layout, 250, 150);
            popupStage.setScene(scene);

            // Show the pop-up window
            popupStage.showAndWait();

            check_outlier_handle.setSelected(false);
        }

    }


    @FXML
    void refresh_logic(MouseEvent event) {
        dataframe = dataframe_with_index;

        //loading sample data table in tableview with 20 rows
        sample_table.getItems().clear();
        sample_table.getColumns().clear();
        sample_table.refresh();
        tableView_loader(dataframe_with_index, sample_table, 20);

        // Summary table formation code

        summary_tab = new Summary_Table().formation(dataframe);
        summary_table.getColumns().clear();
        summary_table.getItems().clear();
        summary_table.refresh();
        tableView_loader(summary_tab, summary_table, summary_tab.rowCount());


        //displaying histogram
        obj = new HistogramChart();
        obj.setData(dataframe);
        obj.setBinSize(6);
        String column_name = dataframe.column(2).name();
        obj.setColumn_name(column_name);
        obj.logic();
        properties_histogram(obj, column_name);


        //displaying outlier plot
        obj_outlier = new Scatter_Outlier();
        obj_outlier.setData(dataframe_with_index);
        String scatter_outlier_col_name = dataframe_with_index.column(0).name();
        obj_outlier.setColumn_name(scatter_outlier_col_name);
        obj_outlier.logic();
        properties_outlier(obj_outlier, scatter_outlier_col_name);


        //displaying   scatter plot
        obj_scatter = new Scatter_plot();
        obj_scatter.setData(dataframe);
        String scatter_col_name1 = dataframe.column(0).name();
        String scatter_col_name2 = dataframe.column(1).name();
        obj_scatter.setColumn_name1(scatter_col_name1);
        obj_scatter.setColumn_name2(scatter_col_name2);
        obj_scatter.logic();
        properties_scatter(obj_scatter, scatter_col_name1, scatter_col_name2);

        refresh.setDisable(true);

    }

    @FXML
    void outlier_handling_graph(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("outlier_handling_screen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Scatter Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();

    }

    void transformation_select(ActionEvent event) {
        String method = transformation.getValue();
        switch (method) {
            case "Label Encoding" -> {
                Transformation tr = new Transformation();
                Table data = dataframe_with_index.copy();
                result = tr.labelEncode(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tLabel encoding is a data transformation technique used to convert categorical variables into numerical representations. Each unique category is assigned a unique integer value. This method is particularly useful for algorithms that require numerical inputs.";
                suggest.setText(sugess);

            }
            case "One-Hot Encoding" -> {
                Transformation tr = new Transformation();
                Table data = dataframe_with_index.copy();
                try {
                    result = tr.oneHotEncode(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tOne-hot encoding is a data transformation technique used to convert categorical variables into a binary format. Each unique category is represented by a binary vector, with a '1' indicating the presence of the category and '0' indicating absence. This method is particularly beneficial for algorithms that require numerical inputs and cannot directly interpret categorical data, facilitating accurate modeling and analysis.";
                suggest.setText(sugess);
            }
            case "Ordinal Encoding" -> {
                Transformation tr = new Transformation();
                Table data = dataframe_with_index.copy();
                result = tr.ordinal_encode(data);
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tOrdinal encoding is a data transformation technique used to convert categorical variables into numerical representations while preserving the inherent order of the categories. Each unique category is assigned a numerical value based on its rank or order.";
                suggest.setText(sugess);
            }
            case "Frequency Encoding" -> {
                Transformation tr = new Transformation();
                Table data = dataframe_with_index.copy();
                try {
                    result = tr.frequency_encoding(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableView.refresh();
                tableView_loader(result, tableView, result.rowCount());
                String sugess="Description:\n\n\tFrequency encoding is a data transformation technique used to convert categorical variables into numerical representations based on their frequency of occurrence. Each category is replaced with the count or proportion of times it appears in the dataset. This method leverages the frequency information to encode categorical variables.";
                suggest.setText(sugess);
            }
        }

    }

    @FXML
    void save_images(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dialog_SaveImages.fxml"));
        Parent root=  fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Saving  Parameters");
        stage.setScene(new Scene(root));
        stage.showAndWait();


        //getting the parameters from the dialog box
        SaveImagesController controller = fxmlLoader.getController();
        String path = controller.getPath_name();
        String histogram_name = controller.getHistogram_name();
        String scatter_name = controller.getScatter_name();
        String outlier_name = controller.getOutlier_name();


        if(histogram_name!=null)
        {
            //saving chats in jafafx
            WritableImage image = histogram.snapshot(null, null);
            File file = new File(path+"/"+histogram_name+".png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if(scatter_name!=null)
        {
            WritableImage image=null;
            if(!scatter_cat.isDisabled())
            {
                image = scatter_cat.snapshot(null, null);
            }
            else if (!scatter.isDisabled())
            {
                image = scatter.snapshot(null, null);
            }
            else if (!scatter_cat_num.isDisabled())
            {
                image = scatter_cat_num.snapshot(null, null);
            }
            else if (!scatter_num_cat.isDisabled())
            {
                image=scatter_num_cat.snapshot(null,null);
            }

            File file = new File(path+"/"+scatter_name+".png");
            try {
                assert image != null;
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if(outlier_name!=null)
        {
            WritableImage image = scatter_outlier.snapshot(null, null);
            File file = new File(path+"/"+outlier_name+".png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            loading_notify();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void  loading_notify() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("Loading.fxml"));
        Parent root1=  fxmlLoader1.load();
        Stage stage1 = new Stage();
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage1.initStyle(StageStyle.UTILITY);
        stage1.setTitle("Notification");
        stage1.setScene(new Scene(root1));
        stage1.showAndWait();
    }


    @FXML
    void start_explore(MouseEvent event) {
        descriptor_tab.setDisable(false);

        tabpane.getSelectionModel().select(descriptor_tab);

    }




}
