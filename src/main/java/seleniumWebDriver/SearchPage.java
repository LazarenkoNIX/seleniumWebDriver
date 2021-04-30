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

    @FindBy(xpath = "//div[@class='right-block']")
    private List<WebElement> priceBlock;

//    @FindBy(xpath = "//tr[contains(@id,'product_')]")
//    private List<ItemBlockCart> itemBlockCart;

    @FindBy(xpath = "//tr[contains(@id,'product_')]")
    private WebElement itemBlockCart;

    @FindBy(xpath = ".//td/p[@class = 'product-name']/a")
    private WebElement cartItemName;

    @FindBy(xpath = ".//span[contains(@id,'total_product_price_')]")
    private WebElement totalItemPriceCart;

    //TODO block for xpath

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
//                prices.add(parseDouble(block.findElement(By.xpath(".//span[@class = 'old-price product-price']")).getText().replace("$", "")));
                prices.add(formatStringToDouble(block.findElement(By.xpath(".//span[@class = 'old-price product-price']"))));
            } else {
//                prices.add(parseDouble(block.findElement(By.xpath(".//span[@class = 'price product-price']")).getText().replace("$", "")));
                prices.add(formatStringToDouble(block.findElement(By.xpath(".//span[@class = 'price product-price']"))));
            }
        }
        sortedPrice.sort(Collections.reverseOrder());
        assertEquals(prices, sortedPrice);
        return this;
    }

    public void saveItemNameAndPrice(Map<String, Double> mapPrice) {
        if (mapPrice.containsKey(itemName)) {
            mapPrice.put(itemName.stream().findFirst().get().getText(), mapPrice.get(itemName) + formatStringToDouble(itemPrice));
        } else {
            mapPrice.put(itemName.stream().findFirst().get().getText(), formatStringToDouble(itemPrice));
        }
    }

    public void addItemToCart() {
        addToCart.click();
        moveToCart.click();
    }

//    public void saveItemNameAndPriceFromCart() {
//        mapCartPrice = IntStream.
//                range(0, cartItemName.size())
//                .boxed()
//                .collect(Collectors.toMap(i -> cartItemName.get(i).getText(),
//                        i -> parseDouble(totalItemPriceCart.get(i).getText().replace("$", ""))));
//    }

//    public void comparePriceFromCart(Map<String, Double> mapPrice) {
//        Map<String, Double> mapPriceCart = new HashMap<>();
//        mapPriceCart.entrySet().stream().collect(Collectors.toMap(
//                i -> cartItemName.getText(),
//                i -> formatStringToDouble(totalItemPriceCart)
//        ));
//        assertThat(mapPrice, equalTo(mapPriceCart));
//        for (Map.Entry entry : mapPriceCart.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + " Value: "
//                    + entry.getValue());
//        }
//    }

    public void comparePriceFromCartTemp(Map<String, Double> mapPrice) {
        mapPrice.entrySet().stream()
//                .findFirst()
                .filter(item -> item.getKey().equals(itemBlockCart.findElement(By.xpath(".//td/p[@class = 'product-name']/a")).getText()))
                .forEach(item -> assertThat(item.getValue(), equalTo(formatStringToDouble(itemBlockCart.findElement(By.xpath(".//span[contains(@id,'total_product_price_')]"))))));
//                .filter(item -> item.getKey().equals(itemBlockCart.stream().findFirst().get().cartItemName.getText()))
//                .forEach(item -> assertThat(item.getValue(), equalTo(formatStringToDouble(itemBlockCart.stream().findFirst().get().totalItemPriceCart))));
    }

//        cartItemName.stream()
//                .filter(mapPrice::containsKey)
//                .map(mapPrice::get)
//                .allMatch();


//    public boolean comparePrice(Map<String, Double> mapPrice) {
//        if (mapPrice.size() != mapCartPrice.size()) {
//            return false;
//        }
//        return mapPrice.entrySet().stream()
//                .allMatch(e -> e.getValue().equals(mapCartPrice.get(e.getKey())));
//    }

//    public void returnMapCartPrice() {
//        for (Map.Entry entry : mapCartPrice.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + " Value: "
//                    + entry.getValue());
//        }
//    }

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
