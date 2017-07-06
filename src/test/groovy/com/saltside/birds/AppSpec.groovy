package com.saltside.birds

import com.google.inject.Guice
import com.saltside.birds.plumbing.TestModule
import com.saltside.birds.utils.DateUtil
import com.saltside.birds.utils.Path
import groovyx.net.http.RESTClient
import spock.lang.Specification
import spock.lang.Unroll

import static spark.Spark.*

/**
 * Created by kunal on 7/6/2017.
 */
class AppSpec extends Specification {

    static def testPort = 4567
    static RESTClient restClient = new RESTClient("http://localhost:" + testPort)

    def setupSpec() {
        restClient.handler.failure = { resp -> resp.status }
        def injector = Guice.createInjector(new TestModule())
        def birdController = injector.getInstance(BirdController.class)
        App.start(birdController, testPort)
    }

    def cleanupSpec() {
        stop()
    }

    def setup() {
        // clear out data from db before each spec is run
        // so that each spec test is self contained.
        def response = restClient.get(path: Path.Route.BIRDS)
        for (String id : response.data) {
            restClient.delete(path: Path.Route.BIRDS + "/" + id)
        }
    }

    def validateResponse(response, reqBody, status, visibility) {
        return response.status == status &&
                response.headers['Content-Type'].toString() == 'Content-Type: application/json' &&
                response.data.id != null &&
                response.data.name == reqBody['name'] &&
                response.data.family == reqBody['family'] &&
                response.data.continents == reqBody['continents'] &&
                response.data.added == DateUtil.now() &&
                response.data.visible == visibility
    }

    @Unroll
    def "valid post request"() {
        when: "valid request is sent to the server"
        def response = restClient.post( path: Path.Route.BIRDS, body: reqBody, requestContentType: 'application/json')

        then: "bird is added to db and has an id with appropriate visibility"
        validateResponse(response, reqBody, 201, visibility) == true

        where:
        reqBody << [
                ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
                , ['name':'mockingbird', 'family':'mimidae', 'continents':['america', 'europe'], 'visible':true]
                , ['name':'quail', 'family':'phasianidae', 'continents':['asia', 'europe'], 'visible':false]
        ]
        visibility << [false, true, false]
    }

    @Unroll
    def "bad post request"() {
        when: "bad request is sent to the server"
        def response = restClient.post( path: Path.Route.BIRDS, body: requestBody, requestContentType: 'application/json')

        then: "request fails validations, expect 400"
        response == 400

        where:
        requestBody << [
                ''
                , null
                , ['name':'bluejay', 'family':'corvidae']
                , ['name':'bluejay', 'invalid_field':'value']
                , ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe'], 'visible':'true']
        ]
    }

    @Unroll
    def "valid get request"() {
        setup: "add some birds first"
        def postResp = restClient.post( path: Path.Route.BIRDS, body: postReqBody, requestContentType: 'application/json')
        def id = postResp.data.id

        when: "sending valid id to get a bird's info"
        def response = restClient.get( path: Path.Route.BIRDS + "/" + id)

        then: "request is honored and bird info is served"
        validateResponse(response, postReqBody, 200, visibility) == true
        response.data.id == id

        where:
        postReqBody << [
                ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
                , ['name':'mockingbird', 'family':'mimidae', 'continents':['america', 'europe'], 'visible':true]
                , ['name':'quail', 'family':'phasianidae', 'continents':['asia', 'europe'], 'visible':false]
        ]
        visibility << [false, true, false]
    }

    def "invalid get request"() {
        setup: "add a bird first"
        def postReqBody = ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
        restClient.post(path: Path.Route.BIRDS, body: postReqBody, requestContentType: 'application/json')
        def invalidId = "invalid_id"

        when: "sending invalid id to server"
        def response = restClient.get(path: Path.Route.BIRDS + "/" + invalidId)

        then: "request is not honored, expect 404"
        response == 404
    }

    def "get all birds"() {
        setup: "add some birds first"
        def postReqBody = [
                ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
                , ['name':'mockingbird', 'family':'mimidae', 'continents':['america', 'europe'], 'visible':true]
                , ['name':'quail', 'family':'phasianidae', 'continents':['asia', 'europe'], 'visible':false]
        ]
        def ids = []
        for (int i= 0; i < 3; i++) {
            def resp = restClient.post( path: Path.Route.BIRDS, body: postReqBody[i], requestContentType: 'application/json')
            if (resp.data.visible) ids[i] = resp.data.id
        }


        when: "getting all birds' ids"
        def response = restClient.get(path: Path.Route.BIRDS)

        then: "request is honored and an array of bird ids is served"
        response.status == 200
        response.headers['Content-Type'].toString() == 'Content-Type: application/json'
        response.data.size() == ids.size() //only visible birds
        for (String id : response.data) {
            ids.contains(id) == true
        }
    }

    def "get all birds when db is empty"() {
        when: "trying to get all birds' ids with an empty db"
        def response = restClient.get(path: Path.Route.BIRDS)

        then: "request is honored and an empty array is sent"
        response.status == 200
        response.headers['Content-Type'].toString() == 'Content-Type: application/json'
        response.data.isEmpty() == true
    }

    @Unroll
    def "valid delete request"() {
        setup: "add some birds first"
        def postResp = restClient.post( path: Path.Route.BIRDS, body: postReqBody, requestContentType: 'application/json')
        def id = postResp.data.id

        when: "sending valid id to delete a bird"
        def response = restClient.delete( path: Path.Route.BIRDS + "/" + id)

        then: "request is honored and bird is deleted"
        response.status == 200

        where:
        postReqBody << [
                ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
                , ['name':'mockingbird', 'family':'mimidae', 'continents':['america', 'europe'], 'visible':true]
                , ['name':'quail', 'family':'phasianidae', 'continents':['asia', 'europe'], 'visible':false]
        ]
        visibility << [false, true, false]
    }

    def "invalid delete request"() {
        setup: "add a bird first"
        def postReqBody = ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
        restClient.post(path: Path.Route.BIRDS, body: postReqBody, requestContentType: 'application/json')
        def invalidId = "invalid_id"

        when: "sending invalid id to server for delete"
        def response = restClient.delete(path: Path.Route.BIRDS + "/" + invalidId)

        then: "request is not honored, expect 404"
        response == 404
    }

    def "get request for a deleted bird"() {
        setup: "add a bird first"
        def postReqBody = ['name':'bluejay', 'family':'corvidae', 'continents':['asia', 'europe']]
        def postResp = restClient.post(path: Path.Route.BIRDS, body: postReqBody, requestContentType: 'application/json')
        def id = postResp.data.id
        restClient.delete( path: Path.Route.BIRDS + "/" + id)

        when: "sending get request for a deleted id"
        def response = restClient.get( path: Path.Route.BIRDS + "/" + id)

        then: "request is not honored, expect 404"
        response == 404
    }

    def "bad route"() {
        when: "request is sent on a bad route"
        def postResp = restClient.post(path: "/bad_route")
        def getResp1 = restClient.get(path: "/bad_route")
        def getResp2 = restClient.get(path: "/birds/")

        then: "server sends a 404"
        postResp == 404
        getResp1 == 404
        getResp2 == 404
    }
}
