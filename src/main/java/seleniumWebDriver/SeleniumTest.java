package seleniumWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;

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

    public class DataProviderClass
    {
        @DataProvider(name = «data-provider»)
        public static Object[][] dataProviderMethod()
        {
            return new Object[][] { { «data one» }, { «data two» } };
        }
    }

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void applySearchSummer() {
        HomePage home = new HomePage(driver);
        home.setSearch("summer");
        SearchPage searchPage = new SearchPage(driver);
        searchPage.isPageOpened();
        searchPage.dropdownPriceHighestFirst();
        searchPage.verifySort();
        searchPage.saveItem();
        searchPage.addItemToCart();
        searchPage.comparePrice();
        searchPage.compareName();
    }

    @Test
    public void applySearchDress() {
        HomePage home = new HomePage(driver);
        home.setSearch("dress");
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