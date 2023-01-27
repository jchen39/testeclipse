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

import pages.Navbars;
import pages.SurchargesPage;
import pages.AdminLoginPage;
import pages.MainPage;
import pages.LoginPage;
import pages.CheckoutPage;
import pages.ReviewOrderPage;

public class SurchargeTests {

	WebDriver driver;
	WebDriverWait wait;
	Navbars navbar;
	SurchargesPage surchargepage;
	AdminLoginPage login;
	MainPage mainpage;
	LoginPage loginpage;
	CheckoutPage checkoutpage;
	ReviewOrderPage reviewpage;
	
	String url = "https://admin-uat.ezordernow.com/";//"https://admin.ezordernow.com";
	String surchargeTitle = "Service Fee";
	String prevWindow;
	
	@BeforeClass
	public void beforeClass() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/jasonchen/Desktop/chromedriver");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		navbar = new Navbars(driver);
		surchargepage = new SurchargesPage(driver);
		login = new AdminLoginPage(driver);
		mainpage = new MainPage(driver, wait);
		loginpage = new LoginPage(driver);
		checkoutpage = new CheckoutPage(driver, wait);
		reviewpage = new ReviewOrderPage(driver, wait);
		driver.navigate().to(url);
		login.login("jasonchen@go3technology.us", "123456789");
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		driver.navigate().to(url);
		prevWindow = driver.getWindowHandle();
	}
	
	@Test(description="Set amount type to fixed", priority = -1)
	public void testFixedType() {
		
		navbar.clickLocationModule();
		navbar.clickSurcharges();
		
		surchargepage.clickEditSurcharge(surchargeTitle);
		surchargepage.selectAmountType("Fix");
		String amount = "5";
		surchargepage.setAmount(amount);
		surchargepage.clickSubmit();
		
		navbar.clickPreview();
		mainpage.clickLogin();
		loginpage.login("jasonchen@go3technology.us", "123456789");
		
		mainpage.goToCheckout();
		checkoutpage.clickContinue();
		
		float surchargeActual = reviewpage.getSummarySection("surcharge");
		float surchargeExpected = Float.parseFloat(amount);
		
		Assert.assertEquals(surchargeActual, surchargeExpected);
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Set amount type to percentage")
	public void testPercentageType() {
		
		navbar.clickLocationModule();
		navbar.clickSurcharges();
		
		surchargepage.clickEditSurcharge(surchargeTitle);
		surchargepage.selectAmountType("Percentage");
		String amount = "15";
		float percentage = Float.parseFloat(amount) / 100;
		surchargepage.setAmount(amount);
		surchargepage.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		
		float surchargeActual = reviewpage.getSummarySection("surcharge");
		String surchargeExpected = String.format("%.2f", reviewpage.getSummarySection("subtotal") * percentage);
		
		Assert.assertEquals(surchargeActual, Float.parseFloat(surchargeExpected));
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Set order type to delivery only")
	public void testDeliveryOnly() {
		
		navbar.clickLocationModule();
		navbar.clickSurcharges();
		
		surchargepage.clickEditSurcharge(surchargeTitle);
		surchargepage.selectOrderType("Delivery only");
		surchargepage.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickPickup();
		checkoutpage.clickContinue();
		
		Assert.assertFalse(reviewpage.findSurcharge(surchargeTitle));
		driver.close();
		driver.switchTo().window(prevWindow);
		surchargepage.selectOrderType("Delivery And Pickup");
		surchargepage.clickSubmit();
	}
	
	@Test(description="Set order type to pickup only")
	public void testPickupOnly() {
		
		navbar.clickLocationModule();
		navbar.clickSurcharges();
		
		surchargepage.clickEditSurcharge(surchargeTitle);
		surchargepage.selectOrderType("Pickup only");
		surchargepage.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickDelivery();
		checkoutpage.clickContinue();
		
		Assert.assertFalse(reviewpage.findSurcharge(surchargeTitle));
		driver.close();
		driver.switchTo().window(prevWindow);
		surchargepage.selectOrderType("Delivery And Pickup");
		surchargepage.clickSubmit();
	}
	
	@Test(description="Set a minimum price required for surcharge")
	public void testSurchargeLimit() {
		
		navbar.clickLocationModule();
		navbar.clickSurcharges();
		
		surchargepage.clickEditSurcharge(surchargeTitle);
		surchargepage.setLimit("50");
		surchargepage.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		
		Assert.assertFalse(reviewpage.findSurcharge(surchargeTitle));
		driver.close();
		driver.switchTo().window(prevWindow);
		surchargepage.setLimit("0");
		surchargepage.clickSubmit();
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
