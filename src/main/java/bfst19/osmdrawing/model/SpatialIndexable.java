package bfst19.osmdrawing.model;

public interface SpatialIndexable {
	float getRepresentativeX();
	float getRepresentativeY();
	Rectangle getMinimumBoundingRectangle();
}
