package bfst19.danmarkskort.model;

import java.util.HashMap;
import java.util.Map;

public class Theme {
    private final Map<WayType, DrawingInfo> drawingInfoMap;

	public Theme() {
        drawingInfoMap = new HashMap<>();
    }

    public DrawingInfo getDrawingInfo(WayType wayType) {
        return drawingInfoMap.get(wayType);
    }

    public void addDrawingInfo(WayType wayType, DrawingInfo drawingInfo) {
        if (drawingInfoMap.containsKey(wayType)) {
            drawingInfo = getDrawingInfo(wayType).createMerged(drawingInfo);
        }
        drawingInfoMap.put(wayType, drawingInfo);
    }
}
