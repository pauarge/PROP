package searcher.controllers.tabs;

import common.domain.NodeType;
import common.domain.Relation;
import common.domain.RelationStructure;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ResourceBundle;

public class RelationsController extends BaseController {

    @FXML private TableView<SemanticPath> pathList;
    @FXML private TableColumn<SemanticPath, String> pathNameColumn;
    @FXML private Label pathName;
    @FXML private TextFlow pathSummary;
    @FXML private Button buttonBotLeft;
    @FXML private Button buttonBotRight;

    @FXML private GridPane pathBuilder;
    @FXML private Button buttonPrev;
    @FXML private Button buttonNext;
    @FXML private Label labelPrev;
    @FXML private Label labelEdge;
    @FXML private Label labelNext;

    @FXML private GridPane paneEdges;
    @FXML private ChoiceBox<NodeType> choiceType;
    @FXML private ChoiceBox<Relation> choiceEdge;
    @FXML private Label labelToHide;
    @FXML private Button buttonAddEdge;

    int shownRelation = 0;
    NodeType builderType;
    RelationStructure builderPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        pathList.setItems(semanticPaths);
        showPathDetails(null);
        pathList.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPathDetails(newValue))
        );

        buttonPrev.setOnAction(event -> handlePrevRelation());
        buttonNext.setOnAction(event -> handleNextRelation());

        choiceType.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choiceType.setConverter(Utils.nodeTypeStringConverter());
        choiceType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateRelations());
        choiceEdge.setConverter(Utils.relationStringConverter());

        switchToBrowse();
    }

    private void updateRelations() {
        NodeType type = choiceType.getValue();
        FilteredList<Relation> relations;
        NodeType lastType;
        if (builderPath != null && !builderPath.isEmpty()) {
            NodeType[] types = Utils.toTypeArray(builderType, builderPath);
            lastType = types[types.length - 1];
        } else {
            lastType = builderType;
        }
        relations = edgeTypes.filtered(r -> r.getNodeTypeA() == lastType && r.getNodeTypeB() == type
                || r.getNodeTypeB() == type && r.getNodeTypeA() == lastType
        );
        choiceEdge.setItems(relations);
        System.out.println("setting: " + relations.size());
        choiceEdge.setDisable(false);
        choiceEdge.getSelectionModel().selectFirst();
    }

    private void handleNextRelation() {
        ++shownRelation;
        updatePathBrowser(pathList.getSelectionModel().getSelectedItem());
    }

    private void handlePrevRelation() {
        --shownRelation;
        updatePathBrowser(pathList.getSelectionModel().getSelectedItem());
    }


    private void showPathDetails(SemanticPath semanticPath) {
        if (semanticPath == null) {
            showPathDetails(null, null, null);
        } else {
            showPathDetails(semanticPath.getName(), semanticPath.getInitialType(), semanticPath.getPath());
        }
    }

    private void showPathDetails(String name, NodeType firstType, RelationStructure path) {
        shownRelation = 0;
        if (name == null) {
            pathName.setText("");
            pathSummary.setVisible(false);
            pathBuilder.setVisible(false);
        } else {
            pathName.setText(name);
            pathSummary.setVisible(true);
            pathBuilder.setVisible(true);
            updatePathBrowser(firstType, path);
        }
    }

    private void updatePathSummary(NodeType firstNode, RelationStructure path) {
        NodeType[] types = Utils.toTypeArray(firstNode, path);
        Text prev = new Text("");
        Text current = new Text("\u21c6");
        current.setFont(Font.font(null, FontWeight.BOLD, 16.0));
        current.setUnderline(true);
        Text next = new Text("");
        for (int i = 0; i < types.length; ++i) {
            String content = Utils.getName(types[i]);
            if (i < shownRelation + 1) {
                prev.setText(prev.getText().concat(" \u21c6 " + content));
            } else if (i == shownRelation + 1) {
                prev.setText(prev.getText().concat(" "));
                next.setText(" " + content);
            } else {
                next.setText(next.getText().concat(" \u21c6 " + content));
            }
        }

        prev.setText(prev.getText().substring(3));
        pathSummary.getChildren().clear();
        pathSummary.getChildren().addAll(prev, current, next);
    }

    private void updatePathBrowser(NodeType first, RelationStructure path) {
        NodeType[] types = Utils.toTypeArray(first, path);
        labelPrev.setText(Utils.getName(types[shownRelation]));
        labelEdge.setText(path.get(shownRelation).getValue());
        labelNext.setText(Utils.getName(types[shownRelation+1]));
        buttonPrev.setDisable(shownRelation == 0);
        buttonNext.setDisable(shownRelation == path.size()-1);
        updatePathSummary(first, path);
    }

    private void updatePathBrowser(SemanticPath semanticPath) {
        updatePathBrowser(semanticPath.getInitialType(), semanticPath.getPath());
    }

    private void switchToBrowse() {
        pathList.setDisable(false);
        buttonBotLeft.setText("Esborra");
        buttonBotLeft.setOnAction(event -> handleDeletePath());
        buttonBotRight.setText("Nou");
        buttonBotRight.setOnAction(event -> handleNewPath());
        showPathDetails(pathList.getSelectionModel().getSelectedItem());
    }

    private void switchToAdd() {
        pathList.setDisable(true);
        buttonBotLeft.setText("Enrere");
        buttonBotLeft.setOnAction(event -> handleCancelNewPath());
        buttonBotRight.setText("Afegeix");
        buttonBotRight.setOnAction(event -> handleAddNewPath());
        showPathDetails(null);
        paneEdges.setVisible(true);
        labelToHide.setVisible(false);
        choiceEdge.setVisible(false);
        buttonAddEdge.setOnAction(event -> handleFirstEdge());
    }

    private void handleFirstEdge() {
        builderType = choiceType.getValue();
        pathSummary.setVisible(true);
        pathSummary.getChildren().clear();
        pathSummary.getChildren().add(new Text(Utils.getName(builderType)));
        buttonAddEdge.setOnAction(event -> handleNextEdge());
        labelToHide.setVisible(true);
        choiceEdge.setVisible(true);
    }

    private void handleNextEdge() {

    }

    private void handleDeletePath() {
        SemanticPath semanticPath = pathList.getSelectionModel().getSelectedItem();
        semanticPaths.remove(semanticPath);
    }

    private void handleNewPath() {
        switchToAdd();
    }

    private void handleAddNewPath() {

    }

    private void handleCancelNewPath() {
        switchToBrowse();
    }
}
