package com.xczg.blockchain.yibaodapp.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;


public class    JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    static {
        //objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //忽略空值
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//解析时忽略不认识的字段
    }

    public static <T> T fromJson(String jsonAsString, Class<T> pojoClass) throws JsonMappingException,
            JsonParseException, IOException {
        return objectMapper.readValue(jsonAsString, pojoClass);
    }

    public static <T> T fromJson(FileReader fr, Class<T> pojoClass) throws JsonParseException, IOException {
        return objectMapper.readValue(fr, pojoClass);
    }

    public static String toJson(Object pojo) throws JsonMappingException, JsonGenerationException, IOException {
        return toJson(pojo, false);
    }

    public static String toJson(Object pojo, boolean prettyPrint) throws JsonMappingException, JsonGenerationException,
            IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jg = jsonFactory.createGenerator(sw);//createJsonGenerator(sw);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        objectMapper.writeValue(jg, pojo);
        return sw.toString();
    }

    public static void toJson(Object pojo, FileWriter fw, boolean prettyPrint) throws JsonMappingException,
            JsonGenerationException, IOException {
        JsonGenerator jg = jsonFactory.createGenerator(fw);//createJsonGenerator(fw);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        objectMapper.writeValue(jg, pojo);
    }

    public static Map<String, Object> parseMap(String jsonStr) throws IOException {
        Map<String, Object> map = objectMapper.readValue(jsonStr, Map.class);
        return map;
    }

    public static JsonNode parse(String jsonStr) throws IOException {
        JsonNode node = null;
        node = objectMapper.readTree(jsonStr);
        return node;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> List<T> fromJsonToList(String jsonAsString, Class<T> pojoClass)  throws JsonMappingException,
            JsonParseException, IOException {
        return JSONArray.parseArray(jsonAsString, pojoClass);
    }

}

