package bfst19.osmdrawing.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {

	private static WaytypeUIController waytypeController;

	public static void init(WaytypeUIController waytypeControllerParam){
		waytypeController = waytypeControllerParam;
	}

	@FXML
	private void onHideElementsBar(final ActionEvent event){
		waytypeController.togglePanel();
	}
}
