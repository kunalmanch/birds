package com.saltside.birds.utils

import spock.lang.Specification

import static com.saltside.birds.utils.Path.Schema.POST_REQUEST_JSON_SCHEMA

/**
 * Created by kunal on 7/5/2017.
 */
class SchemaValidatorTest extends Specification {
    def "post request with valid schema"() {
        given: "a valid input request for post"
        def input = "{\n" +
                "\t\"name\": \"bluejay\",\n" +
                "\t\"family\": \"Corvidae\",\n" +
                "\t\"continents\": [\"asia\", \"europe\"]\n" +
                "}"

        expect: "schema validation should be successful"
        SchemaValidator.validate(input, POST_REQUEST_JSON_SCHEMA) == true
    }

    def "post request invalid schema duplicate continents"() {
        given: "a post request with duplicate continents"
        def input = "{\n" +
                "\t\"name\": \"bluejay\",\n" +
                "\t\"family\": \"Corvidae\",\n" +
                "\t\"continents\": [\"asia\", \"asia\"]\n" +
                "}"

        expect: "schema validation shouldn't be successful"
        SchemaValidator.validate(input, POST_REQUEST_JSON_SCHEMA) == false
    }

    def "post request incomplete json"() {
        given: "an incomplete post request"
        def input = "{\n" +
                "\t\"name\": \"bluejay\",\n" +
                "\t\"family\": \"Corvidae\"\n" +
                "}"

        expect: "schema validation shouldn't be successful"
        SchemaValidator.validate(input, POST_REQUEST_JSON_SCHEMA) == false
    }
}
