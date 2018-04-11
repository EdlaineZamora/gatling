package test

import io.gatling.http.Predef._
import io.gatling.core.Predef._

class GetPassengerSimulation extends Simulation {

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")

  val getPassengerScenario = scenario("Find Passengers")
        .exec(http("Find all")
          .get("passengers"))
        .pause(5)

  setUp(
    getPassengerScenario.inject(atOnceUsers(1))
  ).protocols(httpConfiguration)

}