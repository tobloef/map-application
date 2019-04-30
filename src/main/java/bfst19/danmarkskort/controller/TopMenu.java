package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.Main;
import bfst19.danmarkskort.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class TopMenu {

	private static Model model;
	private static Stage primaryStage;
	private static TopMenu singletonInstance;

	public static void init(Model modelParam, Stage stage){
		model = modelParam;
		primaryStage = stage;
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
		//TODO: Tænker der kan være en alert med et indtastningsfelt, til filnavn fx
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
	@FXML
	private void onSelectTheme(final ActionEvent event){
		//Create fileChooser and set default settings
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select map theme");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML","*.yaml"));

		File file = fileChooser.showOpenDialog(primaryStage);
		System.out.println(file.getAbsolutePath());

		//model.appendNewTheme(file);
	}
}
