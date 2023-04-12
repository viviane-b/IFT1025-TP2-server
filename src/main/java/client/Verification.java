package client;

public class Verification {
    public static boolean verifyEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean verifyMatricule(String matricule) {
        return matricule.matches("^[0-9]{8}$");
    }

    public static boolean verifyName(String firstName) {
        return !(firstName.equals(""));
    }

}
