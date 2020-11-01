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
/*
schema {
  query: Laptop
}

"Directs the executor to include this field or fragment only when the `if` argument is true"
directive @include(
    "Included when true."
    if: Boolean!
  ) on FIELD | FRAGMENT_SPREAD | INLINE_FRAGMENT

"Directs the executor to skip this field or fragment when the `if`'argument is true."
directive @skip(
    "Skipped when true."
    if: Boolean!
  ) on FIELD | FRAGMENT_SPREAD | INLINE_FRAGMENT

"Marks the field or enum value as deprecated"
directive @deprecated(
    "The reason for the deprecation"
    reason: String = "No longer supported"
  ) on FIELD_DEFINITION | ENUM_VALUE

"Exposes a URL that specifies the behaviour of this scalar."
directive @specifiedBy(
    "The URL that specifies the behaviour of this scalar."
    url: String!
  ) on SCALAR

type Laptop {
  cpuCores: Int
  memoryInGB: Int
  os: String
}

*/
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
