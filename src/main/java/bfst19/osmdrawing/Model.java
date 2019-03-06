package bfst19.osmdrawing;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

import static javax.xml.stream.XMLStreamConstants.*;

public class Model {

	Map<WayType,List<Drawable>> ways = new EnumMap<>(WayType.class);
	List<Runnable> observers = new ArrayList<>();
	ModelBounds bounds;

	public Iterable<Drawable> getWaysOfType(WayType type, ModelBounds modelBounds) {
		return ways.get(type);
	}

	public void addObserver(Runnable observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		for (Runnable observer : observers) observer.run();
	}

	public Model(List<String> args) throws IOException, XMLStreamException, ClassNotFoundException {
		initializeWaysEnumMap();
		String filename = args.get(0);
		long time = -System.nanoTime();
		if (filename.endsWith(".obj")) {
			parseObj(filename);
		} else {
			OSMParser parser = new OSMParser(filename, ways);
			bounds = parser.getBounds();
			serializeData(filename);
		}
		time += System.nanoTime();
		System.out.printf("Load time: %.1fs\n", time / 1e9);
	}

	private void parseObj(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			ways = (Map<WayType, List<Drawable>>) input.readObject();
			bounds = new ModelBounds();
			bounds.ymin = input.readDouble();
			bounds.xmin = input.readDouble();
			bounds.ymax = input.readDouble();
			bounds.xmax = input.readDouble();
		}
	}

	private void serializeData(String filename) throws IOException {
		try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename + ".obj")))) {
			output.writeObject(ways);
			output.writeDouble(bounds.ymin);
			output.writeDouble(bounds.xmin);
			output.writeDouble(bounds.ymax);
			output.writeDouble(bounds.xmax);
		}
	}

	private void initializeWaysEnumMap() {
		ways = new EnumMap<>(WayType.class);
		for (WayType type : WayType.values()) {
			ways.put(type, new ArrayList<>());
		}
	}
}
