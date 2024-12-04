package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {
    private WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    private By addHeroDropDown = By.id("dropdownMenuButton2");
    private By selectUploadCSV = By.xpath("//a[normalize-space()='Upload a csv file']");

    public void clickAddHeroDropdown() {
        driver.findElement(addHeroDropDown).click();
    }

    public void selectUploadCSV() {
        driver
                .findElement(selectUploadCSV)
                .click();
    }

}

