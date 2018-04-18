package test

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.parsing.json.{JSONObject}

class CargaSimulation extends Simulation {

  val headers = Map(
    "Content-Type" -> "application/json"
  )

  val httpConfiguration = http.baseURL("https://airlineapi.herokuapp.com/")
    .headers(headers)

  val postPassengerCenarioCarga = scenario("Carga: Create Passengers")
          .exec(http("Create passengers")
          .post("passengers")
          .body(StringBody(
            JSONObject.apply(
              Map("name" -> "teste")
            ).toString()
            )
          )
        )

  setUp(
    postPassengerCenarioCarga.inject(
        rampUsersPerSec(1) to 2 during (2 minutes)
    )
  ).protocols(httpConfiguration)


}
