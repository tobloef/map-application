package bfst19.osmdrawing.view;

import bfst19.osmdrawing.Launcher;
import bfst19.osmdrawing.Main;
import bfst19.osmdrawing.ResourceLoader;
import bfst19.osmdrawing.controller.Controller;
import bfst19.osmdrawing.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class View {
	public View(Model model, Stage stage) throws IOException {


		URL url = ResourceLoader.getResource("view/View.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		Scene scene = loader.load();
		Controller controller = loader.getController();
		stage.setScene(scene);
		stage.show();
		stage.setTitle("Førsteårsprojekt: Danmarkskort");
		controller.init(model);
	}
}
