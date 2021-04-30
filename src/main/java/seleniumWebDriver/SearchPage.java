package seleniumWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class SearchPage {
    @FindBy(xpath = "//span[@class = 'lighter']")
    private WebElement search;

    @FindBy(xpath = "//option[@value = 'price:desc']")
    private WebElement drpPriceHighFirst;

    @FindBy(xpath = "//div[@class = 'right-block']//a[@class = 'product-name']")
    private List<WebElement> itemName;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'price product-price']")
    private WebElement itemPrice;

    @FindBy(xpath = "//a[@title = 'Add to cart']")
    private WebElement addToCart;

    @FindBy(xpath = "//a[@title = 'Proceed to checkout']")
    private WebElement moveToCart;

    @FindBy(xpath = "//div[@class='right-block']")
    private List<WebElement> priceBlock;

    @FindBy(xpath = "//tr[contains(@id,'product_')]")
    private WebElement itemBlockCart;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public SearchPage verifySearchFilterIsDisplayed(String searchText) {
        assertThat(search.getText(), equalToIgnoringCase(format("\"%s\"", searchText)));
        return this;
    }

    public SearchPage dropdownPriceHighestFirst() {
        drpPriceHighFirst.click();
        return this;
    }

    public SearchPage verifySort() {
        List<Double> prices = new ArrayList<Double>();
        List<Double> sortedPrice = prices;
        for (WebElement block : priceBlock) {
            if (noSuchElement(block, ".//span[@class = 'old-price product-price']")) {
                prices.add(formatStringToDouble(block.findElement(By.xpath(".//span[@class = 'old-price product-price']"))));
            } else {
                prices.add(formatStringToDouble(block.findElement(By.xpath(".//span[@class = 'price product-price']"))));
            }
        }
        sortedPrice.sort(Collections.reverseOrder());
        assertEquals(prices, sortedPrice);
        return this;
    }

    public SearchPage saveItemNameAndPrice(Map<String, Double> mapPrice) {
        if (mapPrice.containsKey(itemName)) {
            mapPrice.put(itemName.stream().findFirst().get().getText(), mapPrice.get(itemName) + formatStringToDouble(itemPrice));
        } else {
            mapPrice.put(itemName.stream().findFirst().get().getText(), formatStringToDouble(itemPrice));
        }
        return this;
    }

    public SearchPage addItemToCart() {
        addToCart.click();
        moveToCart.click();
        return this;
    }

    public SearchPage comparePriceFromCartTemp(Map<String, Double> mapPrice) {
        mapPrice.entrySet().stream()
                .filter(item -> item.getKey().equals(itemBlockCart.findElement(By.xpath(".//td/p[@class = 'product-name']/a")).getText()))
                .forEach(item -> assertThat(item.getValue(), equalTo(formatStringToDouble(itemBlockCart.findElement(By.xpath(".//span[contains(@id,'total_product_price_')]"))))));
        return this;
    }

    public boolean noSuchElement(WebElement webElement, String xpath) {
        try {
            webElement.findElement(By.xpath(xpath));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public Double formatStringToDouble(WebElement webElement) {
        return parseDouble(webElement.getText().replace("$", ""));
    }
}