package searcher.controllers.tabs;

import common.domain.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import searcher.controllers.BaseController;
import searcher.controllers.GraphController;
import searcher.models.RelevanceModel;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static searcher.Utils.launchAlert;


public class RelevanceController extends BaseController {

    @FXML
    private TextField relevanceOriginId;
    @FXML
    private TextField relevanceDestinyId;
    @FXML
    ChoiceBox<SemanticPath> choicesRelevance;
    @FXML
    private TableView searchTable;
    @FXML
    private TableColumn<RelevanceModel, String> oIdColumn;
    @FXML
    private TableColumn<RelevanceModel, String> oNameColumn;
    @FXML
    private TableColumn<RelevanceModel, String> dIdColumn;
    @FXML
    private TableColumn<RelevanceModel, String> dNameColumn;
    @FXML
    private TableColumn<RelevanceModel, String> relevanceColumn;

    @FXML
    private void relevanceSearchAction() {
        Stage stage = (Stage) choicesRelevance.getScene().getWindow();
        SemanticPath rel = choicesRelevance.getValue();
        if (rel == null) {
            launchAlert("Has de seleccionar un camí semàntic per a fer la cerca");
        } else {
            NodeType originType = rel.getInitialType();
            NodeType destinyType = rel.getFinalType();

            GraphSearch gs;
            if (relevanceOriginId.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Operació costosa");
                alert.setHeaderText("Estàs segur de fer una cerca lliure?");
                alert.setContentText("Una cerca lliure, sense origen ni destí, és una tasca molt costosa pot afectar al rendiment de l'ordinador.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                    gs = new FreeSearch(graph, rel.getPath());
                else
                    return;
            } else {
                int originId = Integer.parseInt(relevanceOriginId.getText());
                Node originNode = null;
                try {
                    originNode = graph.getNode(originType, originId);
                } catch (GraphException e) {
                    launchAlert("No s'ha pogut trobar cap node inicial amb el valor introduït.");
                    return;
                }
                if (relevanceDestinyId.getText().isEmpty()) {
                    gs = new OriginSearch(graph, rel.getPath(), originNode);
                } else {
                    int destinyId = Integer.parseInt(relevanceDestinyId.getText());
                    Node destintyNode = null;
                    try {
                        destintyNode = graph.getNode(destinyType, destinyId);
                    } catch (GraphException e) {
                        launchAlert("No s'ha pogut trobar cap node de destí amb el valor introduït.");
                        return;
                    }
                    gs = new OriginDestinationSearch(graph, rel.getPath(), originNode, destintyNode);
                }

            }

            gs.search();
            ArrayList<GraphSearch.Result> results = gs.getResults();
            ObservableList<RelevanceModel> res = FXCollections.observableArrayList();
            res.addAll(results.stream().map(r -> new RelevanceModel(r.from, r.to, r.hetesim)).collect(Collectors.toList()));
            searchTable.setItems(res);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choicesRelevance.setItems(semanticPaths);
        choicesRelevance.setConverter(new StringConverter<SemanticPath>() {
            @Override
            public String toString(SemanticPath path) {
                if (path == null) return null;
                return path.getName();
            }

            @Override
            public SemanticPath fromString(String string) {
                return null;
            }
        });
        searchTable.setRowFactory(param -> {
            TableRow<RelevanceModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SemanticPath rel = choicesRelevance.getValue();
                    NodeType originType = rel.getInitialType();
                    NodeType destinyType = rel.getFinalType();
                    RelevanceModel model = row.getItem();
                    GraphController gc = new GraphController(graph);
                    try {
                        gc.getShortestPath(model.getOrigin(), originType, model.getDestiny(), destinyType);
                    } catch (GraphException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        oIdColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getOrigin().getId())));
        oNameColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getOrigin().getValue())));
        dIdColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getDestiny().getId())));
        dNameColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getDestiny().getValue())));
        relevanceColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getRelevance())));
    }

}
