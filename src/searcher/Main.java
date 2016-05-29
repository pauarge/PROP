package searcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;


public class Main extends Application {

    private static final String APP_TITLE = "Cercador Relacional";

    @Override
    public void stop(){
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/landing.fxml"));
        primaryStage.setTitle(APP_TITLE);
        URL iconURL = Main.class.getResource("img/magnifier.png");
        try {
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            //com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
            // Won't work on Windows or Linux.
        }
        primaryStage.getIcons().add(new Image(String.valueOf(iconURL)));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
