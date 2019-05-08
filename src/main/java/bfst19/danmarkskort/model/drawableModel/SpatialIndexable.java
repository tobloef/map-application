package bfst19.danmarkskort.model.drawableModel;

public interface SpatialIndexable {
    float getRepresentativeX();

    float getRepresentativeY();

    Rectangle getMinimumBoundingRectangle();

    float euclideanDistanceSquaredTo(float x, float y);
}
