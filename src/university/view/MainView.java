package university.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * VIEW - Application shell: header, dashboard and module tabs.
 */
public class MainView {

    private final BorderPane root = new BorderPane();
    private final TabPane tabs = new TabPane();

    private final Label studentCount = new Label("0");
    private final Label courseCount = new Label("0");
    private final Label professorCount = new Label("0");
    private final Label enrollmentCount = new Label("0");

    private final Button refreshButton = Ui.secondaryButton("Refresh");

    public MainView() {
        root.setStyle("-fx-background-color: #f8fafc;");
        root.setTop(createHeader());
        root.setCenter(tabs);

        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(createDashboardTab());
    }

    private Node createHeader() {
        Label title = new Label("University Management System");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("JavaFX  •  MVC architecture  •  Text-file storage");
        subtitle.setStyle("-fx-text-fill: #cbd5e1;");

        VBox branding = new VBox(3);
        branding.getChildren().addAll(title, subtitle);

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 28, 16, 28));
        header.setStyle("-fx-background-color: #1e293b;");
        header.getChildren().addAll(branding, refreshButton);

        HBox.setHgrow(branding, Priority.ALWAYS);
        HBox.setHgrow(refreshButton, Priority.NEVER);
        return header;
    }

    private Tab createDashboardTab() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(28));

        Label welcome = Ui.heading("Dashboard");
        Label hint = Ui.caption(
                "Manage students, courses, professors and grades. " +
                "All data is stored in the Data folder."
        );

        GridPane cards = new GridPane();
        cards.setHgap(16);
        cards.setVgap(16);

        cards.add(new javafx.scene.layout.VBox(
                4, new Label("Students"), studentCount), 0, 0);
        cards.add(new javafx.scene.layout.VBox(
                4, new Label("Courses"), courseCount), 1, 0);
        cards.add(new javafx.scene.layout.VBox(
                4, new Label("Professors"), professorCount), 0, 1);
        cards.add(new javafx.scene.layout.VBox(
                4, new Label("Enrollments"), enrollmentCount), 1, 1);

        for (javafx.scene.Node node : cards.getChildren()) {
            node.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #e2e8f0;" +
                    "-fx-border-radius: 10;" +
                    "-fx-padding: 18;"
            );
            ((javafx.scene.layout.VBox) node).setPrefWidth(220);
        }

        styleCount(studentCount);
        styleCount(courseCount);
        styleCount(professorCount);
        styleCount(enrollmentCount);

        content.getChildren().addAll(welcome, hint, cards);

        Tab tab = new Tab("Dashboard", content);
        tab.setClosable(false);
        return tab;
    }

    private void styleCount(Label label) {
        label.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
    }

    public void addModuleTab(String title, Node content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        tabs.getTabs().add(tab);
    }

    public void setStats(int students, int courses, int professors, int enrollments) {
        studentCount.setText(String.valueOf(students));
        courseCount.setText(String.valueOf(courses));
        professorCount.setText(String.valueOf(professors));
        enrollmentCount.setText(String.valueOf(enrollments));
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public BorderPane getRoot() {
        return root;
    }
}
