package searcher.controllers.popups;

import common.domain.NodeType;
import common.domain.Relation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import searcher.Utils;
import searcher.controllers.BaseController;

import java.net.URL;
import java.util.ResourceBundle;

public class EdgesViewController extends BaseController {
    @FXML private ChoiceBox<Relation> choiceRelation;
    @FXML private Label labelTypeA;
    @FXML private Label labelTypeB;

    @FXML private TextField textName;
    @FXML private ChoiceBox<NodeType> choiceNodeA;
    @FXML private ChoiceBox<NodeType> choiceNodeB;

    @FXML private Button buttonDelete;
    @FXML private Button buttonNew;
    @FXML private Button buttonClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceRelation.setItems(edgeTypes);
        choiceRelation.setConverter(Utils.relationStringConverter());
        choiceRelation.valueProperty().addListener((observable, oldValue, newValue) -> showRelation(newValue));
        choiceRelation.getSelectionModel().selectFirst();

        buttonDelete.setOnAction(event -> removeSelectedRelation());
        buttonClose.setOnAction(event -> ((Stage)(textName.getScene().getWindow())).close());
        buttonNew.setOnAction(event -> switchToAdd());

        textName.textProperty().addListener((o, ov, nv) -> buttonNew.setDisable(nv.isEmpty()));
        choiceNodeA.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choiceNodeB.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choiceNodeA.setConverter(Utils.nodeTypeStringConverter());
        choiceNodeB.setConverter(Utils.nodeTypeStringConverter());
    }

    private void switchToAdd() {
        textName.setVisible(true);
        textName.clear();
        choiceNodeA.setVisible(true);
        choiceNodeA.setValue(null);
        choiceNodeA.getSelectionModel().selectFirst();
        choiceNodeB.setVisible(true);
        choiceNodeB.setValue(null);
        choiceNodeB.getSelectionModel().selectFirst();

        buttonNew.setText("Afegeix");
        buttonNew.setDisable(true);
        buttonNew.setOnAction(event -> addEdge());
        buttonDelete.setText("Enrere");
        buttonDelete.setDisable(false);
        buttonDelete.setOnAction(event -> switchToBrowse());
    }

    private void switchToBrowse() {
        textName.setVisible(false);
        choiceNodeA.setVisible(false);
        choiceNodeB.setVisible(false);
        choiceRelation.getSelectionModel().selectFirst();

        buttonDelete.setText("Esborra");
        buttonDelete.setOnAction(event -> removeSelectedRelation());
        buttonNew.setText("Nova");
        buttonNew.setDisable(false);
        buttonNew.setOnAction(event -> switchToAdd());
    }

    private void removeSelectedRelation() {
        Relation relation = choiceRelation.getValue();
        edgeTypes.remove(relation);
        choiceRelation.getSelectionModel().selectFirst();
    }

    private void showRelation(Relation relation) {
        if (relation == null) {
            labelTypeA.setText("");
            labelTypeB.setText("");
            buttonDelete.setDisable(true);
        } else {
            labelTypeA.setText(Utils.getName(relation.getNodeTypeA()));
            labelTypeB.setText(Utils.getName(relation.getNodeTypeB()));
            buttonDelete.setDisable(relation.getId() < 6);
        }
    }

    private void addEdge() {
        Relation edge = new Relation(choiceNodeA.getValue(), choiceNodeB.getValue(), textName.getText());
        graph.addRelation(edge);
        edgeTypes.add(edge);
        switchToBrowse();
    }
}
