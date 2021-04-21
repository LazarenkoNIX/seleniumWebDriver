package seleniumWebDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SearchPage {
    @FindBy(xpath = "//span[@class = 'lighter']")
    private WebElement search;

    @FindBy(xpath = "//option[@value = 'price:desc']")
    private WebElement drpPriceHighFirst;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'price product-price']")
    private WebElement newPrice;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'old-price product-price']")
    private List<WebElement> oldPrice;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'price product-price' or @class = 'old-price product-price']")
            private List<WebElement> price;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void isPageOpened() {
        assertThat(search.getText(), containsString("SUMMER"));
    }

    public void dropdownPriceHighestFirst() {
        drpPriceHighFirst.click();
    }

    public void verifySort() {
        for (WebElement element : price) {
            if(element.getClass().toString().contains("old-price product-price")){
                System.out.println(element);
            }
        }





//        List<Double> afterSortPrice = new ArrayList<>();
//        for (WebElement element : oldPrice) {
//            if (element.isEnabled()) {
//                if (element.getText().contains("$")) {
//                    afterSortPrice.add(Double.parseDouble(element.getText().replace("$", "")));
//                }
//            } else {
//                if (newPrice.getText().contains("$")) {
//                    afterSortPrice.add(Double.parseDouble(newPrice.getText().replace("$", "")));
//                }
//            }
//        }

//        for (WebElement element : oldPrice) {
//            System.out.println(element.getText());
//        }
    }
}
