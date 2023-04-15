package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    /**
     * Commande pour l'inscription à des cours
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * Commande pour le chargement des cours d'une session en particulier
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe Server
     * @param port le port sur lequel le client doit se connecter pour accéder avec ce serveur
     * @throws IOException si la connexion entre le serveur et le client est inexistante ou interrompue
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajouter un événement à la liste d'événements possibles
     * @param h l'événement à ajouter
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Démarre l'exécution du serveur. Le serveur attend qu'un client se connecte pour répondre aux commandes.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream()); // receive data from client
                objectOutputStream = new ObjectOutputStream(client.getOutputStream()); // send data to client
                listen(); // react to client's requests, read or/and write data to stream
                disconnect(); // close streams and socket
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prend les commandes reçues du client et les traite.
     * @throws IOException si la connexion entre le serveur et le client est inexistante ou interrompue
     * @throws ClassNotFoundException
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        while (true) {
            if ((line = this.objectInputStream.readObject().toString()) != null) {
                Pair<String, String> parts = processCommandLine(line);
                String cmd = parts.getKey();
                String arg = parts.getValue();
                this.alertHandlers(cmd, arg);
            }
        }
    }

    /**
     * Transforme la ligne de commande en une paire de commande et arguments.
     * @param line la ligne de commande du client (de forme "CHARGER ..." ou INSCRIRE ..."
     * @return la commande et l'argument séparés
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Mettre fin à la connexion entre le serveur et le client
     * @throws IOException si la connexion entre le serveur et le client est inexistante ou interrompue avant
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Traiter les commandes selon leur type de commande
     * @param cmd la commande, "CHARGER" ou "INSCRIRE"
     * @param arg les arguments de la commande
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        final String COURSES_FILE_PATH = "src/main/java/server/data/cours.txt";
        try {
            File coursesFile = new File(COURSES_FILE_PATH);
            Scanner scanner = new Scanner(coursesFile);
            ArrayList<Course> courses = new ArrayList<Course>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");

                String session = parts[2];
                String courseName = parts[1];
                String courseCode = parts[0];
                if (session.equals(arg)) {
                    Course course = new Course(courseName, courseCode, session);
                    courses.add(course);
                }
            }
            scanner.close();
            objectOutputStream.writeObject(courses);

        } catch (FileNotFoundException e) {
            System.err.println("path " + COURSES_FILE_PATH + " not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gère les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();

            // Save registration form in a text file
            File inscriptionFile = new File("src/main/java/server/data/inscription.txt");
            FileWriter fw = new FileWriter(inscriptionFile, true);
            BufferedWriter writer = new BufferedWriter(fw);

            String inscription =
                    registrationForm.getCourse().getSession() + "\t" +
                    registrationForm.getCourse().getCode() + "\t" +
                    registrationForm.getMatricule() + "\t" +
                    registrationForm.getNom() + "\t" +
                    registrationForm.getPrenom() + "\t" +
                    registrationForm.getEmail() + "\n";
            writer.write(inscription);
            writer.close();
            fw.close();

            // Send success message to client (at the end)
            objectOutputStream.writeObject("SUCCESS");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

