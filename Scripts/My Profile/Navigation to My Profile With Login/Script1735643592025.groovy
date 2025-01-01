import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import java.sql.*;

// Verify the user is able to navigate to My Account page
// Open the website
WebUI.openBrowser('')
WebUI.navigateToUrl('http://127.0.0.1:4200/#')
WebUI.maximizeWindow()

WebDriver driver = DriverFactory.getWebDriver();
// Login first 
WebElement loginNav = driver.findElement(By.xpath("//a[@href='#/auth/login']"));
loginNav.click();

WebUI.waitForElementVisible(findTestObject('Login/Email Field'), 5)
WebElement emailField = driver.findElement(By.xpath("//input[@type='text']"));
emailField.sendKeys("klyminh1@gmail.com");

WebUI.waitForElementVisible(findTestObject('Login/Password Field'), 5)
WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
passwordField.sendKeys("welcome02");

WebElement submitButton = driver.findElement(By.className("btnSubmit"));
submitButton.click();
	
WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 5)

// Navigate to My Account page
WebElement userMenu = driver.findElement(By.id("user-menu"));
userMenu.click();
WebElement myAccount = driver.findElement(By.xpath("//a[@href='#/account/profile']"));
myAccount.click();

WebElement myAccountTitle = WebUI.findWebElement(findTestObject('My Profile Title'))

assert myAccountTitle.isDisplayed() == true

String url = "jdbc:mariadb://localhost:3306/toolshop";
String username = "root";
String password = "root";
Class.forName("org.mariadb.jdbc.Driver"); // Or appropriate driver class
Connection conn = DriverManager.getConnection(url, username, password);

// Get the favorite products count for the user using the user id getting from the user email
def query = "SELECT first_name, last_name, email, phone, address, postcode, city, state, country FROM users WHERE email = 'klyminh1@gmail.com'"
Statement statement = conn.createStatement()
def result = statement.executeQuery(query)

// Verify if the user has favorite products
result.next()

// Fetch values from the web page
String firstName = driver.findElement(By.id("first_name")).getAttribute("value")
String lastName = driver.findElement(By.id("last_name")).getAttribute("value")
String email = driver.findElement(By.id("email")).getAttribute("value")
String phone = driver.findElement(By.id("phone")).getAttribute("value")
String address = driver.findElement(By.id("address")).getAttribute("value")
String postcode = driver.findElement(By.id("postcode")).getAttribute("value")
String city = driver.findElement(By.id("city")).getAttribute("value")
String state = driver.findElement(By.id("state")).getAttribute("value")
String country = driver.findElement(By.id("country")).getAttribute("value")

// Fetch values from the database
String dbFirstName = result.getString("first_name")
String dbLastName = result.getString("last_name")
String dbEmail = result.getString("email")
String dbPhone = result.getString("phone")
String dbAddress = result.getString("address")
String dbPostcode = result.getString("postcode")
String dbCity = result.getString("city")
String dbState = result.getString("state")
String dbCountry = result.getString("country")

// Compare values
assert firstName == dbFirstName
assert lastName == dbLastName
assert email == dbEmail
assert phone == dbPhone
assert address == dbAddress
assert postcode == dbPostcode
assert city == dbCity
assert state == dbState
assert country == dbCountry


result.close();
statement.close();
conn.close();

WebUI.closeBrowser()