package client.mvc;

import server.models.Course;

import java.io.IOException;
import java.util.ArrayList;

public class Controleur {
    //TODO: Write javadoc for Controleur class
        private final Modele modele;
        private final Vue vue;

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
                if (this.vue.getSelectedCourse() != null) {
                    try {
                        String[] formValues = this.vue.getFormValues();
                        //TODO: Add a verification for each field
                        String firstName = formValues[0];
                        String lastName = formValues[1];
                        String email = formValues[2];
                        String matricule = formValues[3];

                        modele.sendInscrire(firstName, lastName, email, matricule, this.vue.getSelectedCourse());
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
