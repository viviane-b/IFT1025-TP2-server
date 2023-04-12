package client;

import client.mvc.Controleur;
import client.mvc.Modele;
import client.mvc.Vue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientGUILauncher extends Application {
	//TODO: bonus question about multithreading
	//TODO: Write javadoc for ClientGUILauncher
	//TODO: Create Jar file for ClientGUILauncher
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
