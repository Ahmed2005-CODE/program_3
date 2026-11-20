package university.controller;

import university.view.MainView;

/**
 * CONTROLLER - Composes the module controllers into one application window
 * and keeps the dashboard statistics in sync with the files.
 */
public class MainController {

    private final MainView view = new MainView();

    private final StudentController studentController = new StudentController();
    private final CourseController courseController = new CourseController();
    private final ProfessorController professorController = new ProfessorController();
    private final EnrollmentController enrollmentController = new EnrollmentController();

    public MainController() {
        view.addModuleTab("Students", studentController.getView().getRoot());
        view.addModuleTab("Courses", courseController.getView().getRoot());
        view.addModuleTab("Professors", professorController.getView().getRoot());
        view.addModuleTab("Enrollments", enrollmentController.getView().getRoot());

        view.getRefreshButton().setOnAction(event -> refreshAll());

        refreshAll();
    }

    private void refreshAll() {
        studentController.refresh();
        courseController.refresh();
        professorController.refresh();
        enrollmentController.refresh();

        view.setStats(
                studentController.count(),
                courseController.count(),
                professorController.count(),
                enrollmentController.count()
        );
    }

    public MainView getView() {
        return view;
    }
}
