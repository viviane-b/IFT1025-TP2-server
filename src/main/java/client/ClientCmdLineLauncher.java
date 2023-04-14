package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientCmdLineLauncher extends Client{
    /**
     * Constructeur de ClientCmdLineLauncher
     * @param host L'adresse IP de connexion
     * @param port Le port sur lequel le client et le serveur sont connectés
     * @throws IOException
     */
    public ClientCmdLineLauncher(String host, int port) throws IOException {
        super(host, port);
    }

    /**
     * Afficher une question dans le terminal, sans choix de réponses
     * @param question La question à afficher
     */
    public static void showQuestion(String question){
        System.out.print(question);
    }

    /**
     * Afficher une question dans le terminal, avec choix de réponses numérotés
     * @param question La question à afficher
     * @param choices La liste de choix à afficher
     */
    public static void showQuestion(String question, String[] choices){
        System.out.println(question);
        int order = 1;
        for (String choice : choices) {
            System.out.printf("%d - %s%n", order, choice);
            order++;
        }
        System.out.print("> Choix: ");
    }

    /**
     *
     * @return la ligne écrite par l'utilisateur
     */
    public static String getAnswer(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }


    /**
     * Imprime un message de confirmation d'inscription au cours si les informations données sont bonnes et un message
     * d'erreur sinon.
     * @param prenom
     * @param courseCode de format 3 lettres et 4 chiffres ABC1234
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void showConfirmation(String prenom, String courseCode) throws IOException, ClassNotFoundException {
        String verdict = this.received().equals("SUCCESS") ? "réussie" : "échouée";
        String msg = String.format("Inscription %s de %s au cours %s", verdict, prenom, courseCode);
        System.out.println(msg);
    }

    /**
     * Affiche le choix des sessions pour lesquelles l'utilisateur peut s'inscire et retourne les cours pour cette session.
     * @return les cours offerts à la session sélectionnée
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ArrayList<Course> askSessionChoice() throws IOException, ClassNotFoundException {
        // Question 1. Demander à l'utilisateur de choisir une session
        ClientCmdLineLauncher.showQuestion("Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:",
                new String[]{"Automne", "Hiver", "Été"});
        String session = ClientCmdLineLauncher.getAnswer().toLowerCase();
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
        ClientCmdLineLauncher.showQuestion("Les cours offerts pendant la session d'" +
                courses.get(0).getSession().toLowerCase() +  " sont:",
                choicesOfCourse);

        return courses;
    }

    /**
     * Affiche les questions pour que l'utilisateur remplisse le formulaire d'inscription, enregistre les réponses,
     * vérifie si elles sont valides et retourne le formulaire d'inscprition si valides.
     * @param courses les cours offerts à la session précisée précédement
     * @return le formulaire d'inscription
     */

    public RegistrationForm askForm(ArrayList<Course> courses){
        String errorMsg, lastName, firstName, email, matricule, courseCode;
        Course chosenCourse;
        do {
            //Demander à l'utilisateur de saisir les informations pour s'inscrire
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre prénom:");
            firstName = ClientCmdLineLauncher.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre nom:");
            lastName = ClientCmdLineLauncher.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre email:");
            email = ClientCmdLineLauncher.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir votre matricule:");
            matricule = ClientCmdLineLauncher.getAnswer();
            ClientCmdLineLauncher.showQuestion("Veuillez saisir le code du cours:");
            courseCode = ClientCmdLineLauncher.getAnswer();

            // Verifier si les informations sont valides
            errorMsg = "";
            if (!Verification.verifyName(firstName))
                errorMsg += "First name is not valid\n";
            if (!Verification.verifyName(lastName))
                errorMsg += "Last name is not valid\n";
            if (!Verification.verifyEmail(email))
                errorMsg += "Email is not valid\n";
            if (!Verification.verifyMatricule(matricule))
                errorMsg += "Matricule is not valid\n";

            // verifier si le code du cours est valid
            chosenCourse = null;
            found:
            {
                for (Course course : courses)
                    if (course.getCode().equals(courseCode)) {
                        chosenCourse = course;
                        break found;
                    }
                errorMsg += "Le code du cours est invalide. Veuillez reessayer.\n";
            }

            // Afficher les erreurs s'il y en a
            System.out.print(errorMsg);

            // Demander à l'utilisateur de ressaisir les informations si elles sont invalides
        } while (errorMsg.length() > 0);

        // Retourner le formulaire d'inscription
        return new RegistrationForm(firstName, lastName, email, matricule, chosenCourse);
    }

    /**
     * Affiche le menu principal et permet à l'utilisateur d'inscrire.
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public void inscrire() throws IOException, ClassNotFoundException {
        // Demander à l'utilisateur de choisir une session et recuperer la liste des cours
        ArrayList<Course> courses = this.askSessionChoice();
        String nextAction;

        do {
            ClientCmdLineLauncher.showQuestion("Veuillez choisir la prochaine action:",
                new String[]{"Consulter les cours offerts par une autre session", "Inscription à un cours"});

            nextAction = ClientCmdLineLauncher.getAnswer().toLowerCase();

            switch (nextAction) {
                case "1" -> courses = this.askSessionChoice();
                case "2" -> {
                    // saisir les informations pour s'inscrire
                    RegistrationForm form = this.askForm(courses);
                    this.send("INSCRIRE");
                    this.send(form);

                    // Afficher le resultat de l'inscription
                    this.showConfirmation(form.getPrenom(), form.getCourse().getCode());
                }
                default -> System.out.println("Veuillez choisir une action valide.");
            }
        } while (!nextAction.equals("2"));
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientCmdLineLauncher client = new ClientCmdLineLauncher("127.0.0.1", 1337);
        System.out.println("Bienvenue au portail d'inscription à des cours de l'UDEM.");
        String nextAction;
        do {
            // Demander à l'utilisateur de s'inscrire
            client.inscrire();

            ClientCmdLineLauncher.showQuestion("Voulez-vous faire une autre inscription?", new String[]{"Oui", "Non"});
            // Verifier si l'utilisateur a choisi une action valide
            boolean validAnswer = true;
            do {
                nextAction = ClientCmdLineLauncher.getAnswer().toLowerCase();
                if (!nextAction.equals("1") && !nextAction.equals("2")){
                    validAnswer = false;
                    System.out.println("Veuillez choisir une action valide.");
                }
            } while (!validAnswer);

            // Continuer si l'utilisateur a choisi "Oui"
        } while (nextAction.equals("1"));
        System.out.println("Merci d'utiliser le système d'inscription de l'UdeM. Au revoir!");
    }
}
