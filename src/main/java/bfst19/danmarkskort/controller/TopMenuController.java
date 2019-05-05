package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public class TopMenuController {
    private static Model model;
    private static Stage primaryStage;
    private static TopMenuController singletonInstance;

    public TopMenuController() {
        singletonInstance = this;
    }

    public static void init(Model modelParam, Stage stage) {
        model = modelParam;
        primaryStage = stage;
    }

    @FXML
    public void onLoadFile(final ActionEvent event) throws IOException, XMLStreamException, ClassNotFoundException {
        //Create fileChooser and set default settings
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select data-file to load");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.osm", "*.zip", "*.ser"),
                new FileChooser.ExtensionFilter("OSM", "*.osm"),
                new FileChooser.ExtensionFilter("ZIP", "*.zip"),
                new FileChooser.ExtensionFilter("Serialised", "*.ser"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            model.loadNewDataset(file.getAbsolutePath());
        }
    }

    @FXML
    private void onHelp(final ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "This is where to show help",
                ButtonType.CLOSE);
        alert.setTitle("Help");
        alert.setHeaderText("Help");
        alert.show();
    }

    @FXML
    private void onAbout(final ActionEvent event) {
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
    private void onPrintToFile(final ActionEvent event) {
        Route route = model.getShortestPath();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select map theme");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(route.getSuggestedFileName());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));
        File file = fileChooser.showSaveDialog(primaryStage);

        route.printToFile(file);
    }

    @FXML
    private void onAStar(final ActionEvent event) {
        //TODO: Enten AlertPane med dropdown selektion eller options knapper. Ellers skal den bare
        //give en checkbox markering eller noget
    }

    @FXML
    private void onDijkstra(final ActionEvent event) {
        //TODO: Enten AlertPane med dropdown selektion eller options knapper. Ellers skal den bare
        //give en checkbox markering eller noget
    }

    @FXML
    private void onPOI(final ActionEvent event) {
        //TODO: Åbne seperat vindue med alle POI, enten i parentvindue, eller seperat
    }

    @FXML
    private void onHDGraphics(final ActionEvent event) {
        model.toggleHDTheme();
    }

    @FXML
    private void onSelectTheme(final ActionEvent event) throws IOException {
        File file = loadThemeAbsolutePath();

        if (file != null) {
            model.changeDefaultTheme(file.getAbsolutePath());
        }
    }

    @FXML
    private void onAppendTheme(final ActionEvent event) throws IOException {
        File file = loadThemeAbsolutePath();

        if (file != null) {
            model.appendTheme(file.getAbsolutePath());
        }
    }

    private File loadThemeAbsolutePath() {
        //Create fileChooser and set default settings
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select map theme");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML", "*.yaml"));

        return fileChooser.showOpenDialog(primaryStage);
    }

}
