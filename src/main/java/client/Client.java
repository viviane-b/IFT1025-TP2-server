package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    private static final int portNumber = 1337;
    protected ArrayList<Command> commands;
    protected ArrayList<Course> courseList;
    protected Socket cS ;
    protected ObjectOutputStream oos ;
    protected ObjectInputStream ois ;


    public void run() {
        try {
            Socket cS = new Socket("127.0.0.1", portNumber);
            oos = new ObjectOutputStream(cS.getOutputStream());
            ois = new ObjectInputStream(cS.getInputStream());


            // Send commands to server
            for (Command command : commands) {
                System.out.println(command.toString());
                oos.writeObject(command);
                if (command.getCmd().equals("CHARGER")) {
                    load(command.getArg());
                }
                if (command.getCmd().equals("INSCRIRE")) {
                    // for testing
                    register(new RegistrationForm("Bob", "Smith", "bobsmith@gmail.com", "12345678", new Course("Programmation2", "IFT1025", "Hiver")));

                }
            }



        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void load(String session) {
        try {
            courseList = (ArrayList<Course>) ois.readObject();
            System.out.println(courseList);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void register(RegistrationForm registrationForm) {
        try {
            // Send registration form to server
            oos.writeObject(registrationForm);
            // Receive confirmation message from server
            System.out.println((String) ois.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    // TO ERASE (main is in ClientCmdLine)
    public static void main(String[] args) {

        try {
            Socket cS = new Socket("127.0.0.1",portNumber);

            OutputStreamWriter os = new OutputStreamWriter(cS.getOutputStream());

            InputStreamReader is = new InputStreamReader(cS.getInputStream());

            ObjectOutputStream oos = new ObjectOutputStream(cS.getOutputStream());
            ObjectInputStream ois =new ObjectInputStream(cS.getInputStream());

            BufferedWriter bw = new BufferedWriter(os);

            Scanner sc = new Scanner(System.in);

            RegistrationForm registrationForm = new RegistrationForm("Bob", "Smith", "bobsmith@gmail.com", "12345678", new Course("Programmation2", "IFT1025", "Hiver"));



            while(sc.hasNext()) {
                String line = sc.nextLine();
                System.out.println("J'ai envoyé " + line);


                //os.append(line+ "\n");
                //os.close();

                // Serialize the command line
                String[] parts = line.split(" ");
                String cmd = parts[0];
                String arg = parts[1];
                Command cmdLine = new Command(cmd, arg);

                System.out.println(cmdLine.toString());

                // Send the Command cmdLine to the Server
                oos.writeObject(cmdLine);


                // Receive Objects from Server
                try {
                    //ArrayList<Course> courseList = (ArrayList<Course>) ois.readObject();
                    //System.out.println(courseList);

                    //Course course = (Course) ois.readObject();
                    if (cmd.equals("CHARGER")) {
                        ArrayList<Course> courseList = (ArrayList<Course>) ois.readObject();

                        System.out.println(courseList);
                    }
                    if (cmd.equals("INSCRIRE")) {
                        oos.writeObject(registrationForm);
                        System.out.println((String) ois.readObject());
                    }

                } catch ( ClassNotFoundException e) {
                    e.printStackTrace();
                }


                //bw.append(line+"\n");
                //bw.flush();

                //   System.out.println(os.toString());

                if (line.equals("exit")) {
                    System.out.println("Au revoir.");
                    break;
                }
            }


            sc.close();




            //bw.close();
            //sc.close();
            cS.close();

        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

