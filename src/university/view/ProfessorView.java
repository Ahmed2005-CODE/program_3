package university.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import university.model.Professor;

/**
 * VIEW - Professor list, search field and action buttons.
 */
public class ProfessorView {

    private final BorderPane root = new BorderPane();

    private final TextField searchField = new TextField();
    private final TableView<Professor> table = new TableView<>();

    private final Button addButton = Ui.button("Add Professor");
    private final Button editButton = Ui.secondaryButton("Edit");
    private final Button deleteButton = Ui.dangerButton("Delete");
    private final Button refreshButton = Ui.secondaryButton("Refresh");

    public ProfessorView() {
        searchField.setPromptText("Search by name, ID, email or department...");
        searchField.setPrefWidth(300);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.getColumns().addAll(
                Ui.column("ID", "id", 90),
                Ui.column("Name", "name", 180),
                Ui.column("Email", "email", 220),
                Ui.column("Department", "department", 200),
                Ui.column("Office", "office", 110)
        );
        table.setPlaceholder(new Label("No professors found."));

        root.setTop(createToolbar());
        root.setCenter(table);
    }

    private VBox createToolbar() {
        VBox toolbar = new VBox(10);
        toolbar.setPadding(new Insets(20, 20, 12, 20));

        VBox titles = new VBox(4);
        titles.getChildren().addAll(
                Ui.heading("Professors"),
                Ui.caption("Deleting a professor unassigns them from their courses.")
        );

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actions.getChildren().addAll(
                searchField,
                spacer,
                addButton,
                editButton,
                deleteButton,
                refreshButton
        );

        toolbar.getChildren().addAll(titles, actions);
        return toolbar;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public TableView<Professor> getTable() {
        return table;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public BorderPane getRoot() {
        return root;
    }
}
