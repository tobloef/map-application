package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMParser;
import bfst19.danmarkskort.utils.ResourceLoader;
import bfst19.danmarkskort.utils.ThemeLoader;
import javafx.geometry.Point2D;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Model {
	private final static long mouseIdleTime = 250;

	private DrawableModel drawableModel = new KDTreeDrawableModel();
	private Set<WayType> blacklistedWaytypes = new HashSet<>();
	private float mouseModelX, mouseModelY;
	private float mouseScreenX, mouseScreenY;
	private PolyRoad start, end;
	private Route shortestPath;
	private VehicleType currentVehicleType = VehicleType.CAR;
	private List<Runnable> wayTypeObservers = new ArrayList<>();
	private List<Runnable> reloadObservers = new ArrayList<>();
	private List<Consumer<Boolean>> mouseIdleObservers = new ArrayList<>();
	private ScheduledExecutorService executor;
	private ScheduledFuture<?> mouseIdleTask;
	private Rectangle modelBounds;
	private boolean isMouseInWindow;
    private Theme theme;
    private boolean HDOn;
    private String themePath;
	private List<Address> addressesByStreetName;
	private List<Address> addressesByCity;
	private List<String> cities;
	private List<String> streetNames;

	//Code duplication, sorry
	public Model(List<String> args) throws IOException, XMLStreamException, ClassNotFoundException {
		System.out.println("Loading data...");
		long time = -System.nanoTime();
		if (args.size() == 0) {
			loadDefaultData();
		} else {
			loadDataFromArgs(args);
		}
		if (args.size() == 2){
			themePath = args.get(1);
		} else {
			themePath = "rs:config/themes/default.yaml";
		}
		theme = ThemeLoader.loadTheme(themePath, null);
		time += System.nanoTime();
		System.out.printf("Load time: %.1fs\n", time / 1e9);
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void cleanup() {
		executor.shutdownNow();
	}

	public Theme getCurrentTheme(){
		return theme;
	}

	public void toggleHDTheme(){
		if (HDOn){
			theme = ThemeLoader.loadTheme(themePath, null);
		} else {
			theme = ThemeLoader.loadTheme("rs:config/themes/hdgraphics.yaml", theme);
		}
		HDOn = !HDOn;
		notifyWayTypeObservers();
	}

	public void changeDefaultTheme(String path){
		themePath = path;
		theme = ThemeLoader.loadTheme(themePath,null);
	}

	public void appendTheme(String path){
		theme = ThemeLoader.loadTheme(path, theme);
	}

	public boolean dontDraw(WayType waytype){
		return blacklistedWaytypes.contains(waytype);
	}

	public Iterable<Drawable> getWaysOfType(WayType type, Rectangle modelBounds) {
		return drawableModel.getDrawablesOfTypeInBounds(type, modelBounds);
	}

	public Iterable<Drawable> getWaysOfType(WayType type) {
		return drawableModel.getAllDrawablesOfType(type);
	}

	public void addWayTypeObserver(Runnable observer) {
		wayTypeObservers.add(observer);
	}

	public void notifyWayTypeObservers() {
		for (Runnable observer : wayTypeObservers) {
			observer.run();
		}
	}

	public void addMouseIdleObserver(Consumer<Boolean> observer) {
		mouseIdleObservers.add(observer);
	}

	public void notifyMouseIdleObservers(boolean isIdle) {
		for (Consumer<Boolean> observer : mouseIdleObservers) {
			observer.accept(isIdle);
		}
	}

	public void addReloadObserver(Runnable observer) {
		reloadObservers.add(observer);
	}

	public void toggleBlacklistWaytype(WayType waytype){
		if (!blacklistedWaytypes.contains(waytype)) {
			blacklistedWaytypes.add(waytype);
		} else {
			blacklistedWaytypes.remove(waytype);
		}
		notifyWayTypeObservers();
	}

	public void emptyBlacklist(){
		for (WayType wayType: WayType.values()){
			blacklistedWaytypes.remove(wayType);
		}
		notifyWayTypeObservers();
	}

	public void fillBlacklist(){
		blacklistedWaytypes.addAll(Arrays.asList(WayType.values()));
		notifyWayTypeObservers();
	}

	public void notifyReloadObservers() {
		for (Runnable observer : reloadObservers) observer.run();
	}


	public void loadNewDataset(String argumentPath) throws IOException, XMLStreamException, ClassNotFoundException{
		System.out.println("Loading data...");
		long time = -System.nanoTime();
		List<String> tempList = new ArrayList<>();
		tempList.add(argumentPath);
		cleanUpShortestPath();
		drawableModel.doNewDataSet();
		loadDataFromArgs(tempList);
		time += System.nanoTime();
		System.out.printf("Load time: %.1fs\n", time / 1e9);
        notifyReloadObservers();
    }

	private void cleanUpShortestPath(){
		start = end = null;
  	}

	// TODO: Update the default data before final release.
	private void loadDefaultData() throws IOException, ClassNotFoundException {
		InputStream inputStream = ResourceLoader.getResourceAsStream("rs:data/default.osm.ser");
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
			setFieldsFromParser(parser);
			String path = filename + ".ser";
			serializeData(path);
		}
	}

	private void setFieldsFromParser(OSMParser parser) {
		// Set model bounds
		modelBounds = parser.getModelBounds();
		// Set addresses
		addressesByStreetName = parser.getAddresses();
		addressesByStreetName.sort(Comparator.comparing(a -> a.getStreetName().toLowerCase()));
		addressesByCity = parser.getAddresses();
		addressesByCity = addressesByCity.stream()
				.filter(a -> a.getCity() != null)
				.sorted(Comparator.comparing(a -> a.getCity().toLowerCase()))
				.collect(Collectors.toList());
		// Set cities
		cities = new ArrayList<>(parser.getCities());
		Collections.sort(cities);
		// Set cities
		streetNames = new ArrayList<>(parser.getStreetNames());
		Collections.sort(streetNames);
	}

	private void parseObj(String path) throws IOException, ClassNotFoundException {
		InputStream inputStream = new FileInputStream(path);
		parseObj(inputStream);
	}

	private void parseObj(InputStream inputStream) throws IOException, ClassNotFoundException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		try (ObjectInputStream input = new ObjectInputStream(bufferedInputStream)) {
			PolyRoad.setAllPolyRoads((PolyRoad[]) input.readObject());
			drawableModel = (KDTreeDrawableModel) input.readObject();
			modelBounds = drawableModel.getModelBounds();
		}
	}

	private void serializeData(String path) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		try (ObjectOutputStream output = new ObjectOutputStream(bufferedOutputStream)) {
			output.writeObject(PolyRoad.getAllPolyRoads());
			output.writeObject(drawableModel);
		}
	}

	public Drawable getNearest(WayType type, Point2D modelCoords) {
		return drawableModel.getNearestNeighbor(type, (float)modelCoords.getX(), (float)modelCoords.getY());
	}

	public void setMouseModelCoords(float mouseX, float mouseY) {
		this.mouseModelX = mouseX;
		this.mouseModelY = mouseY;
	}

	public void setMouseScreenCoords(float mouseX, float mouseY) {
		this.mouseScreenX = mouseX;
		this.mouseScreenY = mouseY;
	}

	public float getMouseModelX() {
		return mouseModelX;
	}

	public float getMouseModelY() {
		return mouseModelY;
	}

	public float getMouseScreenX() {
		return mouseScreenX;
	}

	public float getMouseScreenY() {
		return mouseScreenY;
	}

	public void updateMouseIdle() {
		notifyMouseIdleObservers(false);
		// Re-schedule the drawer to be shown after a period of no movement.
		if (mouseIdleTask != null) {
			mouseIdleTask.cancel(true);
		}
		mouseIdleTask = executor.schedule(() -> {
			notifyMouseIdleObservers(true);
		}, mouseIdleTime, TimeUnit.MILLISECONDS);
	}

	private void updateShortestPath() {
		long time = -System.nanoTime();
		if (start == null || end == null)
			return;
		try {
			shortestPath = Dijkstra.getShortestPath(start, end, currentVehicleType);
		}
		catch (DisconnectedRoadsException e) {
			shortestPath = new Route();
		}
		time += System.nanoTime();
		System.out.printf("Shortest Path Time: %.1fs\n", time / 1e9);
        shortestPath.print();
        notifyWayTypeObservers();
    }

	public Route getShortestPath() {
		if (shortestPath != null){
			return shortestPath;
		}
		else return new Route();
	}

	public void updateEnd() {
		Drawable nearest = getClosestRoad(mouseModelX, mouseModelY);
		if (nearest instanceof PolyRoad){
			end = (PolyRoad) nearest;
			updateShortestPath();
		}
	}

	public void updateStart() {
		Drawable nearest = getClosestRoad(mouseModelX, mouseModelY);
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

	public PolyRoad getClosestRoad(float x, float y) {
		PolyRoad closestRoad = null;
		for (WayType roadType : RoadInformation.allowedRoadTypes.get(currentVehicleType)) {
			Drawable close = getNearest(roadType, new Point2D(x, y));
			if (!(close instanceof PolyRoad)) {
				continue;
			}
			PolyRoad closeRoad = (PolyRoad) close;
			if (closestRoad == null) {
				closestRoad = closeRoad;
				continue;
			}
			boolean isCloser = closestRoad.euclideanDistanceSquaredTo(x, y) > closeRoad.euclideanDistanceSquaredTo(x, y);
			if (!isCloser) {
				continue;
			}
			closestRoad = closeRoad;

		}
		return closestRoad;
	}

	public PolyRoad getClosestRoad() {
    	return getClosestRoad(mouseModelX, mouseModelY);
	}

	public void insert(WayType type,Drawable drawable){
		drawableModel.insert(type, drawable);
		notifyWayTypeObservers();
	}

	public void updateVehicleType(VehicleType vehicleType) {
		this.currentVehicleType = vehicleType;
		updateShortestPath();
	}

	public void addPOIAtCurrentMousePosition() {
		this.addPOIAtPosition(mouseModelX, mouseModelY);
	}

	private void addPOIAtPosition(float mouseX, float mouseY) {
		PointOfInterest pointOfInterest = new PointOfInterest(mouseX, mouseY);
		this.insert(WayType.POI, pointOfInterest);
	}

	public Rectangle getModelBounds() {
		return modelBounds;
	}

	public void setIsMouseInWindow(boolean isMouseInWindow) {
		this.isMouseInWindow = isMouseInWindow;
	}

	public boolean getIsMouseInWindow() {
		return isMouseInWindow;
	}

	public List<Address> getAddressesByStreetName() {
		return addressesByStreetName;
	}

	public List<Address> getAddressesByCity() {
		return addressesByCity;
	}

	public List<String> getCities() {
		return cities;
	}

	public List<String> getStreetNames() {
		return streetNames;
	}

	public void setStart(Address address) {
		start = getClosestRoad(address.getLon(), address.getLat());
		updateShortestPath();
	}

	public void setEnd(Address address) {
		end = getClosestRoad(address.getLon(), address.getLat());
		updateShortestPath();
	}
}
