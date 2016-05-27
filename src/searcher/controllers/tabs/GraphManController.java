package searcher.controllers.tabs;

import common.domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import searcher.controllers.BaseController;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static searcher.Utils.launchAlert;


public class GraphManController extends BaseController {

    @FXML
    ChoiceBox choicesGraphTab;
    @FXML
    TextField addNodeText;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private BorderPane borderPane;

    @FXML
    private void exportGraphAction() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            System.out.println("Starting graph export...");
            pc.exportGraph(selectedDirectory.getAbsolutePath());
            System.out.println("Graph export finished.");
        }
    }

    @FXML
    private void addNodeAction() {
        if (choicesGraphTab.getValue() == null) {
            launchAlert((Stage) anchorPane.getScene().getWindow(), "Per fer l'addició, selecciona un tipus de node");
            return;
        }
        if (addNodeText.getText().length() < 1) {
            launchAlert((Stage) anchorPane.getScene().getWindow(), "Per fer l'addició, escriu un valor pel node");
            return;
        }
        NodeType nt = NodeType.valueOf((String) choicesGraphTab.getValue());
        String v = addNodeText.getText();
        Node node = graph.createNode(nt, v);
        graph.addNode(node);
        System.out.println("Added node " + v);
        addNodeText.clear();
    }

    @FXML
    private void addRelationAction() {

    }

    @FXML
    private void FileNodesAdded() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha afegit l'arxiu de nodes.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void FileRelationsAdded() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha afegit l'arxiu d'arestes.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void NodeCreated() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha creat el node.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void RelationCreated() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha creat l'aresta entre els dos nodes introduits.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void NodeDeleted() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha eliminat el node.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void RelationDeleted() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha eliminat l'aresta entre els dos nodes introduits.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void RelationsExported() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'han exportat les relacions.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void GraphExported() throws Exception {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Modificació realitzada correctament");
        alert.setContentText("S'ha exportat el graf.");
        Optional<ButtonType> result = alert.showAndWait();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
