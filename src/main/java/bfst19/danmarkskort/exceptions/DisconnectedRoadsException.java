package bfst19.danmarkskort.exceptions;

import bfst19.danmarkskort.model.drawables.PolyRoad;

public class DisconnectedRoadsException extends Exception {
    public final PolyRoad start;
    public final PolyRoad end;

    public DisconnectedRoadsException(String message, PolyRoad start, PolyRoad end) {
        super(message);
        this.start = start;
        this.end = end;
    }
}
