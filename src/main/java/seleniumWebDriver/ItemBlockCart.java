package seleniumWebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ItemBlockCart {
    @FindBy(xpath = ".//td/p[@class = 'product-name']/a")
    WebElement cartItemName;

    @FindBy(xpath = ".//span[contains(@id,'total_product_price_')]")
    WebElement totalItemPriceCart;
}
