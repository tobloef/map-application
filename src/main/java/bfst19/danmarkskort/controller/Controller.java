package bfst19.danmarkskort.controller;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.PolyRoad;
import bfst19.danmarkskort.model.VehicleType;
import bfst19.danmarkskort.view.controls.MapCanvas;
import bfst19.danmarkskort.view.View;
import bfst19.danmarkskort.view.drawers.RouteDrawer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.tools.Tool;
import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Controller {
	private Model model;
	private double x, y;
	@FXML
	private MapCanvas mapCanvas;
	@FXML
	private BorderPane borderPane;

	public void init(Model model, Stage stage) {
		this.model = model;
		mapCanvas.initialize(model);
		WaytypeSelectorController.init(model, borderPane);
		TopMenuController.init(model, stage);
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
			case E: {
				model.updateEnd();
				break;
			}
			case S: {
				model.updateStart();
				break;
			}
			case D: {
				model.swapStartAndEnd();
				break;
			}
			case F: {
				RouteDrawer.debugging = !RouteDrawer.debugging;
				break;
			}
			case G:{
				RouteDrawer.ShowExplored = !RouteDrawer.ShowExplored;
				break;
			}
			case C:{
				model.updateVehicleType(VehicleType.CAR);
				break;
			}
			case B:{
				model.updateVehicleType(VehicleType.BICYCLE);
				break;
			}
			case P:{
				model.addPOIAtCurrentMousePosition();
				break;
			}
			case Q: {
				PolyRoad road = model.getClosestRoad();
				System.out.println(road.getName() + " " + road.getRepresentativeX() + " " + road.getRepresentativeY());
			}
		}
	}

	@FXML
	private void onScroll(ScrollEvent e) {
		double factor = Math.pow(1.01, e.getDeltaY());
		mapCanvas.zoom(factor, e.getX(), e.getY());
	}

	@FXML
	private void onMouseDragged(MouseEvent e) {
		if (e.isPrimaryButtonDown()) {
			mapCanvas.pan(e.getX() - x, e.getY() - y);
		}
		x = e.getX();
		y = e.getY();
	}

	@FXML
	private void onMousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	@FXML
	public void onMouseMoved(MouseEvent mouseEvent) {
		float localX = (float)mouseEvent.getX();
		float localY = (float)mouseEvent.getY();
		Point2D modelCoords = mapCanvas.modelCoords(localX, localY);
		model.setMouseModelCoords((float)modelCoords.getX(), (float)modelCoords.getY());
		model.setMouseScreenCoords(localX, localY);
		model.updateMouseIdle();
	}

	@FXML
	public void onMouseEntered(MouseEvent mouseEvent) {
		model.setIsMouseInWindow(true);
	}

	@FXML
	public void onMouseExited(MouseEvent mouseEvent) {
		model.setIsMouseInWindow(false);
	}
}
