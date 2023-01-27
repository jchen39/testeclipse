package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Parameters;
import org.testng.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform;

import java.net.URL;
import java.time.Duration;
import java.net.MalformedURLException;

import pages.LocationConfigPage;
import pages.AdminLoginPage;
import pages.Navbars;
import pages.MainPage;
import pages.CheckoutPage;
import pages.ReviewOrderPage;
import pages.LoginPage;

public class TaxCalculationTests {
	
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
	
	@Test(description="Surcharge amount should be included in tax calculation", priority = -1)
	public void testSurchargeTaxable() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		float tax = Float.parseFloat(driver.findElement(configpage.getTaxRate()).getAttribute("value")) / 100;
		configpage.clickServiceFeeTaxable();
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickLogin();
		ezlogin.login("jasonchen@go3technology.us", "123456789");
		
		mainpage.goToCheckout();
		
		checkoutpage.clickContinue();
		
		float subtotal = reviewpage.getSummarySection("subtotal");
		float surcharge = reviewpage.getSummarySection("surcharge");
		
		float taxActual = reviewpage.getSummarySection("tax");
		String taxExpected = String.format("%.2f", (subtotal + surcharge) * tax);
		
		Assert.assertEquals(taxActual, Float.parseFloat(taxExpected), "Shown tax is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.clickServiceFeeTaxable();
		configpage.clickSubmit();
	}
	
	@Test(description="Delivery fee amount should be included in tax calculation")
	public void testDeliveryTaxable() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		float tax = Float.parseFloat(driver.findElement(configpage.getTaxRate()).getAttribute("value")) / 100;
		configpage.clickShippingTaxable();
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		
		checkoutpage.clickDelivery();
		checkoutpage.clickContinue();
		
		float subtotal = reviewpage.getSummarySection("subtotal");
		float delivery = reviewpage.getSummarySection("delivery");
		
		float taxActual = reviewpage.getSummarySection("tax");
		String taxExpected = String.format("%.2f", (subtotal + delivery) * tax);
		
		Assert.assertEquals(taxActual, Float.parseFloat(taxExpected), "Shown tax is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.clickShippingTaxable();
		configpage.clickSubmit();
	}
	
	@Test(description="Discount amount should be included in tax calculation")
	public void testDiscountTaxable() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		float tax = Float.parseFloat(driver.findElement(configpage.getTaxRate()).getAttribute("value")) / 100;
		configpage.clickDiscountTaxable();
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		
		checkoutpage.clickContinue();
		reviewpage.addCoupon("test");
		
		float subtotal = reviewpage.getSummarySection("subtotal");
		float discount = reviewpage.getSummarySection("discount");
		
		float taxActual = reviewpage.getSummarySection("tax");
		String taxExpected = String.format("%.2f", (subtotal - discount) * tax);
		
		Assert.assertEquals(taxActual, Float.parseFloat(taxExpected), "Shown tax is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.clickDiscountTaxable();
		configpage.clickSubmit();
	}
	
	@Test(description="Tip amount should be included in tax calculation")
	public void testTipTaxable() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		float tax = Float.parseFloat(driver.findElement(configpage.getTaxRate()).getAttribute("value")) / 100;
		configpage.clickTipTaxable();
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		
		checkoutpage.clickContinue();
		reviewpage.clickTipOption();
		
		float subtotal = reviewpage.getSummarySection("subtotal");
		float tip = reviewpage.getSummarySection("tip");
		
		float taxActual = reviewpage.getSummarySection("tax");
		String taxExpected = String.format("%.2f", (subtotal + tip) * tax);
		
		Assert.assertEquals(taxActual, Float.parseFloat(taxExpected), "Shown tax is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.clickTipTaxable();
		configpage.clickSubmit();
	}
	
	@Test(description="Tax calculation should round correctly when needed")
	public void testRoundTax() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		float tax = Float.parseFloat(driver.findElement(configpage.getTaxRate()).getAttribute("value")) / 100;
		configpage.clickRoundTax();
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		
		checkoutpage.clickContinue();
		float subtotal = reviewpage.getSummarySection("subtotal");
		float taxActual = reviewpage.getSummarySection("tax");
		String taxExpected = String.format("%.2f", subtotal * tax);
		
		Assert.assertEquals(taxActual, Float.parseFloat(taxExpected), "Shown tax is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Tax calculation should use ceiling to get the hundredths digit")
	public void testCeilTax() {
		
		navbar.clickLocationModule();
		navbar.clickConfig();
		
		float tax = Float.parseFloat(driver.findElement(configpage.getTaxRate()).getAttribute("value")) / 100;
		configpage.clickCeilTax();
		configpage.clickSubmit();
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		
		checkoutpage.clickContinue();
		float subtotal = reviewpage.getSummarySection("subtotal");
		float taxActual = reviewpage.getSummarySection("tax");
		float taxExpected = (float)Math.ceil((subtotal * tax) * 100) / 100;
		
		Assert.assertEquals(taxActual, taxExpected, "Shown tax is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
		configpage.clickRoundTax();
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
