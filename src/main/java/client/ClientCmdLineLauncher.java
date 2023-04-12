package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientCmdLineLauncher extends Client{
    //TODO: Write javadoc for ClientCmdLineLauncher class
    //TODO: Create Jar file for ClientCmdLineLauncher
    public ClientCmdLineLauncher(String host, int port) throws IOException {
        super(host, port);
    }

    public static void showQuestion(String question){
        System.out.print(question);
    }

    public static void showQuestion(String question, String[] choices){
        System.out.println(question);
        int order = 1;
        for (String choice : choices) {
            System.out.printf("%d - %s%n", order, choice);
            order++;
        }
        System.out.print("> Choix: ");
    }

    public String getAnswer(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public void showConfirmation(String prenom, String courseCode) throws IOException, ClassNotFoundException {
        String verdict;
        if (this.received().equals("SUCCESS"))
            verdict = "réussie";
        else
            verdict = "échouée";

        String msg = String.format("Inscription %s de %s au cours %s", verdict, prenom, courseCode);
        System.out.println(msg);
    }

    public ArrayList<Course> askSessionChoice() throws IOException, ClassNotFoundException {
        // Question 1. Demander à l'utilisateur de choisir une session
        ClientCmdLineLauncher.showQuestion("Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:",
                new String[]{"Automne", "Hiver", "Été"});
        String session = this.getAnswer().toLowerCase();
        switch (session){
            case "1", "automne" -> this.send("CHARGER Automne");
            case "2", "hiver" -> this.send("CHARGER Hiver");
            case "3", "été", "ete" -> this.send("CHARGER Ete");
        }

        // Afficher la liste des cours
        ArrayList<Course> courses = (ArrayList<Course>) this.received();
        String[] choicesOfCourse = new String[courses.size()];
        for (Course course : courses) {
            int idx = courses.indexOf(course);
            choicesOfCourse[idx] = String.format("%s\t%s", course.getCode(), course.getName());
        }
        ClientCmdLineLauncher.showQuestion("Les cours offerts pendant la question sont:", choicesOfCourse);

        return courses;
    }

    public RegistrationForm askForm(ArrayList<Course> courses) throws IOException, ClassNotFoundException {
        RegistrationForm form = null;
        do {
            //Demander à l'utilisateur de saisir les informations pour s'inscrire
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre prenom:");
            String firstName = this.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre nom:");
            String lastName = this.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre email:");
            String email = this.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre matricule:");
            String matricule = this.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir le code du cours:");
            String courseCode = this.getAnswer();

            // Trouver le cours choisi et verifier si le code du cours est valide
            Course chosenCourse = null;
            found:{
                for (Course course : courses)
                    if (course.getCode().equals(courseCode)) {
                        chosenCourse = course;
                        break found;
                    }
                System.out.println("Le code du cours est invalide. Veuillez reessayer.");
            }

            form = new RegistrationForm(firstName, lastName, email, matricule, chosenCourse);

            // Demander à l'utilisateur de ressaisir les informations si elles sont invalides
            // TODO: show the invalid fields
            if (!form.isValid())
                System.out.println("Les informations saisies ne sont pas valides. Veuillez reessayer.");

        } while (!form.isValid());

        // Retourner le formulaire d'inscription
        return form;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientCmdLineLauncher client = new ClientCmdLineLauncher("127.0.0.1", 1337);

        System.out.println("Bienvenue au portail d'inscription à des cours de l'UDEM.");

        // choisir une session et recuperer la liste des cours
        ArrayList<Course> courses = client.askSessionChoice();
        String nextAction;
        do {
            // choisir prochain action
            ClientCmdLineLauncher.showQuestion("Veuillez choisir la prochaine action:",
                    new String[]{"Consulter les cours offerts par une autre session", "Inscription à un cours"});
            nextAction = client.getAnswer().toLowerCase();
            switch (nextAction) {
                //TODO: Add verification for the nextAction
                case "1" -> courses = client.askSessionChoice();
                case "2" -> {
                    // saisir les informations pour s'inscrire
                    RegistrationForm form = client.askForm(courses);
                    client.send("INSCRIRE");
                    client.send(form);

                    // Afficher le resultat de l'inscription
                    client.showConfirmation(form.getPrenom(), form.getCourse().getCode());
                }
            }

        } while (!nextAction.equals("2"));
    }
}
