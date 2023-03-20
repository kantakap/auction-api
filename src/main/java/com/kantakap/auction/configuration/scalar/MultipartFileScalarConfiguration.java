package com.kantakap.auction.configuration.scalar;

import graphql.schema.*;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileScalarConfiguration implements Coercing<MultipartFile, MultipartFile> {

    public static GraphQLScalarType fileScalar() {
        return GraphQLScalarType.newScalar()
                .name("MultipartFile")
                .description("Multipart file.")
                .coercing(new MultipartFileScalarConfiguration())
                .build();
    }

    @Override
    public MultipartFile serialize(Object dataFetcherResult) throws CoercingSerializeException {
        throw new CoercingSerializeException("Upload is an input-only type");
    }

    @Override
    public MultipartFile parseValue(Object input) throws CoercingParseValueException {
        if (input instanceof MultipartFile) {
            return (MultipartFile) input;
        }
        throw new CoercingParseValueException(
                String.format("Expected a 'MultipartFile' like object but was '%s'.", input != null ? input.getClass() : null)
        );
    }

    @Override
    public MultipartFile parseLiteral(Object input) throws CoercingParseLiteralException {
        throw new CoercingParseLiteralException("Parsing literal of 'MultipartFile' is not supported");
    }
}
