package seleniumWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        HomePage home = new HomePage(driver);
        home.setSearch(data);
        SearchPage searchPage = new SearchPage(driver);
        searchPage.isPageOpened("\"" + data + "\"");
        searchPage.dropdownPriceHighestFirst();
        searchPage.verifySort();
        searchPage.saveItem();
        searchPage.addItemToCart();
        searchPage.comparePrice();
        searchPage.compareName();
    }

    @AfterTest
    public void close() {
        driver.close();
    }
}