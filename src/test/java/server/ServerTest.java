package server;

import client.Client;
import junit.framework.TestCase;
import org.junit.Test;
import server.models.Course;

import java.io.IOException;
import java.util.ArrayList;

public class ServerTest extends TestCase {



    @Test
    public void testHandleLoadCourses() {

        try {
            Server server = new Server(1337);
            System.out.println("test");
            server.run();
            System.out.println("running");
            Client client = new Client("127.0.0.1", 1337);
            client.send("CHARGER Ete");
            String arg = new String("Ete");
            ArrayList<Course> coursEte = new ArrayList<>();
            Course course1 = new Course("Base_de_donnees", "IFT2256", "Ete" );
            coursEte.add(course1);
            //System.out.println(client.received().toString());

            assertEquals(coursEte, client.received() );



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    @Test
    public void testHandleRegistration() {
    }
}