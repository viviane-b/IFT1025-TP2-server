package client;

import client.mvc.Controleur;
import client.mvc.Modele;
import client.mvc.Vue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * IMPORTANT :
 * À ajouter à "VM options" quant à l'exécution de client_fx:
 * --module-path "<Chemin absolue mène à la library JavaFX>" --add-modules javafx.fxml,javafx.controls,javafx.graphics
 * Et importer la library JavaFX dans le projet en faisant:
 * File -> Project Structure -> Libraries -> + -> <Dossier lib de la library JavaFX>
 * Il est important de faire ces 2 étapes sinon le programme ne fonctionnera pas.
 */

public class ClientGUILauncher extends Application {

	private final static String HOST = "127.0.0.1";
	private final static int PORT = 1337;
    public static void main (String[] args) {
        launch(args);
    }

	/**
	 * Crée une instance de client et affiche la fenêtre d'inscription
	 * @param stage La fenêtre d'inscription
	 * @throws Exception
	 */
	public void start(Stage stage) throws Exception {
		Modele leModele = new Modele(HOST, PORT);
		Vue laVue = new Vue();
		Controleur leControleur = new Controleur(leModele, laVue);

		Scene scene = new Scene(laVue, 800, 600);

		stage.setScene(scene);
		stage.setTitle("Inscription UdeM");
		stage.show();
	}
}
