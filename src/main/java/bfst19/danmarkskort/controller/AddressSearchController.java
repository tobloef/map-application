package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.WayType;
import bfst19.danmarkskort.utils.EnumHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchController {
    @FXML
    private VBox addressLayoutBox;
    @FXML
    private ScrollPane addressScrollPane;
    private BorderPane borderPane;

    private boolean enabled = true;
    private static Model model;
    private static AddressSearchController singletonInstance;

    public static void init(Model modelParam, BorderPane borderPane){
        singletonInstance.borderPane = borderPane;
        model = modelParam;
        singletonInstance.togglePanel();
    }

    public AddressSearchController(){
        singletonInstance = this;
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
        borderPane.setLeft(addressScrollPane);
        addressScrollPane.setContent(addressLayoutBox);
        addressScrollPane.setPannable(true);
    }

    private void removeUI(){
        addressLayoutBox.getChildren().removeAll();
        addressScrollPane.setContent(null);
        borderPane.setLeft(null);
    }
}