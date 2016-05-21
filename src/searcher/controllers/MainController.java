package searcher.controllers;

import common.domain.*;
import common.persistence.PersistenceController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import searcher.models.TableNode;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static searcher.Utils.closeWindow;


public class MainController implements Initializable {

    private Graph graph;
    private PersistenceController pc;

    @FXML BorderPane borderPane;
    @FXML TabPane mainTabs;
    @FXML ChoiceBox choicesGraphTab;
    @FXML ChoiceBox choicesSearch;
    @FXML TextField addNodeText;
    @FXML TextField searchText;
    @FXML TableView searchTable;

    @FXML
    private void backToLandingAction() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
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

    @FXML
    private void closeWindowAction() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        closeWindow(stage);
    }

    @FXML
    private void exportGraphAction() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            System.out.println("Starting graph export...");
            pc.exportGraph(selectedDirectory.getAbsolutePath());
            System.out.println("Graph export finished.");
        }
    }

    @FXML
    private void addNodeAction(){
        NodeType nt = NodeType.valueOf((String) choicesGraphTab.getValue());
        // TODO: HANDLE NULL nt
        String v = addNodeText.getText();
        // TODO: CHECK IF V IS EMPTY
        Node node = graph.createNode(nt, v);
        graph.addNode(node);
        System.out.println("Added node " + v);
        addNodeText.clear();
    }

    @FXML
    private void searchNodeAction(){
        NodeType nt = NodeType.valueOf((String) choicesSearch.getValue());
        // TODO: HANDLE NULL nt
        String v = searchText.getText();
        // TODO: CHECK IF V IS EMPTY
        SimpleSearch ss = new SimpleSearch(graph, nt, v);
        ss.search();
        ObservableList<TableNode> data = searchTable.getItems();
        data.clear();
        for (GraphSearch.Result r : ss.getResults()) {
            data.add(new TableNode(r.from.getId(), (String) choicesSearch.getValue(), r.from.getValue()));
            System.out.println(r.from.getId() + " " + r.from.getValue());
        }
        searchText.clear();
    }

    public void importDir(String path) {
        System.out.println("Starting graph import...");
        pc.importGraph(path);
        System.out.println("Graph import finished.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Building Graph...");
        graph = new Graph();
        pc = new PersistenceController(graph);
    }

}
