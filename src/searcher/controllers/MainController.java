package searcher.controllers;

import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private Graph graph;
    private PersistenceController pc;
    @FXML private Button returnRoot;

    @FXML
    public void returnRootAction() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../layouts/landing.fxml"));
        Stage stage = (Stage) returnRoot.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void importDir(String path){
        System.out.println("Importing graph...");
        pc.importGraph(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Building Graph...");
        graph = new Graph();
        pc = new PersistenceController(graph);
    }

}
