package searcher.controllers;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import searcher.models.NodeModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NodeViewController extends BaseController {
    private NodeModel model;
    private GraphController graphController;

    @FXML private Tab tabGraph;
    @FXML private AnchorPane anchorGraph;

    public void setModel(NodeModel model) {
        this.model = model;
    }

    public void loadGraph() {
        graphController = new GraphController(graph);
        SwingNode swingNode = graphController.getGraph(model, 2);
        anchorGraph.getChildren().add(swingNode);
        AnchorPane.setBottomAnchor(swingNode, 0.0);
        AnchorPane.setLeftAnchor(swingNode, 0.0);
        AnchorPane.setRightAnchor(swingNode, 0.0);
        AnchorPane.setTopAnchor(swingNode, 0.0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
