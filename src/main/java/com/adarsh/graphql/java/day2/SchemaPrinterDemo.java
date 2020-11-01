package com.adarsh.graphql.java.day2;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLObjectType.newObject;

public class SchemaPrinterDemo {

    public static final Logger LOGGER = LoggerFactory.getLogger(SchemaPrinterDemo.class);

    public static void main(String[] args) {
        final GraphQLObjectType queryTypes = newObject()
                .name("Laptop")
                .field(new GraphQLFieldDefinition.Builder().name("memoryInGB").type(GraphQLInt))
                .field(new GraphQLFieldDefinition.Builder().name("cpuCores").type(GraphQLInt))
                .field(new GraphQLFieldDefinition.Builder().name("os").type(GraphQLString))
                .build();

        final GraphQLSchema schema = GraphQLSchema.newSchema().query(queryTypes).build();

        final String schemaAsString = new SchemaPrinter().print(schema);
        LOGGER.info("schema: {}", schemaAsString);
    }
}
