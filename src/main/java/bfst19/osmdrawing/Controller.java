package bfst19.osmdrawing;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.input.KeyCode.V;

// Test comment

public class Controller {
	private Model model;
	double x, y;
	@FXML
	private MapCanvas mapCanvas;

	public void init(Model model) {
		this.model = model;
		mapCanvas.init(model);
	}

	@FXML
	private void onKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case V:
				try {
					new View(model, new Stage());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
		}
	}

	@FXML
	private void onScroll(ScrollEvent e) {
		double factor = Math.pow(1.01, e.getDeltaY());
		mapCanvas.zoom(factor, e.getX(), e.getY());
	}

	@FXML
	private void onMouseDragged(MouseEvent e) {
		if (e.isPrimaryButtonDown()) mapCanvas.pan(e.getX() - x, e.getY() - y);
		x = e.getX();
		y = e.getY();
	}

	@FXML
	private void onMousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
}
