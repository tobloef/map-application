package bfst19.osmdrawing.controller;

import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.model.WayType;
import bfst19.osmdrawing.utils.EnumHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class WaytypeUIController{
	@FXML
	private VBox layoutBox;
	@FXML
	private ScrollPane scrollPane;

	private boolean enabled = true;
	private static Model model;
	private static WaytypeUIController singletonInstance;

	public static void init(Model modelParam){
		model = modelParam;
		singletonInstance.togglePanel();
		singletonInstance.loadWaytypes();
	}

	public WaytypeUIController(){
		singletonInstance = this;
		SettingsController.init(this);
	}

	private void loadWaytypes(){
		scrollPane.setContent(layoutBox);
		scrollPane.setPannable(true);

		for (WayType wayType: WayType.values()) {
			CheckBox checkBox = new CheckBox(EnumHelper.waytypeToDecoratedString(wayType));
			checkBox.setSelected(true);
			ChangeListener<Boolean> listener = new ChangeListener<>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					model.toggleBlacklistWaytype(wayType);
				}
			};
			checkBox.selectedProperty().addListener(listener);
			layoutBox.getChildren().add(checkBox);
		}
	}

	public void togglePanel(){
		if (enabled){
			scrollPane.setPrefWidth(0);
		} else {
			scrollPane.setPrefWidth(200);
		}
		enabled = !enabled;
	}
}