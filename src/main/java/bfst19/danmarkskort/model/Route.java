package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.PolyRoad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Route extends ArrayList<PolyRoad> {
	public double sumLength() {
		return stream().mapToDouble(PolyRoad::getLength).sum();
	}

	public double sumTime() {
		return stream().mapToDouble(PolyRoad::getDurationInMinutes).sum();
	}

	public List<String> getTextDescription() {
		//fixme x.tostring should be replaced with the name of the road
		List<String> result = new ArrayList<>();
		PolyRoad last = null;
		for (PolyRoad road : this) {
			if (last == null) {
				last = road;
				System.out.println(last.getLength() + " " + last.getSpeedLimit());
				continue;
			}
			String description = "";
			int direction = getDirection(last, road);
			switch (direction) {
				case 1: {
					description += "Turn left an drive on ";
					break;
				}
				case 0: {
					description += "Continue forward on ";
					break;
				}
				case -1: {
					description += "Turn right and drive on ";
					break;
				}
			}
			last = road;
			description += road.toString() + " for " + String.format("%.0f", road.getDurationInMinutes()) + " minutes";
			result.add(description);
		}
		return result;
		//return stream().map(x -> ("Drive on " + x.toString() + " for " + (x.getLength()*110 / x.getSpeedLimit())*60 + "minutes" )).collect(Collectors.toList());
	}

	private int getDirection(PolyRoad last, PolyRoad current) {
		double lastDegree = last.getDegree(true);
		double firstDegree = current.getDegree(false);
		double rightBound = 45;
		double leftBound = -45;
		double deltaDegree = clampAngle(lastDegree - firstDegree);
		if (deltaDegree > rightBound) {
			return 1; //if you are going exactly backwards, you are technically turning left, since backwards would be 180 degrees.
		}
		if (deltaDegree > leftBound) {
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

	public static void printAll(List<String> list) {
		for (String string : list) {
			System.out.println(string);
		}
	}
}
