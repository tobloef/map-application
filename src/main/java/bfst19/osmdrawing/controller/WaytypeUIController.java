package bfst19.osmdrawing.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class WaytypeUIController{
	@FXML
	private VBox layoutBox;
	private boolean enabled = true;

	public WaytypeUIController(){
		SettingsController.init(this);
	}

	public void togglePanel(){
		if (enabled){
			layoutBox.setPrefWidth(0);
		} else {
			layoutBox.setPrefWidth(200);
		}
		enabled = !enabled;
	}
}