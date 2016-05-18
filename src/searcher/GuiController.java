package searcher;

import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;

public class GuiController {

    @FXML private Button closeButton;
    @FXML private Button importGraph;
    @FXML private Button CreateGraph;

    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openDirSelector(){
        Stage stage = (Stage) importGraph.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Select directory");
        final File selectedDirectory = dc.showDialog(stage);
        if (selectedDirectory != null) {
            System.out.println(selectedDirectory.getAbsolutePath());
        }
    }

}
