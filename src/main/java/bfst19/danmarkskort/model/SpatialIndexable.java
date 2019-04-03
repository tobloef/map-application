package bfst19.danmarkskort.model;

public interface SpatialIndexable {
	float getRepresentativeX();
	float getRepresentativeY();
	Rectangle getMinimumBoundingRectangle();
}
