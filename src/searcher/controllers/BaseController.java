package searcher.controllers;


import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import searcher.models.SemanticPath;

public abstract class BaseController implements Initializable {

    protected Graph graph;
    protected PersistenceController pc;
    protected ObservableList<SemanticPath> semanticPaths;

    public void setGraph(Graph g) {
        graph = g;
    }

    public void setPc(PersistenceController pc) {
        this.pc = pc;
    }

    public void setSemanticPaths(ObservableList<SemanticPath> semanticPaths) {
        this.semanticPaths = semanticPaths;
    }
}
