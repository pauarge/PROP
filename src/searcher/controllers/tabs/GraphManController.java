package searcher.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static searcher.Utils.launchAlert;

import searcher.controllers.BaseController;


public class GraphManController extends BaseController {

    @FXML
    GridPane gridPane;

    @FXML
    private void importNodesAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.importNodes(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han importat correctament els nodes.");
        }
    }

    @FXML
    private void exportNodesAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.exportNodes(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han exportat correctament els nodes.");
        }
    }

    @FXML
    private void exportRelationsAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.exportSemanticPaths(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han exportat correctament les relacions.");
        }
    }

    @FXML
    private void importRelationsAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.importSemanticPaths(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han importat correctament les relacions.");
        }
    }

    @FXML
    private void importEdgesAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.importEdges(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han importat correctament les arestes.");
        }
    }

    @FXML
    private void exportEdgesAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.exportEdges(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han exportat correctament les arestes.");
        }
    }

    @FXML
    private void importSessionAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.importGraph(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han importat correctament la sessió.");
        }
    }

    @FXML
    private void exportSessionAction() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            pc.exportGraph(selectedDirectory.getAbsolutePath());
            pc.exportSemanticPaths(selectedDirectory.getAbsolutePath());
            launchAlert(stage, "S'han exportat correctament la sessió.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

}
