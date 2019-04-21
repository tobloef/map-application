package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMParser;
import bfst19.danmarkskort.utils.EnumHelper;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.geometry.Point2D;
import javafx.scene.transform.Transform;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model {
	DrawableModel drawableModel = new KDTreeDrawableModel();
	List<Runnable> observers = new ArrayList<>();
	Set<WayType> blacklistedWaytypes = new HashSet<>();
	public Rectangle modelBounds;
	float mouseX, mouseY;
	PolyRoad start, end;
	List<PolyRoad> shortestPath;
	VehicleType currentVehicleType = VehicleType.CAR;

	public boolean dontDraw(WayType waytype){
		return blacklistedWaytypes.contains(waytype);
	}

	public Iterable<Drawable> getWaysOfType(WayType type, Rectangle modelBounds) {
		return drawableModel.getDrawablesOfTypeInBounds(type, modelBounds);
	}

	public Iterable<Drawable> getWaysOfType(WayType type) {
		return drawableModel.getAllDrawablesOfType(type);
	}

	public void addObserver(Runnable observer) {
		observers.add(observer);
	}

	public void toggleBlacklistWaytype(WayType waytype){
		if (!blacklistedWaytypes.contains(waytype)) {
			blacklistedWaytypes.add(waytype);
		} else {
			blacklistedWaytypes.remove(waytype);
		}
		notifyObservers();
	}

	public void emptyBlacklist(){
		for (WayType wayType: WayType.values()){
			blacklistedWaytypes.remove(wayType);
		}
		notifyObservers();
	}

	public void fillBlacklist(){
		for (WayType wayType: WayType.values()){
			blacklistedWaytypes.add(wayType);
		}
		notifyObservers();
	}

	public void notifyObservers() {
		for (Runnable observer : observers) observer.run();
	}

	public Model(List<String> args) throws IOException, XMLStreamException, ClassNotFoundException {
		System.out.println("Loading data...");
		long time = -System.nanoTime();
		if (args.size() == 0) {
			loadDefaultData();
		} else {
			loadDataFromArgs(args);
		}
		time += System.nanoTime();
		System.out.printf("Load time: %.1fs\n", time / 1e9);
	}

	private void loadDefaultData() throws IOException, ClassNotFoundException {
		InputStream inputStream = ResourceLoader.getResourceAsStream("data/default.osm.ser");
		try {
			parseObj(inputStream);
		} catch (InvalidClassException e) {
			System.err.println("Couldn't load default object data, it's an old version.");
		}
	}

	private void loadDataFromArgs(List<String> args) throws IOException, ClassNotFoundException, XMLStreamException {
		String filename = args.get(0);
		if (filename.endsWith(".ser")) {
			try {
				parseObj(filename);
			} catch (InvalidClassException e) {
				System.err.println("Couldn't load object data from args, it's an old version..");
			}
		} else {
			OSMParser parser = new OSMParser(filename, drawableModel);
			modelBounds = parser.getModelBounds();
			String path = filename + ".ser";
			serializeData(path);
		}
	}

	private void parseObj(String path) throws IOException, ClassNotFoundException {
		InputStream inputStream = new FileInputStream(path);
		parseObj(inputStream);
	}

	private void parseObj(InputStream inputStream) throws IOException, ClassNotFoundException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		try (ObjectInputStream input = new ObjectInputStream(bufferedInputStream)) {
			PolyRoad.allPolyRoads = (PolyRoad[]) input.readObject();
			drawableModel = (KDTreeDrawableModel) input.readObject();
			modelBounds = drawableModel.getModelBounds();
		}
	}

	private void serializeData(String path) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		try (ObjectOutputStream output = new ObjectOutputStream(bufferedOutputStream)) {
			output.writeObject(PolyRoad.allPolyRoads);
			output.writeObject(drawableModel);
		}
	}

	public Drawable getNearest(WayType type, Point2D modelCoords) {
		return drawableModel.getNearestNeighbor(type, (float)modelCoords.getX(), (float)modelCoords.getY());
	}

	public void setMouseCoords(float mouseX, float mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	private void updateShortestPath() {
		if (start == null || end == null)
			return;
		try {
			shortestPath = Dijkstra.getShortestPath(start, end, currentVehicleType);
		}
		catch (DisconnectedRoadsException e) {
			shortestPath = new ArrayList<>();
		}
		notifyObservers();
	}



	public List<? extends Drawable> getShortestPath() {
		if (shortestPath != null){
			return shortestPath;
		}
		else return new ArrayList<>();
	}

	public void updateEnd() {
		Drawable nearest = getClosestRoad(mouseX, mouseY);
		if (nearest instanceof PolyRoad){
			end = (PolyRoad) nearest;
			updateShortestPath();
		}
	}

	public void updateStart() {
		Drawable nearest = getClosestRoad(mouseX, mouseY);
		if (nearest instanceof PolyRoad){
			start = (PolyRoad) nearest;
			updateShortestPath();
		}
	}

	public void swapStartAndEnd() {
		PolyRoad temp = start;
		start = end;
		end = temp;
		updateShortestPath();
	}

	private Drawable getClosestRoad(float x, float y) {
		PolyRoad closestRoad = null;
		for (WayType roadType : RoadInformation.allowedRoadTypes.get(currentVehicleType)){
			Drawable close = getNearest(roadType, new Point2D(x,y));
			if (close == null || !(close instanceof PolyRoad)){
				continue;
			}

			PolyRoad closeRoad = (PolyRoad)close;
			if (closestRoad == null || closestRoad.euclideanDistanceSquaredTo(x, y) > closeRoad.euclideanDistanceSquaredTo(x, y)) {
				closestRoad = closeRoad;
			}

		}

		return closestRoad;
	}

	public void updateVehicleType(VehicleType vehicleType) {
		this.currentVehicleType = vehicleType;
		updateShortestPath();
	}
}
