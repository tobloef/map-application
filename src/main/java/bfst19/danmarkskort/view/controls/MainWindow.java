package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainWindow extends Scene {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TopMenu topMenu;
    @FXML
    private AddressSearchPane addressSearchPane;
    @FXML
    private MapPane mapPane;

    private Model model;
    private Parent root;

    public MainWindow(Parent root) throws IOException {
        super(root);
        this.root = root;
        // Setup FXML component
        URL url = ResourceLoader.getResource("rs:views/MainWindow.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize(Model model, Stage stage) {
        this.model = model;
        // Show window
        stage.setScene(this);
        stage.show();
        // On exit, do necessary cleanup, such as stopping other threads, etc.
        stage.setOnCloseRequest(event -> model.cleanup());
        // Initialize sub-components
        mapPane.initialize(model, addressSearchPane);
        topMenu.initialize(model, stage, root, this::toggleWayTypeSelector);
        addressSearchPane.initialize(model, borderPane, mapPane);
    }

    private void toggleWayTypeSelector() {
        if (borderPane.getRight() instanceof WaytypeSelectorPane) {
            borderPane.setRight(null);
        } else {
            WaytypeSelectorPane waytypeSelectorPane;
            try {
                waytypeSelectorPane = new WaytypeSelectorPane();
                waytypeSelectorPane.initialize(model);
                borderPane.setRight(waytypeSelectorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
