package university.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * VIEW - Small shared helpers so all screens look consistent.
 */
public final class Ui {

    private Ui() {
    }

    public static Button button(String text) {
        return styledButton(text, "#2563eb");
    }

    public static Button secondaryButton(String text) {
        return styledButton(text, "#475569");
    }

    public static Button dangerButton(String text) {
        return styledButton(text, "#dc2626");
    }

    private static Button styledButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefHeight(34);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );
        return button;
    }

    public static Label heading(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        return label;
    }

    public static Label caption(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
        return label;
    }

    public static <S, T> TableColumn<S, T> column(
            String title, String property, double width) {
        TableColumn<S, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    public static void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void error(String message) {
        alert(Alert.AlertType.ERROR, "Invalid Data", message);
    }

    public static void info(String title, String message) {
        alert(Alert.AlertType.INFORMATION, title, message);
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait()
                .filter(button -> button == javafx.scene.control.ButtonType.OK)
                .isPresent();
    }

}
