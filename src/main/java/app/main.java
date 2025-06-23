package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Paths;

import java.io.IOException;

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

    public void abrirVentana(String pathVentana){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathVentana));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }


    }
    public void cerrarVentana(Stage stageVentana){
        stageVentana.close();
    }
}
