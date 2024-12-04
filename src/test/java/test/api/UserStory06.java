package test.api;

import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import specs.ApiSpecifications;

import static io.restassured.RestAssured.given;

@Listeners(config.ListenerTest.class)
public class UserStory06 {

    private static final Logger logger = LogManager.getLogger(UserStory06.class);
    private Response response;

    @BeforeClass
    public void setUp() {
        BasicConfigurator.configure();
    }

    /**
     * User Story 06/AC1
     */
    @Test(priority = 1, description = "Verify that the system successfully retrieve existing voucher details")
    public void testVoucherInsights() {
        logger.info("invoked testVoucherInsights test");
        this.response = given()
                .spec(ApiSpecifications.getRequestSpecification())
                .when()
                .get("/voucher/by-person-and-type");
        response.then().statusCode(200);
    }
}
