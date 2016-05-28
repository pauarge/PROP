package searcher.controllers.tabs;

import common.domain.Container;
import common.domain.Node;
import common.domain.NodeType;
import common.domain.SimpleSearch;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.controllers.NodeViewController;
import searcher.models.NodeModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SearchController extends BaseController {
    @FXML private AnchorPane root;

    @FXML private TextField textSearchField;
    @FXML private Button buttonSearch;

    @FXML private CheckBox checkAuthors;
    @FXML private CheckBox checkConfs;
    @FXML private CheckBox checkLabels;
    @FXML private CheckBox checkPapers;
    @FXML private CheckBox checkTerms;

    @FXML private Button buttonDetails;

    @FXML private TableView<NodeModel> tableResults;
    @FXML private TableColumn<NodeModel, String> idColumn;
    @FXML private TableColumn<NodeModel, String> nameColumn;
    @FXML private TableColumn<NodeModel, String> typeColumn;
    @FXML private Label placeHolder;

    private ObservableList<NodeModel> filterNodes(String filter, NodeType type) {
        ObservableList<NodeModel> ret = FXCollections.observableArrayList();
        if (filter.isEmpty()) {
            Container.ContainerIterator it = graph.getNodeIterator(type);
            while (it.hasNext()) ret.add(new NodeModel((Node) it.next(), type));
        } else {
            SimpleSearch ss = new SimpleSearch(graph, type, filter);
            ss.search();
            ss.getResults().stream().forEach(r -> ret.add(new NodeModel(r.from, type)));
        }
        return ret;
    }

    private ObservableList<NodeModel> filterNodes(String filter, List<NodeType> types) {
        ObservableList<NodeModel> ret = FXCollections.observableArrayList();
        for (NodeType nt : types) {
            ret.addAll(filterNodes(filter, nt));
        }
        return ret;
    }

    private void handleSearch() {
        ArrayList<NodeType> nodeTypes = new ArrayList<>();
        if (checkAuthors.isSelected()) nodeTypes.add(NodeType.AUTHOR);
        if (checkConfs.isSelected()) nodeTypes.add(NodeType.CONF);
        if (checkPapers.isSelected()) nodeTypes.add(NodeType.PAPER);
        if (checkLabels.isSelected()) nodeTypes.add(NodeType.LABEL);
        if (checkTerms.isSelected()) nodeTypes.add(NodeType.TERM);

        ObservableList<NodeModel> result = filterNodes(textSearchField.getText(), nodeTypes);
        tableResults.setItems(result);
        placeHolder.setText("No hi ha resultats");
    }

    private void switchSearchButtonText(boolean isEmpty) {
        if (isEmpty) buttonSearch.setText("Mostra tots");
        else buttonSearch.setText("Cerca");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableResults.setRowFactory(this::graphEnabledRow);
        idColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(String.valueOf(cv.getValue().getNode().getId())));
        nameColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(cv.getValue().getNode().getValue()));
        typeColumn.setCellValueFactory(cv -> new ReadOnlyStringWrapper(Utils.getName(cv.getValue().getNodeType())));

        textSearchField.textProperty().addListener((o, ov, nv) -> switchSearchButtonText(nv.isEmpty()));
        buttonSearch.setOnAction(e -> handleSearch());
        textSearchField.setOnAction(e -> handleSearch());

        tableResults.getSelectionModel().selectedItemProperty().addListener(
                (o, ov, nv) -> buttonDetails.setDisable(nv == null)
        );
        buttonDetails.setOnAction(e -> openNodeDetails(tableResults.getSelectionModel().getSelectedItem()));
    }

    private TableRow<NodeModel> graphEnabledRow(TableView<NodeModel> tv) {
        TableRow<NodeModel> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!row.isEmpty())) {
                NodeModel model = row.getItem();
                openNodeDetails(model);
            }
        });
        return row;
    }

    private boolean openNodeDetails(NodeModel model) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(searcher.Main.class.getResource("layouts/node.fxml"));
            AnchorPane pane = loader.load();

            Stage nodeInfoStage = new Stage();
            nodeInfoStage.setTitle(model.getNode().getValue() + " - " + Utils.getName(model.getNodeType()));
            nodeInfoStage.initModality(Modality.WINDOW_MODAL);
            nodeInfoStage.initOwner(null);
            Scene scene = new Scene(pane);
            nodeInfoStage.setScene(scene);

            NodeViewController controller = loader.getController();
            controller.setModel(model);

            root.setDisable(true);
            nodeInfoStage.showAndWait();
            root.setDisable(false);
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
}
