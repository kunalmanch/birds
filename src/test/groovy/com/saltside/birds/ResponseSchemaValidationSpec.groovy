package com.saltside.birds

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.saltside.birds.utils.Path
import com.saltside.birds.utils.SchemaValidator
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

import static com.saltside.birds.utils.Path.Schema.GET_RESPONSE_JSON_SCHEMA
import static com.saltside.birds.utils.Path.Schema.POST_RESPONSE_JSON_SCHEMA

/**
 * Created by kunal on 7/5/2017.
 */
class ResponseSchemaValidationSpec extends BaseSpec {

    static String requestBody = "{\n" +
            "\t\"name\": \"bluejay\",\n" +
            "\t\"family\": \"Corvidae\",\n" +
            "\t\"continents\": [\"asia\", \"europe\"],\n" +
            "\t\"visible\": true\n" +
            "}"
    static HttpClient httpClient = HttpClientBuilder.create().build()

    def "valid json response schema on a successful post request"() {
        when: "adding a bird is successful"
        HttpPost httpPost = new HttpPost(url + Path.Route.BIRDS)
        httpPost.setEntity(new StringEntity(requestBody))
        HttpResponse httpresponse = httpClient.execute(httpPost)

        then: "response should be as per the specified schema"
        SchemaValidator.validate(EntityUtils.toString(httpresponse.entity), POST_RESPONSE_JSON_SCHEMA) == true
    }

    def "valid json response schema on a successful get request"() {
        setup: "add a bird first"
        HttpPost httpPost = new HttpPost(url + Path.Route.BIRDS)
        httpPost.setEntity(new StringEntity(requestBody))
        HttpResponse httpresponse = httpClient.execute(httpPost)
        JsonObject obj = new Gson().fromJson(EntityUtils.toString(httpresponse.entity), JsonObject.class)

        when: "get request is successful"
        HttpGet httpGet = new HttpGet(url + Path.Route.BIRDS + "/" + obj.get("id").asString)
        httpresponse = httpClient.execute(httpGet)

        then: "response should be as per the specified schema"
        SchemaValidator.validate(EntityUtils.toString(httpresponse.entity), GET_RESPONSE_JSON_SCHEMA) == true
    }

    def "valid json response schema on a successful get all request"() {
        setup: "add a bird first"
        HttpPost httpPost = new HttpPost(url + Path.Route.BIRDS)
        httpPost.setEntity(new StringEntity(requestBody))
        HttpResponse httpresponse = httpClient.execute(httpPost)
        JsonObject obj = new Gson().fromJson(EntityUtils.toString(httpresponse.entity), JsonObject.class)

        when: "get all request is successful"
        HttpGet httpGet = new HttpGet(url + Path.Route.BIRDS)
        httpresponse = httpClient.execute(httpGet)

        then: "response should be as per the specified schema"
        JsonElement jsonElement = new Gson().fromJson(EntityUtils.toString(httpresponse.entity), JsonElement.class)
        jsonElement.isJsonArray() == true
        JsonArray jsonArray = jsonElement.asJsonArray
        jsonArray.size() != 0
        jsonArray.get(0).asString == obj.get("id").asString
    }
}
