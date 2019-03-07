package bfst19.osmdrawing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;


public class ZoomIndicatorDrawer implements Drawer {
	private GraphicsContext graphicsContext;

	public ZoomIndicatorDrawer(GraphicsContext graphicsContext) {
		this.graphicsContext = graphicsContext;
	}

	@Override
	public void draw() {
		int margin = 5;
		int boxWidth = 40;
		int boxHeight = 100;
		Font font = new Font(10);
		Affine affine = graphicsContext.getTransform();
		double oldLineWidth = graphicsContext.getLineWidth();
		graphicsContext.setTransform(new Affine());
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(margin, margin, boxWidth, boxHeight);
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.moveTo(margin + boxWidth - margin, margin + margin); //top line, right side
		graphicsContext.lineTo(margin + margin, margin + margin); // top line, left side
		graphicsContext.lineTo(margin + margin, margin + boxHeight - margin); //bottom line, left side
		graphicsContext.lineTo(margin + boxWidth - margin, margin + boxHeight - margin); // bottom line right side
		graphicsContext.stroke();
		graphicsContext.setLineWidth(0);
		graphicsContext.setFont(font);
		graphicsContext.strokeText("0", margin + boxWidth / 2f, margin + boxHeight / 2f);
		graphicsContext.setLineWidth(oldLineWidth);
		graphicsContext.setTransform(affine);
	}
}
