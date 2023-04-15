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
     * @param prenom le prénom de l'utilisateur
     * @param courseCode de format 3 lettres et 4 chiffres ABC1234
     * @throws IOException si la connexion avec le serveur est perdue
     * @throws ClassNotFoundException si le serveur envoie un objet de type inconnu
     */
    public void showConfirmation(String prenom, String courseCode) throws IOException, ClassNotFoundException {
        String verdict = this.received().equals("SUCCESS") ? "réussie" : "échouée";
        String msg = String.format("Inscription %s de %s au cours %s", verdict, prenom, courseCode);
        System.out.println(msg);
    }

    /**
     * Affiche une question et attend une réponse de l'utilisateur. Si la réponse n'est pas valide, affiche un message
     * d'erreur et redemande la réponse.
     * @param question La question à afficher
     * @param answers Les réponses possibles
     * @param validChoices Les choix valides, qui peuvent être des numéros ou des mots
     * @param errorMsg Le message d'erreur à afficher
     * @return La réponse de l'utilisateur
     */
    public static String loopForValidInput(String question, String[] answers, String[] validChoices, String errorMsg) {
        String choice;
        boolean validInput = false;

        do {
            showQuestion(question, answers);
            choice = getAnswer().toLowerCase();
            for (String validChoice : validChoices)
                if (choice.equals(validChoice)) {
                    validInput = true;
                    break;
                }
            if (!validInput)
                System.out.print(errorMsg);
        } while (!validInput);
        return choice;
    }

    /**
     * Affiche le choix des sessions pour lesquelles l'utilisateur peut s'inscrire et retourne les cours pour cette session.
     * @return les cours offerts à la session sélectionnée
     * @throws IOException si la connexion avec le serveur est perdue
     * @throws ClassNotFoundException si le serveur envoie un objet de type inconnu
     */
    public ArrayList<Course> askSessionChoice() throws IOException, ClassNotFoundException {
        String session = loopForValidInput("Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:",
                new String[]{"Automne", "Hiver", "Été"},
                new String[]{"1", "2", "3"},
                "La session choisie est invalide. Veuillez réessayer.");

        switch (session) {
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
     * vérifie si elles sont valides et retourne le formulaire d'inscription si valide.
     * @param courses les cours offerts à la session précisée précédemment
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
     * Affiche le menu principal et demande à l'utilisateur de choisir une action.
     * @throws IOException si la connexion avec le serveur est perdue
     * @throws ClassNotFoundException si le serveur envoie un objet de type inconnu
     */
    public void inscrireOuConsulter() throws IOException, ClassNotFoundException {
        // Demander à l'utilisateur de choisir une session et récupérer la liste des cours
        ArrayList<Course> courses = this.askSessionChoice();
        String nextAction;

        do {
            nextAction = loopForValidInput("Veuillez choisir la prochaine action:",
                    new String[]{"Consulter les cours offerts par une autre session", "Inscription à un cours"},
                    new String[]{"1", "2"},
                    "Veuillez choisir une action valide.");

            switch (nextAction) {
                case "1" -> courses = this.askSessionChoice();
                case "2" -> {
                    // saisir les informations pour s'inscrire
                    RegistrationForm form = this.askForm(courses);
                    this.send("INSCRIRE");
                    this.send(form);

                    // Afficher le résultat de l'inscription
                    this.showConfirmation(form.getPrenom(), form.getCourse().getCode());
                }
            }
        } while (!nextAction.equals("2"));
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientCmdLineLauncher client = new ClientCmdLineLauncher("127.0.0.1", 1337);
        System.out.println("Bienvenue au portail d'inscription à des cours de l'UDEM.");
        String nextAction;
        do {
            // Demander à l'utilisateur de s'inscrire
            client.inscrireOuConsulter();
            nextAction = loopForValidInput("Voulez-vous faire une autre inscription?",
                    new String[]{"Oui", "Non"},
                    new String[]{"1", "2"},
                    "Veuillez choisir une action valide.");
            // Continuer si l'utilisateur a choisi "Oui"
        } while (nextAction.equals("1"));
        System.out.println("Merci d'utiliser le système d'inscription de l'UdeM. Au revoir!");
    }
}
