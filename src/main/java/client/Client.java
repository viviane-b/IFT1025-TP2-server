package client;

import java.io.*;
import java.net.Socket;


public class Client {
    private final Socket clientSocket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    /**
     * Établit une connexion à un serveur
     * @param host L'adresse IP de connexion
     * @param port Le port sur lequel le client et le serveur sont connectés
     * @throws IOException si la connexion avec le serveur est interrompue ou inexistante
     */
    public Client(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.ois = new ObjectInputStream(this.clientSocket.getInputStream());
    }

    /**
     * Ferme le objectOutputStream, objectInputStream, et le Socket
     * @throws IOException si la connexion avec le serveur est interrompue ou inexistante
     */
    public void disconnect() throws IOException {
        this.oos.close();
        this.ois.close();
        this.clientSocket.close();
    }

    /**
     *
     * @return l'objet reçu du serveur (sous forme d'Object)
     * @throws IOException si la connexion avec le serveur est interrompue ou inexistante
     * @throws ClassNotFoundException
     */
    public Object received() throws IOException, ClassNotFoundException {
        return this.ois.readObject();
    }

    /**
     * Envoyer une commande sous forme de String au serveur.
     * @param line La commande envoyée par le client
     * @throws IOException si la connexion avec le serveur est interrompue ou inexistante
     */
    public void send(String line) throws IOException {
        this.oos.writeObject(line);
        this.oos.flush();
    }

    /**
     * Envoyer un Object au serveur.
     * @param obj l'objet à envoyer
     * @throws IOException si la connexion avec le serveur est interrompue ou inexistante
     */
    public void send(Object obj) throws IOException {
        this.oos.writeObject(obj);
        this.oos.flush();
    }
}
