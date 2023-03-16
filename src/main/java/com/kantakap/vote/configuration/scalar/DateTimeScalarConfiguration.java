package com.kantakap.vote.configuration.scalar;

import graphql.language.StringValue;
import graphql.schema.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateTimeScalarConfiguration {

    public static GraphQLScalarType dateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("DateTime scalar type, i.e.: 2021-01-01T12:00:00.")
                .coercing(new DateTimeScalar())
                .build();
    }

    private static class DateTimeScalar implements Coercing<LocalDateTime, String> {

        @Override
        public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult instanceof LocalDateTime) {
                return ((LocalDateTime) dataFetcherResult).toString();
            }
            throw new CoercingSerializeException("Expected a LocalDate object.");
        }

        @Override
        public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
            try {
                if (input instanceof String) {
                    return LocalDateTime.parse((String) input);
                }
                throw new CoercingParseValueException("Expected a String object.");
            } catch (DateTimeParseException e) {
                throw new CoercingParseValueException("Invalid date time format.");
            }
        }

        @Override
        public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
                if (input instanceof StringValue) {
                    return parseValue(((StringValue) input).getValue());
                }
                throw new CoercingParseLiteralException("Expected a StringValue object.");
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Invalid date time format.");
            }
        }
    }
}
