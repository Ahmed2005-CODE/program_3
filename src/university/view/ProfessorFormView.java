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
import university.model.Professor;

/**
 * VIEW - Modal form used for both adding and editing a professor.
 */
public class ProfessorFormView {

    private final Stage stage;
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField departmentField = new TextField();
    private final TextField officeField = new TextField();
    private final Button saveButton = Ui.button("Save Professor");
    private final Button cancelButton = Ui.secondaryButton("Cancel");

    public ProfessorFormView(Professor professor) {
        boolean editing = professor != null;

        stage = new Stage();
        stage.setTitle(editing ? "Edit Professor" : "Add Professor");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        if (editing) {
            idField.setText(professor.getId());
            idField.setDisable(true);
            nameField.setText(professor.getName());
            emailField.setText(professor.getEmail());
            departmentField.setText(professor.getDepartment());
            officeField.setText(professor.getOffice());
        } else {
            idField.setPromptText("e.g. P004");
        }

        nameField.setPromptText("Full name (e.g. Dr. Sara Ali)");
        emailField.setPromptText("name@university.edu");
        departmentField.setPromptText("e.g. Computer Science");
        officeField.setPromptText("e.g. A-101");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.addRow(0, new Label("Professor ID"), idField);
        form.addRow(1, new Label("Name"), nameField);
        form.addRow(2, new Label("Email"), emailField);
        form.addRow(3, new Label("Department"), departmentField);
        form.addRow(4, new Label("Office"), officeField);

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(18,
                Ui.heading(editing ? "Edit Professor" : "New Professor"),
                form, buttons);
        root.setPadding(new Insets(24));

        stage.setScene(new Scene(root, 480, 410));
    }

    public Professor readProfessor() {
        return new Professor(
                idField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                departmentField.getText().trim(),
                officeField.getText().trim()
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

    public TextField getDepartmentField() {
        return departmentField;
    }

    public TextField getOfficeField() {
        return officeField;
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
