package seleniumWebDriver;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
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

    @FindBy(xpath = "//select[@id = 'selectProductSort']")
    private WebElement clickDrp;

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
    private List<WebElement> itemBlockCart;

    @FindBy(xpath = "//div[@class = 'product-container']")
    private WebElement productContainer;

    protected WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public SearchPage verifySearchFilterIsDisplayed(String searchText) {
        assertThat(search.getText(), equalTo(format("\"%s\"", searchText)));
        return this;
    }

    public SearchPage selectDropdownPriceHighestFirst() {
        clickDrp.click();
        drpPriceHighFirst.click();
        return this;
    }

    public SearchPage verifyHighestFirstSort() {
        List<Double> prices = new ArrayList<Double>();
        List<Double> sortedPrice = prices;
        for (WebElement block : priceBlock) {
            if (isNoSuchElement(block, ".//span[@class = 'old-price product-price']")) {
                prices.add(formatStringToDouble(block.findElement(By.xpath(".//span[@class = 'old-price product-price']"))));
            } else {
                prices.add(formatStringToDouble(block.findElement(By.xpath(".//span[@class = 'price product-price']"))));
            }
        }
        sortedPrice.sort(Collections.reverseOrder());
        assertEquals(prices, sortedPrice);
        return this;
    }

    public SearchPage saveFirstItemNameAndPrice(Map<String, Double> mapPrice) {
        if (mapPrice.containsKey(getFirstItemName(itemName))) {
            mapPrice.put(getFirstItemName(itemName), mapPrice.get(itemName) + formatStringToDouble(itemPrice));
        } else {
            mapPrice.put(getFirstItemName(itemName), formatStringToDouble(itemPrice));
        }

        return this;
    }

    public SearchPage addFirstItemToCart() {
//        isNetworkActivityStopped();
        Actions action = new Actions(driver);
        action.moveToElement(productContainer);
        action.moveToElement(addToCart);
        action.click().build().perform();
        return this;
    }

    public SearchPage moveToCart() {
        moveToCart.click();
        return this;
    }

    public SearchPage comparePriceFromCartTemp(Map<String, Double> mapPrice) {
        String productName = mapPrice.keySet().stream().findFirst().get();
        double price = mapPrice.entrySet().stream().findFirst().get().getValue();
        String xpathForItemName = ".//td/p[@class = 'product-name']/a";
        WebElement firstSameItem = itemBlockCart.stream()
                .filter(item -> item.findElement((By.xpath(xpathForItemName))).getText().equals(productName))
                .findFirst().get();
        assertThat("Price is not same for the first product in the cart", (firstSameItem.findElement(By.xpath(xpathForItemName)).getText().equals(productName))
                && formatStringToDouble(firstSameItem.findElement(By.xpath(".//span[contains(@id,'total_product_price_')]"))).equals(price));
        return this;
    }

    public boolean isNoSuchElement(WebElement webElement, String xpath) {
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

    public String getFirstItemName(List<WebElement> itemName) {
        return itemName.stream().findFirst().get().getText();
    }
    
//    private boolean isNetworkActivityStopped() {
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        return js.executeScript("return window.$.active").equals(0L);
//    }
}