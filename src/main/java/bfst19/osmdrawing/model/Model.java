package bfst19.osmdrawing.model;

import bfst19.osmdrawing.model.parsing.OSMParser;
import bfst19.osmdrawing.utils.ResourceLoader;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
	DrawableModel drawableModel = new KDTreeDrawableModel();
	List<Runnable> observers = new ArrayList<>();
	public Rectangle modelBounds;

	public Iterable<Drawable> getWaysOfType(WayType type, Rectangle modelBounds) {
		return drawableModel.getDrawablesOfTypeInBounds(type, modelBounds);
	}

	public Iterable<Drawable> getWaysOfType(WayType type) {
		return drawableModel.getAllDrawablesOfType(type);
	}

	public void addObserver(Runnable observer) {
		observers.add(observer);
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
		InputStream inputStream = ResourceLoader.getResourceAsStream("data/default.osm.obj");
		try {
			parseObj(inputStream);
		} catch (InvalidClassException e) {
			System.err.println("Couldn't load default object data, it's an old version.");
		}
	}

	private void loadDataFromArgs(List<String> args) throws IOException, ClassNotFoundException, XMLStreamException {
		String filename = args.get(0);
		if (filename.endsWith(".obj")) {
			try {
				parseObj(filename);
			} catch (InvalidClassException e) {
				System.err.println("Couldn't load object data from args, it's an old version..");
			}
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
			drawableModel = (KDTreeDrawableModel) input.readObject();
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
