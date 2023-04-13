package server.models;

import java.io.Serializable;

public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Constructeur de RegistrationForm
     * @param prenom
     * @param nom
     * @param email
     * @param matricule
     * @param course
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Verifie si les champs sont valides

    /**
     * Verifie si les champs sont valides
     * @return true si les champs Email, Matricule, Nom et Cours sont valides
     */
    public boolean isValid() {
        return isEmailValid() && isMatriculeValid() && isNameValid() && isCourseValid();
    }


    public boolean isEmailValid() {
        return this.email.contains("/([a-zA-Z0-9\\-\\.]+)(@)(([a-zA-Z0-9\\-]+)(\\.))+([a-zA-Z0-9\\-]+)/");
    }
    public boolean isMatriculeValid() {
        return this.matricule.length() == 8;
    }
    public boolean isNameValid() {
        return this.prenom != null && this.nom != null;
    }
    public boolean isCourseValid() {
        return this.course != null;
    }

    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
