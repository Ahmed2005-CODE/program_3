package university.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import university.model.Student;

/**
 * VIEW - Modal form used for both adding and editing a student.
 */
public class StudentFormView {

    private final Stage stage;
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField majorField = new TextField();
    private final TextField yearField = new TextField();
    private final Button saveButton = Ui.button("Save Student");
    private final Button cancelButton = Ui.secondaryButton("Cancel");

    public StudentFormView(Student student) {
        boolean editing = student != null;

        stage = new Stage();
        stage.setTitle(editing ? "Edit Student" : "Add Student");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        if (editing) {
            idField.setText(student.getId());
            idField.setDisable(true);
            nameField.setText(student.getName());
            emailField.setText(student.getEmail());
            majorField.setText(student.getMajor());
            yearField.setText(String.valueOf(student.getYear()));
        } else {
            idField.setPromptText("e.g. S1005");
        }

        nameField.setPromptText("Full name");
        emailField.setPromptText("name@university.edu");
        majorField.setPromptText("e.g. Computer Science");
        yearField.setPromptText("1 - 5");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.addRow(0, new Label("Student ID"), idField);
        form.addRow(1, new Label("Name"), nameField);
        form.addRow(2, new Label("Email"), emailField);
        form.addRow(3, new Label("Major"), majorField);
        form.addRow(4, new Label("Year"), yearField);

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(18,
                Ui.heading(editing ? "Edit Student" : "New Student"), form, buttons);
        root.setPadding(new Insets(24));

        stage.setScene(new Scene(root, 480, 410));
    }

    /** Builds a Student from the fields; may throw NumberFormatException. */
    public Student readStudent() {
        return new Student(
                idField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                majorField.getText().trim(),
                Integer.parseInt(yearField.getText().trim())
        );
    }

    public TextField getIdField() {
        return idField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getMajorField() {
        return majorField;
    }

    public TextField getYearField() {
        return yearField;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        stage.showAndWait();
    }
}
