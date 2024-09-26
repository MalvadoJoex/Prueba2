package pages;
 
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import pages.elements.EMercado;

public class MercadoLibre_Page extends BasePage {

    EMercado eml = new EMercado();

    public MercadoLibre_Page(){
 
        super(driver);
    }

    public void scrollToSelector(String selector){
        WebElement targetElement = driver.findElement(By.cssSelector(selector));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", targetElement);
    }

    public void tiempo(int s){
        try {
            Thread.sleep(s*1000);
        } catch (InterruptedException e) {
            
        }
    }

    public void open_ml(){
        navigateTo("https://www.mercadolibre.com/");
    }

    public void select_mexico(){
        WebElement mex = driver.findElement(By.id("MX"));
        mex.click();
        tiempo(2);
        driver.findElement(By.xpath(eml.maas_tarde)).click();

    }

    public void search_ps5(){
        WebElement ps5 = driver.findElement(By.id("cb1-edit"));
        ps5.sendKeys("playstation 5",Keys.ENTER);
    }

    public void filter_nuevo(){
        scrollToSelector(eml.nuevo);
        driver.findElement(By.cssSelector(eml.nuevo)).click();
    }

    public void filter_cdmx(){
        scrollToSelector(eml.cdmx);
        driver.findElement(By.cssSelector(eml.cdmx)).click();
    }

    public void order_by_mayor_menor(){
        clickElement(eml.dropdown);
        tiempo(1);
        clickElement(eml.mayor);
    }

    public String obtain_names(String xpath ){
        return driver.findElement(By.xpath(xpath)).getText();
    }

    public void print_names(){
        for (int i = 0; i < 3; i++) 
            System.out.println("El texto del elemento " + (i + 1) + " es: " + obtain_names(eml.elementos[i]));  
        
        for(int i=0; i<15; i++)
            driver.findElement(By.xpath("html")).sendKeys(Keys.ARROW_DOWN);

        tiempo(3);
        for (int i = 3; i < 5; i++) 
            System.out.println("El texto del elemento " + (i + 1) + " es: " + obtain_names(eml.elementos[i])); 
    }

    
}