package bfst19.osmdrawing;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
	public View(Model model, Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
		Scene scene = loader.load();
		Controller controller = loader.getController();
		stage.setScene(scene);
		stage.show();
		controller.init(model);
	}
}
