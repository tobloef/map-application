package bfst19.danmarkskort.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {

	private static waytypeSelectorController waytypeController;

	public static void init(waytypeSelectorController waytypeControllerParam){
		waytypeController = waytypeControllerParam;
	}

	@FXML
	private void onToggleWaytypes(final ActionEvent event){
		waytypeController.togglePanel();
	}
}
