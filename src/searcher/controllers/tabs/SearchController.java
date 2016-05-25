package searcher.controllers.tabs;

import common.domain.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import searcher.controllers.BaseController;
import searcher.controllers.GraphController;
import searcher.models.TableNode;

import java.net.URL;
import java.util.ResourceBundle;

import static searcher.Utils.launchAlert;


public class SearchController extends BaseController {

    @FXML private ChoiceBox choicesSearch;
    @FXML private TextField searchText;
    @FXML private TableView<TableNode> searchTable;
    @FXML private AnchorPane anchorPane;
    @FXML private Button searchButton;

    private void searchNodeAction() {
        if (choicesSearch.getValue() == null) {
            launchAlert((Stage) anchorPane.getScene().getWindow(), "Per fer la cerca, selecciona un tipus de node");
            return;
        }
        NodeType nt = NodeType.valueOf((String) choicesSearch.getValue());
        String v = searchText.getText();
        SimpleSearch ss = new SimpleSearch(graph, nt, v);
        ss.search();
        ObservableList<TableNode> data = searchTable.getItems();
        data.clear();
        for (GraphSearch.Result r : ss.getResults()) {
            data.add(new TableNode(r.from.getId(), (String) choicesSearch.getValue(), r.from.getValue()));
        }
    }

    private void printAllNodes() {
        if (choicesSearch.getValue() == null) {
            launchAlert((Stage) anchorPane.getScene().getWindow(), "Per fer la cerca, selecciona un tipus de node");
            return;
        }

        NodeType nt = NodeType.valueOf((String) choicesSearch.getValue());
        Container.ContainerIterator it = graph.getNodeIterator(nt);
        ObservableList<TableNode> data = searchTable.getItems();

        while (it.hasNext()) {
            Node node = (Node) it.next();
            data.add(new TableNode(node.getId(), (String) choicesSearch.getValue(), node.getValue()));
        }
    }

    private void switchSearchButtonBehavior(boolean isEmpty) {
        if (isEmpty) {
            searchButton.setOnAction(event -> printAllNodes());
            searchButton.setText("Mostra tots");
        } else {
            searchButton.setOnAction(event -> searchNodeAction());
            searchButton.setText("Cerca");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTable.setRowFactory(tv -> {
            TableRow<TableNode> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TableNode rowData = row.getItem();
                    GraphController gc = new GraphController(graph);
                    gc.execute(rowData.getNumericId(), rowData.getValue(), rowData.getType(), 2);
                }
            });
            return row;
        });

        searchText.textProperty().addListener((o, ov, nv) -> switchSearchButtonBehavior(nv.isEmpty()));
        switchSearchButtonBehavior(true);
    }

}
