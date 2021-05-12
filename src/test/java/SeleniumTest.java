import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import seleniumWebDriver.BaseTest;

import java.util.HashMap;
import java.util.Map;

public class SeleniumTest extends BaseTest {

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"SUMMER"}, {"DRESS"}, {"T-SHIRT"}};
    }

    @Test(dataProvider = "data-provider")
    public void applySearchSummer(String data) {
        Map<String, Double> priceMap = new HashMap<String, Double>();
        //Step1
        getHomePage()
                //Step2
                .setSearch(data)
                //Step3
                .verifySearchFilterIsDisplayed(data)
                //Step4
                .selectDropdownPriceHighestFirst()
                //Step5
                .verifyHighestFirstSort()
                //Step6
                .saveFirstItemNameAndPrice(priceMap)
                //Step7
                .addFirstItemToCart()
                //Step8
                .moveToCart()
                .comparePriceFromCartTemp(priceMap);
    }
}