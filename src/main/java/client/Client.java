package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Client(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.ois = new ObjectInputStream(this.clientSocket.getInputStream());
    }

    public void disconnect() throws IOException {
        this.oos.close();
        this.ois.close();
        this.clientSocket.close();
    }

    public Object received() throws IOException, ClassNotFoundException {
        return this.ois.readObject();
    }

    public void send(String line) throws IOException {
        this.oos.writeObject(line);
        this.oos.flush();
    }

    public void send(Object obj) throws IOException {
        this.oos.writeObject(obj);
        this.oos.flush();
    }


//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        try {
//            Client client = new Client("127.0.0.1", 1337);
//            Scanner scanner = new Scanner(System.in);
//            int i = 0;
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//                client.send(line);
//                System.out.println(client.received().toString());
//            }
//            client.disconnect();
//        } catch (ConnectException x) {
//            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
