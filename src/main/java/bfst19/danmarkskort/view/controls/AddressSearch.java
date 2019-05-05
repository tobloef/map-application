package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.Route;
import bfst19.danmarkskort.model.VehicleType;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddressSearch extends BorderPane {
    private final int MAX_SUGGESTIONS = 10;
    private final int DEBOUNCE_DELAY = 300;

    private boolean enabled = false;
    private Model model;
    private bfst19.danmarkskort.model.AddressSearch addressSearch;
    private Map map;
    private BorderPane parent;
    private boolean dontReopenPopup = false;

    @FXML
    private ScrollPane directionsScrollPane;
    @FXML
    private VBox addressLayoutBox;
    @FXML
    private TextField startField;
    @FXML
    private TextField endField;
    @FXML
    private ComboBox<VehicleType> vehicleSelection;

    public AddressSearch() throws IOException {
        URL url = ResourceLoader.getResource("rs:views/AddressSearch.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize(Model model, BorderPane parent, Map map) {
        this.parent = parent;
        this.model = model;
        this.map = map;
        addressSearch = new bfst19.danmarkskort.model.AddressSearch(model.getAddressData());
        togglePanel();
        vehicleSelection.getItems().setAll(VehicleType.values());
        vehicleSelection.setOnAction(event -> {
            model.setVehicleType(vehicleSelection.getValue());
            updateRouteDescription(model.getShortestPath());
        });
        vehicleSelection.setValue(VehicleType.CAR);
    }

    public void togglePanel() {
        if (enabled) {
            removeUI();
        } else {
            addUI();
        }
        enabled = !enabled;
    }

    public void setStartText(String text) {
        dontReopenPopup = true;
        startField.setText(text);
    }


    public void setEndText(String text) {
        dontReopenPopup = true;
        endField.setText(text);
    }

    private void addUI() {
        parent.setLeft(this);
        setCenter(addressLayoutBox);
        ContextMenu startPopup = new ContextMenu();
        ContextMenu endPopup = new ContextMenu();
        initializePopup(startPopup, startField, endPopup, address -> model.setStart(address));
        initializePopup(endPopup, endField, startPopup, address -> model.setEnd(address));
    }

    private void initializePopup(ContextMenu popup, TextField field, ContextMenu otherPopup, Consumer<Address> addressHandler) {
        popup.setAutoHide(false);
        popup.setAutoFix(false);
        popup.setHideOnEscape(false);
        Duration pauseTime = Duration.millis(DEBOUNCE_DELAY);
        PauseTransition debounce = new PauseTransition(pauseTime);
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            field.positionCaret(newValue.length());
            debounce.setOnFinished(event -> {
                updatePopup(popup, field, otherPopup, newValue, (address) -> {
                    if (address == null) {
                        return;
                    }
                    addressHandler.accept(address);
                    Route route = model.getShortestPath();
                    if (route != null && route.size() > 1) {
                        map.panViewToRoute(route);
                    } else {
                        map.panViewToAddress(address);
                    }
                    updateRouteDescription(route);
                });
                dontReopenPopup = false;
            });
            debounce.playFromStart();
        });
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                popup.hide();
            }
        });
    }

    public void updateRouteDescription(Route route) {
        if (route == null) {
            return;
        }
        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        for (String text : route.getTextDescription()) {
            Label label = new Label(" • " + text);
            label.setStyle("-fx-font-size: 15px");
            label.setWrapText(true);
            vbox.getChildren().add(label);
            vbox.getChildren().add(new Separator());
        }
        directionsScrollPane.setContent(vbox);
    }

    private void removeUI() {
        addressLayoutBox.getChildren().removeAll();
        setCenter(null);
        parent.setLeft(null);
    }

    private void updatePopup(ContextMenu popup, TextField field, ContextMenu otherPopup, String newValue, Consumer<Address> addressHandler) {
        if (otherPopup != null && otherPopup.isShowing()) {
            otherPopup.hide();
        }
        if (newValue == null || newValue.isBlank() || newValue.isEmpty()) {
            popup.hide();
            return;
        }
        List<Pair<String, Address>> suggestions = addressSearch.getSuggestions(newValue);
        if (suggestions == null || suggestions.size() == 0) {
            popup.hide();
            return;
        }
        populatePopup(popup, field, suggestions, addressHandler);
        if (!popup.isShowing() && !dontReopenPopup) {
            popup.show(field, Side.BOTTOM, 0, 0);
        }
    }

    private void populatePopup(ContextMenu popup, TextField field, List<Pair<String, Address>> suggestions, Consumer<Address> addressHandler) {
        List<CustomMenuItem> items = new ArrayList<>();
        for (int i = 0; i < Math.min(suggestions.size(), MAX_SUGGESTIONS); i++) {
            Pair<String, Address> suggestion = suggestions.get(i);
            String string = suggestion.getKey();
            Address address = suggestion.getValue();
            Label entryLabel = new Label(string);
            double width = field.getWidth() - field.getInsets().getLeft() - field.getInsets().getRight();
            entryLabel.setPrefWidth(width);
            entryLabel.setWrapText(true);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setHideOnClick(false);
            item.setOnAction(event -> {
                if (address != null) {
                    dontReopenPopup = true;
                    addressHandler.accept(address);
                    popup.hide();
                }
                field.textProperty().setValue(string);
                field.positionCaret(string.length());
            });
            items.add(item);
        }
        popup.getItems().clear();
        popup.getItems().addAll(items);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
