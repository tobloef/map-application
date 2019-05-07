package bfst19.danmarkskort.model.drawables;

/**
 * Wrapper used so that a class can differentiate between a value being unassigned on purpose or not.
 */
public class NullWrapper<T> {
    T value;

    public NullWrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
