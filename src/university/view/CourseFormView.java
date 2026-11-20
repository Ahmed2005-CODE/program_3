package university.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import university.model.Course;
import university.model.Professor;

import java.util.List;

/**
 * VIEW - Modal form used for both adding and editing a course.
 */
public class CourseFormView {

    private static final String NO_PROFESSOR = "(none)";

    private final Stage stage;
    private final TextField codeField = new TextField();
    private final TextField titleField = new TextField();
    private final TextField creditsField = new TextField();
    private final ComboBox<String> professorCombo = new ComboBox<>();
    private final Button saveButton = Ui.button("Save Course");
    private final Button cancelButton = Ui.secondaryButton("Cancel");

    public CourseFormView(Course course, List<Professor> professors) {
        boolean editing = course != null;

        stage = new Stage();
        stage.setTitle(editing ? "Edit Course" : "Add Course");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        professorCombo.getItems().add(NO_PROFESSOR);
        for (Professor professor : professors) {
            professorCombo.getItems().add(professor.getId());
        }
        professorCombo.setPrefWidth(250);

        if (editing) {
            codeField.setText(course.getCode());
            codeField.setDisable(true);
            titleField.setText(course.getTitle());
            creditsField.setText(String.valueOf(course.getCredits()));
            String professorId = course.getProfessorId();
            professorCombo.setValue(
                    professorId == null || professorId.isEmpty()
                            ? NO_PROFESSOR : professorId);
        } else {
            professorCombo.setValue(NO_PROFESSOR);
            codeField.setPromptText("e.g. CS301");
        }

        titleField.setPromptText("Course title");
        creditsField.setPromptText("e.g. 3");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.addRow(0, new Label("Course Code"), codeField);
        form.addRow(1, new Label("Title"), titleField);
        form.addRow(2, new Label("Credits"), creditsField);
        form.addRow(3, new Label("Professor"), professorCombo);

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(18,
                Ui.heading(editing ? "Edit Course" : "New Course"), form, buttons);
        root.setPadding(new Insets(24));

        stage.setScene(new Scene(root, 480, 380));
    }

    /** Builds a Course from the fields; may throw NumberFormatException. */
    public Course readCourse() {
        return new Course(
                codeField.getText().trim(),
                titleField.getText().trim(),
                Integer.parseInt(creditsField.getText().trim()),
                getProfessorId()
        );
    }

    public String getProfessorId() {
        String value = professorCombo.getValue();
        if (value == null || value.equals(NO_PROFESSOR)) {
            return "";
        }
        return value;
    }

    public TextField getCodeField() {
        return codeField;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public TextField getCreditsField() {
        return creditsField;
    }

    public ComboBox<String> getProfessorCombo() {
        return professorCombo;
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
