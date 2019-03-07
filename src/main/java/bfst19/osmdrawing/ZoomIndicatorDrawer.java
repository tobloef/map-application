package bfst19.osmdrawing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;


public class ZoomIndicatorDrawer implements Drawer {
	private GraphicsContext graphicsContext;
	private int margin = 5;
	private int boxWidth = 40;
	private int boxHeight = 100;
	Font font;

	public ZoomIndicatorDrawer(GraphicsContext graphicsContext) {
		this.graphicsContext = graphicsContext;
		font = new Font(10);
	}

	@Override
	public void draw() {
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
