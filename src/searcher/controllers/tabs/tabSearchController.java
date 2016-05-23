package searcher.controllers.tabs;


import common.domain.Graph;
import common.domain.GraphSearch;
import common.domain.NodeType;
import common.domain.SimpleSearch;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import searcher.controllers.BaseController;
import searcher.models.TableNode;

import java.net.URL;
import java.util.ResourceBundle;

import static searcher.Utils.launchAlert;

public class tabSearchController extends BaseController {

    @FXML ChoiceBox choicesSearch;
    @FXML AnchorPane tabSearch;
    @FXML TextField searchText;
    @FXML TableView searchTable;

    @FXML
    private void searchNodeAction(){
        if(choicesSearch.getValue() == null){
            launchAlert((Stage) tabSearch.getScene().getWindow(), "Per fer la cerca, selecciona un tipus de node");
            return;
        }
        NodeType nt = NodeType.valueOf((String) choicesSearch.getValue());
        String v = searchText.getText();
        SimpleSearch ss = new SimpleSearch(graph, nt, v);
        ss.search();
        ObservableList<TableNode> data = searchTable.getItems();
        data.clear();
        for (GraphSearch.Result r : ss.getResults()) {
            data.add(new TableNode(r.from.getId(), (String) choicesSearch.getValue(), r.from.getValue()));
        }
    }

    public void setGraph(Graph g){
        graph = g;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
