package searcher.persistence;


import common.domain.Graph;
import common.persistence.PersistenceController;
import searcher.models.SemanticPath;

public class ExtendedPersistenceController extends PersistenceController {

    private SemanticPath sp = null;

    public ExtendedPersistenceController(Graph graph) {
        super(graph);
    }

    public ExtendedPersistenceController(Graph graph, SemanticPath sp) {
        super(graph);
        this.sp = sp;
    }

    public void importSemanticPaths() {

    }

    public void exportSemanticPaths() {

    }

}
