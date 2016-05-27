package searcher.persistence;


import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.collections.ObservableList;
import searcher.models.SemanticPath;

import java.util.ArrayList;

import static common.persistence.FileHandler.writeFile;

public class ExtendedPersistenceController extends PersistenceController {

    private static ObservableList<SemanticPath> sp;

    public ExtendedPersistenceController(Graph graph) {
        super(graph);
    }

    public ExtendedPersistenceController(Graph graph, ObservableList<SemanticPath> sp) {
        super(graph);
        this.sp = sp;
    }

    public void exportSemanticPaths(String path) {
        ArrayList<String> strings = new ArrayList<>();
        for (SemanticPath s : sp) {
            strings.add(s.toString());
        }
        writeFile(path + "semanticPaths.txt", strings);
    }

    public void importSemanticPaths(String path) {
        System.out.println("Hello world");
    }

}
