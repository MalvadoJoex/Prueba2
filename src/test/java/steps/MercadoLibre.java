package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.MercadoLibre_Page;

public class MercadoLibre {

    MercadoLibre_Page ml = new MercadoLibre_Page();

    @Given("Open application to mercadolibre.com")
    public void open_app_mercadolibre(){
        ml.open_ml();
    }
    
    @Then("Select Mexico as a country")
    public void Select_Mexico_as_a_country() {
        ml.select_mexico();
    }

    @Then("Search for the term playstation 5")
    public void Search_for_the_term_playstation( ) {
        ml.search_ps5();
    }

    @Then("Filter by condition Nuevos")
    public void Filter_by_condition_Nuevos() {
        ml.filter_nuevo();
    }

    @Then("Filter by location Cdmx")
    public void Filter_by_location_Cdmx() {
        ml.filter_cdmx();
    }

    @Then("Order by mayor a menor precio")
    public void Order_by_mayor_a_menor_precio() {
        ml.order_by_mayor_menor();
    }

    @Then("Obtain the name and the price of the first 5 products and Print these products in the console")
    public void Obtain_the_name_and_the_price_of_the_first_products() {
        ml.print_names();
    }


    

    
    

    

   

    

}
