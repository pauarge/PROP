package searcher.controllers.tabs;

import common.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.controllers.GraphController;
import searcher.models.NodeModel;

import java.net.URL;
import java.util.ResourceBundle;


public class SearchController extends BaseController {

    @FXML private ChoiceBox<NodeType> choicesSearch;
    @FXML private TextField searchText;
    @FXML private TableView<NodeModel> searchTable;
    @FXML private AnchorPane anchorPane;
    @FXML private Button searchButton;

    private void searchNodeAction() {
        NodeType nt = choicesSearch.getValue();
        String v = searchText.getText();
        SimpleSearch ss = new SimpleSearch(graph, nt, v);
        ss.search();
        ObservableList<NodeModel> data = searchTable.getItems();
        data.clear();
        for (GraphSearch.Result r : ss.getResults()) {
            data.add(new NodeModel(r.from.getId(), Utils.getName(nt), r.from.getValue()));
        }

    }

    private void printAllNodes() {
        NodeType nt = choicesSearch.getValue();
        Container.ContainerIterator it = graph.getNodeIterator(nt);
        ObservableList<NodeModel> data = searchTable.getItems();
        data.clear();

        while (it.hasNext()) {
            Node node = (Node) it.next();
            data.add(new NodeModel(node.getId(), Utils.getName(nt), node.getValue()));
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
            TableRow<NodeModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    NodeModel rowData = row.getItem();
                    GraphController gc = new GraphController(graph);
                    gc.execute(rowData.getNumericId(), rowData.getValue(), rowData.getType(), 2);
                }
            });
            return row;
        });

        choicesSearch.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choicesSearch.getSelectionModel().selectFirst();
        choicesSearch.setConverter(Utils.getNodeTypeStringConverter());

        searchText.textProperty().addListener((o, ov, nv) -> switchSearchButtonBehavior(nv.isEmpty()));
        switchSearchButtonBehavior(true);
        searchText.setOnAction(event -> searchButton.fire());
    }

}
