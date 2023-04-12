package client;

import java.io.*;
import java.net.Socket;


public class Client {
    //TODO: Write Javadoc for Client class
    private final Socket clientSocket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public Client(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.ois = new ObjectInputStream(this.clientSocket.getInputStream());
    }

    public void disconnect() throws IOException {
        //TODO: Find a place to call this method
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
}
