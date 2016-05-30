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
import java.util.*;

public class RelationsController extends BaseController {

    @FXML private TableView<SemanticPath> pathList;
    @FXML private TableColumn<SemanticPath, String> pathNameColumn;
    @FXML private Label pathName;
    @FXML private TextFlow pathSummary;
    @FXML private Button buttonBotLeft;
    @FXML private Button buttonBotRight;

    @FXML private GridPane pathBrowser;
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
    @FXML private TextField addNameField;

    private int shownRelation = 0;
    private NodeType builderType;
    private ArrayList<Relation> builderPath;
    private boolean builderMode = false;

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
        choiceType.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> updateRelations());
        choiceEdge.setConverter(Utils.relationStringConverter());
        choiceEdge.getSelectionModel().selectedItemProperty().addListener(
                (o, ov, nv) -> buttonAddEdge.setDisable((nv == null))
        );
        builderPath = new ArrayList<Relation>();

        addNameField.textProperty().addListener((o, ov, nv) -> updateAddButtonStatus());

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
                || r.getNodeTypeA() == type && r.getNodeTypeB() == lastType
        );
        choiceEdge.setItems(relations);
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

    private void showPathDetails(String name, NodeType firstType, ArrayList<Relation> path) {
        shownRelation = 0;
        if (name == null) {
            pathName.setText("");
            pathSummary.setVisible(false);
            pathBrowser.setVisible(false);
        } else {
            pathName.setText(name);
            pathSummary.setVisible(true);
            pathBrowser.setVisible(true);
            updatePathBrowser(firstType, path);
        }
    }

    private void updatePathSummary(NodeType firstNode, ArrayList<Relation> path) {
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
        pathSummary.getChildren().add(new Text(""));
    }

    private void updatePathBrowser(NodeType first, ArrayList<Relation> path) {
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
        builderMode = false;
        pathList.setDisable(false);
        buttonBotLeft.setText("Esborra");
        buttonBotLeft.setOnAction(event -> handleDeletePath());
        buttonBotRight.setText("Nou");
        buttonBotRight.setOnAction(event -> switchToAdd());
        showPathDetails(pathList.getSelectionModel().getSelectedItem());
        showPathDetails(null);
        paneEdges.setVisible(false);
        addNameField.clear();
        addNameField.setVisible(false);

        buttonPrev.setOnAction(event -> handlePrevRelation());
        buttonNext.setOnAction(event -> handleNextRelation());
    }

    private void switchToAdd() {
        builderMode = true;
        pathList.setDisable(true);
        buttonBotLeft.setText("Enrere");
        buttonBotLeft.setOnAction(event -> switchToBrowse());
        buttonBotLeft.setDisable(false);
        buttonBotRight.setText("Afegeix");
        buttonBotRight.setOnAction(event -> handleAddNewPath());
        buttonBotRight.setDisable(true);
        showPathDetails(null);
        paneEdges.setVisible(true);
        labelToHide.setVisible(false);
        choiceEdge.setVisible(false);
        buttonAddEdge.setOnAction(event -> handleFirstEdge());
        buttonAddEdge.setDisable(false);
        builderPath.clear();
        shownRelation = 0;
        addNameField.setVisible(true);

        buttonPrev.setOnAction(event -> handlePrevRelationBuilder());
        buttonNext.setOnAction(event -> handleNextRelationBuilder());
        choiceType.getSelectionModel().selectFirst();
    }

    private void handleNextRelationBuilder() {
        ++shownRelation;
        updatePathBrowser(builderType, builderPath);
    }

    private void handlePrevRelationBuilder() {
        --shownRelation;
        updatePathBrowser(builderType, builderPath);
    }

    private void handleFirstEdge() {
        builderType = choiceType.getValue();
        pathSummary.setVisible(true);
        pathSummary.getChildren().clear();
        pathSummary.getChildren().add(new Text(Utils.getName(builderType)));
        buttonAddEdge.setOnAction(event -> handleNextEdge());
        labelToHide.setVisible(true);
        choiceEdge.setVisible(true);
        updateRelations();
        buttonAddEdge.setDisable(true);
        updateTypes();
    }

    private void handleNextEdge() {
        builderPath.add(choiceEdge.getValue());
        showPathDetails("", builderType, builderPath);
        updateRelations();
        updateAddButtonStatus();
        updateTypes();
    }

    private void updateTypes() {
        NodeType current;
        if (builderPath.isEmpty()) {
            current = builderType;
        } else {
            NodeType[] types = Utils.toTypeArray(builderType, builderPath);
            current = types[types.length-1];
        }

        HashSet<NodeType> targets = new HashSet<>();
        for (Relation r : edgeTypes) {
            if (r.getNodeTypeA() == current) targets.add(r.getNodeTypeB());
            if (r.getNodeTypeB() == current) targets.add(r.getNodeTypeA());
        }
        choiceType.setItems(FXCollections.observableArrayList(targets));
        choiceType.getSelectionModel().selectFirst();
    }

    private void handleDeletePath() {
        SemanticPath semanticPath = pathList.getSelectionModel().getSelectedItem();
        semanticPaths.remove(semanticPath);
    }

    private void handleAddNewPath() {
        NodeType[] types = Utils.toTypeArray(builderType, builderPath);
        NodeType lastType = types[types.length-1];
        RelationStructure structure = null;
        try {
            structure = new RelationStructure(builderType, builderPath, lastType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SemanticPath semanticPath = new SemanticPath(addNameField.getText(), builderType, lastType, structure);
        semanticPaths.add(semanticPath);
        switchToBrowse();
    }

    private void updateAddButtonStatus() {
        if (builderMode) {
            buttonBotRight.setDisable(addNameField.getText().isEmpty() || builderPath.isEmpty());
        }
    }
}
