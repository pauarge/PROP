package searcher.controllers.tabs;

import common.domain.NodeType;
import common.domain.RelationStructure;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ResourceBundle;

import static searcher.Utils.toTypeArray;

public class RelationsController extends BaseController {

    @FXML private TableView<SemanticPath> pathList;
    @FXML private TableColumn<SemanticPath, String> pathNameColumn;
    @FXML private Label pathName;
    @FXML private Label pathSummary;
    @FXML private Button buttonBotLeft;
    @FXML private Button buttonBotRight;

    @FXML private Button buttonPrev;
    @FXML private Button buttonNext;
    @FXML private Label labelPrev;
    @FXML private Label labelEdge;
    @FXML private Label labelNext;
    int shownRelation = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        pathList.setItems(semanticPaths);
        showPathDetails(null);
        pathList.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPathDetails(newValue))
        );

        switchToBrowse();

    }



    private void showPathDetails(SemanticPath semanticPath) {
        if (semanticPath == null) {
            pathName.setText("");
            pathSummary.setText("");
        } else {
            pathName.setText(semanticPath.getName());
            String path = Utils.convertToText(Utils.toTypeArray(semanticPath));
            pathSummary.setText(path);
            updatePathBrowser(semanticPath);
        }
    }

    private void updatePathBrowser(NodeType first, RelationStructure path) {
        NodeType[] types = new NodeType[path.size() + 1];
        NodeType prev = types[0] = first;
        for (int i = 0; i < path.size(); ++i) {
            if (path.get(i).getNodeTypeA() == prev) {
                prev = path.get(i).getNodeTypeB();
            } else {
                prev = path.get(i).getNodeTypeA();
            }
            types[i + 1] = prev;
        }


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
        showPathDetails(null);
    }

    private void switchToAdd() {
        pathList.setDisable(true);
        buttonBotLeft.setText("Enrere");
        buttonBotLeft.setOnAction(event -> handleCancelNewPath());
        buttonBotRight.setText("Afegeix");
        buttonBotRight.setOnAction(event -> handleAddNewPath());
        showPathDetails(null);
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
