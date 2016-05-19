package searcher.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;


public class LandingController {

    @FXML private Button closeButton;
    @FXML private Button importGraph;
    @FXML private Button createGraph;

    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openDirSelector(){
        Stage stage = (Stage) importGraph.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Select directory");
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            System.out.println(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void openMain() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../layouts/main.fxml"));
        Parent root = (Parent) loader.load();
        MainController controller = loader.<MainController>getController();
        controller.importDir("HOLA K ASE");
        Stage stage = (Stage) createGraph.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
