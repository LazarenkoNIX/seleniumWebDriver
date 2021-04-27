package seleniumWebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
//            "//span[contains(text(),'Add to cart')]")
    private WebElement addToCart;

    @FindBy(xpath = "//a[@title = 'Proceed to checkout']")
    private WebElement moveToCart;

    @FindBy(xpath = "//span[@id='total_product_price_5_19_0']")
    private WebElement totalPriceCart;

    @FindBy(xpath = "//td/p[@class = 'product-name']/a")
    private WebElement cartItemName;

    @FindBy(xpath = "//div[@class='right-block']")
    private List<WebElement> priceBlock;

    String saveItemName;
    Double saveItemPrice;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void verifySearchFilterIsDisplayed(String searchText) {
        assertThat(search.getText(), equalTo(format("\"%s\"", searchText)));
        //TODO verify
    }

    public void dropdownPriceHighestFirst() {
        drpPriceHighFirst.click();
    }

    public void verifySort() {
        List<Double> prices = new ArrayList<Double>();
        List<Double> sortedPrice = prices;
//        for (WebElement element : newPrice) {
//            convertToDoublePrice.add(Double.parseDouble(element.getText().replace("$", "")));
//        }
        for (WebElement block : priceBlock) {
            if (noSuchElement(block,".//span[@class = 'old-price product-price']")) {
                prices.add(parseDouble(block.findElement(By.xpath(".//span[@class = 'old-price product-price']")).getText().replace("$", "")));
                //TODO create method
//                System.out.println(block.findElement(By.xpath(".//span[@class = 'old-price product-price']")).getText());
            } else {
                prices.add(parseDouble(block.findElement(By.xpath(".//span[@class = 'price product-price']")).getText().replace("$", "")));
            }
        }
//        System.out.println(prices);

//        List<Double> convertToDoublePriceTemp = priceBlock.stream().collect(Collectors.toList());
//        System.out.println(convertToDoublePrice);
        sortedPrice.sort(Collections.reverseOrder());
//                .stream()
//                .sorted(Collections.reverseOrder())
//                .collect(Collectors.toList());
//        System.out.println(sortedPrice);
        assertEquals(prices, sortedPrice);

//        List<Double> afterSortPrice = new ArrayList<Double>();
//        for (WebElement element : price) {
//            if (element.getText().contains("old")) {
//                if (element.getText().contains("$")) {
//                    afterSortPrice.add(Double.parseDouble(element.getText().replace("$", "")));
//                }
//            } else {
//                if (newPrice.getText().contains("$")) {
//                    afterSortPrice.add(Double.parseDouble(newPrice.getText().replace("$", "")));
//                }
//            }
//        }
//        System.out.println(afterSortPrice);
    }

    public void saveItemNameAndPrice(Map<String, Double> mapPrice) {
        mapPrice.put(itemName.stream().findFirst().get().getText(), parseDouble(itemPrice.getText().replace("$", "")));
        //TODO change to block
    }

    public void addItemToCart() {
        addToCart.click();
        moveToCart.click();
    }

    public SearchPage comparePrice() {
        double totalItemCartPrice = parseDouble(totalPriceCart.getText().replace("$", ""));
        assertThat(totalItemCartPrice, closeTo(saveItemPrice, 0.01));
        return this;
    }

    public void compareName() {
        assertThat(cartItemName.getText(), equalToIgnoringCase(saveItemName));
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
