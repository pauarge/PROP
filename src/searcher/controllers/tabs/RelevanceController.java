package searcher.controllers.tabs;

import common.domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    ChoiceBox choicesRelevance;

    @FXML
    private void relevanceSearchAction() {
        System.out.println("Starting relevance search");
        int originId = Integer.parseInt(relevanceOriginId.getText());
        int destintyId = Integer.parseInt(relevanceDestinyId.getText());
        SemanticPath rel = (SemanticPath) choicesRelevance.getValue();
        NodeType originType = rel.getInitialType();
        NodeType destinyType = rel.getFinalType();
        try {
            Node originNode = graph.getNode(originType, originId);
            Node destintyNode = graph.getNode(destinyType, destintyId);
        } catch (GraphException e) {
            e.printStackTrace();
        }
        System.out.println("Searching " + originId + ", " + rel);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choicesRelevance.setItems(semanticPaths);
    }

}
