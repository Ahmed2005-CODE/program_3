package university.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import university.model.Course;
import university.model.Enrollment;
import university.model.EnrollmentRow;
import university.model.Student;
import university.service.CourseService;
import university.service.EnrollmentService;
import university.service.StudentService;
import university.view.EnrollmentView;
import university.view.Ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CONTROLLER - Enrollment and grade events.
 */
public class EnrollmentController {

    private final EnrollmentView view = new EnrollmentView();
    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    private final ObservableList<EnrollmentRow> rows = FXCollections.observableArrayList();

    public EnrollmentController() {
        reload();
        setupSearch();
        setupEnrollButton();
        setupGradeButton();
        setupRemoveButton();
        setupRefreshButton();
    }

    private void reload() {
        String studentIdBefore = currentStudentId();
        String courseCodeBefore = currentCourseCode();

        List<Student> students = studentService.loadStudents();
        List<Course> courses = courseService.loadCourses();

        view.getStudentCombo().setItems(FXCollections.observableArrayList(students));
        view.getCourseCombo().setItems(FXCollections.observableArrayList(courses));

        restoreStudentSelection(studentIdBefore, students);
        restoreCourseSelection(courseCodeBefore, courses);

        Map<String, String> studentNames = new HashMap<>();
        for (Student student : students) {
            studentNames.put(student.getId(), student.getName());
        }

        Map<String, String> courseTitles = new HashMap<>();
        for (Course course : courses) {
            courseTitles.put(course.getCode(), course.getTitle());
        }

        List<EnrollmentRow> newRows = new ArrayList<>();
        for (Enrollment enrollment : enrollmentService.loadEnrollments()) {
            newRows.add(new EnrollmentRow(
                    enrollment.getStudentId(),
                    enrollment.getCourseCode(),
                    studentNames.getOrDefault(
                            enrollment.getStudentId(), enrollment.getStudentId()),
                    courseTitles.getOrDefault(
                            enrollment.getCourseCode(), enrollment.getCourseCode()),
                    enrollment.getGrade()
            ));
        }

        rows.setAll(newRows);
        view.getSearchField().clear();
        view.getTable().setItems(rows);
        view.getSummary().setText(rows.size() + " enrollment(s)");
        view.getGradeField().clear();
    }

    private String currentStudentId() {
        Student student = view.getStudentCombo().getValue();
        return student == null ? null : student.getId();
    }

    private String currentCourseCode() {
        Course course = view.getCourseCombo().getValue();
        return course == null ? null : course.getCode();
    }

    private void restoreStudentSelection(String id, List<Student> students) {
        if (id == null) {
            return;
        }
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                view.getStudentCombo().setValue(student);
                return;
            }
        }
    }

    private void restoreCourseSelection(String code, List<Course> courses) {
        if (code == null) {
            return;
        }
        for (Course course : courses) {
            if (course.getCode().equalsIgnoreCase(code)) {
                view.getCourseCombo().setValue(course);
                return;
            }
        }
    }

    private void setupSearch() {
        view.getSearchField().textProperty().addListener(
                (observable, oldValue, newValue) -> filterRows(newValue));
    }

    private void filterRows(String searchText) {
        String search = searchText.toLowerCase().trim();

        if (search.isEmpty()) {
            view.getTable().setItems(rows);
            return;
        }

        ObservableList<EnrollmentRow> filtered = FXCollections.observableArrayList();
        for (EnrollmentRow row : rows) {
            if (row.getStudentName().toLowerCase().contains(search)
                    || row.getStudentId().toLowerCase().contains(search)
                    || row.getCourseTitle().toLowerCase().contains(search)
                    || row.getCourseCode().toLowerCase().contains(search)
                    || row.getGrade().toLowerCase().contains(search)) {
                filtered.add(row);
            }
        }
        view.getTable().setItems(filtered);
    }

    private void setupEnrollButton() {
        view.getEnrollButton().setOnAction(event -> enrollStudent());
    }

    private void enrollStudent() {
        Student student = view.getStudentCombo().getValue();
        Course course = view.getCourseCombo().getValue();

        if (student == null || course == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "Missing Selection", "Select both a student and a course.");
            return;
        }

        if (!enrollmentService.addEnrollment(student.getId(), course.getCode())) {
            Ui.error("This student is already enrolled in that course.");
            return;
        }

        reload();
        Ui.info("Success", student.getName() + " enrolled in " + course.getTitle() + ".");
    }

    private void setupGradeButton() {
        view.getGradeButton().setOnAction(event -> saveGrade());
    }

    private void saveGrade() {
        EnrollmentRow selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Select an enrollment row first.");
            return;
        }

        String grade = view.getGradeField().getText().trim();

        if (grade.isEmpty()) {
            Ui.error("Please enter a grade (e.g. A, B+, Pass).");
            return;
        }

        if (grade.contains(",")) {
            Ui.error("A grade cannot contain a comma.");
            return;
        }

        enrollmentService.setGrade(
                selected.getStudentId(), selected.getCourseCode(), grade);
        selected.setGrade(grade);

        view.getTable().refresh();
        view.getGradeField().clear();
        Ui.info("Success", "Grade saved successfully.");
    }

    private void setupRemoveButton() {
        view.getRemoveButton().setOnAction(event -> removeEnrollment());
    }

    private void removeEnrollment() {
        EnrollmentRow selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Select an enrollment row first.");
            return;
        }

        if (!Ui.confirm("Remove Enrollment",
                "Remove " + selected.getStudentName()
                        + " from " + selected.getCourseTitle() + "?")) {
            return;
        }

        enrollmentService.deleteEnrollment(
                selected.getStudentId(), selected.getCourseCode());
        reload();
        Ui.info("Success", "Enrollment removed successfully.");
    }

    private void setupRefreshButton() {
        view.getRefreshButton().setOnAction(event -> refresh());
    }

    public void refresh() {
        reload();
    }

    public int count() {
        return rows.size();
    }

    public EnrollmentView getView() {
        return view;
    }
}
