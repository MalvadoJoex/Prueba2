package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    
    protected static WebDriver driver;
    private static WebDriverWait wait;
    private static Actions actions;

    static {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        //chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    }

    public BasePage(WebDriver driver){
        BasePage.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public static void navigateTo(String url){
        driver.get(url);
    }

    private WebElement Find(String locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }

    public void clickElement(String locator){
        Find(locator).click();

    }

    public void write(String locator, String textToWrite){
        Find(locator).clear();
        Find(locator).sendKeys(textToWrite);
    }

    public void selectFromDropdownByValue(String locator, String valueToSelect){
        Select dropdown = new Select (Find(locator));
        dropdown.selectByValue(valueToSelect);
    }

    public void selectFromDropdownByIndex(String locator, int valueToSelect){
        Select dropdown = new Select (Find(locator));
        dropdown.selectByIndex(valueToSelect);
    }

    public void selectFromDropdownByText(String locator, String valueToSelect){
        Select dropdown = new Select (Find(locator));
        dropdown.selectByVisibleText(valueToSelect);
    }

    public void hoverOverElement(String locator){
        actions.moveToElement(Find(locator));
    }

    public void doubleClick(String locator){
        actions.doubleClick(Find(locator));
    }

    public void rightClick(String locator){
        actions.contextClick(Find(locator));
    }

    //Extraer un elemento de la celda indicada
    public String getValueFromTable(String locator, int row, int column){
        String cellINeed = locator+"/table/tbody/tr["+row+"]/td["+column+"]";

        return Find(cellINeed).getText();
    }

    //Escribir un elemento en la celda indicada
    public void setValueOnTable(String locator, int row, int column, String stringToSed){
        String cellToFill = locator+"/table/tbody/tr["+row+"]/td["+column+"]";

        Find(cellToFill).sendKeys(stringToSed);
    }

    public void switchToIFrame(String iFrameID){
        driver.switchTo().frame(iFrameID);
    }

    public void switchToParentFrame(String iFrameID){
        driver.switchTo().parentFrame();
    }

    public void dismissAlert(){
        try{
            driver.switchTo().alert().dismiss();

        }catch(NoAlertPresentException e){
            e.printStackTrace();
        }
    }

    //ASSERTION
    public String textFromElement(String locator){
        return Find(locator).getText();
    }

    public boolean elementEnabled(String locator){
        return Find(locator).isEnabled();
    }

    public boolean elementIsDisplayed(String locator){
        return Find(locator).isDisplayed();
    }

    public boolean elementIsSelected(String locator){
        return Find(locator).isSelected();
    }

    public List<WebElement> bringMeAllElements(String locator){
        return driver.findElements(By.className(locator));
    }

    public static void closeBrowser(){
        driver.quit();
    }

    
}