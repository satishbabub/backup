package com.example.demo.model;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import static java.util.Collections.singletonList;

public class DataTypeModule extends SimpleModule {
    public DataTypeModule(){
        super("DataTypeModule", Version.unknownVersion(), singletonList(new DataTypeSerializer()));
    }
}
