package bfst19.danmarkskort.model.drawables;

import java.util.HashMap;
import java.util.Map;

public class Theme {
    private final Map<DrawableType, DrawingInfo> drawingInfoMap;

	public Theme() {
        drawingInfoMap = new HashMap<>();
    }

    public DrawingInfo getDrawingInfo(DrawableType drawableType) {
        return drawingInfoMap.get(drawableType);
    }

    public void addDrawingInfo(DrawableType drawableType, DrawingInfo drawingInfo) {
        if (drawingInfoMap.containsKey(drawableType)) {
            drawingInfo = getDrawingInfo(drawableType).createMerged(drawingInfo);
        }
        drawingInfoMap.put(drawableType, drawingInfo);
    }
}
