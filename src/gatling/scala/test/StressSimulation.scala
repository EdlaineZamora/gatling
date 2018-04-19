package test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.parsing.json.JSONObject

/*
Cenário Desempenho: Create Passenger e Get Passengers

- Request de post passenger
- Request de get passengers
- 10 usuários no total inicializados ao mesmo tempo
- O teste inteiro tem duração infinita

- O que a gente quer com esse teste?
  - Testar quantos usuários e requisições o sistema suporta
  - Como o sistema se recupera de falhas
*/

class StressSimulation extends Simulation {

  def createAndGetPassenger = {
    exec(
      http("Create Passenger")
        .post("passengers")
        .body(StringBody(
          JSONObject.apply(
            Map("name" -> "TDC Floripa")
          ).toString()
        ))
    )
      .exec(http("Get Passengers")
        .get("passengers"))
  }

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")

  val postAndGetPassengerCenarioDesempenho = scenario("Stress: Create Passenger and Get Passengers")
    .forever() {
      createAndGetPassenger
    }

  setUp(
    postAndGetPassengerCenarioDesempenho.inject(
      atOnceUsers(1000)
    )
  ).protocols(httpConfiguration)
    .maxDuration(10 seconds)

}