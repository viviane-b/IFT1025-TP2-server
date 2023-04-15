package client;

public class Verification {
    /**
     * @param email
     * @return true si le format de email correspond au vrai format d'un email
     */
    public static boolean verifyEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * @param matricule
     * @return true si le matricule a un format de 8 chiffres
     */
    public static boolean verifyMatricule(String matricule) {
        return matricule.matches("^[0-9]{8}$");
    }

    /**
     * @param firstName
     * @return true si la String firstName n'est pas vide
     */
    public static boolean verifyName(String firstName) {
        return !(firstName.equals(""));
    }
}
