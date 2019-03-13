package bfst19.osmdrawing.model;

import bfst19.osmdrawing.utils.ResourceLoader;
import bfst19.osmdrawing.view.WayType;
import javafx.scene.canvas.GraphicsContext;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
	DrawableModel drawableModel = new KDTreeModel();
	List<Runnable> observers = new ArrayList<>();
	public Rectangle modelBounds;

	public Iterable<Drawable> getWaysOfType(WayType type, Rectangle modelBounds) {
		return drawableModel.getDrawablesOfType(type, modelBounds);
	}

	public void addObserver(Runnable observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		for (Runnable observer : observers) observer.run();
	}

	public Model(List<String> args) throws IOException, XMLStreamException, ClassNotFoundException {
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
		InputStream inputStream = ResourceLoader.getResourceAsStream("data/default.osm.obj");
		parseObj(inputStream);
	}

	private void loadDataFromArgs(List<String> args) throws IOException, ClassNotFoundException, XMLStreamException {
		String filename = args.get(0);
		if (filename.endsWith(".obj")) {
			parseObj(filename);
		} else {
			OSMParser parser = new OSMParser(filename, drawableModel);
			modelBounds = parser.getModelBounds();
			String path = filename + ".obj";
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
			drawableModel = (BasicDrawableModel) input.readObject();
			modelBounds = drawableModel.getModelBounds();
		}
	}

	private void serializeData(String path) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		try (ObjectOutputStream output = new ObjectOutputStream(bufferedOutputStream)) {
			output.writeObject(drawableModel);
		}
	}


}
