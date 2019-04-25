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

public class WaytypeSelectorController {
	@FXML
	private VBox layoutBox;
	@FXML
	private ScrollPane scrollPane;
	private BorderPane borderPane;

	private List<CheckBox> waytypeSelectors = new ArrayList<>();

	private boolean enabled = true;
	private static Model model;
	private static WaytypeSelectorController singletonInstance;

	public static void init(Model modelParam, BorderPane borderPane){
		singletonInstance.borderPane = borderPane;
		model = modelParam;
		singletonInstance.loadWaytypes();
		singletonInstance.togglePanel();
	}

	public WaytypeSelectorController(){
		singletonInstance = this;
		SettingsController.init(this);
	}

	private void loadWaytypes(){
		borderPane.setRight(scrollPane);
		scrollPane.setContent(layoutBox);
		scrollPane.setPannable(true);

		buildCheckboxes();
	}

	private void buildCheckboxes(){
		for (CheckBox checkBox: waytypeSelectors){
			layoutBox.getChildren().remove(checkBox);
		}
		waytypeSelectors.removeAll(waytypeSelectors);
		for (WayType wayType: WayType.values()) {
			CheckBox checkBox = new CheckBox(EnumHelper.waytypeToDecoratedString(wayType));
			checkBox.setSelected(!model.dontDraw(wayType));
			checkBox.selectedProperty().addListener(
					//The parameters are not used, only for syntax
					(observable, oldValue, newValue) -> {
						model.toggleBlacklistWaytype(wayType);
					});
			layoutBox.getChildren().add(checkBox);
			waytypeSelectors.add(checkBox);
		}
	}

	@FXML
	private void onEnableAll(ActionEvent event){
		model.emptyBlacklist();
		buildCheckboxes();
	}

	@FXML
	private void onDisableAll(ActionEvent event){
		model.fillBlacklist();
		buildCheckboxes();
	}

	public void togglePanel(){
		if (enabled){
			removeUI();
		} else {
			loadWaytypes();
		}
		enabled = !enabled;
	}

	private void removeUI(){
		layoutBox.getChildren().removeAll();
		scrollPane.setContent(null);
		borderPane.setRight(null);
	}
}