package searcher.controllers.tabs;

import common.domain.NodeType;
import common.domain.Relation;
import common.domain.RelationStructure;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RelationsController extends BaseController {

    @FXML private TableView<SemanticPath> pathList;
    @FXML private TableColumn<SemanticPath, String> pathNameColumn;
    @FXML private Label pathName;
    @FXML private Label pathSummary;
    @FXML private Button buttonBotLeft;
    @FXML private Button buttonBotRight;

    @FXML private GridPane pathBuildingButtons;
    @FXML private Button buttonAuthor;
    @FXML private Button buttonPaper;
    @FXML private Button buttonConf;
    @FXML private Button buttonTerm;
    @FXML private Button buttonLabel;
    @FXML private Button buttonClear;
    @FXML private TextField addNameField;
    private ObservableList<NodeType> newPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        pathList.setItems(semanticPaths);
        showPathDetails(null);
        pathList.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPathDetails(newValue))
        );

        buttonAuthor.setOnAction(event -> addToPathBuilder(NodeType.AUTHOR));
        buttonPaper.setOnAction(event -> addToPathBuilder(NodeType.PAPER));
        buttonConf.setOnAction(event -> addToPathBuilder(NodeType.CONF));
        buttonTerm.setOnAction(event -> addToPathBuilder(NodeType.TERM));
        buttonLabel.setOnAction(event -> addToPathBuilder(NodeType.LABEL));
        buttonClear.setOnAction(event -> clearPathBuilder());

        switchToBrowse();
        newPath = FXCollections.observableArrayList();
        newPath.addListener((ListChangeListener<? super NodeType>)
                c -> pathSummary.setText(Utils.convertToText(newPath.toArray(new NodeType[0])))
        );
    }

    private void clearPathBuilder() {
        setTypeButtonPaperRestriction(true);
        buttonPaper.setDisable(false);
        newPath.clear();
        addNameField.clear();
    }

    private void setTypeButtonPaperRestriction(boolean paper) {
        buttonAuthor.setDisable(!paper);
        buttonConf.setDisable(!paper);
        buttonPaper.setDisable(paper);
        buttonTerm.setDisable(!paper);
        buttonLabel.setDisable(!paper);
    }

    private void addToPathBuilder(NodeType nt) {
        switch (nt) {
            case AUTHOR:
            case CONF:
            case TERM:
            case LABEL:
                setTypeButtonPaperRestriction(false);
                break;
            case PAPER:
                setTypeButtonPaperRestriction(true);
                break;
        }
        newPath.add(nt);
    }

    private void showPathDetails(SemanticPath semanticPath) {
        if (semanticPath == null) {
            pathName.setText("");
            pathSummary.setText("");
        } else {
            pathName.setText(semanticPath.getName());
            String path = Utils.convertToText(semanticPath.toTypeArray());
            pathSummary.setText(path);
        }
    }

    private void switchToBrowse() {
        pathBuildingButtons.setVisible(false);
        addNameField.setVisible(false);
        pathList.setDisable(false);
        buttonBotLeft.setText("Esborra");
        buttonBotLeft.setOnAction(event -> handleDeletePath());
        buttonBotRight.setText("Nou");
        buttonBotRight.setOnAction(event -> handleNewPath());
        showPathDetails(null);
    }

    private void switchToAdd() {
        pathBuildingButtons.setVisible(true);
        addNameField.setVisible(true);
        pathList.setDisable(true);
        buttonBotLeft.setText("Enrere");
        buttonBotLeft.setOnAction(event -> handleCancelNewPath());
        buttonBotRight.setText("Afegeix");
        buttonBotRight.setOnAction(event -> handleAddNewPath());
        showPathDetails(null);
    }

    private void handleDeletePath() {
        int i = pathList.getSelectionModel().getSelectedIndex();
        if (i >= 0) semanticPaths.remove(i);
    }

    private void handleNewPath() {
        switchToAdd();
        clearPathBuilder();
    }

    private void handleAddNewPath() {
        if (!addNameField.getText().isEmpty() && newPath.size() > 1) {
            NodeType prv = newPath.get(0);
            ArrayList<Relation> alr = new ArrayList<>();
            for (int i = 1; i < newPath.size(); ++i) {
                alr.add(Utils.getDefaultRelation(prv, newPath.get(i)));
                prv = newPath.get(i);
            }
            RelationStructure rs = null;
            try {
                rs = new RelationStructure(newPath.get(0), alr, newPath.get(newPath.size() - 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            SemanticPath sp = new SemanticPath(
                    addNameField.getText(), newPath.get(0), newPath.get(newPath.size() - 1), rs
            );
            semanticPaths.add(sp);
            clearPathBuilder();
            switchToBrowse();
        }
    }

    private void handleCancelNewPath() {
        switchToBrowse();
    }
}
