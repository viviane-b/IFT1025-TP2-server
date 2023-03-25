package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int portNumber = 1337;

    public static void main(String[] args) {

        try {
            Socket cS = new Socket("127.0.0.1",portNumber);

            OutputStreamWriter os = new OutputStreamWriter(cS.getOutputStream());

            BufferedWriter bw = new BufferedWriter(os);

            Scanner sc = new Scanner(System.in);

            while(sc.hasNext()) {
                String line = sc.nextLine();
                System.out.println("J'ai envoyé "+line );
                bw.append(line+"\n");
                bw.flush();
                if(line.equals("exit")) {
                    System.out.println("Au revoir.");
                    break;
                }
            }

            bw.close();
            sc.close();
            cS.close();

        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

