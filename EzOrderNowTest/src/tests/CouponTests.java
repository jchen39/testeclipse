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
import pages.BusinessCouponPage;
import pages.AdminLoginPage;
import pages.MainPage;
import pages.LoginPage;
import pages.CheckoutPage;
import pages.ReviewOrderPage;

public class CouponTests {
	
	WebDriver driver;
	WebDriverWait wait;
	Navbars navbar;
	BusinessCouponPage bcoupons;
	AdminLoginPage login;
	MainPage mainpage;
	LoginPage loginpage;
	CheckoutPage checkoutpage;
	ReviewOrderPage reviewpage;
	
	String url = "https://admin-uat.ezordernow.com/";//"https://admin.ezordernow.com";
	String couponCode = "test";
	String prevWindow;
	
	@BeforeClass
	public void beforeClass() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/jasonchen/Desktop/chromedriver");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		navbar = new Navbars(driver);
		bcoupons = new BusinessCouponPage(driver);
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
	
	@Test(description="Turn on auto apply for coupons", priority = -1)
	public void testAutoApply() {
		
		navbar.clickCouponModule();
		navbar.clickBusinessCoupons();
		
		bcoupons.clickEditCoupon(bcoupons.retrieveCid(couponCode));
		bcoupons.clickAutoApply();
		bcoupons.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickLogin();
		loginpage.login("jasonchen@go3technology.us", "123456789");
		mainpage.clickMenuItem();
		mainpage.clickAddToCart();
		
		Assert.assertTrue(mainpage.discountApplied(), "Discount was not auto applied to order");
		driver.close();
		driver.switchTo().window(prevWindow);
		bcoupons.clickAutoApply();
		bcoupons.clickSubmit();
	}
	
	@Test(description="Set amount type to fixed")
	public void testFixedType() {
		
		navbar.clickCouponModule();
		navbar.clickBusinessCoupons();
		String amount = "2";
		
		bcoupons.clickEditCoupon(bcoupons.retrieveCid(couponCode));
		bcoupons.selectAmountType("Fixed");
		bcoupons.setAmount(amount);
		bcoupons.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		reviewpage.usePromo(couponCode);
		
		float discountActual = reviewpage.getSummarySection("discount");
		float discountExpected = Float.parseFloat(amount);
		
		Assert.assertEquals(discountActual, discountExpected, "Shown discount is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Set amount type to percentage")
	public void testPercentageType() {
		
		navbar.clickCouponModule();
		navbar.clickBusinessCoupons();
		String amount = "15";
		float percentage = Float.parseFloat(amount) / 100;
		
		bcoupons.clickEditCoupon(bcoupons.retrieveCid(couponCode));
		bcoupons.selectAmountType("Percentage");
		bcoupons.setAmount(amount);
		bcoupons.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		reviewpage.usePromo(couponCode);
		
		float discountActual = reviewpage.getSummarySection("discount");
		String discountExpected = String.format("%.2f", reviewpage.getSummarySection("subtotal") * percentage);
		
		Assert.assertEquals(discountActual, Float.parseFloat(discountExpected), "Shown discount is not calculated correctly");
		driver.close();
		driver.switchTo().window(prevWindow);
	}
	
	@Test(description="Set order type to delivery only")
	public void testDeliveryOnly() {
		
		navbar.clickCouponModule();
		navbar.clickBusinessCoupons();
		
		bcoupons.clickEditCoupon(bcoupons.retrieveCid(couponCode));
		bcoupons.selectOrderType("Delivery only");
		bcoupons.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickPickup();
		checkoutpage.clickContinue();
		reviewpage.usePromo(couponCode);
		
		Assert.assertTrue(reviewpage.errorMsgShown(), "Discount was still applied for pickup");
		driver.close();
		driver.switchTo().window(prevWindow);
		bcoupons.selectOrderType("Delivery And Pickup");
		bcoupons.clickSubmit();
	}
	
	@Test(description="Set order type to delivery only")
	public void testPickupOnly() {
		
		navbar.clickCouponModule();
		navbar.clickBusinessCoupons();
		
		bcoupons.clickEditCoupon(bcoupons.retrieveCid(couponCode));
		bcoupons.selectOrderType("Pickup only");
		bcoupons.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickDelivery();
		checkoutpage.clickContinue();
		reviewpage.usePromo(couponCode);
		
		Assert.assertTrue(reviewpage.errorMsgShown(), "Discount was still applied for delivery");
		driver.close();
		driver.switchTo().window(prevWindow);
		bcoupons.selectOrderType("Delivery And Pickup");
		bcoupons.clickSubmit();
	}
	
	@Test(description="Set a minimum price required for discount")
	public void testDiscountLimit() {
		
		navbar.clickCouponModule();
		navbar.clickBusinessCoupons();
		
		bcoupons.clickEditCoupon(bcoupons.retrieveCid(couponCode));
		bcoupons.setLimit("50");
		bcoupons.clickSubmit();
		
		navbar.clickPreview();
		
		mainpage.clickCheckout();
		checkoutpage.clickContinue();
		reviewpage.usePromo(couponCode);
		
		Assert.assertTrue(reviewpage.errorMsgShown(), "Discount was still applied");
		driver.close();
		driver.switchTo().window(prevWindow);
		bcoupons.setLimit("0");
		bcoupons.clickSubmit();
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
