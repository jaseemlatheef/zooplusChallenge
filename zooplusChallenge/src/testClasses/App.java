package testClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import pageClasses.ElementRepo;
public class App {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver","//Users/ibm/Documents/zooplusChallenge/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.zooplus.com/checkout/cartEmpty.htm");
        //Mazimize current window
        driver.manage().window().maximize();
        //Delay execution for 2 seconds to view the maximize operation
        Thread.sleep(2000);
        ElementRepo zooplus = PageFactory.initElements(driver, ElementRepo.class);
		 
		//Using the methods created in pages class to perform actions

        // Test case to click accept cookie if displayed
        zooplus.acceptCookie();
        // Test case to add product to cart
        zooplus.addToCartRecommendation();
        // Test case to assert URL  
        zooplus.urlVerify("overview");
        // Test case to add 4 products to cart
        for(int i=0;i<3; i++) {
            zooplus.addToCartRecommendation();
        }
        // Test case to sort the prices displayed
        Double[] minMaxPrice =  zooplus.sortPrices();
        // Test case to add extra quantity to lowest price product
        zooplus.addQuantityLowestPriceProduct(minMaxPrice[0]);
        // Test case to delete highest priced product
        zooplus.deleteHighestPriceProduct(minMaxPrice[1]);
        // Test case to assert subtotal and calculated product prices
        zooplus.assertSubTotal();
        // Test case to change country
        zooplus.changeCountry();
        driver.quit();   
    }
}