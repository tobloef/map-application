package bfst19.danmarkskort.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Route extends ArrayList<PolyRoad> {
	public double sumLength() {
		return stream().mapToDouble(PolyRoad::getRealLength).sum();
	}

	public double sumTime() {
		return stream().mapToDouble(PolyRoad::getDurationInMinutes).map(Math::sqrt).sum();
	}

	public List<String> getTextDescription() {
		List<String> result = new ArrayList<>();
		PolyRoad last = null;
		double summedDurationInMinutes = 0;
		result.add("Start on " + get(0).getName());
		for (int i = 0; i < this.size(); i++) {
			PolyRoad road = get(i);
			if (last == null) {
				last = road;
				continue;
			}
			double durationInMinutes = road.getDurationInMinutes();
			summedDurationInMinutes += durationInMinutes;
			if (last.getName().equals(road.getName())) {
				continue;
			}
			String description = "";
			description += "Drive for ";
			description += getTimeDescription(summedDurationInMinutes);
			description += getDirectionDescription(get(i-1), road);
			description += road.getName();
			result.add(description);
			summedDurationInMinutes = 0;
			last = road;
		}
		result.add("You have arrived at your destination");
		return result;
	}

	private String getDirectionDescription(PolyRoad actualLast, PolyRoad road) {
		double degree = getDegree(actualLast, road);
		int direction = interpretDirection(degree);
		switch (direction) {
			case 1: {
				return " and turn right onto ";
			}
			case 0: {
				return " and continue forward onto ";
			}
			case -1: {
				return " and turn left onto ";
			}
		}
		throw new RuntimeException("interpretDirection returned a number that isn't -1, 0 or 1");
	}

	private String getTimeDescription(double summedDurationInMinutes) {
		if (summedDurationInMinutes < 1) {
			return "less than a minute";
		}
		else {
			return String.format("%.0f", summedDurationInMinutes) + " minutes";
		}
	}

	private double getDegree(PolyRoad last, PolyRoad current) {
		double lastDegree = last.getDegree(current);
		double firstDegree = current.getDegree(last);
		return clampAngle(lastDegree - firstDegree + 180); //the 180 is because directly ahead is 180, while one degree
		// left of that is -179, so we flip the angle around so it's easier to work with
	}

	private int interpretDirection(double degree) {
		double rightBound = 45;
		double leftBound = -45;
		if (degree > rightBound) {
			return 1; //if you are going exactly backwards, you are technically turning left, since backwards would be 180 degrees.
		}
		if (degree > leftBound) {
			return 0;
		}
		return -1;
	}

	private double clampAngle(double angle) {
		if (angle > 180) {
			angle -= 360;
		}
		else if (angle <=-180) {
			angle += 360;
		}
		return angle;
	}

	public void print() {
		for (String string : this.getTextDescription()) {
			System.out.println(string);
		}
	}

	public void printToFile(File file) {
		if (file == null) {
			return;
		}
		try {
			if (isEmpty()) {
				throw new InvalidUserInputException("Please select a route.");
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (String string : getTextDescription()) {
				bufferedWriter.write(string + String.format("%n"));
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidUserInputException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					e.getMessage(),
					ButtonType.CLOSE);
			alert.setTitle("Error: Wrong input");
			alert.setHeaderText("Error: Wrong input");
			alert.show();
		}
	}

	public String getSuggestedFileName() {
		return get(0).getName() + "_" + get(size()-1).getName() + ".txt";
	}
}
