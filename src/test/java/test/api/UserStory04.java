package test.api;

import com.google.gson.Gson;
import dataaccess.Repository;
import model.Voucher;
import model.Vouchers;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import specs.ApiSpecifications;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;

@Listeners(config.ListenerTest.class)
public class UserStory04 {

    private static final Logger logger = LogManager.getLogger(UserStory04.class);
    private static final String VOUCHER_DATA = "src/test/resources/voucherData.json";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private Vouchers vouchers;

    @BeforeClass
    public void setUp() throws FileNotFoundException {
        Gson gson = new Gson();
        this.vouchers = gson.fromJson(new FileReader(VOUCHER_DATA), Vouchers.class);
        BasicConfigurator.configure();
    }

    /**
     * User Story 04/AC02
     */
    @Test(priority = 1, description = "Verify invalid data format for request voucher creation")
    public void validatePayloadforVoucher() {
        logger.info("invoked validatePayload voucher test");
        Assert.assertTrue(this.vouchers.getNatid().matches("natid-\\d{1,7}"), "Invalid natid format");
        Assert.assertTrue(this.vouchers.getName().matches("[A-Za-z\\s]{1,100}"), "Invalid name format");
        Assert.assertTrue(this.vouchers.getGender().equals("MALE") || this.vouchers.getGender().equals("FEMALE"), "Invalid gender");
        Assert.assertTrue(isValidBirthDateFormat(this.vouchers.getBirthDate()), "Invalid birthDate format or future date");
        Assert.assertTrue(this.vouchers.getDeathDate() == null || isValidDeathDateFormat(this.vouchers.getDeathDate()), "Invalid deathDate format");
        Assert.assertTrue(this.vouchers.getSalary() >= 0, "Salary cannot be negative");
        Assert.assertTrue(this.vouchers.getTaxPaid() >= 0, "TaxPaid cannot be negative");
        Assert.assertTrue((this.vouchers.getBrowniePoints() == 0) || (this.vouchers.getBrowniePoints() >= 0), "Invalid browniePoints");

        for (Voucher voucher : vouchers.getVouchers()) {
            Assert.assertNotNull(voucher.getVoucherName(), "VoucherName cannot be null");
            Assert.assertNotNull(voucher.getVoucherType(), "VoucherType cannot be null");
        }
    }

    /**
     * User Story 04/AC1
     */
    @Test(priority = 2, dependsOnMethods = "validatePayloadforVoucher", description = "Verify that the system successfully create the 'vouchers' with specified payload")
    public void voucherAPICreation() throws FileNotFoundException {
        logger.info("invoked workingClassCreation test");
        JSONObject voucherJSON = new JSONObject(vouchers);
        given()
                .spec(ApiSpecifications.getRequestSpecification())
                .body(voucherJSON.toString())
                .when()
                .post("/hero/vouchers")
                .then()
                .statusCode(200);
    }

    /**
     * User Story 04/AC03
     */
    @Test(priority = 3, dependsOnMethods = "validatePayloadforVoucher", description = "Verify that the system reject the 'vouchers' with invalid payload")
    public void voucherAPIInvalidCreation() throws FileNotFoundException {
        logger.info("invoked invalidWorkingClassCreation test");
        Gson gson = new Gson();
        this.vouchers = gson.fromJson(new FileReader(VOUCHER_DATA), Vouchers.class);
        this.vouchers.setVouchers(null);
        System.out.println(vouchers);
        JSONObject voucherJSON = new JSONObject(vouchers);
        given()
                .spec(ApiSpecifications.getRequestSpecification())
                .body(voucherJSON.toString())
                .when()
                .post("/hero/vouchers")
                .then()
                .statusCode(400);
    }

    /**
     * User Story 04/AC4
     */
    @Test(priority = 4, dependsOnMethods = "validatePayloadforVoucher", description = "Verify record creation in 'Voucher' database table")
    public void fetchVoucherFromDB() {
        logger.info("invoked fetchHeroFromDB test");
        Repository voucherRepo = new Repository();
        Voucher dbVoucher = voucherRepo.getVoucherByName(this.vouchers.getNatid());
        Assert.assertNotNull(dbVoucher.getVoucherName(), "Value not inserted");
    }

    /**
     * User Story 04/AC5 has implemented as dependsOnMethods = "validatePayloadforVoucher",
     */

    private boolean isValidBirthDateFormat(String birthDate) {
        logger.debug("inside isValidBirthDateFormat");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(birthDate);
            Date currentDate = new Date();
            if (parsedDate.after(currentDate)) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidDeathDateFormat(String deathDate) {
        logger.debug("inside isValidDeathDateFormat");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(deathDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
