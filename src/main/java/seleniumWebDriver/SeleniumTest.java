package seleniumWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"summer"}, {"dress"}, {"t-shirt"}};
    }

    @Test(dataProvider = "data-provider")
    public void applySearchSummer(String data) {
        Map<String, Double> priceMap = new HashMap<>();
        //Step1
        HomePage home = new HomePage(driver);
        //Step2
        home.setSearch(data);
        //Step3
        SearchPage searchPage = new SearchPage(driver);
        searchPage.verifySearchFilterIsDisplayed(data);
        //Step4
        searchPage.dropdownPriceHighestFirst();
        //Step5
        searchPage.verifySort();
        //Step6
        searchPage.saveItemNameAndPrice(priceMap);
        for (Map.Entry entry : priceMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: "
                    + entry.getValue());
        }
        System.out.println("end step6");
        //Step7
        searchPage.addItemToCart();
        searchPage.saveItemNameAndPriceFromCart();
        //Step8
        searchPage.comparePrice(priceMap);
        searchPage.returnMap();
    }

    @AfterTest
    public void close() {
        driver.quit();
    }
}