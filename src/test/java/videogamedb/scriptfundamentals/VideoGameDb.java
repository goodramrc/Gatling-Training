package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGameDb extends Simulation{

   private HttpProtocolBuilder httpProtocol = http
           .baseUrl("https://videogamedb.uk/api")
           .acceptHeader("application/json");
//           .contentTypeHeader("application/json");

   private static ChainBuilder authenticate =
           exec(http("Authenticate")
                   .post("/authenticate")
                   .header("content-type", "application/json")
                   .body(StringBody("{\n" +
                           "  \"password\": \"admin\",\n" +
                           "  \"username\": \"admin\"\n" +
                           "}"))
                   .check(jsonPath("$.token").saveAs("jwtToken")));

   private static ChainBuilder createNewGame =
           exec(http("Create new game")
                   .post("/videogame")
                   .header("Authorization", "Bearer #{jwtToken}")
                   .header("content-type", "application/json")
                   .body(StringBody(
                           "{\n" +
                                   "  \"category\": \"Platform\",\n" +
                                   "  \"name\": \"Mario\",\n" +
                                   "  \"rating\": \"Mature\",\n" +
                                   "  \"releaseDate\": \"2012-05-04\",\n" +
                                   "  \"reviewScore\": 85\n" +
                                   "}"
                   )));



//   private static ChainBuilder getAllVideoGames =
//           repeat(3).on(exec(http("Get all videogames")
//                   .get("/videogame")
//                   .check(status().is(200))));
//
//
//   private static ChainBuilder getSpecificGame =
//           repeat(5,"myCounter").on(
//                   exec(http("Get specific game with id: #{myCounter}")
//                           .get("/videogame/#{myCounter}")
//                           .check(status().is(200))));





   private ScenarioBuilder scenario2 = scenario("Video Game Db - Section 5 code")

//           .exec(getAllVideoGames)
//           .pause(5)
//           .exec(getSpecificGame)
//           .pause(5)
//           .repeat(2).on(
//                   exec(getAllVideoGames));
//           .exec(http("Get all video games - 1st call")
//                   .get("/videogame")
//                   .check(status().is(200))
//                   .check(jsonPath("$[?(@.id == 1)].name").is("Resident Evil 4")))
//           .pause(5)

//           .exec(http("Get specific game")
//                   .get("/videogame/1")
//                   .check(status().in(200, 201, 202))
//                   .check(jsonPath("$.name").is("Resident Evil 4"))
//           )
//           .pause(1,10)
//
//           .exec(http("Get all videogames")
//                   .get("/videogame")
//                   .check(status().not(404), status().not(500))
//                   .check(jsonPath("$[1].id").saveAs("gameId"))
//           )
//           .pause(Duration.ofMillis(4000))
//
//
//           .exec(http("Get specific game with Id - #{gameId}")
//                   .get("/videogame/#{gameId}")
//                   .check(jsonPath("$.name").is("Gran Turismo 3"))
//                   .check(bodyString().saveAs("responseBody")));


           .exec(authenticate)
           .exec(createNewGame);


    {
        setUp(
                scenario2.injectOpen(atOnceUsers(1))
        )
                .protocols(httpProtocol);
    }


}
