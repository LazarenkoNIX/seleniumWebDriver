package seleniumWebDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SearchPage {
    @FindBy(xpath = "//span[@class = 'lighter']")
    private WebElement search;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void isPageOpened(){
        assertThat(search.getText(),equalTo("summer"));
//        assertThat(search.getText().toString(), equalTo(expectedSearch));
//        assertThat("summer", search.getText().toString().contains("summer"));
    }
}
