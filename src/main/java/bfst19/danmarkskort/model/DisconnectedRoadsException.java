package bfst19.danmarkskort.model;

public class DisconnectedRoadsException extends Exception {
    public PolyRoad start;
    public PolyRoad end;

    public DisconnectedRoadsException(String message, PolyRoad start, PolyRoad end) {
        super(message);
        this.start = start;
        this.end = end;
    }
}
