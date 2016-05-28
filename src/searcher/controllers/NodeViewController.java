package searcher.controllers;

import common.domain.Node;
import common.domain.NodeType;
import common.domain.Relation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import searcher.Utils;
import searcher.models.NodeModel;
import searcher.models.NodeModelRelated;

import java.net.URL;
import java.util.ResourceBundle;

public class NodeViewController extends BaseController {
    private NodeModel model;
    private GraphController graphController;
    private IntegerProperty distProperty;
    private ObservableList<NodeModelRelated> nodesWithEdge;

    @FXML private VBox paneEdges;
    @FXML private AnchorPane anchorGraph;
    @FXML private Slider sliderDist;
    @FXML private SwingNode nodeGraph;

    @FXML private TableView<NodeModelRelated> tableEdges;
    @FXML private TableColumn<NodeModelRelated, String> columnId;
    @FXML private TableColumn<NodeModelRelated, String> columnName;
    @FXML private TableColumn<NodeModelRelated, String> columnType;
    @FXML private TableColumn<NodeModelRelated, String> columnEdge;

    @FXML private AnchorPane paneAddEdge;

    @FXML private AnchorPane paneToolsEdge;
    @FXML private TextField textFilter;
    @FXML private Button buttonFilter;
    @FXML private Button buttonDelete;
    @FXML private Button buttonNew;

    public void setModel(NodeModel model) {
        this.model = model;
        loadGraph((int) (sliderDist.getValue()));
        nodesWithEdge = this.getAllEdges();
        tableEdges.setItems(nodesWithEdge);
    }

    private void loadGraph(int dist) {
        graphController = new GraphController(graph);
        SwingNode newNodeGraph = graphController.getGraph(model, dist);
        anchorGraph.getChildren().remove(nodeGraph);
        anchorGraph.getChildren().add(newNodeGraph);

        AnchorPane.setBottomAnchor(newNodeGraph, 0.0);
        AnchorPane.setLeftAnchor(newNodeGraph, 0.0);
        AnchorPane.setRightAnchor(newNodeGraph, 0.0);
        AnchorPane.setTopAnchor(newNodeGraph, 0.0);

        nodeGraph = newNodeGraph;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nodeGraph = null;
        distProperty = new SimpleIntegerProperty((int) sliderDist.getValue());
        sliderDist.valueProperty().addListener((o, ov, nv) -> distProperty.set((int)(nv.doubleValue()+0.5)));
        distProperty.addListener((o, ov, nv) -> loadGraph(nv.intValue()));

        columnId.setCellValueFactory(cv -> new ReadOnlyStringWrapper(Integer.toString(cv.getValue().getNode().getId())));
        columnName.setCellValueFactory(cv -> new ReadOnlyStringWrapper(cv.getValue().getNode().getValue()));
        columnType.setCellValueFactory(cv -> new ReadOnlyStringWrapper(Utils.getName(cv.getValue().getNodeType())));
        columnEdge.setCellValueFactory(cv -> new ReadOnlyStringWrapper(cv.getValue().getEdge().getValue()));

        paneEdges.getChildren().remove(paneAddEdge);

        buttonFilter.setOnAction(event -> enableFilter());
        textFilter.textProperty().addListener((o, ov, nv) -> filterList(nv));
        buttonDelete.setOnAction(event -> deleteSelectedEdge());
        buttonNew.setOnAction(event -> openNewEdgeDialog());
    }

    private void openNewEdgeDialog() {
        textFilter.clear();
        paneEdges.getChildren().remove(paneToolsEdge);
        paneEdges.getChildren().add(paneAddEdge);
    }

    private void deleteSelectedEdge() {
        NodeModelRelated nmr = tableEdges.getSelectionModel().getSelectedItem();
        Relation relation = nmr.getEdge();
        Node selected = nmr.getNode();
        try {
            graph.removeEdge(relation.getId(), model.getNode(), selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setModel(model);
        filterList(textFilter.getText());
    }

    private void enableFilter() {
        textFilter.clear();
        textFilter.setVisible(true);
        buttonFilter.setText("\u2715");
        buttonFilter.setOnAction(event -> disableFilter());
    }

    private void disableFilter() {
        textFilter.clear();
        textFilter.setVisible(false);
        buttonFilter.setText(new String(Character.toChars(0x1F50E)));
        buttonFilter.setOnAction(event -> enableFilter());
    }

    private void filterList(String term) {
        if (term.isEmpty()) {
            tableEdges.setItems(nodesWithEdge);
        } else {
            tableEdges.setItems(nodesWithEdge.filtered(
                    nmr -> nmr.getNode().getValue().toLowerCase().matches(".*" + term.toLowerCase() + ".*"))
            );
        }
    }

    private ObservableList<NodeModelRelated> getAllEdges() {
        ObservableList<NodeModelRelated> ret = FXCollections.observableArrayList();
        int originId = model.getNode().getId();
        NodeType originType = model.getNodeType();
        Node originNode = model.getNode();

        try {
            for (Relation rel : edgeTypes) {
                NodeType destType;
                if (rel.getNodeTypeA() == originType || rel.getNodeTypeB() == originType) {
                    if (rel.getNodeTypeA() == originType) {
                        destType = rel.getNodeTypeB();
                    } else {
                        destType = rel.getNodeTypeA();
                    }
                    for (Node node : graph.getEdges(rel.getId(), originNode)) {
                        ret.add(new NodeModelRelated(node, destType, rel));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
