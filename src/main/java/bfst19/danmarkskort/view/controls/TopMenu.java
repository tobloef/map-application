package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.exceptions.InvalidUserInputException;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.drawables.BuiltInThemes;
import bfst19.danmarkskort.model.drawables.Theme;
import bfst19.danmarkskort.model.routePlanning.Route;
import bfst19.danmarkskort.utils.ResourceLoader;
import bfst19.danmarkskort.utils.ThemeLoader;
import bfst19.danmarkskort.view.drawers.RouteDrawer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TopMenu extends MenuBar {
    private Model model;
    private Stage stage;
    private Parent root;
    private Runnable toggleDrawableTypeSelector;

    public TopMenu() throws IOException {
        URL url = ResourceLoader.getResource("rs:views/TopMenu.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize(Model model, Stage stage, Parent root, Runnable toggleDrawableTypeSelector) {
        this.model = model;
        this.stage = stage;
        this.root = root;
        this.toggleDrawableTypeSelector = toggleDrawableTypeSelector;
    }

    @FXML
    public void onLoadMapData(ActionEvent event) {
        File file = openMapDataFileSelect();
        if (file == null) {
            displayMapDataNotLoadedAlert();
            return;
        }
        try {
            model.loadNewMapData(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            displayMapDataNotLoadedAlert();
        }
    }

    private void displayMapDataNotLoadedAlert() {
		displayAlert();
	}

    private File openMapDataFileSelect() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select map data file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.osm", "*.zip", "*.ser"),
                new FileChooser.ExtensionFilter("OSM", "*.osm"),
                new FileChooser.ExtensionFilter("ZIP", "*.zip"),
                new FileChooser.ExtensionFilter("Serialised", "*.ser")
        );
        return fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void onSaveMapData(ActionEvent event) {
        File file = openMapDataFileSave();
        if (file == null) {
            displayMapDataNotSavedAlert();
            return;
        }
        try {
            model.saveMapData(file);
        } catch (IOException e) {
            e.printStackTrace();
            displayMapDataNotSavedAlert();
        }
    }

    private File openMapDataFileSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save map data");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Serialised", "*.ser")
        );
        return fileChooser.showSaveDialog(stage);
    }

    private void displayMapDataNotSavedAlert() {
		displayAlert();
	}

	private void displayAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR,
				"The specified map data couldn't be loaded.",
				ButtonType.CLOSE);
		alert.setTitle("Error loading map data");
		alert.setHeaderText("Error loading map data");
		alert.show();
	}

	@FXML
    private void onLoadTheme(ActionEvent event) {
        File file = openThemeFileSelect();
        if (file == null) {
            displayThemeNotLoadedAlert();
            return;
        }
        Theme theme = ThemeLoader.loadTheme(file.getAbsolutePath());
        if (theme == null) {
            displayThemeNotLoadedAlert();
            return;
        }
        model.setTheme(theme);
    }

    private void displayThemeNotLoadedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "The specified theme couldn't be loaded.",
                ButtonType.CLOSE);
        alert.setTitle("Error loading theme");
        alert.setHeaderText("Error loading theme");
        alert.show();
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
    private void onAbout(ActionEvent event) {
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
    private void onSaveRouteToFile(ActionEvent event) {
        Route route = model.getShortestPath();
        if (route == null) {
            displayNoRouteAlert();
            return;
        }
        File file = openRouteFileSelect(route.getSuggestedFileName());
		try {
			route.printToFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidUserInputException e) {
			makeAlert(e);
		}
	}

    private void displayNoRouteAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "No route to save. Please find a route using the address search before trying to save.",
                ButtonType.CLOSE);
        alert.setTitle("Error saving route");
        alert.setHeaderText("Error saving route");
        alert.show();
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
    private void onToggleDrawableTypeSelector(ActionEvent event) {
        toggleDrawableTypeSelector.run();
    }

    @FXML
    private void onUseDefaultTheme(ActionEvent event) {
        model.setTheme(BuiltInThemes.DefaultTheme);
    }

    @FXML
    private void onUseHDGraphicsTheme(ActionEvent event) {
        model.setTheme(BuiltInThemes.HDGraphics);
    }

    @FXML
    private void onOpenNewView(ActionEvent event) {
        try {
            MainWindow mainWindow = new MainWindow(root);
            mainWindow.initialize(model, new Stage());
        } catch (IOException exception) {
            exception.printStackTrace();
            displayOpenNewViewError();
        }
    }


    private void displayOpenNewViewError() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "An error occurred and we couldn't open the new window.",
                ButtonType.CLOSE);
        alert.setTitle("Error opening window");
        alert.setHeaderText("Error opening window");
        alert.show();
    }

	private void makeAlert(InvalidUserInputException e) {
		Alert alert = new Alert(Alert.AlertType.ERROR,
				e.getMessage(),
				ButtonType.CLOSE);
		alert.setTitle("Error: Wrong input");
		alert.setHeaderText("Error: Wrong input");
		alert.show();
	}

    @FXML
    private void onToggleShowExploredRoutes() {
        RouteDrawer.ShowExplored = !RouteDrawer.ShowExplored;
        model.notifyDrawableTypeObservers();
    }
}
