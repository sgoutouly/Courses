package views;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


import utils.ObjectIdSerializer;


public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        SimpleModule module = new SimpleModule("ObjectIdmodule");
        module.addSerializer(new ObjectIdSerializer());
        this.registerModule(module);
    }

}