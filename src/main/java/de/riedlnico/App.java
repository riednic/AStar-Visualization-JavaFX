package de.riedlnico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App extends Application{

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("grid.fxml"));
            Parent root = fxmlLoader.load();
            GridController controller = fxmlLoader.getController();
            controller.setStageAndInitializeGrid(stage);
            scene = new Scene(root);
            stage.initStyle(StageStyle.UTILITY);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch(FileNotFoundException e) {
           e.printStackTrace();
           throw new IOException("Problem with File");
        }
    }

    public static void main(String[] args) {
        launch();
    }

}