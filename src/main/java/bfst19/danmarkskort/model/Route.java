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
			String description = "";
			int direction = getDirection(last, road);
			last = road;
			result.add("Drive on " + road.toString() + " for " + road.getDurationInMinutes() + "minutes");
		}
		return result;
		//return stream().map(x -> ("Drive on " + x.toString() + " for " + (x.getLength()*110 / x.getSpeedLimit())*60 + "minutes" )).collect(Collectors.toList());
	}

	private int getDirection(PolyRoad last, PolyRoad current) {
		double lastDegree = last.getDegree(true);
		double firstDegree = current.getDegree(false);
		System.out.println(lastDegree);
		System.out.println(firstDegree);
		return 0;
	}

	public static void printAll(List<String> list) {
		for (String string : list) {
			System.out.println(string);
		}
	}
}
