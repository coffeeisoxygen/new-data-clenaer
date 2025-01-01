package com.coffeecode.ui;

import java.io.File;

import com.coffeecode.interfaces.OnDataUpdateListener;
import com.coffeecode.model.DataModel;
import com.coffeecode.services.DataModelServices;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

public class MainViewController implements OnDataUpdateListener {
    @FXML
    private TableView<String[]> dataTable;
    @FXML
    private ProgressBar progressBar;

    private DataModelServices dataService;

    @FXML
    public void initialize() {
        dataService = new DataModelServices();
        dataService.addListener(this);
    }

    @FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            dataService.processFile(file.getPath());
        }
    }

    @Override
    public void onDataLoaded(DataModel dataModel) {
        // Update TableView with data
    }

    @Override
    public void onError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    @Override
    public void onProgress(int progress) {
        progressBar.setProgress(progress / 100.0);
    }
}