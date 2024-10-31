package videogamedb.finalsimulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;


import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class VideoGameDbFullTest extends Simulation{

    // HTTP Protocol
 private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    //RUNTIME PARAMETERS
private static final int USERS = Integer.parseInt(System.getProperty("USERS","25"));
private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "15"));
private static final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION","30"));


    // FEEDER FOR TEST

 private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json").random();

    // BEFORE BLOCK

    @Override
    public void before() {
        System.out.printf("The number of users is %d %n", USERS);
        System.out.printf("Ramping up users over %d seconds%n", RAMP_DURATION);
        System.out.printf("Total test duration is %d seconds%n", TEST_DURATION);
    }


    //HTTP CALLS

    private static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"))
                    .check(jsonPath("$.token").saveAs("jwtToken")));


    private static ChainBuilder getAllVideoGames =
            exec(http("Get all video games")
                    .get("/videogame"));


    private static ChainBuilder createNewGame =
            feed(jsonFeeder)
                    .exec(http("Create New Game - {#name}")
                                    .post("/videogame")
                                    .header("Authorization", "Bearer #{jwtToken}")
                                    .body(ElFileBody("bodies/newGameTemplate2.json")).asJson());

    private static ChainBuilder getLastPostedGame =
            exec(http("Get the last posted game - #{name}")
                    .get("/videogame/#{id}")
                    .check(jsonPath("$.name").isEL("#{name}")));

    private static ChainBuilder deleteLastCreatedGame =
            exec(http("Delete last created game - #{name}")
                    .delete("/videogame/#{id}")
                    .header("Authorization", "Bearer #{jwtToken}")
                    .check(bodyString().is("Video game deleted")));





// SCENARIO OR USER JOURNEY
    // 1. Get all video games
    // 2. Create a new game
    // 3. Get details of newly created game
    // 4. Delete newly created game

    private ScenarioBuilder scn = scenario("Final Scenario")
            .forever().on(
                    exec(getAllVideoGames)
                            .pause(2)
                            .exec(authenticate)
                            .pause(2)
                            .exec(createNewGame)
                            .pause(2)
                            .exec(getLastPostedGame)
                            .pause(2)
                            .exec(deleteLastCreatedGame)
            );


    // LOAD SIMULATION
    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        rampUsers(USERS).during(RAMP_DURATION)
                ).protocols(httpProtocol)
        ).maxDuration(TEST_DURATION);
    }

    // AFTER BLOCK
    @Override
    public void after() {
        System.out.println("Stress test completed");
    }


}
