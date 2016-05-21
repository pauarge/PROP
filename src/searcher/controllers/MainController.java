package searcher.controllers;

import common.domain.*;
import common.persistence.PersistenceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import searcher.controllers.tabs.TabSearchController;
import searcher.models.SemanticPath;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static searcher.Utils.closeWindow;


public class MainController extends BaseController {

    @FXML private BorderPane borderPane;
    @FXML private TabPane mainTabs;
    @FXML private ChoiceBox choicesGraphTab;
    @FXML private TextField addNodeText;
    @FXML
    private Parent tabSearch; //embeddedElement
    @FXML
    private TabSearchController tabSearchController;

    //Relation
    private ObservableList<SemanticPath> pathData = FXCollections.observableArrayList();
    @FXML private TableView<SemanticPath> pathList;
    @FXML private TableColumn<SemanticPath, String> pathNameColumn;
    @FXML private Label pathName;
    @FXML private Label pathSummary;


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

    private void showPathDetails (SemanticPath semanticPath) {
        if (semanticPath == null) {
            pathName.setText("");
            pathSummary.setText("");
        } else {
            pathName.setText(semanticPath.getName());
            pathSummary.setText("Falta implementar");
        }
    }

    public void importDir(String path) {
        System.out.println("Starting graph import...");
        pc.importGraph(path);
        System.out.println("Graph import finished.");
    }

    private void initializeRelationsTab() {
        pathNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        pathList.setItems(pathData);
        pathData.add(new SemanticPath("Prova Numero 1"));
        pathData.add(new SemanticPath("Prova Numero 2"));
        pathData.add(new SemanticPath("Prova Numero 3"));
        showPathDetails(null);
        pathList.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPathDetails(newValue))
        );
    }

    @FXML private void handleDeletePath() {
        int i = pathList.getSelectionModel().getSelectedIndex();
        if (i >= 0) pathData.remove(i);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Building Graph...");
        graph = new Graph();
        pc = new PersistenceController(graph);
        tabSearchController.setGraph(graph);
        initializeRelationsTab();
    }

}
