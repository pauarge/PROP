package searcher.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import searcher.controllers.BaseController;

import java.net.URL;
import java.util.ResourceBundle;


public class RelevanceController extends BaseController {

    @FXML
    private TextField relevanceText;
    @FXML
    ChoiceBox choicesRelevance;

    @FXML
    private void relevanceSearchAction() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choicesRelevance.setItems(semanticPaths);
    }

}
