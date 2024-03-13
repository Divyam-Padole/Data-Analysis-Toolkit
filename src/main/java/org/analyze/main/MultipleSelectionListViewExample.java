package org.analyze.main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MultipleSelectionListViewExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a ListView with sample items
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Option 1", "Option 2", "Option 3", "Option 4");

        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(comboBox.getItems()));

        comboBox.setOnAction(event -> {
            listView.getSelectionModel().clearSelection();
            listView.getSelectionModel().select(comboBox.getValue());
        });

        VBox root = new VBox(10, comboBox, listView);
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setTitle("Multiple Selection Combo Box Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

