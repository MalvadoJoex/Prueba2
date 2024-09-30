package runner;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import pages.BasePage;

@RunWith(Cucumber.class)
@CucumberOptions(
    features =  "src/test/resources/features",
    glue = "steps",
    plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" /*"json:cucumber-report.json"*/},
    //plugin = {"pretty", "json:target/cucumber.json"},
    monochrome = true
    //tags = "@tag3 or @DesafioCasa or @TodoTDD_TDC or @TodoMisPedidos or @TodoAyuda"
    )
  
public class Runner {
    @AfterClass
    public static void cleanDriver() {
        BasePage.closeBrowser();
    }
}
