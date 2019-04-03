package bfst19.danmarkskort.view;

import bfst19.danmarkskort.utils.ResourceLoader;
import bfst19.danmarkskort.controller.Controller;
import bfst19.danmarkskort.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class View {
	public View(Model model, Stage stage) throws IOException {


		URL url = ResourceLoader.getResource("views/View.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		Scene scene = loader.load();
		Controller controller = loader.getController();
		stage.setScene(scene);
		stage.show();
		stage.setTitle("First year project: Map");
		controller.init(model);
	}
}