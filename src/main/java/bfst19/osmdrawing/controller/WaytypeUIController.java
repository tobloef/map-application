package bfst19.osmdrawing.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class WaytypeUIController {
	@FXML
	private VBox layoutBox;

	public void togglePanel(){
		layoutBox.setDisable(!layoutBox.isDisabled());
	}
}