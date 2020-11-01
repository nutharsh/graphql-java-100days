package com.adarsh.graphql.java.day1;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.SelectedField;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleSDLExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSDLExample.class);
    private static final List<String> graphqlFiles = Collections.unmodifiableList(
            Arrays.asList(
                    "Root.graphql",
                    "User.graphql"
            )
    );

    public static void main(String[] args) {
        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();
        SchemaParser schemaParser = new SchemaParser();
        graphqlFiles.forEach(graphqlFile -> {
                    try {
                        final URL resource = Thread.currentThread().getContextClassLoader().getResource(graphqlFile);
                        assert resource != null;
                        LOGGER.info("found file: {}", resource);
                        typeDefinitionRegistry.merge(schemaParser.parse(resource.openStream()));
                    } catch (Exception e) {
                        LOGGER.error("error while reading graphql config files", e);
                    }
                }
        );
        final RuntimeWiring rw = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("user", new UserDataFetcher()))
                .build();
        SchemaGenerator sg = new SchemaGenerator();
        final GraphQLSchema executableSchema = sg.makeExecutableSchema(typeDefinitionRegistry, rw);
        final GraphQL graphQLObj = GraphQL.newGraphQL(executableSchema).build();

        final ExecutionResult executionResult = graphQLObj.execute("{user{id name}}");
        LOGGER.info("execution result: {}", executionResult);
        LOGGER.info("execution result spec: {}", executionResult.toSpecification());
    }

    static class UserDataFetcher implements DataFetcher<User> {
        static final Logger LOG = LoggerFactory.getLogger(UserDataFetcher.class);

        @Override
        public User get(DataFetchingEnvironment environment) {
            final List<SelectedField> fields = environment.getSelectionSet().getFields();
            final Set<String> selectedFields = fields.stream().map(SelectedField::getName).collect(Collectors.toSet());
            LOG.info("selectedFields: {}", selectedFields);
            final User user = new User(UUID.randomUUID().toString(), "name" + System.currentTimeMillis());
            LOG.info("user: {}", user);
            return user;
        }
    }
}
