package com.adarsh.graphql.java.issue2087;

import graphql.TypeResolutionEnvironment;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.TypeDefinition;
import graphql.language.UnionTypeDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaProblem;

import java.net.URL;
import java.util.Map;

public class Issue2087 {
    public static void main(String[] args) throws Exception {
        final URL resource = Issue2087.class.getClassLoader().getResource("issue2087.graphqls");
        SchemaParser schemaParser = new SchemaParser();
        if (resource == null) {
            System.out.println("resource is not found");
            return;
        }
        final TypeDefinitionRegistry definitionRegistry = schemaParser.parse(resource.openStream());
        final Map<String, TypeDefinition> typeDefinitionMap = definitionRegistry.types();
        TypeResolver defaultTypeResolver = new DefaultTypeResolver();
        final RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring();
        // had to come up with default type resolver for 153 interface/union types to pass through MissingTypeResolver GraphQLError
        typeDefinitionMap.entrySet().stream()
                .filter(entry -> (entry.getValue() instanceof InterfaceTypeDefinition || entry.getValue() instanceof UnionTypeDefinition))
                .forEach(entry -> runtimeWiringBuilder.type(entry.getKey(), builder -> builder.typeResolver(defaultTypeResolver)));
        final RuntimeWiring runtimeWiring = runtimeWiringBuilder.build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        try {
            final GraphQLSchema schema = schemaGenerator.makeExecutableSchema(definitionRegistry, runtimeWiring);
            System.out.println("Making executable schema is a success." + schema);
        } catch (SchemaProblem sp) {
            sp.printStackTrace();
        }
    }

    static class DefaultTypeResolver implements TypeResolver {
        @Override
        public GraphQLObjectType getType(TypeResolutionEnvironment env) {
            return null;
        }
    }
}
