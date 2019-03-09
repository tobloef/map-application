
package bfst19.osmdrawing;

import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Model model = new Model(getParameters().getRaw());
		View view = new View(model, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
