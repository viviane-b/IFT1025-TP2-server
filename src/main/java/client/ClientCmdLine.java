package client;

import server.models.Course;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientCmdLine {

    public static void main(String[] args) {
        Client client = new Client();

        client.commands = new ArrayList<Command>();

        Scanner reader = new Scanner(System.in);

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM *** \n" +
                "Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours: \n" +
                "1. Automne \n" +
                "2. Hiver \n" +
                "3. Ete  \n" +
                "> Choix: ");

        int choice_session = reader.nextInt();
        String arg = new String("");
        switch (choice_session) {
            case 1: arg = "Automne";
                    break;
            case 2: arg = "Hiver";
                    break;
            case 3: arg = "Ete";
                    break;
        }

        client.commands.add(new Command("CHARGER", arg));
        client.run();

        System.out.println("Les cours offerts pendant la session d'" + arg.toLowerCase() + " sont: \n");
        for (Course course : client.courseList) {
            System.out.println(client.courseList.indexOf(course) + ". " + course.getCode() + "\t" + course.getName());

        }


        //client.commands.add(new Command("INSCRIRE", "i")); // Probleme: le serveur arrete de traiter les commandes apres la premiere.
        System.out.println(client.commands);

        // Take commands through command line
        //TODO




    }


}
