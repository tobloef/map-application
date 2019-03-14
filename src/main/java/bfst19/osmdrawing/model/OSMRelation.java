package bfst19.osmdrawing.model;

import java.util.ArrayList;
import java.util.List;

public class OSMRelation {
	private List<OSMWay> list = new ArrayList<>();

	public List<OSMWay> getList() {
		return list;
	}
	public void add(OSMWay item) {
		list.add(item);
	}
}
