package searcher.controllers;

import common.domain.Graph;
import common.domain.NodeType;
import common.persistence.PersistenceController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private Graph graph;
    private PersistenceController pc;
    @FXML private Button returnRoot;
    @FXML private Button exportGraph;

    @FXML
    public void returnRootAction() throws Exception {
        Stage stage = (Stage) returnRoot.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Estàs segur de tornar a la finestra principal?");
        alert.setContentText("Fent això, es descartaran els canvis no exportats.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("../layouts/landing.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    public void exportGraphAction(){
        Stage stage = (Stage) exportGraph.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if(selectedDirectory != null) {
            System.out.println("Starting graph export...");
            pc.exportGraph(selectedDirectory.getAbsolutePath());
            System.out.println("Graph export finished.");
        }
    }

    public void importDir(String path){
        System.out.println("Starting graph import...");
        pc.importGraph(path);
        System.out.println("Graph import finished.");
    }

    @FXML
    private void getTextAndSearch(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Building Graph...");
        graph = new Graph();
        pc = new PersistenceController(graph);
    }

}
