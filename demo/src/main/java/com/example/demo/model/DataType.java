package com.example.demo.model;

import java.util.Objects;

public abstract class DataType<T> {
    private final T value;

    protected DataType(T value) {
        this.value = value;
    }

    public T value() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataType<?> microType = (DataType<?>) o;
        return Objects.equals(this.value(), microType.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value());
    }
}
