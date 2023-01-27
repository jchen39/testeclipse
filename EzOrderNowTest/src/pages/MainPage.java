package pages;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage {

	WebDriver driver;
	WebDriverWait wait;
	
	//main page elements
	By loginBtn = By.xpath("//a[text()='Log In']");
	By logoutBtn = By.id("btnCustomerLogOut");
	By langBtn = By.xpath("//a[@data-target='#languageSwitchModal']");
	By profileIcon = By.xpath("//i[@class='icon-user']//..");
	By headerName = By.xpath("//a[text()='Welcome To']");
	By locationName = By.xpath("//h2[@class='restaurant-header-title']");
	By hoursIcon = By.xpath("//a[@data-target='#hoursModal']");
	By contactIcon = By.xpath("//a[@data-target='#contactModal']");
	By scheduleBtn = By.xpath("//a[@data-target='#orderSettingModal']");
	By termsConditions = By.xpath("//a[@data-target='#termsAndConditions']");
	By refundPolicy = By.xpath("//a[@data-target='#privacyPolicy']");
	By privacyPolicy = By.xpath("//a[@data-target='#ezordernowPrivacyPolicy']");
	By categoryHeader = By.xpath("//div[@class='ez-accordion-header']");
	By categoryBody = By.xpath("//div[@class='ez-accordion-body p-0']");
	By shareBtn = By.id("shareBtn");
	By scrollUpBtn = By.id("backToUp");
	By orderDetails = By.xpath("//div[@class='cart-body external-cart-items']");
	By itemInCart = By.xpath("//div[@class='cart-item row mx-0']");
	By itemInCartName = By.xpath("//div[@class='cart-item-menu']");
	By cartDiscount = By.xpath("//div[text()='Discount']");
	By cartSubtotal = By.xpath("//div[@class='cart-compute-amount']");
	By emptyCartBtn = By.xpath("//a[text()='Empty Cart']");
	By scheduleDate = By.xpath("//a[@data-target='#orderSettingModal']//span[@class='text-color-light']");
	By menuItem = By.xpath("//ul[@class='ez-accordion']//div[@class='menu-info-right']");
	By checkoutBtn = By.xpath("//a[@class='btn btn-block btn-checkout ']");
	
	//modals
	By hoursModal = By.id("hoursLabel");
	By contactModal = By.id("contactLabel");
	By scheduleModal = By.id("orderSettingLabel");
	By termsCondModal = By.id("termsAndConditions");
	By refundPolModal = By.id("privacyPolicy");
	By privacyPolModal = By.id("ezordernowPrivacyPolicy");
	By shareModal = By.id("exampleModalCenterTitle");
	By menuItemModal = By.xpath("//div[@class='modal-content']//h5[@class='menu-item-name']");
	
	//elements in modals
	By langOptions = By.xpath("//select[@class='form-control']");
	By langSubmit = By.xpath("//button[@class='btn btn-theme btn-block']");
	By profileOption = By.xpath("//span[text()='Profile']");
	By ordersOption = By.xpath("//span[text()='Orders']");
	By paymentsOption = By.xpath("//span[text()='Payments']");
	By addressOption = By.xpath("//span[text()='Address']");
	By logoutOption = By.xpath("//span[text()='Logout']");
	By scheduleToday = By.xpath("//label[text()='Today']");
	By scheduleLater = By.xpath("//label[text()='Later']");
	By timeOptions = By.xpath("//div[@class='form-group mt-3']//select[@class='form-control']");
	By calendarDays = By.xpath("//div[@class='d-flex justify-content-around']//a[@class='calender__item ']");
	By selectedLaterDate = By.xpath("//div[@class='text-center p-3 mt-3']");
	By updateSched = By.xpath("//button[text()='Update']");
	By addToCart = By.xpath("//button[@class='btn btn-primary btn-block rounded-pill']");
	By addQuantity = By.xpath("//i[@class='icon-plus']");
	By minusQuantity = By.xpath("//i[@class='icon-minus']");
	By itemQuantity = By.xpath("//div[@class='item-qty']");
	
	
	public MainPage(WebDriver driver, WebDriverWait wait) {
		
		this.driver = driver;
		this.wait = wait;
		
		/*if(!driver.getTitle().contains("EzOrderNow")) {
			throw new IllegalStateException("This is not the correct page! You are at: " + driver.getCurrentUrl());
		}*/
	}
	
	public void clickLogin() {
		driver.findElement(loginBtn).click();
	}
	
	public void clickLogout() {
		driver.findElement(logoutBtn).click();
	}
	
	public void clickLangBtn() {
		driver.findElement(langBtn).click();
	}
	
	public void changeLangTo(String lang) {
		driver.findElement(langOptions).click();
		Select dropdown = new Select(driver.findElement(langOptions));
		dropdown.selectByVisibleText(lang);
		driver.findElement(langSubmit).click();
	}
	
	public String langBtnText() {
		return driver.findElement(langBtn).getText();
	}
	
	public void clickProfileIcon() {
		driver.findElement(profileIcon).click();
	}
	
	public void clickProfileOption() {
		driver.findElement(profileOption).click();
	}
	
	public void clickOrdersOption() {
		driver.findElement(ordersOption).click();
	}
	
	public void clickPaymentsOption() {
		driver.findElement(paymentsOption).click();
	}
	
	public void clickAddressOption() {
		driver.findElement(addressOption).click();
	}
	
	public void clickLogoutOption() {
		driver.findElement(logoutOption).click();
	}
	
	public void clickHeaderName() {
		driver.findElement(headerName).click();
	}
	
	public void clickHoursIcon() {
		driver.findElement(hoursIcon).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(hoursModal));
	}
	
	public void clickContactIcon() {
		driver.findElement(contactIcon).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(contactModal));
	}
	
	public void clickSchedule() {
		driver.findElement(scheduleBtn).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(scheduleModal));
	}
	
	public void clickTermsConditions() {
		driver.findElement(termsConditions).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(termsCondModal));
	}
	
	public void clickRefundPolicy() {
		driver.findElement(refundPolicy).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(refundPolModal));
	}
	
	public void clickPrivacyPolicy() {
		driver.findElement(privacyPolicy).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(privacyPolModal));
	}
	
	public void clickShareBtn() {
		driver.findElement(shareBtn).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(shareModal));
	}
	
	public void clickScrollUp() {
		driver.findElement(scrollUpBtn).click();
	}
	
	public void emptyCart() {
		driver.findElement(emptyCartBtn).click();
	}
	
	public void submitLang() {
		driver.findElement(langSubmit).click();
	}
	
	public void scheduleToday() {
		driver.findElement(scheduleToday).click();
	}
	
	public void scheduleLater() {
		driver.findElement(scheduleLater).click();
	}
	
	public void submitSchedule() {
		driver.findElement(updateSched).click();
	}
	
	public void clickMenuItem() {
		
		List<WebElement> menuItems = driver.findElements(menuItem);
		Random rand = new Random();
		int randomItem = rand.nextInt(menuItems.size());
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({'block':'center','inline':'center'});", menuItems.get(3));
		menuItems.get(3).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(menuItemModal));
	}
	
	public void addQuantity(int frequency) {
		for(int i = 0; i < frequency; i++) {
			driver.findElement(addQuantity).click();
		}
	}
	
	public void minusQuantity(int frequency) {
		for(int i = 0; i < frequency; i++) {
			driver.findElement(minusQuantity).click();
		}
	}
	
	public void clickAddToCart() {
		driver.findElement(addToCart).click();
	}
	
	public void clickCheckout() {
		driver.findElement(checkoutBtn).click();
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
	}
	
	public String quantityCount() {
		return driver.findElement(itemQuantity).getText();
	}
	
	public void addItemToCart() {
		clickMenuItem();
		clickAddToCart();
	}
	
	public void goToCheckout() {
		
		clickMenuItem();
		clickAddToCart();
		clickCheckout();
	}
	
	public Boolean discountApplied() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(cartDiscount));
		return driver.findElement(cartDiscount).isDisplayed();
	}
	
	public By getLoginBtn() { return loginBtn; }
	
	public By getLogoutBtn() { return logoutBtn; }
	
	public By getProfileIcon() { return profileIcon; }
	
	public By getLangBtn() { return langBtn; }
	
	public By getHeaderName() { return headerName; }
	
	public By getLocationName() { return locationName; }
	
	public By getHoursIcon() { return hoursIcon; }
	
	public By getContactIcon() { return contactIcon; }
	
	public By getScheduleBtn() { return scheduleBtn; }
	
	public By getTermsConditions() { return termsConditions; }
	
	public By getRefundPolicy() { return refundPolicy; }
	
	public By getPrivacyPolicy() { return privacyPolicy; }
	
	public By getCategoryHeader() { return categoryHeader; }
	
	public By getCategoryBody() { return categoryBody; }
	
	public By getShareBtn() { return shareBtn; }
	
	public By scrollUpBtn() { return scrollUpBtn; }
	
	public By getOrderDetails() { return orderDetails; }
	
	public By getItemInCart() { return itemInCart; }
	
	public By getItemInCartName() { return itemInCartName; }
	
	public By getCartSubtotal() { return cartSubtotal; }
	
	public By getEmptyCartBtn() { return emptyCartBtn; }
	
	public By getHoursModal() { return hoursModal; }
	
	public By getContactModal() { return contactModal; }
	
	public By getScheduleModal() { return scheduleModal; }
	
	public By getTermsCondModal() { return termsCondModal; }
	
	public By getRefundPolModal() { return refundPolModal; }
	
	public By getPrivacyPolModal() { return privacyPolModal; }
	
	public By getShareModal() { return shareModal; }
	
	public By getLangOptions() { return langOptions; }
	
	public By getLangSubmit() { return langSubmit; }
	
	public By getScheduleToday() { return scheduleToday; }
	
	public By getScheduleLater() { return scheduleLater; }
	
	public By getTimeOptions() { return timeOptions; }
	
	public By getCalendarDays() { return calendarDays; }
	
	public By getSelectedDate() { return selectedLaterDate; }
	
	public By getUpdateSched() { return updateSched; }
	
	public By getScheduleDate() { return scheduleDate; }
	
	public By getMenuItem() { return menuItem; }
	
	public By getMenuItemModal() { return menuItemModal; }
	
}