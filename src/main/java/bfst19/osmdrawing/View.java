package bfst19.osmdrawing;

import bfst19.osmdrawing.Controller.Controller;
import bfst19.osmdrawing.Model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
	public View(Model model, Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
		Scene scene = loader.load();
		Controller controller = loader.getController();
		stage.setScene(scene);
		stage.show();
		stage.setTitle("Førsteårsprojekt: Danmarkskort");
		controller.init(model);
	}
}
