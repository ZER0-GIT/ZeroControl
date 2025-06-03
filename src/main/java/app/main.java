package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Paths;

public class main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane load = FXMLLoader.load(getClass().getResource(Paths.LoginFXML));
        Scene scene = new Scene(load);
        stage.setScene(scene);

        stage.show();
    }
}
