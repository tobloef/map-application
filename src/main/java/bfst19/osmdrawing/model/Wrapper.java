package bfst19.osmdrawing.model;

public class Wrapper<T> {
    T value;

    public Wrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
