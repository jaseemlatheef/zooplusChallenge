package pageClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;


public class ElementRepo {
	
	final  WebDriver driver;
	
	
	//Constructor, as every page needs a Webdriver to find elements
	public ElementRepo(WebDriver driver){
			this.driver=driver;
		}
	
	@FindBy(id="onetrust-accept-btn-handler")
	WebElement acceptCookie;
	
	@FindBy(css=".ot-sdk-container")
	WebElement cookieBox;

	@FindBy(css=".lSNext .slider__icon.checkout__icon")
	WebElement enabledSlider;

	@FindBy(css=".lSNext .slider__icon.checkout__icon.icon__disabled")
	WebElement disabledSlider;


	@FindBy(css ="li.product-box.lslide.active div form button")
	WebElement  addToCart1Element;

	@FindBy(css =".cart__table.two__column")
	WebElement  productListBox;

	@FindBy(css =".cart__table.two__column .cart__table__row div.value")
	WebElement  productPrice;

	@FindBy(css ="input[name=articleVariantId]")
	WebElement  p_id;

	@FindBy(css =".product__title span")
	WebElement  p_name;

	@FindBy(css ="span[data-zta='overviewSubTotalValue']")
	WebElement  subTotal;

	@FindBy(css="span[data-zta='shippingCountryName']")
	WebElement changeCountryLink;
    
	@FindBy(css="div.shipping__country")
	WebElement changeCountryPopup;

	@FindBy(css="ul.dropdown__list")
	WebElement countryList;

	@FindBy(css ="button[data-zta='shippingCostPopoverAction']")
	WebElement  updateButtonElement;

	//Constants

	private static final String DELETE_BUTTON = "button[data-zta='removeArticleBtn']";
	private static final String INCREASE_QUANTITY = "button[data-zta='increaseQuantityBtn']";
	private static final String PRODUCT_ROW = ".cart__table__row";
	private static final String PORTUGAL = "li[data-label='Portugal']";
	private static final String PRODUCT_PRICE = "span.v3-price__main";
	private static final String PRODUCT_SUB_PRICE = ".cart__table__row .value";
	private static final String COUNTRY_DROPDOWN = "button.dropdown__button";
	private static final String DISABLED_ICON ="icon__disabled";


	
	public void acceptCookie(){
        if (cookieBox.isDisplayed()) {
			acceptCookie.click();
        }
	}

    public void addToCartRecommendation() throws InterruptedException{
		Thread.sleep(5000);
		while(enabledSlider.getAttribute("class").contains(DISABLED_ICON)) {
			enabledSlider.click();	
		}
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", addToCart1Element);
		Thread.sleep(4000);
		
	}	

	public void urlVerify(String urlpath) throws InterruptedException {
		Thread.sleep(5000);
		String urlName = driver.getCurrentUrl();
		Assert.assertTrue(urlName.contains(urlpath));
		System.out.println("URL:" + urlName);
	}

	public Double[] sortPrices() {
		List<WebElement> prPrice = productListBox.findElements(By.cssSelector(PRODUCT_PRICE));
		ArrayList<Double> priceListArray = new ArrayList<Double>();
		Double[] minMaxProductPrices = new Double[2]; 
		for (int i =0; i<prPrice.size(); i++) {	
			Double price = Double.parseDouble(prPrice.get(i).getText().replaceAll("[^a-zA-Z0-9.]",""));
			System.out.println("Price:" + price);
			priceListArray.add(price);	
		}
		System.out.println("Price List Before sort" + priceListArray);
		Collections.sort(priceListArray);
		System.out.println("Price List After sort" + priceListArray);
		minMaxProductPrices[0] = priceListArray.get(0);	
		minMaxProductPrices[1] = priceListArray.get(priceListArray.size()-1);	 
		return minMaxProductPrices;
	}

	public void assertSubTotal() {
		List<WebElement> productRow = productListBox.findElements(By.cssSelector(PRODUCT_SUB_PRICE));
		Double calculatedSubTotal = 0.0;
		for (int i =0; i<productRow.size();i++) {
			Double pPrice =Double.parseDouble(productRow.get(i).getText().replaceAll("[^a-zA-Z0-9.]",""));
			calculatedSubTotal = Double.sum(calculatedSubTotal, pPrice);
		}
		Double cartSubTotal = Double.parseDouble(subTotal.getText().replaceAll("[^a-zA-Z0-9.]",""));
		System.out.println("calculatedSubTotal = " +calculatedSubTotal );
		calculatedSubTotal = Math.round(calculatedSubTotal * 100.0) / 100.0;
		System.out.println("calculatedSubTotal = " +calculatedSubTotal + " " + "cart subtotal=" +  cartSubTotal+ " ");
		Assert.assertEquals(calculatedSubTotal, cartSubTotal);
	}

	public void deleteHighestPriceProduct(Double highestPrice) {	
		List<WebElement> productRow = productListBox.findElements(By.cssSelector(PRODUCT_ROW));
		for (int i =0; i<productRow.size();i++) {
			Double pPrice =Double.parseDouble(productRow.get(i).findElement(By.className("value")).getText().replaceAll("[^a-zA-Z0-9.]",""));
			if(Double.compare(pPrice, highestPrice) == 0) {
				productRow.get(i).findElement(By.cssSelector(DELETE_BUTTON)).click();
			}
		}
	}

	public void changeCountry() throws InterruptedException {
		Thread.sleep(4000);
		changeCountryLink.click();
		changeCountryPopup.findElement(By.cssSelector(COUNTRY_DROPDOWN)).click();
		countryList.findElement(By.cssSelector(PORTUGAL)).click();
		updateButtonElement.click();
	}

	public void addQuantityLowestPriceProduct(Double lowestPrice) throws InterruptedException {
		
		List<WebElement> productRow = productListBox.findElements(By.cssSelector(PRODUCT_ROW));	
		for (int i =0; i<productRow.size();i++) {
			Double pPrice =Double.parseDouble(productRow.get(i).findElement(By.className("value")).getText().replaceAll("[^a-zA-Z0-9.]",""));
			if(Double.compare(pPrice, lowestPrice) == 0) {
				productRow.get(i).findElement(By.cssSelector(INCREASE_QUANTITY)).click();
			}
		}
	}
}
