package searcher.controllers;

import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.fxml.Initializable;


public abstract class BaseController implements Initializable {

    protected Graph graph;
    protected PersistenceController pc;

    public void setGraph(Graph g){
        graph = g;
    }

    public void setPersistenceController(PersistenceController pc){
        this.pc = pc;
    }

}
