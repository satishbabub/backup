package com.example.demo.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DataTypeSerializer extends StdSerializer<DataType<Object>> {

    @SuppressWarnings("unused")
    DataTypeSerializer() {
        super(DataType.class, true);
    }

    @Override
    public void serialize(DataType<Object> value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeObject(value.value());
    }
}
