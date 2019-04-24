package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.PolyRoad;

import java.util.ArrayList;
import java.util.List;

public class Route extends ArrayList<PolyRoad> {
	public double sumLength() {
		return stream().mapToDouble(PolyRoad::getLength).sum();
	}
}
