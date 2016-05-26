package searcher.controllers.tabs;

import common.domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
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
    private void relevanceSearchAction() {
        System.out.println("Starting relevance search");

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
        StringBuilder sb = new StringBuilder();
        for (GraphSearch.Result r : gs.getResults()) {
            sb.append("From: ");
            sb.append(r.from.getValue());
            sb.append("\t\t\tTo: ");
            sb.append(r.to.getValue());
            sb.append("\t\t\tRelevance: ");
            sb.append(r.hetesim);
            sb.append('\n');
        }

        System.out.println(sb);
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
    }

}
