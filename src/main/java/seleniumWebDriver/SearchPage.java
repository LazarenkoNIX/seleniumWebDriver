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

    @FindBy(xpath = "//div[@class = 'right-block']//span[ contains(@class, 'price') and contains (@class, 'product-price')]")
    private List<WebElement> itemPrice;

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
        for (WebElement block : priceBlock) {
            if (isNoSuchElement(block, ".//span[contains(@class, 'old-price') and contains(@class, 'product-price')]")) {
                prices.add(formatPriceToDouble(block.findElement(By.xpath(".//span[contains(@class, 'old-price') and contains(@class, 'product-price')]"))));
            } else {
                prices.add(formatPriceToDouble(block.findElement(By.xpath(".//span[ contains(@class, 'price') and contains (@class, 'product-price')]"))));
            }
        }
        List<Double> sortedPrice = new ArrayList<>(prices);
        sortedPrice.sort(Collections.reverseOrder());
        assertEquals(prices, sortedPrice);
        return this;
    }

    public SearchPage saveFirstItemNameAndPrice(Map<String, Double> mapPrice) {
        if (mapPrice.containsKey(getFirstItemName(itemName))) {
            mapPrice.put(getFirstItemName(itemName), mapPrice.get(getFirstItemName(itemName)) + formatPriceToDouble(getFirstItemPrice(itemPrice)));
        } else {
            mapPrice.put(getFirstItemName(itemName), formatPriceToDouble(getFirstItemPrice(itemPrice)));
        }
        return this;
    }

    public SearchPage addFirstItemToCart() {
        Actions action = new Actions(driver);
        action.moveToElement(productContainer).build().perform();
        addToCart.click();
        return this;
    }

    public SearchPage moveToCart() {
        moveToCart.click();
        return this;
    }

    public String getNameOfItemFromBlockForCompare(WebElement webElement, String xpath) {
        return webElement.findElement(By.xpath(xpath)).getText();
    }

    public Double getPriceOfItemFromBlockForCompare(WebElement webElement) {
        return formatPriceToDouble(webElement.findElement(By.xpath(".//span[contains(@id,'total_product_price_')]")));
    }

    public SearchPage comparePriceFromCartTemp(Map<String, Double> mapPrice) {
        String productName = mapPrice.keySet().stream().findFirst().get();
        double price = mapPrice.entrySet().stream().findFirst().get().getValue();
        String xpathForItemName = ".//td/p[@class = 'product-name']/a";
        WebElement firstSameItem = itemBlockCart.stream()
                .filter(item -> item.findElement((By.xpath(xpathForItemName))).getText().equals(productName))
                .findFirst().get();
        assertThat(format("Product %s with price %f in the cart is not equals to product %s with price %f",
                productName, price, getNameOfItemFromBlockForCompare(firstSameItem, xpathForItemName), getPriceOfItemFromBlockForCompare(firstSameItem)),
                (getNameOfItemFromBlockForCompare(firstSameItem, xpathForItemName).equals(productName))
                        && getPriceOfItemFromBlockForCompare(firstSameItem).equals(price));
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

    public Double formatPriceToDouble(WebElement webElement) {
        return parseDouble(webElement.getText().replace("$", ""));
    }

    public String getFirstItemName(List<WebElement> itemName) {
        return itemName.stream().findFirst().get().getText();
    }

    public WebElement getFirstItemPrice(List<WebElement> itemPrice) {
        return itemPrice.stream().findFirst().get();
    }
}