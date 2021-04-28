package seleniumWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'price product-price']")
    private List<WebElement> newPrice;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'old-price product-price']")
    private List<WebElement> oldPrice;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'old-price product-price']")
    private WebElement oldPriceTmp;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'price product-price' or @class = 'old-price product-price']")
    private List<WebElement> price;

    @FindBy(xpath = "//div[@class = 'right-block']//a[@class = 'product-name']")
    private List<WebElement> itemName;

    @FindBy(xpath = "//div[@class = 'right-block']//span[@class = 'price product-price']")
    private WebElement itemPrice;

    @FindBy(xpath = "//a[@title = 'Add to cart']")
    private WebElement addToCart;

    @FindBy(xpath = "//a[@title = 'Proceed to checkout']")
    private WebElement moveToCart;

    @FindBy(xpath = "//span[contains(@id,'total_product_price_')]")
    private List<WebElement> totalItemPriceCart;

    @FindBy(xpath = "//td/p[@class = 'product-name']/a")
    private List<WebElement> cartItemName;

    @FindBy(xpath = "//div[@class='right-block']")
    private List<WebElement> priceBlock;
    //TODO block for xpath

    Map<String, Double> mapCartPrice = new HashMap<>();

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void verifySearchFilterIsDisplayed(String searchText) {
        assertThat(search.getText(), equalToIgnoringCase(format("\"%s\"", searchText)));
    }

    public void dropdownPriceHighestFirst() {
        drpPriceHighFirst.click();
    }

    public void verifySort() {
        List<Double> prices = new ArrayList<Double>();
        List<Double> sortedPrice = prices;
        for (WebElement block : priceBlock) {
            if (noSuchElement(block, ".//span[@class = 'old-price product-price']")) {
                prices.add(parseDouble(block.findElement(By.xpath(".//span[@class = 'old-price product-price']")).getText().replace("$", "")));
                //TODO create method
            } else {
                prices.add(parseDouble(block.findElement(By.xpath(".//span[@class = 'price product-price']")).getText().replace("$", "")));
            }
        }
        sortedPrice.sort(Collections.reverseOrder());
        assertEquals(prices, sortedPrice);
    }

    public void saveItemNameAndPrice(Map<String, Double> mapPrice) {
        mapPrice.put(itemName.stream().findFirst().get().getText(), parseDouble(itemPrice.getText().replace("$", "")));
    }

    public void addItemToCart() {
        addToCart.click();
        moveToCart.click();
    }

    public void saveItemNameAndPriceFromCart() {
        mapCartPrice = IntStream.
                range(0, cartItemName.size())
                .boxed()
                .collect(Collectors.toMap(i -> cartItemName.get(i).getText(),
                        i -> parseDouble(totalItemPriceCart.get(i).getText().replace("$", ""))));
    }

    public boolean comparePrice(Map<String, Double> mapPrice) {
        if (mapPrice.size() != mapCartPrice.size()) {
            return false;
        }
        return mapPrice.entrySet().stream()
                .allMatch(e -> e.getValue().equals(mapCartPrice.get(e.getKey())));
    }

    public void returnMapCartPrice(){
        for (Map.Entry entry : mapCartPrice.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: "
                    + entry.getValue());
        }
    }

    public boolean noSuchElement(WebElement webElement, String xpath) {
        try {
            webElement.findElement(By.xpath(xpath));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
