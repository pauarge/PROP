package searcher.controllers.popups;

import common.domain.NodeType;
import common.domain.Relation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import searcher.Utils;
import searcher.controllers.BaseController;

import java.net.URL;
import java.util.ResourceBundle;

public class EdgesViewController extends BaseController {
    @FXML private TextField textName;
    @FXML private ChoiceBox<NodeType> choiceNodeA;
    @FXML private ChoiceBox<NodeType> choiceNodeB;
    @FXML private Button buttonNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonNew.setOnAction(event -> addEdge());
        choiceNodeA.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choiceNodeB.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choiceNodeA.setConverter(Utils.nodeTypeStringConverter());
        choiceNodeB.setConverter(Utils.nodeTypeStringConverter());
    }

    private void addEdge() {
        Relation edge = new Relation(choiceNodeA.getValue(), choiceNodeB.getValue(), textName.getText());
        graph.addRelation(edge);
        edgeTypes.add(edge);
        ((Stage)(textName.getScene().getWindow())).close();
    }
}
