package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.PolyRoad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Route extends ArrayList<PolyRoad> {
	public double sumLength() {
		return stream().mapToDouble(PolyRoad::getLength).sum();
	}

	public List<String> getTextDescription() {
		//fixme x.tostring should be replaced with the name of the road
		return stream().map(x -> ("Drive on " + x.toString() + " for " + (x.getLength()*110 / x.getSpeedLimit())*60 + "minutes" )).collect(Collectors.toList());
	}

	public static void printAll(List<String> list) {
		for (String string : list) {
			System.out.println(string);
		}
	}
}
