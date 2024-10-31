package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class MyFirstTest extends Simulation{

// 1 Http Configuration
    private HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://videogamedb.uk/api")
        .acceptHeader("application/json");

// 2 Scenario Definition
   private ScenarioBuilder scenario1 = scenario("My first test")
        .exec(http("Get all games")
                .get("/videogame"));



// 3 Load Simulation

    {
      setUp(
              scenario1.injectOpen(atOnceUsers(1))
      )
              .protocols(httpProtocol);
    }



}
