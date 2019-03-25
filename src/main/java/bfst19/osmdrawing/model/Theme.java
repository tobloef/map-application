package bfst19.osmdrawing.model;

import java.util.HashMap;
import java.util.Map;

public class Theme {
    private Map<WayType, DrawingInfo> drawingInfoMap;

    public Theme(Map<WayType, DrawingInfo> drawingInfoMap) {
        this.drawingInfoMap = drawingInfoMap;
    }

    public Theme() {
        drawingInfoMap = new HashMap<>();
    }

    public DrawingInfo getDrawingInfo(WayType wayType) {
        return drawingInfoMap.get(wayType);
    }

    public void addDrawingInfo(WayType wayType, DrawingInfo drawingInfo) {
        if (drawingInfoMap.containsKey(wayType)) {
            drawingInfo = getDrawingInfo(wayType).mergeWith(drawingInfo);
        }
        drawingInfoMap.put(wayType, drawingInfo);
    }
}
