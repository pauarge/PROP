package searcher.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ResourceBundle;

public class RelationsController extends BaseController {
    @FXML
    private TableView<SemanticPath> pathList;
    @FXML
    private TableColumn<SemanticPath, String> pathNameColumn;
    @FXML
    private Label pathName;
    @FXML
    private Label pathSummary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        pathList.setItems(semanticPaths);
        semanticPaths.add(new SemanticPath("Prova Numero 1"));
        semanticPaths.add(new SemanticPath("Prova Numero 2"));
        semanticPaths.add(new SemanticPath("Prova Numero 3"));
        showPathDetails(null);
        pathList.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showPathDetails(newValue))
        );
    }

    private void showPathDetails(SemanticPath semanticPath) {
        if (semanticPath == null) {
            pathName.setText("");
            pathSummary.setText("");
        } else {
            pathName.setText(semanticPath.getName());
            pathSummary.setText("Falta implementar");
        }
    }

    @FXML
    private void handleDeletePath() {
        int i = pathList.getSelectionModel().getSelectedIndex();
        if (i >= 0) semanticPaths.remove(i);
    }

    @FXML
    private void handleNewPath() {
        System.err.println("new path");
    }
}
