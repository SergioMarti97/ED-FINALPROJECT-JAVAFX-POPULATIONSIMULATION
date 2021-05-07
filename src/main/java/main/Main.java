package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Scene scene;

    @Override
    public void init() throws Exception {
        super.init();
        Parent root = FXMLLoader.load(getClass().getResource("/main/MainFxml.fxml"));
        scene = new Scene(root, 1200, 900);
        scene.getStylesheets().add(getClass().getResource("/main/MainStyle.css").toExternalForm());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulación poblacional");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
