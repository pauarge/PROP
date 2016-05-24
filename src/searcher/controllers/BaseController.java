package searcher.controllers;


import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.fxml.Initializable;
import searcher.models.SemanticPath;

import java.util.HashMap;

public abstract class BaseController implements Initializable {

    protected Graph graph;
    protected PersistenceController pc;
    protected HashMap<String, SemanticPath> semanticPathMap;

    public void setGraph(Graph g) {
        graph = g;
    }

    public void setPc(PersistenceController pc) {
        this.pc = pc;
    }

    public void setSemanticPathMap(HashMap<String, SemanticPath> semanticPathMap) {
        this.semanticPathMap = semanticPathMap;
    }
}
