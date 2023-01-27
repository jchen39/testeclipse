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

public class ServiceTypeTests {

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
	
	@Test(description="Set pickup only as the service type", priority = -1)
	public void testPickupOnly() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		configpage.selectServiceType("Pick Up Only");
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickLogin();
		ezlogin.login("jasonchen@go3technology.us", "123456789");
		mainpage.goToCheckout();
		
		Assert.assertFalse(checkoutpage.findDeliveryOption(), "Delivery option can still be selected");
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Set delivery only as the service type")
	public void testDeliveryOnly() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		configpage.selectServiceType("Delivery Only");
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		
		Assert.assertFalse(checkoutpage.findPickupOption(), "Pickup option can still be selected");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.selectServiceType("Pick Up And Delivery");
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
