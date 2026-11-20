package university.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import university.model.Course;
import university.model.EnrollmentRow;
import university.model.Student;

/**
 * VIEW - Enroll students into courses and enter grades.
 */
public class EnrollmentView {

    private final BorderPane root = new BorderPane();

    private final ComboBox<Student> studentCombo = new ComboBox<>();
    private final ComboBox<Course> courseCombo = new ComboBox<>();
    private final TextField gradeField = new TextField();
    private final TextField searchField = new TextField();
    private final Label summary = Ui.caption("");

    private final TableView<EnrollmentRow> table = new TableView<>();

    private final Button enrollButton = Ui.button("Enroll Student");
    private final Button gradeButton = Ui.secondaryButton("Save Grade");
    private final Button removeButton = Ui.dangerButton("Remove");
    private final Button refreshButton = Ui.secondaryButton("Refresh");

    public EnrollmentView() {
        studentCombo.setPrefWidth(230);
        courseCombo.setPrefWidth(260);
        gradeField.setPrefWidth(80);
        gradeField.setPromptText("Grade");
        searchField.setPromptText("Search student or course...");
        searchField.setPrefWidth(260);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.getColumns().addAll(
                Ui.column("Student", "studentName", 180),
                Ui.column("Student ID", "studentId", 100),
                Ui.column("Course", "courseTitle", 240),
                Ui.column("Code", "courseCode", 90),
                Ui.column("Grade", "grade", 90)
        );
        table.setPlaceholder(new Label("No enrollments found."));

        root.setTop(createToolbar());
        root.setCenter(table);
    }

    private VBox createToolbar() {
        VBox toolbar = new VBox(12);
        toolbar.setPadding(new Insets(20, 20, 12, 20));

        VBox titles = new VBox(4);
        titles.getChildren().addAll(
                Ui.heading("Enrollments & Grades"),
                Ui.caption("Enroll a student in a course, then assign a grade.")
        );

        HBox form = new HBox(10);
        form.setAlignment(Pos.CENTER_LEFT);
        form.getChildren().addAll(
                new Label("Student"), studentCombo,
                new Label("Course"), courseCombo,
                new Label("Grade"), gradeField,
                enrollButton
        );

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actions.getChildren().addAll(
                searchField,
                spacer,
                summary,
                gradeButton,
                removeButton,
                refreshButton
        );

        toolbar.getChildren().addAll(titles, form, actions);
        return toolbar;
    }

    public ComboBox<Student> getStudentCombo() {
        return studentCombo;
    }

    public ComboBox<Course> getCourseCombo() {
        return courseCombo;
    }

    public TextField getGradeField() {
        return gradeField;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Label getSummary() {
        return summary;
    }

    public TableView<EnrollmentRow> getTable() {
        return table;
    }

    public Button getEnrollButton() {
        return enrollButton;
    }

    public Button getGradeButton() {
        return gradeButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public BorderPane getRoot() {
        return root;
    }
}
