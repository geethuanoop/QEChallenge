package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class BookKeeperDashboardPage {
    private WebDriver driver;

    public BookKeeperDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    private By generateTax = By.id("tax_relief_btn");
    private By taxGenaratedMessage = By.id("tax_relief_status_id");

    public void clickGenarateTax() {
        driver.findElement(generateTax).click();
    }

    public String responseGenarateTax() {
        return driver.findElement(taxGenaratedMessage).getText();
    }

    public void implicitlyWait() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}

