package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Assert;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform;

import java.net.URL;

import pages.LoginPage;
import pages.MainPage;

public class LoginPageTests {
	
	WebDriver driver;
	WebDriverWait wait;
	MainPage mainpage;
	LoginPage loginpage;
	
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
		mainpage.clickLogin();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		driver.navigate().refresh();
	}
	
	@Test(description="Attempt to log in with only email")
	public void testEmailOnly() {
		
		loginpage.setEmail("jasonchen@go3technology.us");
		loginpage.clickLogin();
		
		Assert.assertTrue(driver.getTitle().contains("Sign In"), "User did not remain on sign in page");
	}
	
	@Test(description="Attempt to log in with only password")
	public void testPasswordOnly() {
		
		loginpage.setPassword("123456789");
		loginpage.clickLogin();
		
		Assert.assertTrue(driver.getTitle().contains("Sign In"), "User did not remain on sign in page");
	}
	
	@Test(description="Attempt to log in with invalid credentials")
	public void testInvalidLogin() {
		
		loginpage.login("random@email.com", "randompassword");
		
		Assert.assertTrue(driver.getTitle().contains("Sign In"), "User did not remain on sign in page");
	}
	
	@Test(description="Attempt to log in with valid credentials", priority = 1)
	public void testValidLogin() {
		
		loginpage.login("jasonchen@go3technology.us", "123456789");
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
		
		Assert.assertEquals(driver.getCurrentUrl(), url, "User was not redirected to main page");
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
