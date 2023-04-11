package client;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();

    }
}
