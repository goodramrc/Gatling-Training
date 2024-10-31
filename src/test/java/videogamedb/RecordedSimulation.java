package videogamedb;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class RecordedSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("https://www.videogamedb.uk")
      .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
      .acceptHeader("application/json")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-GB,en;q=0.9,en-US;q=0.8")
      .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0");
    
    Map<CharSequence, String> headers_0 = new HashMap<>();
    headers_0.put("Sec-Fetch-Dest", "empty");
    headers_0.put("Sec-Fetch-Mode", "cors");
    headers_0.put("Sec-Fetch-Site", "same-origin");
    headers_0.put("sec-ch-ua", "Microsoft Edge\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129");
    headers_0.put("sec-ch-ua-mobile", "?0");
    headers_0.put("sec-ch-ua-platform", "Windows");
    
    Map<CharSequence, String> headers_2 = new HashMap<>();
    headers_2.put("Content-Type", "application/json");
    headers_2.put("Origin", "https://www.videogamedb.uk");
    headers_2.put("Sec-Fetch-Dest", "empty");
    headers_2.put("Sec-Fetch-Mode", "cors");
    headers_2.put("Sec-Fetch-Site", "same-origin");
    headers_2.put("sec-ch-ua", "Microsoft Edge\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129");
    headers_2.put("sec-ch-ua-mobile", "?0");
    headers_2.put("sec-ch-ua-platform", "Windows");
    
    Map<CharSequence, String> headers_3 = new HashMap<>();
    headers_3.put("Content-Type", "application/json");
    headers_3.put("Origin", "https://www.videogamedb.uk");
    headers_3.put("Sec-Fetch-Dest", "empty");
    headers_3.put("Sec-Fetch-Mode", "cors");
    headers_3.put("Sec-Fetch-Site", "same-origin");
    headers_3.put("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNzcwODczNywiZXhwIjoxNzI3NzEyMzM3fQ.sMdJwt0rFJBCiXuqvhR1CLxwn-8ExFXUZop0_DlnlyQ");
    headers_3.put("sec-ch-ua", "Microsoft Edge\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129");
    headers_3.put("sec-ch-ua-mobile", "?0");
    headers_3.put("sec-ch-ua-platform", "Windows");
    
    Map<CharSequence, String> headers_5 = new HashMap<>();
    headers_5.put("Origin", "https://www.videogamedb.uk");
    headers_5.put("Sec-Fetch-Dest", "empty");
    headers_5.put("Sec-Fetch-Mode", "cors");
    headers_5.put("Sec-Fetch-Site", "same-origin");
    headers_5.put("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNzcwODczNywiZXhwIjoxNzI3NzEyMzM3fQ.sMdJwt0rFJBCiXuqvhR1CLxwn-8ExFXUZop0_DlnlyQ");
    headers_5.put("sec-ch-ua", "Microsoft Edge\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129");
    headers_5.put("sec-ch-ua-mobile", "?0");
    headers_5.put("sec-ch-ua-platform", "Windows");


    ScenarioBuilder scn = scenario("RecordedSimulation")
      .exec(
        http("request_0")
          .get("/api/videogame")
          .headers(headers_0)
      )
      .pause(32)
      .exec(
        http("request_1")
          .get("/api/videogame/2")
          .headers(headers_0)
      )
      .pause(101)
      .exec(
        http("request_2")
          .post("/api/authenticate")
          .headers(headers_2)
          .body(RawFileBody("videogamedb/recordedsimulation/0002_request.json"))
      )
      .pause(54)
      .exec(
        http("request_3")
          .post("/api/videogame")
          .headers(headers_3)
          .body(RawFileBody("videogamedb/recordedsimulation/0003_request.json"))
      )
      .pause(21)
      .exec(
        http("request_4")
          .put("/api/videogame/3")
          .headers(headers_3)
          .body(RawFileBody("videogamedb/recordedsimulation/0004_request.json"))
      )
      .pause(18)
      .exec(
        http("request_5")
          .delete("/api/videogame/3")
          .headers(headers_5)
      );

	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
