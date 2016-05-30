package searcher.controllers.popups;

import common.domain.Container;
import common.domain.Node;
import common.domain.NodeType;
import common.domain.Relation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.controllers.GraphController;
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
    @FXML private ChoiceBox<NodeType> choiceType;
    @FXML private ChoiceBox<Relation> choiceRelation;
    @FXML private ComboBox<Node> comboNode;
    @FXML private Button buttonAdd;
    @FXML private Button buttonBack;
    private ObservableList<Node> comboFullList;

    @FXML private AnchorPane paneToolsEdge;
    @FXML private TextField textFilter;
    @FXML private Button buttonFilter;
    @FXML private Button buttonDelete;
    @FXML private Button buttonNew;

    public void setModel(NodeModel model) {
        this.model = model;
        loadGraph();
        nodesWithEdge = this.getAllEdges();
        tableEdges.setItems(nodesWithEdge);
    }

    private void loadGraph() {
        int dist = distProperty.get();
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
        distProperty.addListener((o, ov, nv) -> loadGraph());

        columnId.setCellValueFactory(cv -> new ReadOnlyStringWrapper(Integer.toString(cv.getValue().getNode().getId())));
        columnName.setCellValueFactory(cv -> new ReadOnlyStringWrapper(cv.getValue().getNode().getValue()));
        columnType.setCellValueFactory(cv -> new ReadOnlyStringWrapper(Utils.getName(cv.getValue().getNodeType())));
        columnEdge.setCellValueFactory(cv -> new ReadOnlyStringWrapper(cv.getValue().getEdge().getValue()));

        paneEdges.getChildren().remove(paneAddEdge);

        buttonFilter.setOnAction(event -> enableFilter());
        textFilter.textProperty().addListener((o, ov, nv) -> filterList(nv));
        buttonDelete.setOnAction(event -> deleteSelectedEdge());
        buttonNew.setOnAction(event -> openNewEdgeDialog());

        choiceRelation.setConverter(Utils.relationStringConverter());
        choiceRelation.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> updateCombo());
        choiceType.valueProperty().addListener((o, ov, nv) -> updateRelations());
        choiceType.setItems(FXCollections.observableArrayList(NodeType.class.getEnumConstants()));
        choiceType.setConverter(Utils.nodeTypeStringConverter());
        comboNode.setEditable(false);
        comboNode.setConverter(new StringConverter<Node>() {
            @Override
            public String toString(Node node) {
                if (node == null) return "";
                return node.getValue();
            }

            @Override
            public Node fromString(String string) {
                return null;
            }
        });
        //comboNode.getEditor().textProperty().addListener((o, ov, nv) ->  filterCombo());
        comboNode.valueProperty().addListener((o, ov, nv) -> buttonAdd.setDisable(nv == null));
        buttonAdd.setOnAction(event ->  addEdge());
        buttonBack.setOnAction(event -> backFromAddEdge());

    }

    private void backFromAddEdge() {
        choiceType.setValue(null);
        choiceRelation.setValue(null);
        comboNode.setValue(null);
        comboNode.hide();
        paneEdges.getChildren().remove(paneAddEdge);
        paneEdges.getChildren().add(paneToolsEdge);
    }

    private void addEdge() {
        int rid = choiceRelation.getValue().getId();
        Node node = comboNode.getValue();
        try {
            graph.addEdge(rid, model.getNode(), node);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableEdges.getItems().add(new NodeModelRelated(node, choiceType.getValue(), choiceRelation.getValue()));
        loadGraph();
        backFromAddEdge();
    }

    private void filterCombo() {
        String filter = comboNode.getEditor().getText();
        if (filter.isEmpty()) {
            comboNode.setItems(comboFullList);
        } else {
            comboNode.setItems(comboFullList.filtered(
                    node -> node.getValue().toLowerCase().matches(".*" + filter.toLowerCase() + ".*"))
            );
        }
        comboNode.show();
    }

    private void updateCombo() {
        Relation relation = choiceRelation.getValue();
        if (relation == null) {
            comboNode.setValue(null);
            comboNode.setDisable(true);
        } else {
            NodeType sourceType = model.getNodeType();
            NodeType targetType;
            if (relation.getNodeTypeA() == sourceType) {
                targetType = relation.getNodeTypeB();
            } else {
                targetType = relation.getNodeTypeA();
            }

            ObservableList<Node> nodes = FXCollections.observableArrayList();
            Container.ContainerIterator it = graph.getNodeIterator(targetType);
            while (it.hasNext()) nodes.add((Node) it.next());
            nodesWithEdge.stream().forEach(nmr -> nodes.remove(nmr.getNode()));
            comboFullList = nodes;
            comboNode.setItems(nodes);
            comboNode.setDisable(false);
        }
    }

    private void updateRelations() {
        NodeType type = choiceType.getValue();
        if (type == null) {
            choiceRelation.setValue(null);
            choiceRelation.setDisable(true);
        } else {
            FilteredList<Relation> relations;
            relations = edgeTypes.filtered(r -> r.getNodeTypeA() == type && r.getNodeTypeB() == model.getNodeType()
                    || r.getNodeTypeB() == type && r.getNodeTypeA() == model.getNodeType()
            );
            choiceRelation.setItems(relations);
            choiceRelation.setDisable(false);
        }
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

        tableEdges.getItems().remove(nmr);
        loadGraph();
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
