package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.Route;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.function.Function;

public class TopMenu extends MenuBar {
    private Model model;
    private Stage stage;
    private Runnable  toggleWayTypeSelector;

    public TopMenu() throws IOException {
        URL url = ResourceLoader.getResource("rs:views/TopMenu.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize(Model model, Stage stage, Runnable toggleWayTypeSelector) {
        this.model = model;
        this.stage = stage;
        this.toggleWayTypeSelector = toggleWayTypeSelector;
    }

    @FXML
    public void onLoadMapData(final ActionEvent event) throws IOException, XMLStreamException, ClassNotFoundException {
        File file = openMapDataFileSelect();
        if (file != null) {
            model.loadNewDataset(file.getAbsolutePath());
        }
    }

    private File openMapDataFileSelect() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select map data file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.osm", "*.zip", "*.ser"),
                new FileChooser.ExtensionFilter("OSM", "*.osm"),
                new FileChooser.ExtensionFilter("ZIP", "*.zip"),
                new FileChooser.ExtensionFilter("Serialised", "*.ser"));
        return fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void onLoadTheme(final ActionEvent event) {
        File file = openThemeFileSelect();
        if (file != null) {
            model.changeDefaultTheme(file.getAbsolutePath());
        }
    }

    private File openThemeFileSelect() {
        //Create fileChooser and set default settings
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select theme");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML", "*.yaml"));
        return fileChooser.showOpenDialog(stage);
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
                "This project was made by ITU students (Group G):\n\n" +
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
    private void onSaveRouteToFile(final ActionEvent event) {
        Route route = model.getShortestPath();
        File file = openRouteFileSelect(route.getSuggestedFileName());
        route.printToFile(file);
    }

    private File openRouteFileSelect(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select map theme");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));
        return fileChooser.showSaveDialog(stage);
    }

    @FXML
    private void onToggleWaytypeSelector(final ActionEvent event) {
        toggleWayTypeSelector.run();
    }

    @FXML
    private void onUseDefaultTheme(final ActionEvent event) {
        // TODO: Implement this
    }

    @FXML
    private void onUseHDGraphicsTheme(final ActionEvent event) {
        // TODO: Implement this
    }
}
