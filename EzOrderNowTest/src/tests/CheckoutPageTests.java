package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform;

import java.net.URL;
import java.time.Duration;

import pages.MainPage;
import pages.LoginPage;
import pages.CheckoutPage;
import pages.ReviewOrderPage;

public class CheckoutPageTests {

	WebDriver driver;
	WebDriverWait wait;
	MainPage mainpage;
	LoginPage loginpage;
	CheckoutPage checkoutpage;
	ReviewOrderPage reviewpage;
	
	String url = "https://uat.ezordernow.com/ezordernow";//"https://www.ezordernow.com/new-york-10004-demo-store-1-802455";
	
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
		checkoutpage = new CheckoutPage(driver, wait);
		reviewpage = new ReviewOrderPage(driver, wait);
		mainpage.clickLogin();
		loginpage.login("jasonchen@go3technology.us", "123456789");
		wait.until(ExpectedConditions.visibilityOfElementLocated(mainpage.getLocationName()));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		driver.navigate().to(url);
	}
	
	@Test(description="Verify if delivery option is still selected on review page", priority = -1)
	public void testPickupSelected() {
		
		mainpage.goToCheckout();
		checkoutpage.clickPickup();
		checkoutpage.clickContinue();
		
		Assert.assertEquals(reviewpage.isPickupSelected(), "true", "Pickup option is not selected");
	}
	
	@Test(description="Verify if delivery option is still selected on review page")
	public void testDeliverySelected() {
		
		mainpage.clickCheckout();
		checkoutpage.clickDelivery();
		checkoutpage.clickContinue();
		
		Assert.assertEquals(reviewpage.isDeliverySelected(), "true", "Delivery option is not selected");
	}
	
	@Test(description="Verify if name field is correct")
	public void testVerifyName() {
		
		mainpage.clickCheckout();
		String fname = "test";
		String lname = "name";
		checkoutpage.setFirstName(fname);
		checkoutpage.setLastName(lname);
		checkoutpage.clickContinue();
		
		Assert.assertTrue(reviewpage.getName().equalsIgnoreCase(fname + " " + lname));
	}
	
	@Test(description="Verify if email field is correct")
	public void testVerifyEmail() {
		
		mainpage.clickCheckout();
		String email = "test@email.com";
		checkoutpage.setEmail(email);
		checkoutpage.clickContinue();
		
		Assert.assertTrue(reviewpage.getEmail().equals(email));
	}
	
	@AfterMethod
	public void afterMethod() {
		
		System.out.println("Test completed");
	}
	
	@AfterClass
	public void afterClass() {
		
		driver.quit();
	}
}
