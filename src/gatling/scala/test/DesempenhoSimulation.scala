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
- O teste inteiro tem duração de 2 minutos

- O que a gente quer com esse teste?
  - Testar o desempenho (estabilidade) do sistema a medida que o tempo de execução aumenta
*/

class DesempenhoSimulation extends Simulation {

  def createAndGetPassenger = {
    exec(
      http("Create Passenger")
        .post("passengers")
        .body(StringBody(
          JSONObject.apply(
            Map("name" -> "MTC 2018")
          ).toString()
        ))
    )
      .exec(http("Get Passengers")
        .get("passengers"))
  }

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")

  val postPassengerCenarioDesempenho = scenario("Desempenho: Create and Get Passenger")
    .during(2 minutes) {
      createAndGetPassenger
    }

  setUp(
    postPassengerCenarioDesempenho.inject(
      atOnceUsers(10)
    )
  ).protocols(httpConfiguration)
    .maxDuration(2 minutes)

}