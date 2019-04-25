package bfst19.danmarkskort.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {

	private static WaytypeSelectorController waytypeController;

	public static void init(WaytypeSelectorController waytypeControllerParam){
		waytypeController = waytypeControllerParam;
	}

	@FXML
	private void onToggleWaytypes(final ActionEvent event){
		waytypeController.togglePanel();
	}
}
