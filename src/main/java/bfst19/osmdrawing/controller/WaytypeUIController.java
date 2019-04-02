package bfst19.osmdrawing.controller;

import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.model.WayType;
import bfst19.osmdrawing.utils.EnumHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class WaytypeUIController{
	@FXML
	private VBox layoutBox;
	@FXML
	private ScrollPane scrollPane;
	private BorderPane borderPane;

	private boolean enabled = true;
	private static Model model;
	private static WaytypeUIController singletonInstance;

	public static void init(Model modelParam, BorderPane borderPane){
		singletonInstance.borderPane = borderPane;
		model = modelParam;
		singletonInstance.loadWaytypes();
		singletonInstance.togglePanel();
	}

	public WaytypeUIController(){
		singletonInstance = this;
		SettingsController.init(this);
	}

	private void loadWaytypes(){
		borderPane.setRight(scrollPane);
		scrollPane.setContent(layoutBox);
		scrollPane.setPannable(true);

		//TODO: Rebuild korrekt ud fra om de eksisterer i blacklist
		for (WayType wayType: WayType.values()) {
			CheckBox checkBox = new CheckBox(EnumHelper.waytypeToDecoratedString(wayType));
			checkBox.setSelected(!model.dontDraw(wayType));
			checkBox.selectedProperty().addListener(
					//The parameters are not used, only for syntax
					(observable, oldValue, newValue) -> {
						model.toggleBlacklistWaytype(wayType);
					});
			layoutBox.getChildren().add(checkBox);
		}
	}

	public void togglePanel(){
		if (enabled){
			layoutBox.getChildren().removeAll();
			scrollPane.setContent(null);
			borderPane.setRight(null);
		} else {
			loadWaytypes();
		}
		enabled = !enabled;
	}
}