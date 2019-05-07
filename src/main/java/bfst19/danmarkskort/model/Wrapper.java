package bfst19.danmarkskort.model;

public class Wrapper<T> { //todo ind bedre navn, lav bedre kommentar
    T value;

    public Wrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
