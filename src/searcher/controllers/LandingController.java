package searcher.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;

import static searcher.Utils.closeWindow;


public class LandingController {

    @FXML
    private Button closeButton;
    @FXML
    private Button importGraph;
    @FXML
    private Button createGraph;

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        closeWindow(stage);
    }

    @FXML
    private void openDirSelector() {
        Stage stage = (Stage) importGraph.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../layouts/main.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainController controller = loader.<MainController>getController();
            controller.importDir(selectedDirectory.getAbsolutePath());
            stage = (Stage) createGraph.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    public void openMain() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../layouts/main.fxml"));
            Parent root = (Parent) loader.load();
            MainController controller = loader.<MainController>getController();
            Stage stage = (Stage) createGraph.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    @FXML
    private void changingStyle() {
    Stage stage = (Stage) importGraph.hoverProperty();
    stage.sty} */



}
