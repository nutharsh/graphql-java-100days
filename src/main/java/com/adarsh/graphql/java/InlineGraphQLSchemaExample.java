package com.adarsh.graphql.java;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InlineGraphQLSchemaExample {
    private static final Logger LOG = LoggerFactory.getLogger(InlineGraphQLSchemaExample.class);

    public static void main(String[] args) {
        // define inline schema definition
        String schema = "type Query { greeting : String }";

        // takes graphql schema definition and spits type def registry
        SchemaParser schemaParser = new SchemaParser();

        // parse inline schema def
        final TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        // runtime wiring helps us to register handlers/resolvers for fields under types
        // such as 'greeting' field under 'Query' type
        final RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query",
                        builder -> builder.dataFetcher(
                                "greeting",
                                new StaticDataFetcher("Hello World! Welcome to graphql-java.")))
                .build();

        // schema generator take both type registry and runtime wiring to
        // create executable schema
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        final GraphQLSchema executableGraphQLSchema =
                schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        /*
        graphql object which is ready to execute query documents for us.
         */
        final GraphQL graphQL = GraphQL.newGraphQL(executableGraphQLSchema).build();

        // Get Set Go
        final ExecutionResult executionResult = graphQL.execute("{ greeting }");
        LOG.info("is data present?: {}", executionResult.isDataPresent());
        LOG.info("errors: {}", executionResult.getErrors());
        LOG.info("extensions: {}", executionResult.getExtensions());
        LOG.info("to specs: {}", executionResult.toSpecification());
        LOG.info("data: {}", executionResult.isDataPresent()?executionResult.getData(): "no data present!!!");
    }
}
