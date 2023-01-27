package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform;

import java.net.URL;
import java.time.Duration;

import pages.LocationConfigPage;
import pages.AdminLoginPage;
import pages.Navbars;
import pages.MainPage;
import pages.CheckoutPage;
import pages.ReviewOrderPage;
import pages.LoginPage;

public class PaymentTypeTests {

	WebDriver driver;
	WebDriverWait wait;
	AdminLoginPage loginpage;
	LoginPage ezlogin;
	LocationConfigPage configpage;
	MainPage mainpage;
	CheckoutPage checkoutpage;
	ReviewOrderPage reviewpage;
	Navbars navbar;
	
	String url = "https://admin-uat.ezordernow.com/";//"https://admin.ezordernow.com";
	String prevWindow;
	
	@BeforeClass
	public void beforeClass() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/jasonchen/Desktop/chromedriver");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.navigate().to(url);
		
		loginpage = new AdminLoginPage(driver);
		ezlogin = new LoginPage(driver);
		configpage = new LocationConfigPage(driver);
		mainpage = new MainPage(driver, wait);
		checkoutpage = new CheckoutPage(driver, wait);
		reviewpage = new ReviewOrderPage(driver, wait);
		navbar = new Navbars(driver);
		
		loginpage.login("jasonchen@go3technology.us", "123456789");
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		driver.navigate().to(url);
		prevWindow = driver.getWindowHandle();
	}
	
	@Test(description="Set cash only as the payment type", priority = -1)
	public void testCashOnly() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		configpage.selectPaymentType("Cash Only");
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickLogin();
		ezlogin.login("jasonchen@go3technology.us", "123456789");
		mainpage.goToCheckout();
		checkoutpage.clickContinue();
		
		Assert.assertFalse(reviewpage.findCreditCard(), "Card option can still be selected");
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Set card only as the payment type")
	public void testCardOnly() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		configpage.selectPaymentType("Credit/Debit Card");
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		
		Assert.assertFalse(reviewpage.findPayAtStore(), "Pay cash option can still be selected");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.selectPaymentType("Cash And Card");
		configpage.clickSubmit();
	}
	
	@Test(description="Check out when subtotal is below the card minimum", priority = 1)
	public void testCardMinimum() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		configpage.setCardMinimum("30");
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		
		reviewpage.clickSavedCard();
		wait.until(ExpectedConditions.attributeToBe(reviewpage.getSavedCard(), "aria-checked", "true"));
		reviewpage.clickPlaceOrder();
		
		Assert.assertTrue(reviewpage.errorMsgShown(), "No error message shown");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.setCardMinimum("10");
		configpage.clickSubmit();
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
