package searcher;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;


public final class Utils {

    static public void closeWindow(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sortida del programa");
        alert.setHeaderText("Estàs segur de tancar el programa?");
        alert.setContentText("Recorda que tots els canvis que no hagis guardat es perdràn si continues.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        }
    }

    static public void launchAlert(Stage stage, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerta");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

}
