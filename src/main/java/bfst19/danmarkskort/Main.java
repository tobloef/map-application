package bfst19.danmarkskort;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.controls.controls.MainWindow;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model(getParameters().getRaw());
        Group root = new Group();
        MainWindow mainWindow = new MainWindow(root);
        mainWindow.initialize(model, stage);
    }
}
