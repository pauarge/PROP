package searcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private static void launchTerminalInterface() {
        Controller controller = new Controller();

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String readLine;
        String outputLine;
        System.out.println("Projecte de prop del grup 18-3. Escriu 'help' per l'ajuda, o 'quit' per sortir.");
        try {
            System.out.print(">");
            while ((readLine = bufferedReader.readLine()) != null) {
                outputLine = controller.executeLine(readLine);
                if (outputLine != null) {
                    System.out.println(outputLine);
                }
                if (controller.isReadyToQuit()) {
                    System.exit(0);
                }
                System.out.print(">");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-tui")) {
            launchTerminalInterface();
        } else {
            launch(args);
        }
    }

}
