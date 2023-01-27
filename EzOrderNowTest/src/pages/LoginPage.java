package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	
	WebDriver driver;
	
	By email = By.id("email");
	By password = By.id("password");
	By loginBtn = By.xpath("//button[text()='Sign In']");
	By signInText = By.xpath("//h2[text()='Sign In']");
	
	public LoginPage(WebDriver driver) {
		
		this.driver = driver;
		
		/*if(!driver.getTitle().contains("Sign In")) {
			throw new IllegalStateException("This is not the correct page! You are at: " + driver.getCurrentUrl());
		}*/
	}
	
	public void setEmail(String email) {
		driver.findElement(this.email).sendKeys(email);
	}
	
	public void setPassword(String password) {
		driver.findElement(this.password).sendKeys(password);
	}
	
	public void clickLogin() {
		driver.findElement(loginBtn).click();
	}
	
	public void login(String email, String password) {
		
		setEmail(email);
		setPassword(password);
		clickLogin();
	}
	
	public Boolean signInTextDisplayed() {
		return driver.findElement(signInText).isDisplayed();
	}
	
	public By getSignInText() { return signInText; }
}
