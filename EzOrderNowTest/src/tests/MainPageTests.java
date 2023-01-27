package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import pages.MainPage;
import pages.LoginPage;

public class MainPageTests {
	
	WebDriver driver;
	WebDriverWait wait;
	MainPage mainpage;
	LoginPage loginpage;
	Random rand = new Random();
	
	String url = "https://uat.ezordernow.com/ezordernow";//"https://www.ezordernow.com/new-york-10004-demo-store-1-802455";
	
	@BeforeClass
	public void beforeClass() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/jasonchen/Desktop/chromedriver");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		mainpage = new MainPage(driver, wait);
		loginpage = new LoginPage(driver);
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		driver.navigate().to(url);
	}
	
	@Test(description="Clicking the Log In button", priority = -2)
	public void testClickLogin() {
		
		mainpage.clickLogin();
		
		Assert.assertEquals(driver.getCurrentUrl(), url + "/login", "Login page was not loaded");
	}
	
	@Test(description="Logging into an account", priority = -1)
	public void testLogIn() {
		
		mainpage.clickLogin();
		loginpage.login("jasonchen@go3technology.us", "123456789");
		wait.until(ExpectedConditions.visibilityOfElementLocated(mainpage.getLogoutBtn()));
		
		Assert.assertTrue(driver.findElement(mainpage.getLogoutBtn()).isDisplayed(), "User not logged in");
	}
	
	@Test(description="Logging out of account", priority = 2)
	public void testLogout() {
		
		mainpage.clickLogout();
		wait.until(ExpectedConditions.visibilityOfElementLocated(mainpage.getLoginBtn()));
		
		Assert.assertTrue(driver.findElement(mainpage.getLoginBtn()).isDisplayed(), "User not logged out");
	}
	
	@Test(description="Changing the language of the site", priority = 3)
	public void testChangeLang() {
		
		String langName = mainpage.langBtnText();
		
		mainpage.clickLangBtn();
		mainpage.changeLangTo("Chinese");
		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOfElementLocated(mainpage.getLangBtn()));
		
		String newLangName = driver.findElement(mainpage.getLangBtn()).getText();
		
		Assert.assertNotEquals(newLangName, langName, "Language was not changed");
	}
	
	@Test(description="Go to account profile")
	public void testClickProfile() {
		
		mainpage.clickProfileIcon();
		mainpage.clickProfileOption();
		
		Assert.assertEquals(driver.getCurrentUrl(), url + "/account/profile", "Profile page not loaded");
	}
	
	@Test(description="Go to account order history")
	public void testClickOrders() {
		
		mainpage.clickProfileIcon();
		mainpage.clickOrdersOption();
		
		Assert.assertEquals(driver.getCurrentUrl(), url + "/account/orders", "Orders page not loaded");
	}
	
	@Test(description="Go to account saved payments")
	public void testClickPayments() {
		
		mainpage.clickProfileIcon();
		mainpage.clickPaymentsOption();
		
		Assert.assertEquals(driver.getCurrentUrl(), url + "/account/payments", "Payments page not loaded");
	}
	
	@Test(description="Go to account saved addresses")
	public void testClickAddress() {
		
		mainpage.clickProfileIcon();
		mainpage.clickAddressOption();
		
		Assert.assertEquals(driver.getCurrentUrl(), url + "/account/address", "Address page not loaded");
	}
	
	@Test(description="Clicking to see business hours")
	public void testViewHours() {
		
		mainpage.clickHoursIcon();
		
		Assert.assertTrue(driver.findElement(mainpage.getHoursModal()).isDisplayed(), "Hours modal not displayed");
	}
	
	@Test(description="Clicking to see contact info")
	public void testViewContacts() {
		
		mainpage.clickContactIcon();
		
		Assert.assertTrue(driver.findElement(mainpage.getContactModal()).isDisplayed(), "Contact modal not displayed");
	}
	
	@Test(description="Clicking Schedule Order")
	public void testClickSchedule() {
		
		mainpage.clickSchedule();
		
		Assert.assertTrue(driver.findElement(mainpage.getScheduleModal()).isDisplayed(), "Schedule Order modal not displayed");
	}
	
	@Test(description = "Scheduling a future order on same day")
	public void testScheduleToday() {
		
		mainpage.clickSchedule();
		mainpage.scheduleToday();
		
		Select dropdown = new Select(driver.findElement(mainpage.getTimeOptions()));
		int selectTime = rand.nextInt(dropdown.getOptions().size());
		
		String time = dropdown.getOptions().get(selectTime).getText();
		dropdown.selectByVisibleText(time);
		
		//remove leading 0 if present from time
		if(time.charAt(0) == '0') {
			
			StringBuffer sb = new StringBuffer(time);
			sb.replace(0, 1, "");
			time = sb.toString();
		}
		time = time.toLowerCase();
		mainpage.submitSchedule();
		wait.until(ExpectedConditions.attributeContains(mainpage.getScheduleDate(), "innerHTML", time));
		String date = driver.findElement(mainpage.getScheduleDate()).getText();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy ");
		String currentDate = sdf.format(Calendar.getInstance().getTime()) + time;
		
		Assert.assertEquals(date, currentDate, "Display does not match current date and selected time");
	}
	
	@Test(description = "Scheduling a future order on a future date")
	public void testScheduleLaterDay() {
		
		mainpage.clickSchedule();
		mainpage.scheduleLater();
		
		List<WebElement> days = driver.findElements(mainpage.getCalendarDays());
		int selectDay = rand.nextInt(days.size());
		
		days.get(selectDay).click();
		String selectedDate = driver.findElement(mainpage.getSelectedDate()).getText() + " ";
		Select dropdown = new Select(driver.findElement(mainpage.getTimeOptions()));
		int selectTime = rand.nextInt(dropdown.getOptions().size());
		
		String time = dropdown.getOptions().get(selectTime).getText();
		dropdown.selectByVisibleText(time);
		
		//remove leading 0 if present from time
		if(time.charAt(0) == '0') {
			
			StringBuffer sb = new StringBuffer(time);
			sb.replace(0, 1, "");
			time = sb.toString();
		}
		time = time.toLowerCase();
		mainpage.submitSchedule();
		wait.until(ExpectedConditions.attributeContains(mainpage.getScheduleDate(), "innerHTML", time));
		String date = driver.findElement(mainpage.getScheduleDate()).getText();
		
		Assert.assertEquals(date, selectedDate + time, "Display does not match selected date and time");
	}
	
	@Test(description="Clicking the share button")
	public void testShareButton() {
		
		mainpage.clickShareBtn();
		
		Assert.assertTrue(driver.findElement(By.id("exampleModalCenterTitle")).isDisplayed(), "Share Modal not displayed");
	}
	
	@Test(description="Clicking Terms & Conditions")
	public void testClickTerms() {
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(mainpage.getTermsConditions()));
		mainpage.clickTermsConditions();
		
		Assert.assertTrue(driver.findElement(mainpage.getTermsCondModal()).isDisplayed(), "Terms & Conditions not displayed");
	}
	
	@Test(description="Clicking Return & Refund Policy")
	public void testClickRRpolicy() {
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(mainpage.getRefundPolicy()));
		mainpage.clickRefundPolicy();
		
		Assert.assertTrue(driver.findElement(mainpage.getRefundPolModal()).isDisplayed(), "Return & Refund policy not displayed");
	}
	
	@Test(description="Clicking Privacy Policy")
	public void testClickPrivacyPolicy() {
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(mainpage.getPrivacyPolicy()));
		mainpage.clickPrivacyPolicy();
		
		Assert.assertTrue(driver.findElement(mainpage.getPrivacyPolModal()).isDisplayed(), "Privacy Policy not displayed");
	}
	
	@Test(description="Clicking on a category to collapse the menu")
	public void testCollapseCategory() {
		
		List<WebElement> catHeader = driver.findElements(mainpage.getCategoryHeader());
		List<WebElement> catBody = driver.findElements(mainpage.getCategoryBody());
		int selectCategory = rand.nextInt(catHeader.size());
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({'block':'center','inline':'center'});", catHeader.get(selectCategory));
		catHeader.get(selectCategory).click();
		
		Assert.assertFalse(catBody.get(selectCategory).isDisplayed(), "Category items not collapsed, still visible");
	}
	
	@Test(description="Clicking to scroll back to top of page", priority = 1)
	public void testAutoScrollUp() {
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(mainpage.getPrivacyPolicy()));
		driver.findElement(By.id("backToUp")).click();
	}
	
	@Test(description="Clicking the location name at the header", priority = -1)
	public void testClickHeaderName() {
		
		mainpage.clickHeaderName();
		
		Assert.assertEquals(driver.getCurrentUrl(), url, "Main page not loaded");
	}
	
	@Test(description="Click on any menu item")
	public void testClickAnyItem() {
		
		mainpage.clickMenuItem();
		
		Assert.assertTrue(driver.findElement(mainpage.getMenuItemModal()).isDisplayed(), "Menu item modal is not displayed");
	}
	
	@Test(description="Increase quantity of selected item")
	public void testAddQuantity() {
		
		mainpage.clickMenuItem();
		mainpage.addQuantity(1);
		
		Assert.assertEquals(mainpage.quantityCount(), "2", "Incorrect quantity shown");
	}
	
	@Test(description="Decrease quantity of selected item")
	public void testMinusQuantity() {
		
		mainpage.clickMenuItem();
		mainpage.addQuantity(3);
		mainpage.minusQuantity(1);
		
		Assert.assertEquals(mainpage.quantityCount(), "3", "Incorrect quantity shown");
	}
	
	@Test(description="Add menu item to cart")
	public void testAddToCart() {
		
		mainpage.clickMenuItem();
		mainpage.clickAddToCart();
		
		Assert.assertTrue(driver.findElement(mainpage.getItemInCart()).isDisplayed(), "Item not displayed in the cart");
		mainpage.emptyCart();
	}
	
	@Test(description="Clicking Empty Cart button")
	public void testEmptyCart() {
		
		mainpage.clickMenuItem();
		mainpage.clickAddToCart();
		WebElement orderDetails = driver.findElement(mainpage.getItemInCart());
		WebElement subtotal = driver.findElement(mainpage.getCartSubtotal());
		
		wait.until(ExpectedConditions.visibilityOf(orderDetails));
		String prevPrice = subtotal.getText();
		
		mainpage.emptyCart();
		
		wait.until(ExpectedConditions.invisibilityOf(orderDetails));
		String newPrice = subtotal.getText();
		
		Assert.assertNotEquals(newPrice, prevPrice, "Cart not emptied, item still present");
	}
	
	@Test(description="Go to the checkout page", priority = 1)
	public void testGoToCheckout() {
		
		mainpage.clickMenuItem();
		mainpage.clickAddToCart();
		mainpage.clickCheckout();
		
		Assert.assertTrue(driver.getTitle().contains("Check Out"), "Checkout page not loaded");
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
