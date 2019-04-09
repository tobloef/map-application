package bfst19.danmarkskort.model.parsing;

public class OSMRoadWay extends OSMWay {
	private double speedLimit;

	public OSMRoadWay(OSMWay oldWay, double speedLimit) {
		super(oldWay.id);
		list = oldWay.getNodes();
		this.speedLimit = speedLimit;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}
}
