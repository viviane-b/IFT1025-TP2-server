package client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Vue extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage){
        //primaryStage.setHeight(800);
        //primaryStage.setHeight(600);
        HBox root = new HBox();
        Scene scene = new Scene(root, 400, 150);
        Text titre = new Text("Ceci n'est pas un titre");
        root.getChildren().add(titre);
        root.getChildren().add(new Separator());
        HBox buttonGroup = new HBox();
        Button gauche = new Button("Gauche");
        Button centre = new Button("Centre");
        Button droite = new Button("Droite");
        buttonGroup.getChildren().add(gauche);
        buttonGroup.getChildren().add(centre);
        buttonGroup.getChildren().add(droite);
        buttonGroup.setAlignment(Pos.CENTER);
        root.getChildren().add(buttonGroup);
        root.getChildren().add(new Separator());
        CheckBox checkBox = new CheckBox("Voulez-vous cocher ... ?");
        root.getChildren().add(checkBox);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();


    }


}
