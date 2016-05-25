package searcher.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ResourceBundle;


public class RelevanceController extends BaseController {

    @FXML
    private TextField relevanceText;
    @FXML
    ChoiceBox<SemanticPath> choicesRelevance;

    @FXML
    private void relevanceSearchAction() {

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
