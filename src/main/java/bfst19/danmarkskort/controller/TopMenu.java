package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.view.drawers.MapDrawer;
import bfst19.danmarkskort.model.InvalidUserInputException;
import bfst19.danmarkskort.model.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TopMenu {

	private static Model model;
	private static TopMenu singletonInstance;
	private Stage primaryStage;

	public static void init(Model modelParam){
		model = modelParam;
	}

	public TopMenu(){
		singletonInstance = this;
	}

	@FXML
	private void onHelp(final ActionEvent event){
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"This is where to show help",
				ButtonType.CLOSE);
		alert.setTitle("Help");
		alert.setHeaderText("Help");
		alert.show();
	}

	@FXML
	private void onAbout(final ActionEvent event){
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"This project was made by ITU students (Group G):\n" +
						"Anders Parsberg Wagner\n" +
						"Anne-Sophie Bak\n" +
						"Hjalte Mac Dalland\n" +
						"Jakob Israelsen\n" +
						"Tobias Løfgren",
				ButtonType.CLOSE);
		alert.setTitle("About");
		alert.setHeaderText("About");
		alert.show();
	}

	@FXML
	private void onPrintToFile(final ActionEvent event){
		Route route = model.getShortestPath();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select map theme");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.setInitialFileName(route.getSuggestedFileName());

		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file","*.txt"));

		File file = fileChooser.showSaveDialog(primaryStage);

		route.printToFile(file);
	}

	@FXML
	private void onAStar(final ActionEvent event){
		//TODO: Enten AlertPane med dropdown selektion eller options knapper. Ellers skal den bare
		//give en checkbox markering eller noget
	}

	@FXML
	private void onDijkstra(final ActionEvent event){
		//TODO: Enten AlertPane med dropdown selektion eller options knapper. Ellers skal den bare
		//give en checkbox markering eller noget
	}

	@FXML
	private void onPOI(final ActionEvent event){
		//TODO: Åbne seperat vindue med alle POI, enten i parentvindue, eller seperat
	}

	@FXML
	private void onHDGraphics(final ActionEvent event){
		model.toggleHDTheme();
	}
}
