package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressSearch;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.Route;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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
        endPopup = new ContextMenu();
        initializePopup(startPopup, startField, endPopup, Model::setStart);
        initializePopup(endPopup, endField, startPopup, Model::setEnd);
    }

    private void initializePopup(ContextMenu popup, TextField field, ContextMenu otherPopup, BiConsumer<Model, Address> modelSetter) {
        popup.setAutoHide(false);
        popup.setAutoFix(false);
        popup.setOnShowing(newValue -> {
            System.out.println("onShowing");
        });
        popup.setOnHiding(newValue -> {
            System.out.println("onHiding");
        });
        Duration pauseTime = Duration.millis(DEBOUNCE_DELAY);
        PauseTransition debounce = new PauseTransition(pauseTime);
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            debounce.setOnFinished(event -> {
                updatePopup(popup, field, otherPopup, newValue, (address) -> {
                    modelSetter.accept(model, address);
                    updateRouteDescription();
                });
            });
            debounce.playFromStart();
        });
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                popup.hide();
            }
        });
    }

    private void updateRouteDescription() {
        Route route = model.getShortestPath();
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

    private void removeUI(){
        addressLayoutBox.getChildren().removeAll();
        addressPane.setCenter(null);
        borderPane.setLeft(null);
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
            double width = field.getWidth() - field.getInsets().getLeft() - field.getInsets().getRight();
            entryLabel.setPrefWidth(width);
            entryLabel.setWrapText(true);
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
}