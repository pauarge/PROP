package searcher.controllers.tabs;

import common.domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    GridPane gridPane;

    @FXML
    private void exportGraphAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.exportGraph(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void addNodeAction() {
        if (choicesGraphTab.getValue() == null) {
            launchAlert((Stage) gridPane.getScene().getWindow(), "Per fer l'addició, selecciona un tipus de node");
            return;
        }
        if (addNodeText.getText().length() < 1) {
            launchAlert((Stage) gridPane.getScene().getWindow(), "Per fer l'addició, escriu un valor pel node");
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
    private void exportRelationsAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if(selectedDirectory != null){
            pc.exportSemanticPaths(selectedDirectory.getAbsolutePath());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
