package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {
	
	WebDriver driver;
	WebDriverWait wait;
	
	By firstName = By.id("first_name");
	By lastName = By.id("last_name");
	By email = By.id("email");
	By phoneNumber = By.name("phone_number");
	By deliveryOption = By.xpath("//span[text()='Delivery']");
	By address = By.id("address_line1");
	By aptBldgNum = By.id("address_line2");
	By city = By.id("address_city");
	By state = By.id("address_state");
	By zipCode = By.id("address_zip");
	By pickupOption = By.xpath("//span[text()='Pickup']");
	By instructions = By.name("instruction");
	By backBtn = By.xpath("//button[text()='Back to Menu']");
	By continueBtn = By.xpath("//button[text()='Continue to payment method']");
	By locationName = By.xpath("//h2[@class='font-bold text-3xl py-5 text-center border-b px-4']");
	By itemName = By.xpath("//div[@class='flex p-4 text-sm font-semibold']//div[@class='flex-1']");
	By itemModName = By.xpath(".//span[@class='text-xs text-gray-500']");
	
	
	public CheckoutPage(WebDriver driver, WebDriverWait wait) {
		
		this.driver = driver;
		this.wait = wait;
		
		/*if(!driver.getTitle().contains("Check Out")) {
			throw new IllegalStateException("This is not the correct page! You are at: " + driver.getCurrentUrl());
		}*/
	}
	
	public void setFirstName(String fname) {
		driver.findElement(firstName).clear();
		driver.findElement(firstName).sendKeys(fname);
	}
	
	public void setLastName(String lname) {
		driver.findElement(lastName).clear();
		driver.findElement(lastName).sendKeys(lname);
	}
	
	public void setEmail(String email) {
		driver.findElement(this.email).clear();
		driver.findElement(this.email).sendKeys(email);
	}
	
	public void setPhoneNum(String pNum) {
		driver.findElement(phoneNumber).clear();
		driver.findElement(phoneNumber).sendKeys(pNum);
	}
	
	public void clickDelivery() {
		driver.findElement(deliveryOption).click();
	}
	
	public void clickPickup() {
		driver.findElement(pickupOption).click();
	}
	
	public void writeInstructions(String input) {
		driver.findElement(instructions).sendKeys(input);
	}
	
	public void clickBackBtn() {
		driver.findElement(backBtn).click();
	}
	
	public void clickContinue() {
		driver.findElement(continueBtn).click();
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
	}
	
	public Boolean findDeliveryOption() {
		
		try { 
			return driver.findElement(deliveryOption).isDisplayed();
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public Boolean findPickupOption() {
		
		try { 
			return driver.findElement(pickupOption).isDisplayed();
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public void fillOutForm() {
		
		setFirstName("asd");
		setLastName("asd");
		setEmail("asd@asd.com");
		setPhoneNum("1231231231");
	}
	
	public By getFirstName() { return firstName; }
	
	public By getLastName() { return lastName; }
	
	public By getEmail() { return email; }
	
	public By getPhoneNumber() { return phoneNumber; }
	
	public By getLocationName() { return locationName; }
	
	public By getItemName() { return itemName; }
	
	public By getItemModName() { return itemModName; }
	
}
