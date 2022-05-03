package br.magnus.vehicle

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class ProblemTest {

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/problem")
          .then()
             .statusCode(200)
             .body(`is`("hello"))
    }

}