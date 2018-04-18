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

  def createPassenger = {
    exec(
      http("Create Passenger")
        .post("passengers")
        .body(StringBody(
          JSONObject.apply(
            Map("name" -> "TDC Floripa")
          ).toString()
        ))
    )
  }

  def getPassengers = {
    exec(
      http("Get Passengers")
        .get("passengers")
    )
  }

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")

  val postAndGetPassengerCenarioDesempenho = scenario("Desempenho: Create Passenger and Get Passengers")
    .during(2 minutes) {
      createPassenger
      getPassengers
    }

  setUp(
    postAndGetPassengerCenarioDesempenho.inject(
      atOnceUsers(10)
    )
  ).protocols(httpConfiguration)

}