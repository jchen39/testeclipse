package pages;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountPage {

	WebDriver driver;
	WebDriverWait wait;
	
	By headerName = By.xpath("//a[text()='Welcome To']");
	By profileLink = By.xpath("//a[text()='Profile']");
	By ordersLink = By.xpath("//a[text()='Orders']");
	By orderCard = By.xpath("//div[@class='order-history-list']//div[@class='card']");
	By orderReceipt = By.xpath("//a[@class='view-receipt']");
	By receiptPage = By.id("thanksInfo");
	By orderItems = By.xpath(".//div[@class='order-item']");
	By addToBag = By.xpath(".//button[text()='Add to bag']");
	By reorder = By.xpath(".//button[text()='Reorder']");
	By paymentsLink = By.xpath("//a[text()='Payments']");
	By addressLink = By.xpath("//a[text()='Address']");
	By logout = By.xpath("//a[text()='Logout']");
	
	public AccountPage(WebDriver driver, WebDriverWait wait) {
		
		this.driver = driver;
		this.wait = wait;
		
		/*if(!driver.getTitle().contains("EzOrderNow")) {
			throw new IllegalStateException("This is not the correct page! You are at: " + driver.getCurrentUrl());
		}*/
	}
	
	public void clickProfile() {
		driver.findElement(profileLink).click();
	}
	
	public void clickOrders() {
		driver.findElement(ordersLink).click();
	}
	
	public void clickPayments() {
		driver.findElement(paymentsLink).click();
	}
	
	public void clickAddress() {
		driver.findElement(addressLink).click();
	}
	
	public void clickLogout() {
		driver.findElement(logout).click();
	}
	
	public void clickHeader() {
		driver.findElement(headerName).click();
	}
	
	public void clickReceipt() {
		
		List<WebElement> receipts = driver.findElements(orderReceipt);
		Random rand = new Random();
		int randomReceipt = rand.nextInt(receipts.size());
		
		receipts.get(randomReceipt).click();
	}
	
	public void clickAddToBag() {
		driver.findElement(addToBag).click();
	}
	
	public void clickReorder() {
		driver.findElement(reorder).click();
	}
	
	public By getHeaderName() { return headerName; }
	public By getOrderCard() { return orderCard; }
	public By getOrderItems() { return orderItems; }
	public By getAddToBag() { return addToBag; }
	public By getReorder() { return reorder; }
	public By getReceiptPage() { return receiptPage; }
}
