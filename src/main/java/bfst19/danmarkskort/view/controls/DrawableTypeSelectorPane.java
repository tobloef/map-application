package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.utils.EnumHelper;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DrawableTypeSelectorPane extends VBox {
    private  Model model;
    private final List<CheckBox> drawableTypeSelectors = new ArrayList<>();

    @FXML
    private VBox drawableTypeLayoutBox;
    @FXML
    private ScrollPane scrollPane;

    public DrawableTypeSelectorPane() throws IOException {
        URL url = ResourceLoader.getResource("rs:views/DrawableTypeSelector.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize(Model model) {
        this.model = model;
        buildCheckboxes();
    }

    private void buildCheckboxes() {
        for (CheckBox checkBox : drawableTypeSelectors) {
            drawableTypeLayoutBox.getChildren().remove(checkBox);
        }
        drawableTypeSelectors.clear();
        for (DrawableType drawableType : DrawableType.values()) {
            CheckBox checkBox = new CheckBox(EnumHelper.drawableToDecoratedString(drawableType));
            checkBox.setSelected(!model.dontDraw(drawableType));
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> model.toggleBlacklistDrawable(drawableType));
            VBox.setMargin(checkBox, new Insets(0, 0, 5, 0));
            drawableTypeLayoutBox.getChildren().add(checkBox);
            drawableTypeSelectors.add(checkBox);
        }
    }

    @FXML
    private void onEnableAll(ActionEvent event) {
        model.emptyBlacklist();
        buildCheckboxes();
    }

    @FXML
    private void onDisableAll(ActionEvent event) {
        model.fillBlacklist();
        buildCheckboxes();
    }
}