package com.adarsh.graphql.java.day2;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

public class ProgrammingStyleSchemaDefinition {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProgrammingStyleSchemaDefinition.class);

    public static void main(String[] args) {
        final GraphQLObjectType queryType = newObject().name("Foo")
                .field(newFieldDefinition().name("bar")
                        .type(GraphQLString))
                .build();

        final GraphQLCodeRegistry codeRegistry = newCodeRegistry().dataFetcher(
                coordinates("Foo", "bar")
                , new FooDataFetcher())
                .build();

        final GraphQLSchema schema = GraphQLSchema.newSchema()
                .codeRegistry(codeRegistry)
                .query(queryType)
                .build();

        final GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        final ExecutionResult executionResult = graphQL.execute("{ bar }");

        LOGGER.info("Execution result: {}", executionResult);
    }
}
