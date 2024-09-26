Feature: Desafio para casa MercaadoLibre
 

    @DesafioCasa
    Scenario: Busqueda y filtro de consola
        Given Open application to mercadolibre.com
        Then Select Mexico as a country 
        Then Search for the term playstation 5
        Then Filter by condition Nuevos
        Then Filter by location Cdmx
        Then Order by mayor a menor precio
        Then Obtain the name and the price of the first 5 products and Print these products in the console

    
