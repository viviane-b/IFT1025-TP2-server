package client.mvc;

import client.Client;

import java.io.IOException;
import java.util.ArrayList;
import server.models.Course;
import server.models.RegistrationForm;

public class Modele {
    //TODO: Write javadoc for Modele class
    private final Client client;

    public Modele(String host, int port) {
        try {
            client = new Client(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCharger(String session) throws IOException {
        this.client.send("CHARGER " + session);
    }

    public ArrayList<Course> receiveCharger() throws IOException, ClassNotFoundException {
        return (ArrayList<Course>) this.client.received();
    }

    public void sendInscrire(String firstName, String lastName, String email,
                             String matricule, Course chosenCourse) throws IOException {
        this.client.send("INSCRIRE");
        this.client.send(new RegistrationForm(firstName, lastName, email, matricule, chosenCourse));
    }

    public boolean receiveResultInscrire() throws IOException, ClassNotFoundException {
        //TODO: Call this method in pop-up window GUI
        return this.client.received().equals("SUCCESS");
    }
}
