package university.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import university.model.Course;
import university.model.Professor;
import university.service.CourseService;
import university.service.EnrollmentService;
import university.service.ProfessorService;
import university.view.CourseFormView;
import university.view.CourseView;
import university.view.Ui;

import java.util.List;

/**
 * CONTROLLER - Handles all course UI events and validation.
 */
public class CourseController {

    private final CourseView view = new CourseView();
    private final CourseService service = new CourseService();
    private final ProfessorService professorService = new ProfessorService();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    private final ObservableList<Course> allCourses =
            FXCollections.observableArrayList(service.loadCourses());

    public CourseController() {
        view.getTable().setItems(allCourses);
        view.getAddButton().setOnAction(event -> addCourse());
        view.getEditButton().setOnAction(event -> editSelectedCourse());
        view.getDeleteButton().setOnAction(event -> deleteSelectedCourse());
        view.getRefreshButton().setOnAction(event -> refresh());
        setupSearch();
    }

    private void setupSearch() {
        view.getSearchField().textProperty().addListener((observable, oldValue, text) -> {
            String search = text.toLowerCase().trim();
            ObservableList<Course> filtered = FXCollections.observableArrayList();
            for (Course course : allCourses) {
                if ((course.getCode() + course.getTitle() + course.getProfessorId())
                        .toLowerCase().contains(search)) {
                    filtered.add(course);
                }
            }
            view.getTable().setItems(search.isEmpty() ? allCourses : filtered);
        });
    }

    private List<Professor> professors() {
        return professorService.loadProfessors();
    }


    private void addCourse() {
        CourseFormView form = new CourseFormView(null, professors());

        form.getCancelButton().setOnAction(event -> form.getStage().close());
        form.getSaveButton().setOnAction(event -> saveNewCourse(form));
        form.show();
    }

    private void saveNewCourse(CourseFormView form) {
        try {
            Course course = form.readCourse();

            if (!validate(course, true)) {
                return;
            }

            service.addCourse(course);
            allCourses.add(course);

            view.getSearchField().clear();
            view.getTable().setItems(allCourses);
            view.getTable().refresh();

            form.getStage().close();
            Ui.info("Success", "Course added successfully.");

        } catch (NumberFormatException e) {
            Ui.error("Credits must be a whole number.");
        }
    }


    private void editSelectedCourse() {
        Course selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Please select a course first.");
            return;
        }

        CourseFormView form = new CourseFormView(selected, professors());

        form.getCancelButton().setOnAction(event -> form.getStage().close());
        form.getSaveButton().setOnAction(event -> saveEditedCourse(selected, form));
        form.show();
    }

    private void saveEditedCourse(Course oldCourse, CourseFormView form) {
        try {
            Course updated = form.readCourse();

            if (!validate(updated, false)) {
                return;
            }

            service.updateCourse(oldCourse, updated);

            int index = allCourses.indexOf(oldCourse);
            if (index >= 0) {
                allCourses.set(index, updated);
            } else {
                allCourses.add(updated);
            }

            view.getTable().refresh();
            form.getStage().close();
            Ui.info("Success", "Course updated successfully.");

        } catch (NumberFormatException e) {
            Ui.error("Credits must be a whole number.");
        }
    }

    private boolean validate(Course course, boolean adding) {
        if (course.getCode().isEmpty() || course.getTitle().isEmpty()) {
            Ui.error("Course code and title are required.");
            return false;
        }

        if (course.getCredits() < 1 || course.getCredits() > 10) {
            Ui.error("Credits must be between 1 and 10.");
            return false;
        }

        if (course.getCode().contains(",") || course.getTitle().contains(",")) {
            Ui.error("Fields cannot contain a comma (it is the file delimiter).");
            return false;
        }

        if (adding && service.exists(course.getCode())) {
            Ui.error("A course with code " + course.getCode() + " already exists.");
            return false;
        }

        return true;
    }


    private void deleteSelectedCourse() {
        Course selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Please select a course first.");
            return;
        }

        if (!Ui.confirm("Delete Course",
                "Delete " + selected.getTitle() + " and its enrollments?")) {
            return;
        }

        service.deleteCourse(selected.getCode());
        enrollmentService.deleteByCourse(selected.getCode());
        allCourses.remove(selected);

        view.getSearchField().clear();
        view.getTable().setItems(allCourses);
        Ui.info("Success", "Course deleted successfully.");
    }


    public void refresh() {
        allCourses.setAll(service.loadCourses());
        view.getSearchField().clear();
        view.getTable().setItems(allCourses);
    }

    public int count() {
        return allCourses.size();
    }

    public CourseView getView() {
        return view;
    }
}
