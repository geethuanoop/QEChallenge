package test.ui;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.Hero;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UploadCSVPage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners(config.ListenerTest.class)
public class UserStory02 extends BaseTest {

    private static final Logger logger = LogManager.getLogger(UserStory02.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private CSVReader reader;

    @BeforeMethod
    public void loginBeforeTest(Method method) throws FileNotFoundException {

        if (super.shouldSkipBeforeMethod(method.getName())) {
            LoginPage loginPage = new LoginPage(super.getDriver());
            loginPage.enterUsername("clerk");
            loginPage.enterPassword("clerk");
            loginPage.clickLoginButton();
            DashboardPage dashboardPage = new DashboardPage(super.getDriver());
            dashboardPage.clickAddHeroDropdown();
            dashboardPage.selectUploadCSV();
        } else {
            this.reader = new CSVReader(new FileReader(super.getResource().getString("file_path")));
        }
    }

    /**
     * User Story 2 / AC01
     */
    @Test(priority = 1, description = "Verify user can upload the csv file")
    public void testUploadCSVButtonClick() {
        logger.info("invoked userUpload csv");

        Assert.assertEquals(super.getDriver().getCurrentUrl(),
                (super.getResource().getString("base_url") + "clerk/upload-csv"),
                "Unable to to find upload csv path");
    }

    /**
     * User Story 2 / AC02
     */
    @Test(priority = 2, description = "Verify csv file contains valid data")
    public void testCSVFields() throws CsvValidationException, IOException {
        logger.info("invoked csvFile validations");
        String[] record;
        Hero hero = new Hero();
        while ((record = reader.readNext()) != null) {
            hero.setNatid(record[0]);
            hero.setName(record[1]);
            hero.setGender(record[2]);
            hero.setBirthDate(record[3]);
            hero.setDeathDate(record[4]);
            hero.setSalary(Double.parseDouble(record[5]));
            hero.setTaxPaid(Double.parseDouble(record[6]));
            hero.setBrowniePoints(Integer.parseInt(record[7]));
        }
        System.out.println(hero.getDeathDate());
        Assert.assertTrue(hero.getNatid().matches("natid-\\d{1,7}"), "Invalid natid format");
        Assert.assertTrue(hero.getName().matches("[A-Za-z\\s]{1,100}"), "Invalid name format");
        Assert.assertTrue(hero.getGender().equals("MALE") || hero.getGender().equals("FEMALE"), "Invalid gender");
        Assert.assertTrue(isValidBirthDateFormat(hero.getBirthDate()), "Invalid birthDate format or future date");
        Assert.assertTrue(hero.getDeathDate().equals("null") || isValidDeathDateFormat(hero.getDeathDate()), "Invalid deathDate format");
        Assert.assertTrue(hero.getSalary() >= 0, "Salary cannot be negative");
        Assert.assertTrue(hero.getTaxPaid() >= 0, "TaxPaid cannot be negative");
        Assert.assertTrue(hero.getBrowniePoints() == 0 || hero.getBrowniePoints() >= 0, "Invalid browniePoints");
        reader.close();

    }

    /**
     * User Story 2 / AC03
     */
    @Test(priority = 3, description = "Verify user can upload the csv file")
    public void testUploadCSV() {
        logger.info("invoked userUpload csv");
        UploadCSVPage uploadCSVPage = new UploadCSVPage(super.getDriver());
        uploadCSVPage.selectCSV(super.getResource().getString("file_path"));
        uploadCSVPage.createButtonClick();
        uploadCSVPage.implicitlyWait();
        String response = uploadCSVPage.getResponseMessage();

        Assert.assertEquals(response, "Created Successfully!", "Unable to create hero!");
    }

    /**
     * User Story 2 / AC04
     */
    @Test(priority = 4, description = "Verify user can upload the csv file")
    public void testUploadInvalidCSV() {
        logger.info("invoked userUnsuccessful upload");
        UploadCSVPage uploadCSVPage = new UploadCSVPage(super.getDriver());
        uploadCSVPage.selectCSV(super.getResource().getString("file_path"));
        uploadCSVPage.createButtonClick();
        uploadCSVPage.implicitlyWait();
        String response = uploadCSVPage.getResponseMessage();

        Assert.assertEquals(response, "Unable to create hero!", "Unable to create hero!");
    }

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
