package client;

import java.util.ArrayList;

public class ClientCmdLine {

    public static void main(String[] args) {
        Client client = new Client();

        client.commands = new ArrayList<Command>();

        // For testing
        client.commands.add(new Command("CHARGER", "Hiver"));
        //client.commands.add(new Command("INSCRIRE", "i")); // Probleme: le serveur arrete de traiter les commandes apres la premiere.
        System.out.println(client.commands);

        // Take commands through command line
        //TODO

        client.run();


    }


}
