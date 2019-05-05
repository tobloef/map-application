package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainWindow extends Scene {
    @FXML
    private MapCanvas mapCanvas;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button toggleSearchButton;
    @FXML
    private TopMenu topMenu;
    @FXML
    private AddressSearch addressSearch;

    private Model model;
    private double x, y;
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
        mapCanvas.initialize(model);
        topMenu.initialize(model, stage, root, this::toggleWayTypeSelector);
        addressSearch.initialize(model, borderPane, mapCanvas);
        updateSearchToggleButtonText();
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

    @FXML
    private void onScroll(ScrollEvent e) {
        double factor = Math.pow(1.01, e.getDeltaY());
        mapCanvas.zoom(factor, e.getX(), e.getY());
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            mapCanvas.pan(e.getX() - x, e.getY() - y);
        }
        x = e.getX();
        y = e.getY();
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @FXML
    public void onMouseMoved(MouseEvent mouseEvent) {
        float localX = (float) mouseEvent.getX();
        float localY = (float) mouseEvent.getY();
        Point2D modelCoords = mapCanvas.modelCoords(localX, localY);
        model.setMouseModelCoords((float) modelCoords.getX(), (float) modelCoords.getY());
        model.setMouseScreenCoords(localX, localY);
        model.updateMouseIdle();
    }

    @FXML
    public void onMouseEntered(MouseEvent mouseEvent) {
        model.setIsMouseInWindow(true);
    }

    @FXML
    public void onMouseExited(MouseEvent mouseEvent) {
        model.setIsMouseInWindow(false);
    }


    @FXML
    private void toggleSearchPane(ActionEvent actionEvent) {
        addressSearch.togglePanel();
        updateSearchToggleButtonText();
    }

    private void updateSearchToggleButtonText() {
        if (addressSearch.isEnabled()) {
            toggleSearchButton.setText("<<");
        } else {
            toggleSearchButton.setText(">>");
        }
    }
}
