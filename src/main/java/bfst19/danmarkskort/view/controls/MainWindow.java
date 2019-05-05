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
    private AddressSearch addressSearch;
    @FXML
    private Map map;

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
        map.initialize(model, addressSearch);
        topMenu.initialize(model, stage, root, this::toggleWayTypeSelector);
        addressSearch.initialize(model, borderPane, map);
    }

    private void toggleWayTypeSelector() {
        if (borderPane.getRight() instanceof WaytypeSelector) {
            borderPane.setRight(null);
        } else {
            WaytypeSelector waytypeSelector;
            try {
                waytypeSelector = new WaytypeSelector();
                waytypeSelector.initialize(model);
                borderPane.setRight(waytypeSelector);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
