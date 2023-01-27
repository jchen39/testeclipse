package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class ReviewOrderPage {

	WebDriver driver;
	WebDriverWait wait;
	
	By name = By.xpath("//div[@class='checkout_userinfo__item']//div[@class='font-semibold capitalize']");
	By email = By.xpath("//div[@class='checkout_userinfo__item']//div[@class='font-semibold']");
	By phone = By.xpath("//div[@class='checkout_userinfo__item']//div[text()='Phone Number']//div[@class='font-semibold capitalize']");
	By deliveryOption = By.xpath("//span[text()='Delivery']//..");
	By pickupOption = By.xpath("//span[text()='Pickup']//..");
	By creditCard = By.xpath("//span[text()='Credit Card']//..");
	By payAtStore = By.xpath("//span[text()='Pay at Store']//..");
	By savedCard = By.xpath("//span[text()='Saved Card']//..");
	By addPromoCode = By.xpath("//button[text()='+ Add promo code']");
	By changePromo = By.xpath("//button[text()='+ Change promo code']");
	By enterCode = By.xpath("//input[@class='mt-1 block w-full rounded-full shadow-sm focus:border-transparent focus:ring-gray-900 focus:ring-2 bg-gray-100 border-transparent']");
	By applyCodeBtn = By.xpath("//button[text()='Apply']");
	By tipOptions = By.xpath("//div[@class='p-4']//div[@class='flex flex-1 w-full h-full cursor-pointer focus:outline-none focus:ring-0']");
	By customTip = By.xpath("//input[@class='pl-40 py-2 block w-full rounded-full shadow-sm focus:outline-none focus:ring-0 focus:border-primary bg-gray-100 pl-14 pr-2']");
	By priceTypeName = By.xpath("//div[@class='flex justify-between']//div[contains(text(), ':')]");
	By priceBreakdown = By.xpath("//div[@class='flex justify-between']//div[contains(text(), '$')]");
	By discountPrice = By.xpath("//div[contains(text(), '- ')]");
	By totalPrice = By.xpath("//div[@class='flex justify-between text-3xl font-bold py-6']//div[contains(text(), '$')]");
	By errorMsg = By.xpath("//div[@class='toast-message']");
	By placeOrder = By.xpath("//div[@class='pb-6 pt-2']//button[contains(text(), 'Place Your Order')]");
	
	public ReviewOrderPage(WebDriver driver, WebDriverWait wait) {
		
		this.driver = driver;
		this.wait = wait;
		
		/*if(!driver.getTitle().contains("Check Out")) {
			throw new IllegalStateException("This is not the correct page! You are at: " + driver.getCurrentUrl());
		}*/
	}
	
	public String getName() {
		return driver.findElement(name).getText();
	}
	
	public String getEmail() {
		return driver.findElement(email).getText();
	}
	
	public String getPhone() {
		return driver.findElement(phone).getText();
	}
	
	public String isDeliverySelected() {
		return driver.findElement(deliveryOption).getAttribute("aria-checked");
	}
	
	public String isPickupSelected() {
		return driver.findElement(pickupOption).getAttribute("aria-checked");
	}
	
	public By getDeliveryOption() {
		return deliveryOption;
	}
	
	public By getPickupOption() {
		return pickupOption;
	}
	
	public void clickCreditCard() {
		driver.findElement(creditCard).click();
	}
	
	public void clickSavedCard() {
		driver.findElement(savedCard).click();
	}
	
	public void clickPayAtStore() {
		driver.findElement(payAtStore).click();
	}
	
	public void clickPlaceOrder() {
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(placeOrder));
		wait.until(ExpectedConditions.elementToBeClickable(placeOrder));
		driver.findElement(placeOrder).click();
	}
	
	public void clickAddPromo() {
		driver.findElement(addPromoCode).click();;
	}
	
	public void clickChangePromo() {
		driver.findElement(changePromo).click();
	}
	
	public void enterPromoCode(String code) {
		driver.findElement(enterCode).sendKeys(code);
	}
	
	public void applyPromoCode() {
		driver.findElement(applyCodeBtn).click();
	}
	
	public void addCoupon(String code) {
		clickAddPromo();
		enterPromoCode(code);
		applyPromoCode();
	}
	
	public void changeCoupon(String code) {
		clickChangePromo();
		enterPromoCode(code);
		applyPromoCode();
	}
	
	public void usePromo(String code) {
		
		try {
			addCoupon(code);
			
		} catch(Exception e) {
			changeCoupon(code);
		}
	}
	
	public void clickTipOption() {
		
		List<WebElement> tips = driver.findElements(tipOptions);
		tips.get(1).click();
	}
	
	public void enterCustomTip(String tip) {
		driver.findElement(customTip).clear();
		driver.findElement(customTip).sendKeys(tip);
	}
	
	public By getPriceBreakdown() { return priceBreakdown; }
	
	public By getSavedCard() { return savedCard; }
	
	public Boolean findCreditCard() {
		
		try {
			return driver.findElement(creditCard).isDisplayed();
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public Boolean findPayAtStore() {
		
		try {
			return driver.findElement(payAtStore).isDisplayed();
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public Boolean findSurcharge(String surcharge) {
		
		try {
			return driver.findElement(By.xpath("//div[contains(text(),'"+surcharge+"')]")).isDisplayed();
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean errorMsgShown() {
		
		Boolean result = false;
		
		try {
			if(driver.findElement(errorMsg).isDisplayed()) {
				result = true;
			}
		} catch(Exception e) {
			result = false;
		}
		return result;
	}
	
	public float convertToPrice(String price, int startSubstring) {
		
		price = price.substring(startSubstring, price.length());
		float newPrice = Float.parseFloat(price);
		
		return newPrice;
	}

	public float getSummarySection(String section) {
		
		List<WebElement> totalSummary = driver.findElements(priceBreakdown);
		List<WebElement> summaryNames = driver.findElements(priceTypeName);
		
		float result = 0;
		int index = 0;
		
		for(int i = 0; i < summaryNames.size(); i++) {
			if(summaryNames.get(i).getText().toLowerCase().contains(section)) {
				index = i;
			}
		}
		
		switch(section)
		{
			case "subtotal":
				String subtotal = totalSummary.get(index).getText();
				result = convertToPrice(subtotal, 1);
				break;
				
			case "tax":
				String tax = totalSummary.get(index).getText();
				result = convertToPrice(tax, 1);
				break;
				
			case "tip":
				String tip = totalSummary.get(index).getText();
				result = convertToPrice(tip, 1);
				break;
				
			case "delivery":
				String delivery = totalSummary.get(index).getText();
				result = convertToPrice(delivery, 1);
				break;
				
			case "surcharge":
				String surcharge = totalSummary.get(summaryNames.size() - 1).getText();
				result = convertToPrice(surcharge, 1);
				break;
				
			case "discount":
				String discount = driver.findElement(discountPrice).getText();
				result = convertToPrice(discount, 3);
				break;
				
			case "total":
				String total = driver.findElement(totalPrice).getText();
				result = convertToPrice(total, 1);
				break;
		}
		
		return result;
	}
}
