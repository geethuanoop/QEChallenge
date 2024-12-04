package test.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.BookKeeperDashboardPage;
import pages.LoginPage;

@Listeners(config.ListenerTest.class)
public class UserStory03 extends BaseTest {

    private static final Logger logger = LogManager.getLogger(UserStory03.class);

    /**
     * User Story 03/AC1
     */
    @Test(priority = 1, description = "Verify that user can generate a Tax Relief File")
    public void testBookDashboard() throws InterruptedException {
        logger.info("invoked bookKeeper dashboard");
        LoginPage loginPage = new LoginPage(super.getDriver());
        loginPage.enterUsername("bk");
        loginPage.enterPassword("bk");
        loginPage.clickLoginButton();
        BookKeeperDashboardPage bookDashboardPage = new BookKeeperDashboardPage(super.getDriver());
        String initialText = bookDashboardPage.responseGenarateTax();
        if (initialText.equals("Nothing running at the moment")) {
            bookDashboardPage.clickGenarateTax();
            bookDashboardPage.implicitlyWait();
        }
        Thread.sleep(2000);
        String response = bookDashboardPage.responseGenarateTax();
        Assert.assertEquals(response, "Egress Tax Relief file process in progress", "Generating tax file");
    }

}
