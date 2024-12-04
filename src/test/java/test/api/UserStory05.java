package test.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import model.Hero;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import specs.ApiSpecifications;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

@Listeners(config.ListenerTest.class)
public class UserStory05 {

    private static final String HEROES_DATA = "src/test/resources/heroData.json";

    private static final Logger logger = LogManager.getLogger(UserStory05.class);
    private Hero hero;
    private int natID;
    private Response response;

    private String respond;

    @BeforeClass
    public void setUp() throws FileNotFoundException {
        Gson gson = new Gson();
        this.hero = gson.fromJson(new FileReader(HEROES_DATA), Hero.class);
        BasicConfigurator.configure();
    }

    /**
     * User Story 05/AC02
     */
    @Test(priority = 1, description = "validate natid parameters")
    public void validateNatid() {

        this.natID = Integer.parseInt(this.hero.getNatid().split("-")[1]);
        String numberPattern = "^[+-]?\\d*\\.?\\d+$";
        Pattern pattern = Pattern.compile(numberPattern);
        Matcher matcher = pattern.matcher(this.hero.getNatid().split("-")[1]);
        Assert.assertTrue(matcher.matches(), "String is not a valid number");
    }

    /**
     * User Story 05/AC01
     */
    @Test(priority = 2, description = "Verify that the system successfully retrieve the OWE money details by specified <natid>")
    public void getOweMoneyDetails() {

        logger.info("invoked getOweMoneyDetails test");
        this.response = given()
                .spec(ApiSpecifications.getRequestSpecification())
                .when()
                .get("/hero/owe-money?natid=" + natID);
        response.then().statusCode(200);//.body("natid", matchesPattern("\\d+"));
    }

    /**
     * User Story 05/AC4
     */
    @Test(priority = 3, description = "validate natid parameters")
    public void validateResponse() {
        JsonObject jsonResponse = JsonParser
                .parseString(response
                        .getBody()
                        .asString())
                .getAsJsonObject();
        this.respond = jsonResponse.get("message").getAsString();
        Assert.assertNotNull(respond, "Invalid type");
    }

    /**
     * User Story 05/AC3
     */
    @Test(priority = 4, description = "validate natid param")
    public void validateMessageResponse() {
        JsonObject jsonResponse = null;

        try {
            jsonResponse = JsonParser
                    .parseString(respond)
                    .getAsJsonObject();
        } catch (NullPointerException e) {

        }

        Assert.assertTrue(jsonResponse.get("status").getAsString().equals("OWE") || jsonResponse.get("status").getAsString().equals("NIL"), "Invalid type");
        Assert.assertEquals(jsonResponse.get("data").getAsString(), ("natid-" + natID), "Failed to retrieve natid.");
    }

}
