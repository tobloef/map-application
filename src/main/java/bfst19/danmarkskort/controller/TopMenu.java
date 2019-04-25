package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TopMenu {
	private static Model model;
	public static void init(Model model) {
		TopMenu.model = model;
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
		try {
			List<String> routeDescription = model.getShortestPath().getTextDescription();
			if (routeDescription.isEmpty()) {
				//Fixme find a better exception for this
				throw new Exception("Please select a route.");
			}
			//fixme figure out where the file should be outputted
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sample.txt"));
			for (String string : routeDescription) {
				bufferedWriter.write(string + String.format("%n"));
			}
			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onAStar(final ActionEvent event){
		//TODO: Enten AlertPane med dropdown selektion eller options knapper. Ellers skal den bare
		//give en checkbox markering eller noget
	}

	@FXML
	private void onDjikstra(final ActionEvent event){
		//TODO: Enten AlertPane med dropdown selektion eller options knapper. Ellers skal den bare
		//give en checkbox markering eller noget
	}

	@FXML
	private void onPOI(final ActionEvent event){
		//TODO: Åbne seperat vindue med alle POI, enten i parentvindue, eller seperat
	}

	@FXML
	private void onEE(final ActionEvent event){
		//TODO: Alt upside down?
	}
}
