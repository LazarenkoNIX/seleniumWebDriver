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
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"summer"}, {"dress"}, {"t-shirt"}};
    }

    @Test(dataProvider = "data-provider")
    public void applySearchSummer(String data) {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Map<String, Double> priceMap = new HashMap<String, Double>();
        //Step1
        HomePage home = new HomePage(driver);
        //Step2
        home.setSearch(data);
        //Step3
        SearchPage searchPage = new SearchPage(driver);
        searchPage.verifySearchFilterIsDisplayed(data)
                //Step4
                .dropdownPriceHighestFirst()
                //Step5
                .verifySort()
                //Step6
                .saveItemNameAndPrice(priceMap)
                //Step7
                .addItemToCart()
                //Step8
                .comparePriceFromCartTemp(priceMap);
        driver.close();
    }

    @AfterTest
    public void close() {
        driver.quit();
    }
}