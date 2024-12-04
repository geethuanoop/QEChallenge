package test.ui;

import config.ConnectionObject;
import config.ListenerTest;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.ResourceBundle;

@Listeners(ListenerTest.class)
public class BaseTest {
    private WebDriver driver;
    private final Logger logger = LogManager.getLogger(BaseTest.class);
    private ResourceBundle resource;

    @BeforeMethod
    public void openBrowser(Method method) {

        if (this.shouldSkipBeforeMethod(method.getName())) {
            logger.info("Open browser");
            ConnectionObject connection = new ConnectionObject();
            this.driver = connection.getDriver();
            this.resource = ResourceBundle.getBundle("config");
            String loginUrl = resource.getString("base_url") + "login";
            this.driver.manage().window().maximize();
            this.driver.get(loginUrl);
            BasicConfigurator.configure();
        }
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public ResourceBundle getResource() {
        return this.resource;
    }

    public boolean shouldSkipBeforeMethod(String methodName) {
        return !methodName.equals("testCSVFields");
    }

    @AfterMethod
    public void after() throws InterruptedException {
        logger.info("Close browser");
        Thread.sleep(2000);
        this.driver.quit();
    }
}
