package searcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Application {

    private String APP_TITLE = "Cercador Relacional";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/root_layout.fxml"));
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-tui")) {
            TextController textController = new TextController();

            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine;
            String outputLine;
            System.out.println("Projecte de prop del grup 18-?. Escriu 'help' per l'ajuda, o 'quit' per sortir.");
            try {
                System.out.print(">");
                while ((readLine = bufferedReader.readLine()) != null) {
                    outputLine = textController.executeLine(readLine);
                    if (outputLine != null) {
                        System.out.println(outputLine);
                    }
                    if (textController.isReadyToQuit()) {
                        System.exit(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            launch(args);
        }
    }

}
