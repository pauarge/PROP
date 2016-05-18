package searcher.controllers;


        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.stage.Stage;

public class MainController {

    @FXML private Button returnRoot;

    @FXML
    public void returnRootAction() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../layouts/root.fxml"));
        Stage stage = (Stage) returnRoot.getScene().getWindow();
        stage.setTitle("FXML Welcome");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();
    }



}
