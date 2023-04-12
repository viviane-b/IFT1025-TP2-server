package client.mvc;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import server.models.Course;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Vue extends HBox {
    //TODO: Write javadoc for Vue Class

    /**
     * Les boutons pour sélectionner une session et charger les cours de cette session
     */
    public static class SessionSubmitButton extends HBox {

        /**
         * Constructeur pour la classe SessionSubmitButton
         */
        public SessionSubmitButton() {
            ComboBox comboBox = new ComboBox(FXCollections.observableArrayList("Automne", "Hiver", "Ete"));
            Button submitButton = new Button("Charger");

            //Style
            this.getStyleClass().add("session-submit-box");

            this.getChildren().add(comboBox);
            this.getChildren().add(submitButton);
        }

        /**
         * @return la session choisie par l'utilisateur
         */
        public String getSelectedSession() {
            return ((ComboBox) this.getChildren().get(0)).getValue().toString();
        }

        /**
         * @return le bouton sur lequel l'utilisateur clique pour charger une session
         */
        public Button getSubmitButton() {
            return (Button) this.getChildren().get(1);
        }
    }

    /**
     * La partie gauche de la fenêtre (affichage des cours pour une session choisie)
     */
    public static class CourseView extends VBox {
        /**
         * Ajoute les cours de la session choisie à la partie 'Affichage des cours' de la fenêtre
         * @param courses la liste des cours d'une session choisie
         */
        public CourseView(ArrayList<Course> courses) {
            TableView<Course> table = new TableView<Course>();
            TableColumn<Course, String> courseCodeColumn = new TableColumn<Course, String>("Code");
            TableColumn<Course, String> courseNameColumn = new TableColumn<Course, String>("Nom");

            courseCodeColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.35));
            courseNameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.65));

            courseCodeColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
            courseNameColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));

            table.getColumns().add(courseCodeColumn);
            table.getColumns().add(courseNameColumn);

            //Show this when there is no courses
            table.setPlaceholder(new Label("No content in table"));

            for (Course course : courses) {
                table.getItems().add(course);
            }
            this.getChildren().add(table);
        }

        /**
         * @return le cours sélectionné par l'utilisateur
         */
        public Course getSelectedCourse() {
            return (Course) ((TableView) this.getChildren().get(0)).getSelectionModel().getSelectedItem();

        }
    }

    /**
     * Formulaire d'inscription avec questions et endroits pour répondre
     */
    public static class Form extends GridPane {
        public Form() {
            //Style
            this.getStyleClass().add("grid-form");

            this.add(new Label("Prénom"), 0, 0);
            this.add(new Label("Nom"), 0, 1);
            this.add(new Label("Email"), 0, 2);
            this.add(new Label("Matricule"), 0, 3);

            this.add(new TextField(), 1, 0);
            this.add(new TextField(), 1, 1);
            this.add(new TextField(), 1, 2);
            this.add(new TextField(), 1, 3);
        }

        /**
         * @return les informations entrées par l'utilisateur dans le formulaire d'inscription (en String)
         */
        public String[] getFormValues() {
            String[] values = new String[4];
            values[0] = ((TextField) this.getChildren().get(4)).getText();
            values[1] = ((TextField) this.getChildren().get(5)).getText();
            values[2] = ((TextField) this.getChildren().get(6)).getText();
            values[3] = ((TextField) this.getChildren().get(7)).getText();
            return values;
        }

        /**
         * Efface le texte dans les champs de réponse du formulaire d'inscription
         */
        public void clearForm() {
            ((TextField) this.getChildren().get(4)).setText("");
            ((TextField) this.getChildren().get(5)).setText("");
            ((TextField) this.getChildren().get(6)).setText("");
            ((TextField) this.getChildren().get(7)).setText("");
        }
    }

    private ArrayList<Course> courses;
    private CourseView courseView;
    private SessionSubmitButton sessionSubmitButton;
    private Form form;
    private Button sendFormButton;

    /**
     * Constructeur de la fenêtre d'inscrption
     */
    public Vue() {
        File f = new File("src/main/java/client/mvc/vue.css");
        try {
            this.getStylesheets().add(f.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        //Main box
        VBox leftBox = new VBox();
        VBox rightBox = new VBox();

        this.getChildren().add(leftBox);
        this.getChildren().add(new Separator());
        this.getChildren().add(rightBox);

        // Left box
        Text titleLeft = new Text("Liste des cours");
        courses = new ArrayList<Course>();
        courseView = new CourseView(courses);
        sessionSubmitButton = new SessionSubmitButton();

        leftBox.getChildren().add(titleLeft);
        leftBox.getChildren().add(courseView);
        leftBox.getChildren().add(new Separator());
        leftBox.getChildren().add(sessionSubmitButton);

        // Right box
        Text titleRight = new Text("Formulaire d'inscription");
        form = new Form();
        sendFormButton = new Button("Envoyer");

        rightBox.getChildren().add(titleRight);
        rightBox.getChildren().add(form);
        rightBox.getChildren().add(sendFormButton);

        // Style
        leftBox.getStyleClass().add("box");
        rightBox.getStyleClass().add("box");
        titleLeft.getStyleClass().add("h1");
        titleRight.getStyleClass().add("h1");

    }

    /**
     * @return la session sélectionnée par l'utilisateur
     */
    public String getSelectedSession() {
        return sessionSubmitButton.getSelectedSession();
    }

    /**
     * @return le cours sélectionné par l'utilisateur
     */
    public Course getSelectedCourse() {
        return courseView.getSelectedCourse();
    }

    /**
     * Affiche les cours de la session sélectionnée dans la fenêtre.
     * @param courses la liste des cours de la session sélectionnée
     */
    public void setCourseView(ArrayList<Course> courses) {
        ((VBox) this.getChildren().get(0)).getChildren().set(1, new CourseView(courses));
    }

    /**
     * @return le bouton de sélection de session
     */
    public Button getSessionSubmitButton() {
        return sessionSubmitButton.getSubmitButton();
    }

    /**
     * @return les informations rentrées par l'utilisateur dans le formulaire d'inscription sous forme de tableau de String
     */
    public String[] getFormValues() {
        return form.getFormValues();
    }

    /**
     * Réinitialise les champs de réponses du formulaire d'inscription
     */
    public void clearForm() {
        form.clearForm();
    }

    /**
     * @return le bouton d'envoi du formulaire d'inscription
     */
    public Button getSendFormButton() {
        return sendFormButton;
    }
}
