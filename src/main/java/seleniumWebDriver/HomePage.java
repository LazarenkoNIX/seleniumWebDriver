package seleniumWebDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    protected WebDriver driver;

    @FindBy(xpath = "//input[@name = 'search_query']")
    private WebElement inputSearch;

    @FindBy(xpath = "//button[@name = 'submit_search']")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        String pageUrl = "http://automationpractice.com/index.php";
        driver.get(pageUrl);
        PageFactory.initElements(driver, this);
    }

    public void setSearch(String search) {
        inputSearch.clear();
        inputSearch.sendKeys(search);
        searchButton.click();
    }
}
