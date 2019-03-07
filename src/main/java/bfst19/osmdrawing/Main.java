
package bfst19.osmdrawing;

import bfst19.osmdrawing.Model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Model model = new Model(getParameters().getRaw());
		View view = new View(model, stage);
	}
}
