package seleniumWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class SeleniumTest {
    WebDriver driver;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void applySearch() {
        HomePage home = new HomePage(driver);
        home
                .setSearch("summer")
        ;
        SearchPage searchPage = new SearchPage(driver);
        searchPage.isPageOpened();
        searchPage.dropdownPriceHighestFirst();
        searchPage.verifySort();
        searchPage.saveItem();
        searchPage.addItemToCart();
        searchPage.comparePrice();
        searchPage.compareName();
    }

    @After
    public void close() {
        driver.close();
    }
}