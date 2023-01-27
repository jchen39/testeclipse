package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import pages.MainPage;
import pages.AccountPage;
import pages.LoginPage;
import pages.CheckoutPage;

public class AccountPageTests {

	WebDriver driver;
	WebDriverWait wait;
	MainPage mainpage;
	LoginPage loginpage;
	AccountPage accountpage;
	CheckoutPage checkoutpage;
	Random rand = new Random();
	
	String url = "https://uat.ezordernow.com/ezordernow";//"https://www.ezordernow.com/new-york-10004-demo-store-1-802455";
	String asserturl = "https://uat.ezordernow.com/ezordernow";//"https://www.ezordernow.com/new-york-10004-demo-store-1-802455";
	
	@BeforeClass
	public void beforeClass() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/jasonchen/Desktop/chromedriver");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.navigate().to(url);
		
		mainpage = new MainPage(driver, wait);
		loginpage = new LoginPage(driver);
		accountpage = new AccountPage(driver, wait);
		checkoutpage = new CheckoutPage(driver, wait);
		
		mainpage.clickLogin();
		loginpage.login("jasonchen@go3technology.us", "123456789");
		wait.until(ExpectedConditions.visibilityOfElementLocated(mainpage.getLocationName()));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		driver.navigate().to(url);
	}
	
	@Test(description="Access the account page", priority = -1)
	public void testGoToAccount() {
		
		mainpage.clickProfileIcon();
		mainpage.clickProfileOption();
		url = driver.getCurrentUrl();
		
		Assert.assertEquals(url, asserturl + "/account/profile", "Profile page not loaded");
	}
	
	@Test(description="Access the profile page")
	public void testClickProfile() {
		
		accountpage.clickProfile();
		
		Assert.assertEquals(driver.getCurrentUrl(), asserturl + "/account/profile", "Profile page not loaded");
	}
	
	@Test(description="Access the order history page")
	public void testClickOrders() {
	
		accountpage.clickOrders();
		
		Assert.assertEquals(driver.getCurrentUrl(), asserturl + "/account/orders", "Orders page not loaded");
	}
	
	@Test(description="Access the saved cards page")
	public void testClickPayments() {
		
		accountpage.clickPayments();
		
		Assert.assertEquals(driver.getCurrentUrl(), asserturl + "/account/payments", "Payments page not loaded");
	}
	
	@Test(description="Access the saved addresses page")
	public void testClickAddress() {
		
		accountpage.clickAddress();
		
		Assert.assertEquals(driver.getCurrentUrl(), asserturl + "/account/address", "Address page not loaded");
	}
	
	@Test(description="Click the logout button", priority = 1)
	public void testClickLogout() {
		
		accountpage.clickLogout();
		wait.until(ExpectedConditions.visibilityOfElementLocated(mainpage.getLoginBtn()));
		
		Assert.assertTrue(driver.findElement(mainpage.getLoginBtn()).isDisplayed(), "User was not logged out");
	}
	
	@Test(description="View the receipt of a previous order")
	public void testViewReceipt() {
		
		accountpage.clickOrders();
		accountpage.clickReceipt();
		
		Assert.assertTrue(driver.findElement(accountpage.getReceiptPage()).isDisplayed(), "Receipt page was not loaded");
	}
	
	@Test(description="Add items from previous order to bag")
	public void testAddToBag() {
		
		//create lists to store items to compare at assert
		List<String> itemsToAdd = new ArrayList<String>();
		List<String> confirmItems = new ArrayList<String>();
		
		accountpage.clickOrders();
		
		//list of each individual order section
		List<WebElement> ordersList = driver.findElements(accountpage.getOrderCard());
		int randomOrder = rand.nextInt(ordersList.size());
		
		//list of all items in selected order
		List<WebElement> orderItems = ordersList.get(randomOrder).findElements(accountpage.getOrderItems());
		
		//add items in selected order to reorder list
		//replace used to get rid of text retrieved from child element
		for(int i = 0; i < orderItems.size(); i++) {
			itemsToAdd.add(orderItems.get(i).getText().replace(orderItems.get(i).findElement(By.tagName("span")).getText(), ""));
		}
		
		//click add to bag
		ordersList.get(randomOrder).findElement(accountpage.getAddToBag()).click();
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
		
		//list of order items shown in cart
		List<WebElement> orderDetailItems = driver.findElements(mainpage.getItemInCartName());
		
		//add items shown in cart to confirmitems list
		for(int i = 0; i < orderDetailItems.size(); i++) {
			confirmItems.add(orderDetailItems.get(i).getText().replace(orderDetailItems.get(i).findElement(By.tagName("span")).getText(), "").trim());
		}
		
		//compare the two lists
		Assert.assertEquals(confirmItems, itemsToAdd, "Items in cart do not match expected items");
		mainpage.emptyCart();
	}
	
	@Test(description="Reorder a previous order")
	public void testReorder() {
		
		//create lists to store items to compare at assert
		List<String> reorderItems = new ArrayList<String>();
		List<String> confirmReorder = new ArrayList<String>();
		
		accountpage.clickOrders();
		
		//list of each individual order section
		List<WebElement> ordersList = driver.findElements(accountpage.getOrderCard());
		int randomOrder = rand.nextInt(ordersList.size());
		
		//list of all items in selected order
		List<WebElement> orderItems = ordersList.get(randomOrder).findElements(accountpage.getOrderItems());
		
		//add items in selected order to reorder list
		//replace used to get rid of text retrieved from child element
		for(int i = 0; i < orderItems.size(); i++) {
			reorderItems.add(orderItems.get(i).getText().replace(orderItems.get(i).findElement(By.tagName("span")).getText(), "").trim());
		}
		
		//click reorder
		ordersList.get(randomOrder).findElement(accountpage.getReorder()).click();
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
		
		//list of order items shown in checkout screen
		List<WebElement> orderDetailItems = driver.findElements(checkoutpage.getItemName());
		
		//add items shown in checkout screen to confirmreorder list
		try {
			String item;
			
			//replace span text for items with > 1 span elements
			for(int i = 0; i < orderDetailItems.size(); i++) {
				item = orderDetailItems.get(i).getText();
				for(int j = 0; j < orderDetailItems.get(i).findElements(checkoutpage.getItemModName()).size(); j++) {
					item = item.replace(orderDetailItems.get(i).findElements(checkoutpage.getItemModName()).get(j).getText(), "").trim();
				}
				confirmReorder.add(item);
			}
		//when a span element is not found because item has no modifiers displayed
		} catch(Exception e) {
			for(int i = 0; i < orderDetailItems.size(); i++) {
				confirmReorder.add(orderDetailItems.get(i).getText().trim());
			}
		}
		
		//compare the two lists
		Assert.assertEquals(confirmReorder, reorderItems, "Items in checkout do not match expected items");
	}
	
	@Test(description="Clicking the location name header in profile page")
	public void testClickHeader() {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(accountpage.getHeaderName()));
		accountpage.clickHeader();
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(url)));
		
		Assert.assertEquals(driver.getCurrentUrl(), asserturl);
	}
	
	@AfterMethod
	public void afterMethod() {
		
		System.out.println("Test completed");
	}
	
	@AfterClass
	public void afterTest() {
		
		driver.quit();
	}
}
