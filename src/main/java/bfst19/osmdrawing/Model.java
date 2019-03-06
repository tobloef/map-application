package bfst19.osmdrawing;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;

public class Model {
	DrawableModel drawableModel = new BasicDrawableModel();
	List<Runnable> observers = new ArrayList<>();
	ModelBounds bounds;

	public Iterable<Drawable> getWaysOfType(WayType type, ModelBounds modelBounds) {
		return drawableModel.getDrawablesOfType(type, modelBounds);
	}

	public void addObserver(Runnable observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		for (Runnable observer : observers) observer.run();
	}

	public Model(List<String> args) throws IOException, XMLStreamException, ClassNotFoundException {
		String filename = args.get(0);
		long time = -System.nanoTime();
		if (filename.endsWith(".obj")) {
			parseObj(filename);
		} else {
			OSMParser parser = new OSMParser(filename, drawableModel);
			bounds = parser.getBounds();
			serializeData(filename);
		}
		time += System.nanoTime();
		System.out.printf("Load time: %.1fs\n", time / 1e9);
	}

	private void parseObj(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			drawableModel = (BasicDrawableModel) input.readObject();
			bounds = new ModelBounds();
			bounds.ymin = input.readDouble();
			bounds.xmin = input.readDouble();
			bounds.ymax = input.readDouble();
			bounds.xmax = input.readDouble();
		}
	}

	private void serializeData(String filename) throws IOException {
		try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename + ".obj")))) {
			output.writeObject(drawableModel);
			output.writeDouble(bounds.ymin);
			output.writeDouble(bounds.xmin);
			output.writeDouble(bounds.ymax);
			output.writeDouble(bounds.xmax);
		}
	}


}
