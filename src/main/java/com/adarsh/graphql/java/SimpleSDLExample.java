package com.adarsh.graphql.java;

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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleSDLExample {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleSDLExample.class);

    public static void main(String[] args) throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        final URL resource = Thread.currentThread().getContextClassLoader().getResource("starter.graphql");
        assert resource != null;
        final TypeDefinitionRegistry tdr = schemaParser.parse(resource.openStream());
        final RuntimeWiring rw = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("user", new UserDataFetcher()))
                .build();
        SchemaGenerator sg = new SchemaGenerator();
        final GraphQLSchema executableSchema = sg.makeExecutableSchema(tdr, rw);

        final GraphQL graphQLObj = GraphQL.newGraphQL(executableSchema).build();

        final ExecutionResult executionResult = graphQLObj.execute("{user{id name}}");
        LOG.info("execution result: {}", executionResult);
        LOG.info("execution result spec: {}", executionResult.toSpecification());

    }

    static class UserDataFetcher implements DataFetcher<User> {
        static final Logger LOG = LoggerFactory.getLogger(UserDataFetcher.class);

        @Override
        public User get(DataFetchingEnvironment environment) throws Exception {
            final List<SelectedField> fields = environment.getSelectionSet().getFields();
            final Set<String> selectedFields = fields.stream().map(SelectedField::getName).collect(Collectors.toSet());
            LOG.info("selectedFields: {}", selectedFields);
            final User user = new User(UUID.randomUUID().toString(), "name" + System.currentTimeMillis());
            LOG.info("user: {}", user);
            return user;
        }
    }
}
