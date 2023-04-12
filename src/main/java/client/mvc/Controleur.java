package client.mvc;

import server.models.Course;

import java.io.IOException;
import java.util.ArrayList;

public class Controleur {

        private final Modele modele;
        private final Vue vue;

    /**
     * Constructeur pour la classe Controleur. Fait le lien entre les classes Vue et Modele.
     * @param modele
     * @param vue
     */
    public Controleur(Modele modele, Vue vue) {
            this.modele = modele;
            this.vue = vue;

            this.vue.getSessionSubmitButton().setOnAction((action) -> {
                if (this.vue.getSelectedSession() != null) {
                    ArrayList<Course> courses = null;
                    try {
                        this.modele.sendCharger(this.vue.getSelectedSession());
                        courses = this.modele.receiveCharger();
                        this.vue.setCourseView(courses);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    //TODO: Add a popup
                    System.out.println("No session selected");
                }
            });

            this.vue.getSendFormButton().setOnAction((action) -> {
                Course selectedCourse = this.vue.getSelectedCourse();
                System.out.println(this.vue.getSelectedCourse());
                if (selectedCourse != null) {
                    try {
                        String[] formValues = this.vue.getFormValues();
                        //TODO: Add a verification for each field
                        String firstName = formValues[0];
                        String lastName = formValues[1];
                        String email = formValues[2];
                        String matricule = formValues[3];

                        modele.sendInscrire(firstName, lastName, email, matricule, selectedCourse);
                        this.vue.clearForm();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    //TODO: Add a popup
                    System.out.println("No course selected");
                }
            });
        }
}
