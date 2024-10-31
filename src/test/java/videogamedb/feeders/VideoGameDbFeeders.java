package videogamedb.feeders;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGameDbFeeders extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");
// csv feeder
//    private static FeederBuilder.FileBased<String> csvFeeder = csv("data/gameCsvFile.csv").circular();
//json feeder
//private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json").random();

    public static LocalDate randomDate() {
        LocalDate minDate = LocalDate.of(1990,1,1);
        LocalDate maxDate = LocalDate.now();

        long minDay = minDate.toEpochDay();
        long maxDay = maxDate.toEpochDay();

        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

 private static Iterator<Map<String, Object>> customFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        Random rand = new Random();
                        int gameId = rand.nextInt(10 - 1 + 1) +1 ;

                      String gameName = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
                      String releaseDate = randomDate().toString();
                      int reviewScore = rand.nextInt(100 - 1 + 1) + 1;
                      String category = RandomStringUtils.randomAlphanumeric(5) + "-category";
                      String rating = RandomStringUtils.randomAlphanumeric(4) + "-rating";

                      HashMap<String, Object> hmap = new HashMap<String, Object>();
                      hmap.put("gameId", gameId);
                      hmap.put("gameName",gameName);
                      hmap.put("releaseDate", releaseDate);
                      hmap.put("reviewScore", reviewScore);
                      hmap.put("category", category);
                      hmap.put("rating", rating);
                      return hmap;

            }

            ).iterator();

    private static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"))
                    .check(jsonPath("$.token").saveAs("jwtToken")));

//    private static ChainBuilder getSpecificGame =
//            feed(customFeeder)
//                    .exec(http("Get video game with id - #{gameId}")
//                    .get("/videogame/#{gameId}"));

    private static ChainBuilder createNewGame =
            feed(customFeeder)
                    .exec(http("Create New game - #{gameName}")
                            .post("/videogame")
                            .header("authorization", "Bearer #{jwtToken}")
                            .body(ElFileBody("bodies/newGameTemplate.json")).asJson()
                            .check(bodyString().saveAs("responseBody")))
                    .exec(session -> {
                        System.out.println(session.getString("responseBody"));
                        return session;
                    });



    private ScenarioBuilder scn = scenario("Video Game Db - Section 6 code")
            .exec(authenticate)
            .repeat(10).on(
                    exec(createNewGame)
//                            .pause(1)
            );


    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }

}
