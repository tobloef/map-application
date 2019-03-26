package bfst19.osmdrawing.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {

	private WaytypeUIController waytypeController;

	public void init(WaytypeUIController wayTypeController){
		this.waytypeController = waytypeController;
	}

	@FXML
	private void onHideElementsBar(final ActionEvent event){
		waytypeController.togglePanel();
	}
}
