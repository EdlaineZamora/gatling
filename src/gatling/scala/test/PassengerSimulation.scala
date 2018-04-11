package test

import io.gatling.http.Predef._
import io.gatling.core.Predef._

class PassengerSimulation extends Simulation {

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")

  val passengerScenario = scenario("Find Passengers")
        .exec(http("Find all")
          .get("passengers"))
        .pause(5)

  setUp(
    passengerScenario.inject(atOnceUsers(4))
  ).protocols(httpConfiguration)

}