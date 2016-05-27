package searcher.controllers;

import common.domain.Graph;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import searcher.models.SemanticPath;
import searcher.persistence.ExtendedPersistenceController;


public abstract class BaseController implements Initializable {

    protected static Graph graph;
    protected static ExtendedPersistenceController pc;
    protected static ObservableList<SemanticPath> semanticPaths;

    public void setGraph(Graph g) {
        graph = g;
    }

    public void setPc(ExtendedPersistenceController pc) {
        this.pc = pc;
    }

    public void setSemanticPaths(ObservableList<SemanticPath> semanticPaths) {
        this.semanticPaths = semanticPaths;
    }
}
