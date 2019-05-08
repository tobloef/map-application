package bfst19.danmarkskort;

import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.model.drawables.DrawingInfo;
import bfst19.danmarkskort.model.drawables.NullWrapper;
import bfst19.danmarkskort.model.drawables.Theme;
import bfst19.danmarkskort.utils.ThemeLoader;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ThemeLoaderTest {

	private Theme loadedTheme, expectedTheme;

	@BeforeAll
	void loadTestTheme(){
		String filePath = this.getClass().getResource("themeTest.yaml").getPath();
		loadedTheme = ThemeLoader.loadTheme(filePath);
		//Generate a theme based on the Yaml
		expectedTheme = new Theme();
		NullWrapper<Color> fillColorNull = null;
		NullWrapper<Color> strokeColorNull = null;
		NullWrapper<Double> lineDashNull = null;
		NullWrapper<Double> lineWidthNull = null;
		NullWrapper<Double> lineWidthMaxNull = null;
		NullWrapper<Double> lineWidthMinNull = null;
		NullWrapper<Double> zoomLevelNull = null;
		NullWrapper<Boolean> alwaysDrawNull = null;
		NullWrapper<ImagePattern> textureNull = null;
		NullWrapper<Color> fillColorTest = new NullWrapper<>(parseColor("EEF0D5"));
		NullWrapper<Double> lineWidthTest = new NullWrapper<>(0.0006);
		NullWrapper<Color> strokeColorTest = new NullWrapper<>(parseColor("FFFFFF"));
		DrawingInfo fillTest = new DrawingInfo(
				fillColorTest,
				strokeColorNull,
				lineDashNull,
				lineWidthNull,
				lineWidthMaxNull,
				lineWidthMinNull,
				zoomLevelNull,
				alwaysDrawNull,
				textureNull
		);
		DrawingInfo strokeTest = new DrawingInfo(
				fillColorNull,
				strokeColorTest,
				lineDashNull,
				lineWidthTest,
				lineWidthMaxNull,
				lineWidthMinNull,
				zoomLevelNull,
				alwaysDrawNull,
				textureNull
		);
		expectedTheme.addDrawingInfo(DrawableType.COASTLINE,fillTest);
		expectedTheme.addDrawingInfo(DrawableType.RESIDENTIAL_ROAD, strokeTest);
	}

	@Test
	void testLoadNewTheme(){
		DrawingInfo coastLineExpected = expectedTheme.getDrawingInfo(DrawableType.COASTLINE);
		DrawingInfo coastLineLoaded = loadedTheme.getDrawingInfo(DrawableType.COASTLINE);
		DrawingInfo roadExpected = expectedTheme.getDrawingInfo(DrawableType.RESIDENTIAL_ROAD);
		DrawingInfo roadLoaded = loadedTheme.getDrawingInfo(DrawableType.RESIDENTIAL_ROAD);
		//Assert values
		assertEquals(coastLineExpected.getFillColor(), coastLineLoaded.getFillColor());
		assertEquals(roadExpected.getLineWidth(), roadLoaded.getLineWidth());
		assertEquals(roadExpected.getStrokeColor(), roadLoaded.getStrokeColor());
	}

	@Test
	void testOverwrite(){
		String filePath = this.getClass().getResource("themeTestOverwrite.yaml").getPath();
		loadedTheme = ThemeLoader.loadTheme(filePath,loadedTheme);

		NullWrapper<Color> fillColorNull = null;
		NullWrapper<Color> strokeColorNew = new NullWrapper<>(parseColor("000000"));
		NullWrapper<Double> lineDashNull = null;
		NullWrapper<Double> lineWidthNull = null;
		NullWrapper<Double> lineWidthMaxNull = null;
		NullWrapper<Double> lineWidthMinNull = null;
		NullWrapper<Double> zoomLevelNull = null;
		NullWrapper<Boolean> alwaysDrawNull = null;
		NullWrapper<ImagePattern> textureNull = null;
		DrawingInfo newCoastline = new DrawingInfo(
			fillColorNull,
			strokeColorNew,
			lineDashNull,
			lineWidthNull,
			lineWidthMaxNull,
			lineWidthMinNull,
			zoomLevelNull,
			alwaysDrawNull,
			textureNull
		);
		expectedTheme.addDrawingInfo(DrawableType.RESIDENTIAL_ROAD, newCoastline);

		//Assert values
		DrawingInfo roadExpected = expectedTheme.getDrawingInfo(DrawableType.RESIDENTIAL_ROAD);
		DrawingInfo roadLoaded = loadedTheme.getDrawingInfo(DrawableType.RESIDENTIAL_ROAD);

		assertEquals(roadExpected.getFillColor(), roadLoaded.getFillColor());
	}

	private Color parseColor(Object value){
		String fillColorHex = null;
		if (value instanceof String) {
			fillColorHex = (String) value;
		} else if (value instanceof Integer) {
			fillColorHex = ((Integer) value).toString();
		}
		if (fillColorHex != null) {
			return Color.web(fillColorHex);
		}
		return null;
	}
}
