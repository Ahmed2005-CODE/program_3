package university.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import university.model.Student;
import university.service.EnrollmentService;
import university.service.StudentService;
import university.view.StudentFormView;
import university.view.StudentView;
import university.view.Ui;

/**
 * CONTROLLER - Handles all student UI events and validation.
 */
public class StudentController {

    private final StudentView view = new StudentView();
    private final StudentService service = new StudentService();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    private final ObservableList<Student> allStudents =
            FXCollections.observableArrayList(service.loadStudents());

    public StudentController() {
        view.getTable().setItems(allStudents);
        setupSearch();
        setupAddButton();
        setupEditButton();
        setupDeleteButton();
        setupRefreshButton();
    }

    private void setupSearch() {
        view.getSearchField().textProperty().addListener(
                (observable, oldValue, newValue) -> filterStudents(newValue));
    }

    private void filterStudents(String searchText) {
        String search = searchText.toLowerCase().trim();

        if (search.isEmpty()) {
            view.getTable().setItems(allStudents);
            return;
        }

        ObservableList<Student> filtered = FXCollections.observableArrayList();
        for (Student student : allStudents) {
            if (student.getId().toLowerCase().contains(search)
                    || student.getName().toLowerCase().contains(search)
                    || student.getEmail().toLowerCase().contains(search)
                    || student.getMajor().toLowerCase().contains(search)) {
                filtered.add(student);
            }
        }
        view.getTable().setItems(filtered);
    }

    private void setupAddButton() {
        view.getAddButton().setOnAction(event -> addStudent());
    }

    private void addStudent() {
        StudentFormView form = new StudentFormView(null);

        form.getCancelButton().setOnAction(event -> form.getStage().close());
        form.getSaveButton().setOnAction(event -> saveNewStudent(form));
        form.show();
    }

    private void saveNewStudent(StudentFormView form) {
        try {
            Student student = form.readStudent();

            if (!validate(student, true)) {
                return;
            }

            service.addStudent(student);
            allStudents.add(student);

            view.getSearchField().clear();
            view.getTable().setItems(allStudents);
            view.getTable().refresh();

            form.getStage().close();
            Ui.info("Success", "Student added successfully.");

        } catch (NumberFormatException e) {
            Ui.error("Year must be a whole number (1 - 5).");
        }
    }

    private void setupEditButton() {
        view.getEditButton().setOnAction(event -> editSelectedStudent());
    }

    private void editSelectedStudent() {
        Student selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Please select a student first.");
            return;
        }

        StudentFormView form = new StudentFormView(selected);

        form.getCancelButton().setOnAction(event -> form.getStage().close());
        form.getSaveButton().setOnAction(event -> saveEditedStudent(selected, form));
        form.show();
    }

    private void saveEditedStudent(Student oldStudent, StudentFormView form) {
        try {
            Student updated = form.readStudent();

            if (!validate(updated, false)) {
                return;
            }

            service.updateStudent(oldStudent, updated);

            int index = allStudents.indexOf(oldStudent);
            if (index >= 0) {
                allStudents.set(index, updated);
            } else {
                allStudents.add(updated);
            }

            view.getTable().refresh();
            form.getStage().close();
            Ui.info("Success", "Student updated successfully.");

        } catch (NumberFormatException e) {
            Ui.error("Year must be a whole number (1 - 5).");
        }
    }

    private boolean validate(Student student, boolean adding) {
        if (student.getId().isEmpty() || student.getName().isEmpty()
                || student.getMajor().isEmpty()) {
            Ui.error("ID, name and major are required.");
            return false;
        }

        if (!student.getEmail().contains("@")) {
            Ui.error("Please enter a valid email address.");
            return false;
        }

        if (student.getYear() < 1 || student.getYear() > 5) {
            Ui.error("Year must be between 1 and 5.");
            return false;
        }

        if (containsComma(student)) {
            Ui.error("Fields cannot contain a comma (it is the file delimiter).");
            return false;
        }

        if (adding && service.exists(student.getId())) {
            Ui.error("A student with ID " + student.getId() + " already exists.");
            return false;
        }

        return true;
    }

    private boolean containsComma(Student student) {
        return student.getId().contains(",")
                || student.getName().contains(",")
                || student.getEmail().contains(",")
                || student.getMajor().contains(",");
    }

    private void setupDeleteButton() {
        view.getDeleteButton().setOnAction(event -> deleteSelectedStudent());
    }

    private void deleteSelectedStudent() {
        Student selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Please select a student first.");
            return;
        }

        if (!Ui.confirm("Delete Student",
                "Delete " + selected.getName() + " and their enrollments?")) {
            return;
        }

        service.deleteStudent(selected.getId());
        enrollmentService.deleteByStudent(selected.getId());
        allStudents.remove(selected);

        view.getSearchField().clear();
        view.getTable().setItems(allStudents);
        Ui.info("Success", "Student deleted successfully.");
    }

    private void setupRefreshButton() {
        view.getRefreshButton().setOnAction(event -> refresh());
    }

    public void refresh() {
        allStudents.setAll(service.loadStudents());
        view.getSearchField().clear();
        view.getTable().setItems(allStudents);
    }

    public int count() {
        return allStudents.size();
    }

    public StudentView getView() {
        return view;
    }
}
