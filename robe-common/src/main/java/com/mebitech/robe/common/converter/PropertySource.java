package com.mebitech.robe.common.converter;

import java.io.InputStream;

/**
 * Created by kamilbukum on 10/03/2017.
 */
public class PropertySource<I> {
    private final Type type;
    private final I input;
    public PropertySource(Type type, I input) {
        this.type = type;
        this.input = input;
    }

    public Type getType() {
        return type;
    }

    public I getInput() {
        return input;
    }

    public enum Type {
        yaml, yml, json, properties
    }

}
