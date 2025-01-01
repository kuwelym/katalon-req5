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
import com.kuwelym.keywords.LoginHelper
import internal.GlobalVariable as GlobalVariable

// Verify the user is able to navigate to My Account page
// Open the website
WebUI.openBrowser('')
WebUI.navigateToUrl('http://127.0.0.1:4200/#')
WebUI.maximizeWindow()

WebDriver driver = DriverFactory.getWebDriver();
// Login first
LoginHelper.login(email, "welcome02")
	
WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 5)

// Navigate to My Account page
WebElement userMenu = driver.findElement(By.id("user-menu"));
userMenu.click()
WebElement myAccount = driver.findElement(By.xpath("//a[@href='#/account/profile']"))
myAccount.click()

WebElement myAccountTitle = WebUI.findWebElement(findTestObject('My Profile Title'))

assert myAccountTitle.isDisplayed() == true

WebElement UIfield = driver.findElement(By.xpath("//input[@data-test='${field}']"))
UIfield.clear()
UIfield.sendKeys(data)
WebElement submitButton = driver.findElement(By.xpath("//button[@data-test='update-profile-submit']"))
submitButton.click();

String url = "jdbc:mariadb://localhost:3306/toolshop";
String username = "root";
String password = "root";
Class.forName("org.mariadb.jdbc.Driver"); // Or appropriate driver class
Connection conn = DriverManager.getConnection(url, username, password);

// Get the field from the database, change the - to _ for the column name

String fieldName = field.toString().replaceAll("-", "_")
def query = "SELECT ${fieldName} FROM users WHERE email = '${email}'"
Statement statement = conn.createStatement()
def result = statement.executeQuery(query)

result.next()

String UIdata = UIfield.getAttribute("value")
String dbData = result.getString(fieldName)

if(field == "email") {

	WebElement message = driver.findElement(By.xpath("//div[@role='alert']"))	
	
	assert message.getText().toLowerCase().contains("cannot modify email") == true
}

assert UIdata == dbData : "Update ${field} to ${data} failed"

result.close();
statement.close();
conn.close();

WebUI.closeBrowser()