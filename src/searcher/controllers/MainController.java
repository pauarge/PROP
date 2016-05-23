package searcher.controllers;

import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import searcher.controllers.tabs.GraphController;
import searcher.controllers.tabs.RelationsController;
import searcher.controllers.tabs.SearchController;

import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import static searcher.Utils.closeWindow;


public class MainController extends BaseController {

    @FXML private BorderPane borderPane;
    @FXML private TabPane mainTabs;
    @FXML private Parent tabSearch;
    @FXML private SearchController tabSearchController;
    @FXML private Parent tabGraph;
    @FXML private GraphController tabGraphController;
    @FXML private Parent tabRelations;
    @FXML private RelationsController tabRelationsController;
    @FXML private Parent tabTerminal;
    @FXML private TuiController tabTerminalController;

    @FXML private CheckMenuItem terminalToggle;
    @FXML private Tab terminalSelector;

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
    private void handleTerminalToggle() {
        boolean status = terminalToggle.isSelected();
        //terminalSelector.getGraphic().setVisible(status);
    }

    @FXML
    private void closeWindowAction() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        closeWindow(stage);
    }

    public void importDir(String path) {
        System.out.println("Starting graph import...");
        pc.importGraph(path);
        System.out.println("Graph import finished.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graph = new Graph();
        pc = new PersistenceController(graph);
        semanticPathMap = new HashMap<>();

        tabSearchController.setGraph(graph);
        tabGraphController.setGraph(graph);
        tabRelationsController.setGraph(graph);

        tabTerminalController.setPc(pc);

        tabTerminalController.setSemanticPathMap(semanticPathMap);
    }

}
