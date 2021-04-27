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

//@RunWith(Parameterized.class)
public class SeleniumTest {
    WebDriver driver;
//    private String firstSearch;
//    private String secondSearch;
//    private String thirdSearch;

//    public SeleniumTest(String firstSearch, String secondSearch, String thirdSearch) {
//        super();
//        this.firstSearch = firstSearch;
//        this.secondSearch = secondSearch;
//        this.thirdSearch = thirdSearch;
//    }
//
//    @Parameterized.Parameters
//    public static searchItems() {
//        return;
//    }

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"SUMMER"}, {"DRESS"}, {"T-SHIRT"}};
    }

    @Test(dataProvider = "data-provider")
    public void applySearchSummer(String data) {
        Map<String, Double> priceMap = new HashMap<>();

        HomePage home = new HomePage(driver);
        //Step1
        home.setSearch(data);
        SearchPage searchPage = new SearchPage(driver);
        searchPage.verifySearchFilterIsDisplayed(data);
        searchPage.dropdownPriceHighestFirst();
        searchPage.verifySort();
        searchPage.saveItemNameAndPrice(priceMap);
        searchPage.addItemToCart();
        searchPage.comparePrice()
                .compareName();
    }

    @AfterTest
    public void close() {
        driver.close();
    }
}