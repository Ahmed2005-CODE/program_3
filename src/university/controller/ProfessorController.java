package university.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import university.model.Professor;
import university.service.CourseService;
import university.service.ProfessorService;
import university.view.ProfessorFormView;
import university.view.ProfessorView;
import university.view.Ui;

/**
 * CONTROLLER - Handles all professor UI events and validation.
 */
public class ProfessorController {

    private final ProfessorView view = new ProfessorView();
    private final ProfessorService service = new ProfessorService();
    private final CourseService courseService = new CourseService();

    private final ObservableList<Professor> allProfessors =
            FXCollections.observableArrayList(service.loadProfessors());

    public ProfessorController() {
        view.getTable().setItems(allProfessors);
        setupSearch();
        setupAddButton();
        setupEditButton();
        setupDeleteButton();
        setupRefreshButton();
    }

    private void setupSearch() {
        view.getSearchField().textProperty().addListener(
                (observable, oldValue, newValue) -> filterProfessors(newValue));
    }

    private void filterProfessors(String searchText) {
        String search = searchText.toLowerCase().trim();

        if (search.isEmpty()) {
            view.getTable().setItems(allProfessors);
            return;
        }

        ObservableList<Professor> filtered = FXCollections.observableArrayList();
        for (Professor professor : allProfessors) {
            if (professor.getId().toLowerCase().contains(search)
                    || professor.getName().toLowerCase().contains(search)
                    || professor.getEmail().toLowerCase().contains(search)
                    || professor.getDepartment().toLowerCase().contains(search)) {
                filtered.add(professor);
            }
        }
        view.getTable().setItems(filtered);
    }

    private void setupAddButton() {
        view.getAddButton().setOnAction(event -> addProfessor());
    }

    private void addProfessor() {
        ProfessorFormView form = new ProfessorFormView(null);

        form.getCancelButton().setOnAction(event -> form.getStage().close());
        form.getSaveButton().setOnAction(event -> saveNewProfessor(form));
        form.show();
    }

    private void saveNewProfessor(ProfessorFormView form) {
        Professor professor = form.readProfessor();

        if (!validate(professor, true)) {
            return;
        }

        service.addProfessor(professor);
        allProfessors.add(professor);

        view.getSearchField().clear();
        view.getTable().setItems(allProfessors);
        view.getTable().refresh();

        form.getStage().close();
        Ui.info("Success", "Professor added successfully.");
    }

    private void setupEditButton() {
        view.getEditButton().setOnAction(event -> editSelectedProfessor());
    }

    private void editSelectedProfessor() {
        Professor selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Please select a professor first.");
            return;
        }

        ProfessorFormView form = new ProfessorFormView(selected);

        form.getCancelButton().setOnAction(event -> form.getStage().close());
        form.getSaveButton().setOnAction(event -> saveEditedProfessor(selected, form));
        form.show();
    }

    private void saveEditedProfessor(Professor oldProfessor, ProfessorFormView form) {
        Professor updated = form.readProfessor();

        if (!validate(updated, false)) {
            return;
        }

        service.updateProfessor(oldProfessor, updated);

        int index = allProfessors.indexOf(oldProfessor);
        if (index >= 0) {
            allProfessors.set(index, updated);
        } else {
            allProfessors.add(updated);
        }

        view.getTable().refresh();
        form.getStage().close();
        Ui.info("Success", "Professor updated successfully.");
    }

    private boolean validate(Professor professor, boolean adding) {
        if (professor.getId().isEmpty() || professor.getName().isEmpty()) {
            Ui.error("Professor ID and name are required.");
            return false;
        }

        if (!professor.getEmail().isEmpty() && !professor.getEmail().contains("@")) {
            Ui.error("Please enter a valid email address.");
            return false;
        }

        if (professor.getId().contains(",") || professor.getName().contains(",")
                || professor.getEmail().contains(",")
                || professor.getDepartment().contains(",")
                || professor.getOffice().contains(",")) {
            Ui.error("Fields cannot contain a comma (it is the file delimiter).");
            return false;
        }

        if (adding && service.exists(professor.getId())) {
            Ui.error("A professor with ID " + professor.getId() + " already exists.");
            return false;
        }

        return true;
    }

    private void setupDeleteButton() {
        view.getDeleteButton().setOnAction(event -> deleteSelectedProfessor());
    }

    private void deleteSelectedProfessor() {
        Professor selected = view.getTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            Ui.alert(Alert.AlertType.WARNING,
                    "No Selection", "Please select a professor first.");
            return;
        }

        if (!Ui.confirm("Delete Professor",
                "Delete " + selected.getName() + "? Their courses will be unassigned.")) {
            return;
        }

        service.deleteProfessor(selected.getId());
        courseService.clearProfessor(selected.getId());
        allProfessors.remove(selected);

        view.getSearchField().clear();
        view.getTable().setItems(allProfessors);
        Ui.info("Success", "Professor deleted successfully.");
    }

    private void setupRefreshButton() {
        view.getRefreshButton().setOnAction(event -> refresh());
    }

    public void refresh() {
        allProfessors.setAll(service.loadProfessors());
        view.getSearchField().clear();
        view.getTable().setItems(allProfessors);
    }

    public int count() {
        return allProfessors.size();
    }

    public ProfessorView getView() {
        return view;
    }
}
