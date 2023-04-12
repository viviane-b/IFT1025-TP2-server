package client.mvc;

import client.Client;

import java.io.IOException;
import java.util.ArrayList;
import server.models.Course;
import server.models.RegistrationForm;

public class Modele {
    private final Client client;

    /**
     * Constructeur de la classe Modele
     * @param host L'adresse IP de connexion
     * @param port Le port sur lequel le client et le serveur sont connectés
     */
    public Modele(String host, int port) {
        try {
            client = new Client(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Envoie la requête au serveur de charger les cours pour une certaine session
     * @param session la session pour laquelle on veut recevoir les cours
     * @throws IOException
     */
    public void sendCharger(String session) throws IOException {
        this.client.send("CHARGER " + session);
    }

    /**
     * Recevoir les cours pour une session choisie
     * @return La liste des cours de la session choisie
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ArrayList<Course> receiveCharger() throws IOException, ClassNotFoundException {
        return (ArrayList<Course>) this.client.received();
    }

    /**
     * Envoie le formulaire d'inscription complété au serveur
     * @param firstName
     * @param lastName
     * @param email
     * @param matricule
     * @param chosenCourse le code du cours choisi
     * @throws IOException
     */
    public void sendInscrire(String firstName, String lastName, String email,
                             String matricule, Course chosenCourse) throws IOException {
        this.client.send("INSCRIRE");
        this.client.send(new RegistrationForm(firstName, lastName, email, matricule, chosenCourse));
    }

    /**
     * @return vrai si l'inscription est valide et faux sinon.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean receiveResultInscrire() throws IOException, ClassNotFoundException {
        return this.client.received().equals("SUCCESS");
    }
}
