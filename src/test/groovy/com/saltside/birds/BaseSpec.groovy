package com.saltside.birds

import com.google.inject.Guice
import com.saltside.birds.plumbing.TestModule
import com.saltside.birds.utils.Path
import groovyx.net.http.RESTClient
import spock.lang.Specification

import static spark.Spark.stop

/**
 * Created by kunal on 7/6/2017.
 */
class BaseSpec extends Specification {
    static def testPort = 4567
    static def url = "http://localhost:" + testPort
    static RESTClient restClient = new RESTClient(url)

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
}
