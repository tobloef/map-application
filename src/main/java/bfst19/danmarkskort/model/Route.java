package bfst19.danmarkskort.model;

import java.util.ArrayList;
import java.util.List;

public class Route extends ArrayList<PolyRoad> {
	public double sumLength() {
		return stream().mapToDouble(PolyRoad::getLength).sum();
	}

	public double sumTime() {
		return stream().mapToDouble(PolyRoad::getDurationInMinutes).sum();
	}

	public List<String> getTextDescription() {
		List<String> result = new ArrayList<>();
		PolyRoad last = null;
		double summedDuration = 0;
		for (PolyRoad road : this) {
			if (last == null) {
				last = road;
				continue;
			}
			double durationInMinutes = road.getDurationInMinutes();
			summedDuration += durationInMinutes;
			System.out.println();
			if (last.getName().equals(road.getName())) {
				continue;
			}
			String description = "";
			double degree = getDegree(last, road);
			description += "(" + degree + ")";
			int direction = interpretDirection(degree);
			switch (direction) {
				case 1: {
					description += "Turn right an drive on ";
					break;
				}
				case 0: {
					description += "Continue forward on ";
					break;
				}
				case -1: {
					description += "Turn left and drive on ";
					break;
				}
			}
			last = road;
			description += road.getName() + " for ";
			if (durationInMinutes < 1) {
				description += "less than a minute (" + summedDuration * 60 + " seconds)";
			}
			else {
				description += String.format("%.0f", summedDuration) + " minutes";
			}
			result.add(description);
			summedDuration = 0;
		}
		return result;
		//return stream().map(x -> ("Drive on " + x.toString() + " for " + (x.getLength()*110 / x.getSpeedLimit())*60 + "minutes" )).collect(Collectors.toList());
	}

	private double getDegree(PolyRoad last, PolyRoad current) {
		double lastDegree = last.getDegree(current);
		double firstDegree = current.getDegree(last);
		return clampAngle(lastDegree - firstDegree);
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
}
