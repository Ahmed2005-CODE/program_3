package university;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import university.controller.MainController;

/**
 * ENTRY POINT - Mirrors the sample project's Main class:
 * create a controller and place its view's root inside a Scene.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        MainController controller = new MainController();

        Scene scene = new Scene(
                controller.getView().getRoot(),
                1150,
                720
        );

        stage.setTitle("University Management System");
        stage.setScene(scene);
        stage.setMinWidth(950);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
