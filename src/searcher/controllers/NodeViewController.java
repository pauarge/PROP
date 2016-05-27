package searcher.controllers;

import searcher.models.NodeModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NodeViewController extends BaseController {
    private NodeModel model;

    public void setModel(NodeModel model) {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
