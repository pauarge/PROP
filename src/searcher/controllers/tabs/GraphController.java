package searcher.controllers.tabs;

import common.domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import searcher.controllers.BaseController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class GraphController extends BaseController {

    @FXML ChoiceBox choicesGraphTab;
    @FXML TextField addNodeText;
    @FXML AnchorPane anchorPane;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Hello world");
    }

}
