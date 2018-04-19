package test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.parsing.json.JSONObject

/*
Cenário Carga: Create Passengers

- Request de post passenger
- 5 usuários no total
- Cada usuário é inicializado a cada 24 segundos
- É enviado um header
- O teste inteiro tem duração de 2 minutos
- Uma pausa de 1 segundo entre as requisições do usuário
- Com assert de tempo de resposta máximo de todas as requisições sendo de no máximo 100 milissegundos
- Com assert de status http igual a 200
- Com assert de percentual de falha de todos os requests sendo menor que 5%

- O que a gente quer com esse teste?
  - Testar a carga do sistema em condições normais de uso a medida que o número de usuários aumenta
*/

class CargaSimulation extends Simulation {

  def createPassenger = {
    exec(
      http("Create Passenger")
        .post("passengers")
        .body(StringBody(
          JSONObject.apply(
            Map("name" -> "TDC Floripa")
          ).toString()
        ))
        .check(status.is(200))
    ).pause(1 seconds)
  }

  val headers = Map(
    "Content-Type" -> "application/json"
  )

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")
    .headers(headers)

  val postPassengerCenarioCarga = scenario("Carga: Create Passenger")
    .during(2 minutes) {
      createPassenger
    }

  setUp(
    postPassengerCenarioCarga.inject(
      rampUsers(5) over(2 minutes)
    )
  ).protocols(httpConfiguration)
    .assertions(forAll.failedRequests.percent.lessThan(5))
    .assertions(global.responseTime.max.lessThan(100))
    .maxDuration(2 minutes)

}