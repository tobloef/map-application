package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressSearch;
import bfst19.danmarkskort.model.Model;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddressSearchController {
    private static final int MAX_SUGGESTIONS = 10;
    private static final int DEBOUNCE_DELAY = 300;


    @FXML
    private ScrollPane directionsScrollPane;
    @FXML
    private VBox addressLayoutBox;
    @FXML
    private BorderPane addressPane;
    @FXML
    private TextField startField;
    @FXML
    private TextField endField;

    private BorderPane borderPane;
    private ContextMenu startPopup;
    private ContextMenu endPopup;

    private static Model model;
    private static AddressSearch addressSearch;

    public boolean enabled = false;
    public static AddressSearchController instance;

    public AddressSearchController(){
        instance = this;
    }

    public static void init(Model model, BorderPane borderPane){
        instance.borderPane = borderPane;
        AddressSearchController.model = model;
        addressSearch = new AddressSearch(
                model.getAddressesByStreetName(),
                model.getAddressesByCity(),
                model.getStreetNames(),
                model.getCities()
        );
        instance.togglePanel();
    }

    private void updatePopup(ContextMenu popup, TextField field, ContextMenu otherPopup, String newValue, Consumer<Address> addressHandler) {
        if (otherPopup != null) {
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
        if (!popup.isShowing()) {
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
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(event -> {
                field.textProperty().setValue(string);
                field.positionCaret(string.length());
                if (address != null) {
                    popup.hide();
                    addressHandler.accept(address);
                }
            });
            items.add(item);
        }
        popup.getItems().clear();
        popup.getItems().addAll(items);
    }

    public void togglePanel() {
        if (enabled) {
            removeUI();
        } else {
            addUI();
        }
        enabled = !enabled;
    }

    private void addUI() {
        borderPane.setLeft(addressPane);
        addressPane.setCenter(addressLayoutBox);
        startPopup = new ContextMenu();
        Duration pauseTime = Duration.millis(DEBOUNCE_DELAY);
        // Start position controls
        PauseTransition pauseStart = new PauseTransition(pauseTime);
        startField.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseStart.setOnFinished(event -> {
                updatePopup(startPopup, startField, endPopup, newValue, model::setStart);
            });
            pauseStart.playFromStart();
        });
        startField.focusedProperty().addListener((observable, oldValue, newValue) -> startPopup.hide());
        // End position controls
        endPopup = new ContextMenu();
        PauseTransition pauseEnd = new PauseTransition(pauseTime);
        endField.textProperty().addListener((observable, oldValue, newValue) -> {
            pauseEnd.setOnFinished(event -> {
                updatePopup(endPopup, endField, startPopup, newValue, model::setEnd);
            });
        });
        endField.focusedProperty().addListener((observable, oldValue, newValue) -> endPopup.hide());
    }

    private void removeUI(){
        addressLayoutBox.getChildren().removeAll();
        addressPane.setCenter(null);
        borderPane.setLeft(null);
    }
}