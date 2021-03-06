package searcher.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static searcher.Utils.closeWindow;


public class LandingController {

    private static final String APP_TITLE = "Cercador Relacional";

    @FXML
    GridPane gridPane;

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        closeWindow(stage);
    }

    @FXML
    private void openDirSelector() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searcher/layouts/main.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/searcher/img/magnifier.png"));
            stage.setTitle(APP_TITLE);
            stage.show();
            ((Stage) gridPane.getScene().getWindow()).close();

            MainController.getPc().importDir(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void openMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searcher/layouts/main.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/searcher/img/magnifier.png"));
            stage.setTitle(APP_TITLE);
            stage.show();
            ((Stage) gridPane.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
