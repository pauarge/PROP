package searcher.controllers.tabs;

import common.domain.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import searcher.controllers.BaseController;
import searcher.models.RelevanceModel;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RelevanceController extends BaseController {

    private GraphSearch gs;
    @FXML
    private TextField relevanceOriginId;
    @FXML
    private TextField relevanceDestinyId;
    @FXML
    ChoiceBox<SemanticPath> choicesRelevance;
    @FXML
    private TableView searchTable;
    @FXML
    private TableColumn<RelevanceModel, String>  oIdColumn;
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
        SemanticPath rel = (SemanticPath) choicesRelevance.getValue();
        NodeType originType = rel.getInitialType();
        NodeType destinyType = rel.getFinalType();

        if(relevanceOriginId.getText().isEmpty()){
            gs = new FreeSearch(graph, rel.getPath());
        } else {
            int originId = Integer.parseInt(relevanceOriginId.getText());
            Node originNode = null;
            try {
                originNode = graph.getNode(originType, originId);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            if(relevanceDestinyId.getText().isEmpty()) {
                gs = new OriginSearch(graph, rel.getPath(), originNode);
            } else {
                int destinyId = Integer.parseInt(relevanceDestinyId.getText());
                Node destintyNode = null;
                try {
                    destintyNode = graph.getNode(destinyType, destinyId);
                } catch (GraphException e) {
                    e.printStackTrace();
                }
                new OriginDestinationSearch(graph, rel.getPath(), originNode, destintyNode);
            }

        }

        gs.search();
        ArrayList<GraphSearch.Result> results = gs.getResults();
        ObservableList<RelevanceModel> res = FXCollections.observableArrayList();
        for(GraphSearch.Result r : results){
            res.add(new RelevanceModel(r.from, r.to, r.hetesim));
        }
        searchTable.setItems(res);

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
        oIdColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getOrigin().getId())));
        oNameColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getOrigin().getValue())));
        dIdColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getDestiny().getId())));
        dNameColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getDestiny().getValue())));
        relevanceColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getRelevance())));
    }

}
