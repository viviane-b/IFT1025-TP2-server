package client.mvc;

import client.Verification;
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
            else
                this.vue.showErrorPopup("No session selected");
        });

        this.vue.getSendFormButton().setOnAction((action) -> {
            Course selectedCourse = this.vue.getSelectedCourse();
            if (selectedCourse != null) {
                try {
                    String[] formValues = this.vue.getFormValues();

                    String firstName = formValues[0];
                    String lastName = formValues[1];
                    String email = formValues[2];
                    String matricule = formValues[3];

                    String errorMsg = "";
                    if (!Verification.verifyName(firstName))
                        errorMsg += "First name is not valid\n";
                    else if (!Verification.verifyName(lastName))
                        errorMsg += "Last name is not valid\n";
                    else if (!Verification.verifyEmail(email))
                        errorMsg += "Email is not valid\n";
                    else if (!Verification.verifyMatricule(matricule))
                        errorMsg += "Matricule is not valid\n";

                    if (errorMsg.equals("")) {
                        this.modele.sendInscrire(firstName, lastName, email, matricule, selectedCourse);
                        this.vue.clearForm();
                        String msg = String.format(
                            "Inscription %s de %s au cours %s",
                            this.modele.receiveResultInscrire() ? "réussie" : "échouée",
                            firstName,
                            this.vue.getSelectedCourse().getCode());
                        this.vue.showPopup(msg);
                    } else {
                        this.vue.showErrorPopup(errorMsg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                this.vue.showErrorPopup("No course selected");
        });
    }
}
