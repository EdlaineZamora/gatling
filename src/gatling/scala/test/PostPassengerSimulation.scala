package test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostPassengerSimulation extends Simulation {

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")

  val postPassengerScenario = scenario("Create Passenger")
        .exec(http("Create")
          .post("passengers")
          .body(StringBody("""{ "name": "myHardCodedValue" }""")).asJSON)
        .pause(5)

  setUp(
    postPassengerScenario.inject(atOnceUsers(1))
  ).protocols(httpConfiguration)

}