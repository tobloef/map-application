package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.address.Address;
import bfst19.danmarkskort.model.drawableModel.Rectangle;
import bfst19.danmarkskort.model.drawables.PolyRoad;
import bfst19.danmarkskort.model.routePlanning.Route;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.function.BiConsumer;

public class MapPane extends Pane {
    private static final float tooltipMaxDistance = 50f;
    private static final float tooltipMinZoom = 0.00003f;

    private double mouseX;
    private double mouseY;
    private Model model;
    private Tooltip tooltip;
    private ContextMenu contextMenu;
    private AddressSearchPane addressSearchPane;

    @FXML
    private MapCanvas mapCanvas;
    @FXML
    private Button toggleSearchButton;

    public MapPane() throws IOException {
        URL url = ResourceLoader.getResource("rs:views/Map.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize(Model model, AddressSearchPane addressSearchPane) {
        this.addressSearchPane = addressSearchPane;
        this.model = model;
        updateSearchToggleButtonText();
        // Address tooltip
        tooltip = new Tooltip();
        tooltip.setAutoHide(false);
        tooltip.setAutoFix(false);
        model.addMouseIdleObserver(idle -> {
            if (idle) {
                showTooltip();
            } else {
                hideTooltip();
            }
        });
        // Right-click menu
        MenuItem placePOIItem = new MenuItem("Add point of interest");
        placePOIItem.setOnAction(event -> model.addPOIAtCurrentMousePosition());
        MenuItem useAsStartItem = new MenuItem("Use as origin");
        useAsStartItem.setOnAction(e -> handleAddressSelect(AddressSearchPane::setStartText, Model::setStart));
        MenuItem useAsEndItem = new MenuItem("Use as destination");
        useAsEndItem.setOnAction(e -> handleAddressSelect(AddressSearchPane::setEndText, Model::setEnd));
        contextMenu = new ContextMenu(
                placePOIItem,
                useAsStartItem,
                useAsEndItem
        );
        mapCanvas.setOnContextMenuRequested(this::openContextMenu);
        // Initialize sub-components
        mapCanvas.initialize(model);
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        double factor = Math.pow(1.01, e.getDeltaY());
        mapCanvas.zoom(factor, e.getX(), e.getY());
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            mapCanvas.pan(e.getX() - mouseX, e.getY() - mouseY);
        }
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @FXML
    public void onMouseMoved(MouseEvent mouseEvent) {
        float localX = (float) mouseEvent.getX();
        float localY = (float) mouseEvent.getY();
        Point2D modelCoords = mapCanvas.screenToModelCoords(localX, localY);
        if (modelCoords == null) {
            return;
        }
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
        addressSearchPane.togglePanel();
        updateSearchToggleButtonText();
    }

    private void updateSearchToggleButtonText() {
        if (addressSearchPane.isEnabled()) {
            toggleSearchButton.setText("<<");
        } else {
            toggleSearchButton.setText(">>");
        }
    }

    private void openContextMenu(ContextMenuEvent event) {
        float mouseScreenX = model.getMouseScreenX();
        float mouseScreenY = model.getMouseScreenY();
        Point2D screenCoords = this.localToScreen(mouseScreenX, mouseScreenY);
        contextMenu.show(this, screenCoords.getX(), screenCoords.getY());
    }

    private void handleAddressSelect(BiConsumer<AddressSearchPane, String> searchSetter, BiConsumer<Model, PolyRoad> modelSetter) {
        PolyRoad road = model.getClosestRoad();
        modelSetter.accept(model, road);
        searchSetter.accept(addressSearchPane, road.getStreetName());
        Route route = model.getShortestPath();
        if (route != null) {
            addressSearchPane.updateRouteDescription(route);
        }
    }

    private void hideTooltip() {
        if (tooltip != null) {
            tooltip.hide();
        }
    }

    private void showTooltip() {
        if (!model.getIsMouseInWindow()) {
            return;
        }
        if (tooltip == null) {
            return;
        }
        if (mapCanvas.getDegreesLatitudePerPixel() > tooltipMinZoom) {
            return;
        }
        float mouseModelX = model.getMouseModelX();
        float mouseModelY = model.getMouseModelY();
        float mouseScreenX = model.getMouseScreenX();
        float mouseScreenY = model.getMouseScreenY();
        PolyRoad road = model.getClosestRoad(mouseModelX, mouseModelY);
        if (road == null) {
            return;
        }
        if (road.getStreetName() == null) {
            return;
        }
        double distance = Math.sqrt(road.euclideanDistanceSquaredTo(mouseModelX, mouseModelY));
        distance = distance / mapCanvas.getDegreesLatitudePerPixel();
        if (distance > tooltipMaxDistance) {
            return;
        }
        Platform.runLater(() -> {
            tooltip.setText(road.getStreetName());
            Point2D screenCoords = this.localToScreen(mouseScreenX, mouseScreenY);
            tooltip.show(this, screenCoords.getX(), screenCoords.getY() - 30);
        });
    }

    public void panViewToAddress(Address address) {
        Rectangle boundingBox = new Rectangle(
                address.getLon(),
                address.getLat(),
                address.getLon(),
                address.getLat()
        );
        panToBoundingBox(boundingBox);
    }

    public void panViewToRoute(Route route) {
        Rectangle routeBBox = route.getBoundingBox();
        panToBoundingBox(routeBBox);
        //TODO: get zoom to fit route in view.
        //double sizeDelta = routeBBox.getSizeLargestDelta(screenBounds) / getDegreesLatitudePerPixel();
        //System.out.println(sizeDelta);
        //zoom(sizeDelta, routeBBox.getMiddleX(), routeBBox.getMiddleY() );
    }

    private void panToBoundingBox(Rectangle boundingBox) {
        Rectangle screenBounds = mapCanvas.getScreenBounds();
        double x = -(boundingBox.getMiddleX() - screenBounds.getMiddleX()) / mapCanvas.getDegreesLatitudePerPixel();
        double y = (boundingBox.getMiddleY() - screenBounds.getMiddleY()) / mapCanvas.getDegreesLatitudePerPixel();
        mapCanvas.pan(x, y);
    }
}
