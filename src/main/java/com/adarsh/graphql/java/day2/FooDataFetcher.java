package com.adarsh.graphql.java.day2;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FooDataFetcher implements DataFetcher<Map<String, Object>> {
    static final Logger LOGGER = LoggerFactory.getLogger(FooDataFetcher.class);

    @Override
    public Map<String, Object> get(DataFetchingEnvironment environment) throws Exception {
        LOGGER.info("selected fields: {}", environment.getSelectionSet().getFields().stream().map(f -> f.getName()).collect(Collectors.toSet()));
        Map<String, Object> values = new HashMap<>();
        values.put("bar", "some bar");
        values.put("baz", "some baz");
        return values;
    }
}
